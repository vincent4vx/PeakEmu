/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import com.mysql.jdbc.StringUtils;
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
import org.peakemu.world.NPCQuestion;
import org.peakemu.world.NPCTemplate;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NPCTemplateDAO {
    final private Database database;
    final private NPCQuestionDAO questionDAO;
    final private ItemDAO itemDAO;
    
    final private Map<Integer, NPCTemplate> templates = new HashMap<>();

    public NPCTemplateDAO(Database database, NPCQuestionDAO questionDAO, ItemDAO itemDAO) {
        this.database = database;
        this.questionDAO = questionDAO;
        this.itemDAO = itemDAO;
    }
    
    private NPCTemplate createByRS(ResultSet RS) throws SQLException{
        int id = RS.getInt("id");
        int bonusValue = RS.getInt("bonusValue");
        int gfxID = RS.getInt("gfxID");
        int scaleX = RS.getInt("scaleX");
        int scaleY = RS.getInt("scaleY");
        int sex = RS.getInt("sex");
        int color1 = RS.getInt("color1");
        int color2 = RS.getInt("color2");
        int color3 = RS.getInt("color3");
        String access = RS.getString("accessories");
        int extraClip = RS.getInt("extraClip");
        int customArtWork = RS.getInt("customArtWork");
        int initQId = RS.getInt("initQuestion");
        String ventes = RS.getString("ventes");
        String quests = RS.getString("quests");
        NPCQuestion question = questionDAO.getQuestionById(initQId);
        
        Collection<ItemTemplate> store = new ArrayList<>();
        
        for(String tplId : StringUtil.split(ventes, ",")){
            tplId = tplId.trim();
            if(tplId.isEmpty() || tplId.equals("-1"))
                continue;
            
            ItemTemplate tpl = itemDAO.getById(Integer.parseInt(tplId));

            if(tpl != null)
                store.add(tpl);
        }
        
        NPCTemplate template = new NPCTemplate(
            id,
            bonusValue,
            gfxID,
            scaleX,
            scaleY,
            sex,
            color1,
            color2,
            color3,
            access,
            extraClip,
            customArtWork,
            question,
            store,
            quests
        );
        
        templates.put(id, template);
        return template;
    }
    
    public Collection<NPCTemplate> getAll(){
        if(!templates.isEmpty())
            return templates.values();
        
        try(ResultSet RS = database.query("SELECT * FROM npc_template")){
            while(RS.next())
                createByRS(RS);
        }catch(SQLException e){
            Peak.errorLog.addToLog(e);
        }
        
        return templates.values();
    }
    
    public NPCTemplate getTemplateById(int id){
        if(templates.containsKey(id))
            return templates.get(id);
        
        try(PreparedStatement stmt = database.prepare("SELECT * FROM npc_template WHERE id = ?")){
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
