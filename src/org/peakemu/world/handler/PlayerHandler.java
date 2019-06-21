/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Constants;
import org.peakemu.common.Logger;
import org.peakemu.common.SocketManager;
import org.peakemu.database.dao.AccountDAO;
import org.peakemu.database.dao.ClassDataDAO;
import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.SpellList;
import org.peakemu.game.out.game.GameActionResponse;
import org.peakemu.objects.Account;
import org.peakemu.objects.player.Player;
import org.peakemu.objects.player.SpellBook;
import org.peakemu.world.ClassData;
import org.peakemu.world.ExpLevel;
import org.peakemu.world.config.CharacterCreationConfig;
import org.peakemu.world.GameMap;
import org.peakemu.world.Inventory;
import org.peakemu.world.MapCell;
import org.peakemu.world.Spell;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.Stats;
import org.peakemu.world.config.PlayerConfig;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.PlayerRace;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class PlayerHandler {
    final private AccountDAO accountDAO;
    final private PlayerDAO playerDAO;
    final private ClassDataDAO classDataDAO;
    final private CharacterCreationConfig characterCreationConfig;
    final private MapHandler mapHandler;
    final private GuildHandler guildHandler;
    final private SpellHandler spellHandler;
    final private ExpHandler expHandler;
    final private PlayerConfig playerConfig;

    public PlayerHandler(AccountDAO accountDAO, PlayerDAO playerDAO, ClassDataDAO classDataDAO, CharacterCreationConfig characterCreationConfig, MapHandler mapHandler, GuildHandler guildHandler, SpellHandler spellHandler, ExpHandler expHandler, PlayerConfig playerConfig) {
        this.accountDAO = accountDAO;
        this.playerDAO = playerDAO;
        this.classDataDAO = classDataDAO;
        this.characterCreationConfig = characterCreationConfig;
        this.mapHandler = mapHandler;
        this.guildHandler = guildHandler;
        this.spellHandler = spellHandler;
        this.expHandler = expHandler;
        this.playerConfig = playerConfig;
    }
    
    public void load(){
        System.out.print("Chargement des données de classes : ");
        System.out.println(classDataDAO.getAll().size() + " classes chargées");
        
        System.out.print("Chargement des personnages : ");
        System.out.println(playerDAO.getAll().size() + " personnages chargés");
    }

    public boolean isValidName(String name) {
        if (playerDAO.nameExists(name)) {
            return false;
        }

        if (name.length() < characterCreationConfig.getMinLength() || name.length() > characterCreationConfig.getMaxLength()) {
            return false;
        }

        if (characterCreationConfig.getForbidden().getEquals().contains(name)) {
            return false;
        }

        for (String str : characterCreationConfig.getForbidden().getStart()) {
            if (name.toLowerCase().startsWith(str)) {
                return false;
            }
        }

        for (String str : characterCreationConfig.getForbidden().getContains()) {
            if (name.toLowerCase().contains(str)) {
                return false;
            }
        }

        for (String str : characterCreationConfig.getForbidden().getEnd()) {
            if (name.toLowerCase().endsWith(str)) {
                return false;
            }
        }
        System.out.println(characterCreationConfig.getNameRegex());
        return name.matches(characterCreationConfig.getNameRegex());
    }
    
    private SpellBook getStartSpellBook(PlayerRace race){
        Map<Integer, SpellLevel> spells = new LinkedHashMap<>();
        
        for(Spell spell : classDataDAO.getClassDataByRace(race).getSpells()){
            spells.put(spell.getSpellID(), spell.getLevel(1));
        }
        
        return new SpellBook(spells, new HashMap<>(Constants.getStartSortsPlaces(race.ordinal())));
    }

    public boolean createCharacter(String name, int race, byte gender, int color1, int color2, int color3, Account account) {
        if (race >= PlayerRace.values().length) {
            return false;
        }
        if(gender < 0 || gender > 1)
            return false;
        
        PlayerRace pr = PlayerRace.values()[race];
        
        GameMap startMap = mapHandler.getMap(pr.getStartMap());
        MapCell startCell = startMap.getCell(pr.getStartCell());

        Player perso = new Player(
                -1,
                name,
                gender,
                pr,
                color1,
                color2,
                color3,
                characterCreationConfig.getStartKamas(),
                ((characterCreationConfig.getStartLevel() - 1) * 1),
                ((characterCreationConfig.getStartLevel() - 1) * 5),
                10000,
                expHandler.getLevel(1),
                0,
                100,
                pr.getGfxId(gender),
                (byte) 0,
                account,
                new Stats(),
                (byte) 1,
                (byte) 0,
                (byte) 0,
                "*#%!pi$:?",
                startMap,
                startCell,
                new Inventory(""), //start without items
                null,
                100,
                getStartSpellBook(pr),
                startMap.getId() + "," + startCell.getID(),
                null,
                0,
                null,
                0,
                0,
                0,
                new HashSet<WaypointHandler.Waypoint>(),
                "",
                false,
                ""
        );
        
        setLevel(perso, characterCreationConfig.getStartLevel());
        
        return playerDAO.insert(perso) != null;
    }
    
    public void teleport(Player player, GameMap map, MapCell cell){
        if(!player.getMap().equals(map)){
            player.getMap().removePlayer(player);

            player.setMap(map);
            
            player.send(new GameActionResponse(player.getSpriteId() + "", 2, "", "")); //teleport
            SocketManager.GAME_SEND_MAPDATA(
                player.getAccount().getGameThread(),
                map.getId(),
                map.get_date(),
                map.get_key()
            );
            
//            player.confirmObjective(4, map.getId() + "", null);
        }
        
        player.setCell(cell);
        
        if(player.getFight() == null){
            map.addPlayer(player);

            for (Player t : player._Follower.values()) {
                if (t.isOnline()) {
                    SocketManager.GAME_SEND_FLAG_PACKET(t, player);
                } else {
                    player._Follower.remove(t.getSpriteId());
                }
            }
        }
    }
    
    public void teleport(Player player, short mapId, int cellId){
        GameMap map = mapHandler.getMap(mapId);
        
        if(map == null){
            Peak.worldLog.addToLog(Logger.Level.INFO, "Invalid MapId : %d", mapId);
            return;
        }
        
        MapCell cell = map.getCell(cellId);
        
        if(cell == null){
            Peak.worldLog.addToLog(Logger.Level.INFO, "Invalid cell %d on map %d", cellId, mapId);
            return;
        }
        
        teleport(player, map, cell);
    }
    
    public void warpToSavePos(Player player){
        String[] infos = player.getSavePos().split(",");
        Peak.worldLog.addToLog(Logger.Level.INFO, "Warping to save pos %s", player.getName());
        teleport(player, Short.parseShort(infos[0]), Integer.parseInt(infos[1]));
    }
    
    public void setPositionToSavePos(Player player){
        String[] infos = player.getSavePos().split(",");
        GameMap map = mapHandler.getMap(Short.parseShort(infos[0]));
        MapCell cell = map.getCell(Integer.parseInt(infos[1]));
        player.setMap(map);
        player.setCell(cell);
    }
    
    public void setPosition(Player player, short mapId, int cellId){
        GameMap map = mapHandler.getMap(mapId);
        
        if(map == null){
            Peak.worldLog.addToLog(Logger.Level.INFO, "Invalid MapId : %d", mapId);
            return;
        }
        
        MapCell cell = map.getCell(cellId);
        
        if(cell == null){
            Peak.worldLog.addToLog(Logger.Level.INFO, "Invalid cell %d on map %d", cellId, mapId);
            return;
        }
        
        player.setMap(map);
        player.setCell(cell);
    }

    public Player getPlayerById(int id) {
        return playerDAO.getPlayerById(id);
    }
    
    public boolean deleteCharacter(Player character){
        if(character.getGuild() != null){
            guildHandler.kickGuild(character.getGuildMember());
        }
        
        if(!playerDAO.delete(character))
            return false;
        
        character.getAccount().removePlayer(character);
        return true;
    }
    
    public boolean addXp(Player player, long xp){
        if(!playerConfig.getAddXpOnMaxLevel() && player.getLevel() >= expHandler.getMaxPlayerLevel())
            return false;
        
        player.setCurExp(player.getCurExp() + xp);
        
        ExpLevel expLevel = player.getExpLevel();
        
        while(expLevel.getNext() != null 
            && expLevel.getNext().player != -1
            && player.getCurExp() >= expLevel.getNext().player){
            
            expLevel = expLevel.getNext();
            addLevelUpBonus(expLevel, player);
        }
        
        boolean levelUp = false;
        
        if(!player.getExpLevel().equals(expLevel)){ //level up
            player.setExpLevel(expLevel);
            setLevelUpSpells(player, expLevel);
            player.refreshStats();
            levelUp = true;
        }
        
        SocketManager.GAME_SEND_STATS_PACKET(player);
        return levelUp;
    }
    
    public void setLevel(Player player, int newLevel){
        if(newLevel > expHandler.getMaxPlayerLevel())
            newLevel = expHandler.getMaxPlayerLevel();
        
        if(newLevel <= player.getLevel()
            || player.getLevel() >= expHandler.getMaxPlayerLevel())
            return;
        
        ExpLevel expLevel = player.getExpLevel();
        
        while(expLevel.getNext() != null 
            && expLevel.getNext().player != -1
            && expLevel.level < newLevel){
            
            expLevel = expLevel.getNext();
            addLevelUpBonus(expLevel, player);
        }
        
        player.setExpLevel(expLevel);
        setLevelUpSpells(player, expLevel);
        player.refreshStats();
        
        player.setCurExp(expLevel.player);
        SocketManager.GAME_SEND_STATS_PACKET(player);
    }
    
    private void addLevelUpBonus(ExpLevel expLevel, Player player){
        player.addCapital(playerConfig.getCapitalPointsPerLevel());
        player.addSpellPoint(playerConfig.getSpellPointsPerLevel());

        if(playerConfig.getBonusApLevels().contains(expLevel.level)){ //AP Bonus
            player.getBaseCharacter().getBaseStats().addOneStat(Effect.ADD_PA, 1);
        }
    }
    
    private void setLevelUpSpells(Player player, ExpLevel expLevel){
        boolean newSpell = false;
        
        for(Spell spell : classDataDAO.getClassDataByRace(player.getRace()).getSpellsByLevel(expLevel.level)){
            if(player.getBaseCharacter().getSpellBook().learnSpell(spell))
                newSpell = true;
        }
        
        if(newSpell)
            player.send(new SpellList(player));
    }
    
    public ClassData getClassData(Player player){
        return classDataDAO.getClassDataByRace(player.getRace());
    }
    
    public void addHonor(Player player, int honor){
        if(honor > 0 
            && player.getAlignLevel() >= expHandler.getMaxPvpLevel() //Max honor reached
            && !playerConfig.getAddHonorOnMax())
            return;
        
        int totalHonor = player.getHonor() + honor;
        
        if(totalHonor < 0)
            totalHonor = 0;
        
        player.setHonor(totalHonor);
        
        int level = player.getAlignLevel();
        int newLevel = expHandler.getRealPvpLevel(player.getHonor());
        player.setAlignLevel(newLevel);
        
        if(level < newLevel){ //upgrade
            player.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(82, newLevel));
        }else if(newLevel < level){ //downgrade
            player.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(83, newLevel));
        }
    }
    
    public void removeEnergy(Player player, int energy){
        if(!playerConfig.getEnableEnergy())
            return;
        
        if(energy > player.getEnergy())
            energy = player.getEnergy();
        
        player.setEnergy(player.getEnergy() - energy);
            
        if(player.getEnergy() <= 0)
            player.setFuneralStone();

        player.send(new InfoMessage(InfoMessage.Type.INFO).addMessage(34, energy));
    }
}
