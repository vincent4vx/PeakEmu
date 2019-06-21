/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.common.util.StringUtil;
import org.peakemu.database.Database;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.Job;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class JobDAO {
    final private Database database;
    final private ItemDAO itemDAO;
    
    final private Map<Integer, Job> jobs = new HashMap<>();

    public JobDAO(Database database, ItemDAO itemDAO) {
        this.database = database;
        this.itemDAO = itemDAO;
    }
    
    private Job createByRS(ResultSet RS) throws SQLException{
        int id = RS.getInt("id");
        Collection<ItemTemplate> tools = new ArrayList<>();
        
        for(String strT : StringUtil.split(RS.getString("tools"), ",")){
            if(strT.isEmpty())
                continue;
            
            tools.add(itemDAO.getById(Integer.parseInt(strT)));
        }
        
        int baseJobId = RS.getInt("baseJob");
        Job base = RS.wasNull() ? null : getJobById(baseJobId);
        
        Job job = new Job(id, tools, base);
        jobs.put(id, job);
        return job;
    }
    
    public Collection<Job> getAll(){
        if(!jobs.isEmpty())
            return jobs.values();
        
        try(ResultSet RS = database.query("SELECT * FROM jobs_data")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return jobs.values();
    }
    
    public Job getJobById(int id){
        if(jobs.containsKey(id))
            return jobs.get(id);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM jobs_data WHERE id = ?")){
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
}