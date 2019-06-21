/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.util.StringUtil;
import org.peakemu.database.Database;
import org.peakemu.world.Craft;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.Job;
import org.peakemu.world.JobSkill;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JobSkillDAO {
    final private Database database;
    final private JobDAO jobDAO;
    final private CraftDAO craftDAO;
    final private ItemDAO itemDAO;
    
    final private Map<Integer, JobSkill> skills = new HashMap<>();

    public JobSkillDAO(Database database, JobDAO jobDAO, CraftDAO craftDAO, ItemDAO itemDAO) {
        this.database = database;
        this.jobDAO = jobDAO;
        this.craftDAO = craftDAO;
        this.itemDAO = itemDAO;
    }
    
    private JobSkill createByRS(ResultSet RS) throws SQLException{
        int id = RS.getInt("id");
        Job job = jobDAO.getJobById(RS.getInt("job"));
        String name = RS.getString("name");
        Collection<Craft> crafts = new ArrayList<>();
        int level = RS.getInt("level");
        
        for(String craft : StringUtil.split(RS.getString("crafts"), ";")){
            if(craft.isEmpty())
                continue;
            
            Craft c = craftDAO.getCraftById(Integer.parseInt(craft));
            
            if(c == null){
                Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined craft %s", craft);
                continue;
            }
            
            crafts.add(c);
        }
        
        int harvestId = RS.getInt("item");
        ItemTemplate harvest = null;
        
        if(!RS.wasNull())
            harvest = itemDAO.getById(harvestId);
        
        JobSkill skill = new JobSkill(id, job, name, crafts, harvest, level);
        skills.put(id, skill);
        job.addSkill(skill);
        return skill;
    }
    
    public Collection<JobSkill> getAll(){
        if(!skills.isEmpty())
            return skills.values();
        
        try(ResultSet RS = database.query("SELECT * FROM job_skills")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return skills.values();
    }
    
    public JobSkill getSkillById(int id){
        return skills.get(id);
    }
}
