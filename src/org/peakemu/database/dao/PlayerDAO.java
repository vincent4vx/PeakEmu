/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;
import org.peakemu.database.Database;
import org.peakemu.database.parser.SpellParser;
import org.peakemu.objects.Account;
import org.peakemu.objects.Mount;
import org.peakemu.objects.player.BaseCharacter;
import org.peakemu.objects.player.JobStats;
import org.peakemu.objects.player.Player;
import org.peakemu.world.ExpLevel;
import org.peakemu.world.GameMap;
import org.peakemu.world.MapCell;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.PlayerRace;
import org.peakemu.world.handler.WaypointHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerDAO {

    final private Database database;
    final private AccountDAO accountDAO;
    final private InventoryDAO inventoryDAO;
    final private MapDAO mapDAO;
    final private PlayerStoreDAO playerStoreDAO;
    final private MountDAO mountDAO;
    final private JobDAO jobDAO;
    final private SpellDAO spellDAO;
    final private ExpDAO expDAO;
    
    final private Map<Integer, Player> players = new ConcurrentHashMap<>();

    public PlayerDAO(Database database, AccountDAO accountDAO, InventoryDAO inventoryDAO, MapDAO mapDAO, PlayerStoreDAO playerStoreDAO, MountDAO mountDAO, JobDAO jobDAO, SpellDAO spellDAO, ExpDAO expDAO) {
        this.database = database;
        this.accountDAO = accountDAO;
        this.inventoryDAO = inventoryDAO;
        this.mapDAO = mapDAO;
        this.playerStoreDAO = playerStoreDAO;
        this.mountDAO = mountDAO;
        this.jobDAO = jobDAO;
        this.spellDAO = spellDAO;
        this.expDAO = expDAO;
    }

    public int getCharacterCountByAccount(int accountId) {
        PreparedStatement stmt = null;
        ResultSet RS = null;

        try {
            stmt = database.prepare("SELECT COUNT(*) FROM personnages WHERE account = ?");
            stmt.setInt(1, accountId);
            RS = stmt.executeQuery();
            RS.next();
            return RS.getInt(1);
        } catch (SQLException e) {
            Peak.errorLog.addToLog(e);
            return 0;
        } finally {
            try {
                RS.close();
                stmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    private Set<WaypointHandler.Waypoint> parseZaapList(String zaapsStr){
        Set<WaypointHandler.Waypoint> zaaps = new HashSet<>();
        
        for(String zaap : StringUtil.split(zaapsStr, "|")){
            if(zaap.isEmpty())
                continue;
            
            String[] data = StringUtil.split(zaap, ",", 2);
            GameMap map = mapDAO.getMapById(Short.parseShort(data[0]));
            MapCell cell = map.getCell(Integer.parseInt(data[1]));
            zaaps.add(new WaypointHandler.Waypoint(map, cell));
        }
        
        return zaaps;
    }
    
    private String zaapListToString(Set<WaypointHandler.Waypoint> zaaps){
        StringBuilder sb = new StringBuilder(10 * zaaps.size());
        
        boolean b = false;
        
        for(WaypointHandler.Waypoint waypoint : zaaps){
            if(b)
                sb.append('|');
            else
                b = true;
            
            sb.append(waypoint.getMap().getId()).append(',').append(waypoint.getCell().getID());
        }
        
        return sb.toString();
    }
    
    private Collection<JobStats> parseJobStats(String str){
        Collection<JobStats> jobs = new ArrayList<>();
        
        for(String s : StringUtil.split(str, ";")){
            if(s.isEmpty())
                continue;
            
            String[] data = StringUtil.split(s, ",", 2);
            
            int jobId = Integer.parseInt(data[0]);
            long xp = Integer.parseInt(data[1]);
            
            ExpLevel expLevel = expDAO.getLevel(1);
            
            jobs.add(new JobStats(jobDAO.getJobById(jobId), expLevel, xp));
        }
        
        return jobs;
    }
    
    private Player createByRS(ResultSet RS) throws SQLException{
        GameMap map = mapDAO.getMapById(RS.getShort("map"));
        MapCell cell = map.getCell(RS.getInt("cell"));
        Account account = accountDAO.getAccountById(RS.getInt("account"));
        int playerId = RS.getInt("guid");
        
        int mountId = RS.getInt("mount");
        Mount mount = RS.wasNull() ? null : mountDAO.getMountById(mountId);
        
        Player perso = new Player(
            playerId,
            RS.getString("name"),
            RS.getInt("sexe"),
            PlayerRace.values()[RS.getInt("class")],
            RS.getInt("color1"),
            RS.getInt("color2"),
            RS.getInt("color3"),
            RS.getLong("kamas"),
            RS.getInt("spellboost"),
            RS.getInt("capital"),
            RS.getInt("energy"),
            expDAO.getLevel(RS.getInt("level")),
            RS.getLong("xp"),
            RS.getInt("size"),
            RS.getInt("gfx"),
            RS.getByte("alignement"),
            account,
            Stats.parseStringStats(RS.getString("stats")),
            RS.getByte("seeFriend"),
            RS.getByte("seeAlign"),
            RS.getByte("seeSeller"),
            RS.getString("canaux"),
            map,
            cell,
            inventoryDAO.getInventory(InventoryDAO.OWNER_TYPE_PLAYER + playerId),
            playerStoreDAO.getStoreByPlayerId(playerId),
            RS.getInt("pdvper"),
            SpellParser.parsePlayerSpellBook(spellDAO, RS.getString("spells")),
            RS.getString("savepos"),
            //metier
            parseJobStats(RS.getString("jobs")),
            RS.getInt("mountxpgive"),
            mount,
            RS.getInt("honor"),
            RS.getInt("deshonor"),
            RS.getInt("alvl"),
            parseZaapList(RS.getString("zaaps")),
            RS.getString("title"),
            RS.getBoolean("isSeller"),
            RS.getString("quests")
        );
        
        players.put(perso.getSpriteId(), perso);
        account.addPlayer(perso);;
        
        return perso;
    }
    
    public Collection<Player> getAll(){
        if(!players.isEmpty())
            return players.values();
        
        try(ResultSet RS = database.query("SELECT * FROM personnages")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return players.values();
    }

    public Collection<Player> getCharactersByAccount(Account account) {
        Collection<Player> chars = new ArrayList<>(5);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM personnages WHERE account = ?")){
            stmt.setInt(1, account.get_GUID());
            
            try(ResultSet RS = stmt.executeQuery()){
                while (RS.next()) {
                    if(players.containsKey(RS.getInt("guid")))
                        chars.add(players.get(RS.getInt("guid")));
                    else
                        chars.add(createByRS(RS));
                }
            }
        } catch (SQLException e) {
            Peak.errorLog.addToLog(e);
        }
        
        return chars;
    }
    
    public Player getPlayerById(int id){
        if(players.containsKey(id))
            return players.get(id);
        
        PreparedStatement stmt = null;
        ResultSet RS = null;
        
        try{
            stmt = database.prepare("SELECT * FROM personnages WHERE guid = ?");
            stmt.setInt(1, id);
            RS = stmt.executeQuery();
            
            if(RS.next()){
                return createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }finally{
            try{
                RS.close();
                stmt.close();
            }catch(Exception e){}
        }
        
        return null;
    }
    
    public boolean nameExists(String name){
        PreparedStatement stmt = null;
        ResultSet RS = null;
        
        try{
            stmt = database.prepare("SELECT COUNT(*) FROM personnages WHERE name = ?");
            stmt.setString(1, name);
            RS = stmt.executeQuery();
            RS.next();
            return RS.getInt(1) > 0;
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return true;
        }finally{
            try{
                stmt.close();
                RS.close();
            }catch(Exception e){}
        }
    }
    
    
    public Player insert(Player perso) {
        String baseQuery = "INSERT INTO personnages(`name` , `sexe` , `class` , `color1` , `color2` , `color3` , `kamas` , `spellboost` , `capital` , `energy` , `level` , `xp` , `size` , `gfx` , `account`,`cell`,`map`,`spells`,`stats`)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        try(PreparedStatement stmt = database.prepareInsert(baseQuery)){
            //!\\ Warning : Use base character to save stats and spells //!\\
            BaseCharacter baseCharacter = perso.getBaseCharacter();
            
            stmt.setString(1, perso.getName());
            stmt.setInt(2, perso.getGender());
            stmt.setInt(3, perso.getRace().ordinal());
            stmt.setInt(4, perso.getColor1());
            stmt.setInt(5, perso.getColor2());
            stmt.setInt(6, perso.getColor3());
            stmt.setLong(7, perso.getKamas());
            stmt.setInt(8, perso.getSpellPoints());
            stmt.setInt(9, perso.getCapitalPoints());
            stmt.setInt(10, perso.getEnergy());
            stmt.setInt(11, perso.getLevel());
            stmt.setLong(12, perso.getCurExp());
            stmt.setInt(13, perso.getSize());
            stmt.setInt(14, baseCharacter.getGfxID());
            stmt.setInt(15, perso.getAccount().get_GUID());
            stmt.setInt(16, perso.getCell().getID());
            stmt.setInt(17, perso.getMap().getId());
            stmt.setString(18, SpellParser.serializePlayerSpellBook(baseCharacter.getSpellBook()));
            stmt.setString(19, baseCharacter.getBaseStats().parseToItemSetStats());

            stmt.execute();
            
            try(ResultSet RS = stmt.getGeneratedKeys()){
                if(!RS.next())
                    return null;
                
                int id = RS.getInt(1);
                
                return getPlayerById(id);
            }
        } catch (SQLException e) {
            Peak.errorLog.addToLog(e);
            return null;
        }
    }
    
    public void save(Player player){
        save(player, false);
    }
    
    public void save(Player player, boolean bigSave){
        String baseQuery = "UPDATE `personnages` SET "
                + "`kamas`= ?,"
                + "`spellboost`= ?,"
                + "`spells` = ?,"
                + "`capital`= ?,"
                + "`energy`= ?,"
                + "`level`= ?,"
                + "`xp`= ?,"
                + "`size` = ?,"
                + "`gfx`= ?,"
                + "`alignement`= ?,"
                + "`honor`= ?,"
                + "`deshonor`= ?,"
                + "`alvl`= ?,"
                + "`seeSpell`= ?,"
                + "`seeFriend`= ?,"
                + "`seeAlign`= ?,"
                + "`seeSeller`= ?,"
                + "`canaux`= ?,"
                + "`map`= ?,"
                + "`cell`= ?,"
                + "`pdvper`= ?,"
                + "`savepos`= ?,"
                + "`zaaps`= ?,"
                + "`jobs`= ?,"
                + "`mountxpgive`= ?,"
                + "`mount`= ?,"
                + "`title`= ?,"
                + "`isSeller`= ?,"
                + "`quests`= ?,"
                + " `stats` = ?"
                + " WHERE `personnages`.`guid` = ? LIMIT 1 ;";

        try(PreparedStatement stmt = database.prepare(baseQuery)){
            //!\\ Warning : Use base character to save stats and spells //!\\
            BaseCharacter baseCharacter = player.getBaseCharacter();
            
            int i = 0;

            stmt.setLong(++i, player.getKamas());
            stmt.setInt(++i, player.getSpellPoints());
            stmt.setString(++i, SpellParser.serializePlayerSpellBook(baseCharacter.getSpellBook()));
            stmt.setInt(++i, player.getCapitalPoints());
            stmt.setInt(++i, player.getEnergy());
            stmt.setInt(++i, player.getLevel());
            stmt.setLong(++i, player.getCurExp());
            stmt.setInt(++i, player.getSize());
            stmt.setInt(++i, baseCharacter.getGfxID());
            stmt.setInt(++i, player.getAlignement());
            stmt.setInt(++i, player.getHonor());
            stmt.setInt(++i, player.getDisgrace());
            stmt.setInt(++i, player.getAlignLevel());
            stmt.setInt(++i, (player.showSpells() ? 1 : 0));
            stmt.setInt(++i, (player.showFriendConnection() ? 1 : 0));
            stmt.setInt(++i, (player.showWings() ? 1 : 0));
            stmt.setBoolean(++i, player.isSeller());
            stmt.setString(++i, player.getChanels());
            stmt.setInt(++i, player.getMap().getId());
            stmt.setInt(++i, player.getCell().getID());
            stmt.setInt(++i, player.getLifePercent());
            stmt.setString(++i, player.getSavePos());
            stmt.setString(++i, zaapListToString(player.getZaaps()));
            //metier
            stmt.setString(++i, player.parseJobData());
            stmt.setInt(++i, player.getMountXpGive());
            
            if(player.getMount() == null)
                stmt.setNull(++i, Types.INTEGER);
            else
                stmt.setInt(++i, player.getMount().getId());
            
            stmt.setString(++i, (player.get_title()));
            stmt.setBoolean(++i, player.isSeller());
            stmt.setString(++i, ""); //TODO: quests
            stmt.setString(++i, baseCharacter.getBaseStats().parseToItemSetStats());
            stmt.setInt(++i, player.getSpriteId());

            stmt.executeUpdate();
            
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Personnage " + player.getName() + " sauvegarde");
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
        }
        
        if(bigSave){
            inventoryDAO.save(player.getItems());
            playerStoreDAO.save(player.getStore());
        }
    }
    
    public void saveKamas(Player player){
        try(PreparedStatement stmt = database.prepare("UPDATE personnages SET kamas = ? WHERE guid = ?")){
            stmt.setLong(1, player.getKamas());
            stmt.setInt(2, player.getSpriteId());
            stmt.executeUpdate();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
    
    public boolean delete(Player player){
        try(PreparedStatement stmt = database.prepare("DELETE FROM personnages WHERE guid = ?")){
            stmt.setInt(1, player.getId());
            stmt.execute();
            players.remove(player.getId());
            player.getAccount().removePlayer(player);
            inventoryDAO.delete(player.getItems());
            playerStoreDAO.delete(player.getStore());
            return true;
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
            return false;
        }
    }
}
