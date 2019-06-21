/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import org.peakemu.Peak;
import org.peakemu.common.Logger;
import org.peakemu.common.SocketManager;
import org.peakemu.database.dao.IOTemplateDAO;
import org.peakemu.database.dao.JobSkillDAO;
import org.peakemu.game.in.gameaction.GameActionArg;
import org.peakemu.maputil.MapUtil;
import org.peakemu.objects.player.Player;
import org.peakemu.world.IOActionPerformer;
import org.peakemu.world.JobSkill;
import org.peakemu.world.MapCell;
import org.peakemu.world.ioaction.BuyMountPark;
import org.peakemu.world.ioaction.CraftTable;
import org.peakemu.world.ioaction.GoIncarnam;
import org.peakemu.world.ioaction.HarvestItem;
import org.peakemu.world.ioaction.ModifyMountParkPrice;
import org.peakemu.world.ioaction.OpenMountPark;
import org.peakemu.world.ioaction.OpenZaap;
import org.peakemu.world.ioaction.OpenZaapi;
import org.peakemu.world.ioaction.SavePosition;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class InteractiveObjectHandler {
    final private IOTemplateDAO ioTemplateDAO;
    final private Peak peak;
    final private JobSkillDAO jobSkillDAO;

    public InteractiveObjectHandler(IOTemplateDAO ioTemplateDAO, Peak peak, JobSkillDAO jobSkillDAO) {
        this.ioTemplateDAO = ioTemplateDAO;
        this.peak = peak;
        this.jobSkillDAO = jobSkillDAO;
    }
    
    private void registerActions(){
        ModifyMountParkPrice modifyMountParkPrice = new ModifyMountParkPrice(peak.getDao().getMountParkDAO());
        
        for(int skillId : ModifyMountParkPrice.SKILLS_IDS){
            setCustomAction(skillId, modifyMountParkPrice);
        }
        
        setCustomAction(SavePosition.SKILL_ID, new SavePosition());
        setCustomAction(OpenMountPark.SKILL_ID, new OpenMountPark(peak.getDao().getMountParkDAO()));
        setCustomAction(OpenZaap.SKILL_ID, new OpenZaap(peak.getDao().getPlayerDAO(), peak.getWorld().getWaypointHandler()));
        setCustomAction(OpenZaapi.SKILL_ID, new OpenZaapi(peak.getWorld().getWaypointHandler()));
        setCustomAction(BuyMountPark.SKILL_ID, new BuyMountPark(peak.getDao().getMountParkDAO()));
        setCustomAction(GoIncarnam.SKILL_ID, new GoIncarnam(peak.getWorld().getConfig(), peak.getWorld().getPlayerHandler()));
    }
    
    public void setCustomAction(int skillId, IOActionPerformer action){
        jobSkillDAO.getSkillById(skillId).setActionPerformer(action);;
    }

    public void load() {
        System.out.print("Chargement des templates d'IO : ");
        System.out.println(ioTemplateDAO.getAll().size() + " templates chargées");
        
        System.out.print("Initialisation des IO actions : ");
        
        HarvestItem harvestItem = new HarvestItem(peak.getWorld().getJobHandler(), peak.getWorld().getItemHandler());
        CraftTable craftTable = new CraftTable(peak.getWorld().getJobHandler(), peak.getWorld().getItemHandler());
        
        for(JobSkill skill : jobSkillDAO.getAll()){
            if(skill.isHarvest()){
                skill.setActionPerformer(harvestItem);
            }else{
                skill.setActionPerformer(craftTable);
            }
        }
        
        registerActions();
        System.out.println("Ok");
    }

    public void startAction(MapCell cell, Player player, GameActionArg GA) {
        if(!MapUtil.isAdjacent(cell, player.getCell()))
            return;
        
        int actionID = (int) GA.getAttachement("IO_ACTION");

        if (actionID == -1) {
            return;
        }

        if (cell.getObject().getType() == null) {
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined IOType : %d", cell.getObject().getID());
            return;
        }

        if (!cell.getObject().getType().canDoAction(actionID)) {
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Invalid action %d on IO %s", actionID, cell.getObject().getType());
            return;
        }

        JobSkill skill = jobSkillDAO.getSkillById(actionID);
        
        if(skill == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Job skill not found %d", actionID);
            return;
        }
        
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Start IO Action %s", skill.getName());
        
        IOActionPerformer action = skill.getActionPerformer();

        if (action == null) {
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Action %d not found on IO %s", actionID, cell.getObject().getType());

            //use old and ugly ancestra switch
            switch (actionID) {
                case 62: //Source de jouvence
                    if (player.getLifePoints() < player.getMaxLifePoints() && player.getLevel() < 16) {
                        player.fullPDV();
                        SocketManager.GAME_SEND_STATS_PACKET(player);
                    } else //SocketManager.GAME_SEND_MESSAGE(perso, "Action impossible, vous avez toute votre vie ou vous avez un niveau supérieur à 15!", "ff0000");
                    {
                        return;
                    }
                    break;
                case 151: //Etabli pyrotechnique
                    SocketManager.GAME_SEND_MESSAGE(player, "L'établi est endommagé! Lances échange au PNJ pour confectionner des fées!", "ff0000");
                    break;
//                case 81://Vérouiller maison
//                    House h = House.get_house_id_by_coord(player.getMap().getId(), CcellID);
//                    if (h == null) {
//                        return;
//                    }
//                    player.setInHouse(h);
//                    h.Lock(player);
//                    break;
//                case 97://Acheter maison
//                    House h3 = House.get_house_id_by_coord(player.getMap().getId(), CcellID);
//                    if (h3 == null) {
//                        return;
//                    }
//                    player.setInHouse(h3);
//                    h3.BuyIt(player);
//                    break;
//                case 84://Rentrer dans une maison
//                    House h2 = House.get_house_id_by_coord(player.getMap().getId(), CcellID);
//                    if (h2 == null) {
//                        return;
//                    }
//                    player.setInHouse(h2);
//                    h2.HopIn(player);
//                    break;
//                case 104://Ouvrir coffre privé
//                    Trunk trunk = Trunk.get_trunk_id_by_coord(player.getMap().getId(), CcellID);
//                    if (trunk == null) {
//                        GameServer.addToLog("Game: INVALID TRUNK ON MAP : " + player.getMap().getId() + " CELLID : " + CcellID);
//                        return;
//                    }
//                    player.setInTrunk(trunk);
//                    trunk.HopIn(player);
//                    break;
//                case 105://Vérouiller coffre
//                    Trunk t = Trunk.get_trunk_id_by_coord(player.getMap().getId(), CcellID);
//                    if (t == null) {
//                        GameServer.addToLog("Game: INVALID TRUNK ON MAP : " + player.getMap().getId() + " CELLID : " + CcellID);
//                        return;
//                    }
//                    player.setInTrunk(t);
//                    t.Lock(player);
//                    break;
//
//                case 98://Vendre
//                case 108://Modifier prix de vente
//                    House h4 = House.get_house_id_by_coord(player.getMap().getId(), CcellID);
//                    if (h4 == null) {
//                        return;
//                    }
//                    player.setInHouse(h4);
//                    h4.SellIt(player);
//                    break;
                //Baskwo concasseur
                case 121:
                case 181:
                    SocketManager.GameSendGDFToMap(player, player.getMap(), player.getCell().getID() + ";3;1");
//                    SocketManager.GAME_SEND_ECK_PACKET(player, 3, "8;181");
                    player.set_isTradingWith(-18);
                    break;
            }
        } else {
            if(!action.canDoAction(skill, cell, player, GA)){
                Peak.worldLog.addToLog(Logger.Level.DEBUG, "Can't do action");
                return;
            }
            
            action.perform(skill, cell, player, GA);
        }
    }

    public void finishAction(MapCell cell, Player perso, GameActionArg GA) {
        int actionID = (int)GA.getAttachement("IO_ACTION");
        
        JobSkill skill = jobSkillDAO.getSkillById(actionID);
        
        if (cell.getObject().getType() == null) {
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Undefined IOType : %d", cell.getObject().getID());
            return;
        }

        if (!cell.getObject().getType().canDoAction(actionID)) {
            Peak.worldLog.addToLog(Logger.Level.DEBUG, "Invalid action %d on IO %s", actionID, cell.getObject().getType());
            return;
        }
        
        if(skill == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Job skill not found %d", actionID);
            return;
        }
        
        Peak.worldLog.addToLog(Logger.Level.DEBUG, "Finish IO Action %s", skill.getName());
        
        IOActionPerformer action = skill.getActionPerformer();
        
        if(action == null){
            Peak.worldLog.addToLog(Logger.Level.ERROR, "Action %d not found on IO %s", actionID, cell.getObject().getType());
            return;
        }
        
        try{
            action.finish(skill, cell, perso, GA);
        }catch(Exception e){
            Peak.errorLog.addToLog(e);
        }
        
//        
//        switch (actionID) {
//            case 44://Sauvegarder a un zaap
//            case 81://Vérouiller maison
//            case 84://ouvrir maison
//            case 97://Acheter maison.
//            case 98://Vendre
//            case 104://Ouvrir coffre
//            case 105://Code coffre
//            case 108://Modifier prix de vente
//            case 157://Zaapi
//                break;
//            case 102://Puiser
//                cell.getObject().setState(IOState.EMPTY);
//                cell.getObject().setInteractive(false);
//                SocketManager.GAME_SEND_GDF_PACKET_TO_MAP(perso.getMap(), cell);
//                int qua = Formulas.getRandomValue(1, 10);//On a entre 1 et 10 eaux
//                Item obj = World.getObjTemplate(311).createNewItem(qua, false);
//                if (perso.addItem(obj, true)) {
//                }
//                SocketManager.GAME_SEND_IQ_PACKET(perso, perso.getSpriteId(), qua);
//                break;
//            case 62://jouvence
//                break;
//            case 183:
//                break;
//            //Baskwo:Concasseur
//            case 121:
//            case 181:
//                break;
//            default:
//                GameServer.addToLog("[FIXME]Case.finishAction non definie pour l'actionID = " + actionID);
//                break;
//        }
    }
}
