/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.database.Database;
import org.peakemu.objects.Mount;
import org.peakemu.world.ItemStorage;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MountDAO {
    final private Database database;
    final private InventoryDAO inventoryDAO;
    final private MountTemplateDAO mountTemplateDAO;
    final private ExpDAO expDAO;
    
    final private Map<Integer, Mount> mounts = new HashMap<>();

    public MountDAO(Database database, InventoryDAO inventoryDAO, MountTemplateDAO mountTemplateDAO, ExpDAO expDAO) {
        this.database = database;
        this.inventoryDAO = inventoryDAO;
        this.mountTemplateDAO = mountTemplateDAO;
        this.expDAO = expDAO;
    }

    private Mount createByRS(ResultSet RS) throws SQLException {
        int id = RS.getInt("id");
        ItemStorage items = inventoryDAO.getMountStorage(id);
        
        Mount mount = new Mount(
            id,
            mountTemplateDAO.getMountTemplateById(RS.getInt("color")),
            RS.getInt("sexe"),
            RS.getInt("amour"),
            RS.getInt("endurance"),
            expDAO.getLevel(RS.getInt("level")),
            RS.getLong("xp"),
            RS.getString("name"),
            RS.getInt("fatigue"),
            RS.getInt("energie"),
            RS.getInt("reproductions"),
            RS.getInt("maturite"),
            RS.getInt("serenite"),
            items,
            RS.getString("ancetres"),
            RS.getString("ability")
        );
        
        mounts.put(id, mount);
        return mount;
    }
    
    public Collection<Mount> getAll(){
        if(!mounts.isEmpty())
            return mounts.values();
        
        try(ResultSet RS = database.query("SELECT * FROM mounts_data")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return mounts.values();
    }
    
    public void save(Mount mount){
        String baseQuery = "UPDATE `mounts_data` SET `color` = ?,`sexe` = ?,`name` = ?,`xp` = ?,`level` = ?,"
                + "`endurance` = ?,`amour` = ?,`maturite` = ?,`serenite` = ?,`reproductions` = ?,`fatigue` = ?,"
                + "`ancetres` = ?,`energie` = ?,`ability` = ? WHERE id = ?";

        try(PreparedStatement stmt =  database.prepare(baseQuery)){
            int i = 0;
            stmt.setInt(++i, mount.getTemplate().getId());
            stmt.setInt(++i, mount.getGender());
            stmt.setString(++i, mount.getName());
            stmt.setLong(++i, mount.getExp());
            stmt.setInt(++i, mount.getLevel());
            stmt.setInt(++i, mount.get_endurance());
            stmt.setInt(++i, mount.get_amour());
            stmt.setInt(++i, mount.get_maturite());
            stmt.setInt(++i, mount.get_serenite());
            stmt.setInt(++i, mount.get_reprod());
            stmt.setInt(++i, mount.get_fatigue());
            stmt.setString(++i, mount.get_ancetres());
            stmt.setInt(++i, mount.get_energie());
            stmt.setString(++i, mount.get_ability());
            stmt.setInt(++i, mount.getId());

            stmt.execute();
        } catch (SQLException e) {
            Peak.errorLog.addToLog(e);
        }
    }
    
    public Mount getMountById(int id){
        if(mounts.containsKey(id))
            return mounts.get(id);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM mounts_data WHERE id = ?")){
            stmt.setInt(1, id);
            
            try(ResultSet RS = stmt.executeQuery()){
                if(RS.next())
                    return createByRS(RS);
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return null;
    }
    
    public Mount insert(Mount mount){
        String baseQuery = "INSERT INTO `mounts_data`(`color`,`sexe`,`name`,`xp`,`level`,"
                + "`endurance`,`amour`,`maturite`,`serenite`,`reproductions`,`fatigue`,"
                + "`ancetres`,`energie`,`ability`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        
        try(PreparedStatement stmt = database.prepareInsert(baseQuery)){
            stmt.setInt(1, mount.getTemplate().getId());
            stmt.setInt(2, mount.getGender());
            stmt.setString(3, mount.getName());
            stmt.setLong(4, mount.getExp());
            stmt.setInt(5, mount.getLevel());
            stmt.setInt(6, mount.get_endurance());
            stmt.setInt(7, mount.get_amour());
            stmt.setInt(8, mount.get_maturite());
            stmt.setInt(9, mount.get_serenite());
            stmt.setInt(10, mount.get_reprod());
            stmt.setInt(11, mount.get_fatigue());
            stmt.setString(12, mount.get_ancetres());
            stmt.setInt(13, mount.get_energie());
            stmt.setString(14, mount.get_ability());

            stmt.execute();
            
            try(ResultSet RS = stmt.getGeneratedKeys()){
                if(RS.next())
                    return getMountById(RS.getInt(1));
            }
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return null;
    }
    
    public void remove(Mount mount){
        try(PreparedStatement stmt = database.prepare("DELETE FROM mounts_data WHERE id = ?")){
            mounts.remove(mount.getId());
            stmt.setInt(1, mount.getId());
            stmt.execute();
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
    }
}
