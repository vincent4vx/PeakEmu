package org.peakemu.game;

import org.peakemu.objects.item.Item;
import org.peakemu.common.CryptManager;
import org.peakemu.Ancestra;
import org.peakemu.maputil.OldPathfinding;
import org.peakemu.world.World;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
//Metier
import org.peakemu.Peak;
import org.peakemu.command.AdminConsoleCommandPerformer;

import org.peakemu.common.Logger;
import org.peakemu.database.Database;
import org.peakemu.common.SocketManager;
import org.peakemu.database.dao.AccountDAO;
import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.in.gameaction.GameActionHandler;
import org.peakemu.game.in.gameaction.GameActionRegistry;
import org.peakemu.game.out.HelloGame;
import org.peakemu.network.DofusClient;
import org.peakemu.network.InputPacketRegistry;

import org.peakemu.objects.player.Player;
import org.peakemu.objects.player.Group;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.handler.SessionHandler;
import org.peakemu.world.listener.OutputInventoryListener;

public class GameClient extends DofusClient{
    private Player player;
    final private GameActionHandler gameActionHandler;
    final private PlayerDAO playerDAO;
    final private AccountDAO accountDAO;
    final private Map attachements = new HashMap();
    
    private AdminConsoleCommandPerformer adminConsoleCommandPerformer;
    
    private long _timeLastTradeMsg = 0, _timeLastRecrutmentMsg = 0, _timeLastsave = 0, _timeLastAlignMsg = 0;

    public GameClient(Socket sock, InputPacketRegistry inputPacketRegistry, SessionHandler sessionHandler, PlayerDAO playerDAO, AccountDAO accountDAO, GameActionRegistry gameActionRegistry) throws IOException {
        super(sock, inputPacketRegistry, sessionHandler);
        
        this.playerDAO  = playerDAO;
        this.accountDAO = accountDAO;
        gameActionHandler = new GameActionHandler(gameActionRegistry);
    }

    public GameActionHandler getGameActionHandler() {
        return gameActionHandler;
    }

    @Override
    public Logger log() {
        return Peak.gameLog;
    }

    @Override
    public void onConnect() {
        send(new HelloGame());
    }

    @Override
    public void close() {
        super.close();
        
        if(player != null){
            playerDAO.save(player);
            player.getItems().removeListener(getRegistry(OutputInventoryListener.class));
            player.logout();
            player = null;
        }
        
        if(getAccount() != null){
            accountDAO.save(getAccount());
            getAccount().setGameThread(null);
            setAccount(null);
        }
    }

    public AdminConsoleCommandPerformer getAdminConsoleCommandPerformer() {
        if(adminConsoleCommandPerformer == null)
            adminConsoleCommandPerformer = new AdminConsoleCommandPerformer(this);
        
        return adminConsoleCommandPerformer;
    }
    
    public void attach(Object key, Object value){
        attachements.put(key, value);
    }
    
    public Object detach(Object key){
        return attachements.remove(key);
    }
    
    public Object getAttachement(Object key){
        return attachements.get(key);
    }
    
    public boolean containsAttachement(Object key){
        return attachements.containsKey(key);
    }

    @Override
    protected void parsePacket(String packet) {
        if (player != null) {
            player.refreshLastPacketTime();
        }
        
        super.parsePacket(packet);

        switch (packet.charAt(0)) {
            case 'A':
                parseAccountPacket(packet);
                break;
            case 'B':
                parseBasicsPacket(packet);
                break;
            case 'c':
                parseChanelPacket(packet);
                break;
            case 'd':
                parseDocPacket(packet);
                break;
            case 'E':
                parseExchangePacket(packet);
                break;
            case 'e':
                parse_environementPacket(packet);
                break;
            case 'F':
                parse_friendPacket(packet);
                break;
            case 'f':
                parseFightPacket(packet);
                break;
            case 'G':
                parseGamePacket(packet);
                break;
            case 'g':
                parseGuildPacket(packet);
                break;
//            case 'h':
//                parseHousePacket(packet);
//                break;
//            case 'K':
//                parseHouseKodePacket(packet);
//                break;
            case 'O':
                parseObjectPacket(packet);
                break;
            case 'P':
                parseGroupPacket(packet);
                break;
            case 'R':
                parseMountPacket(packet);
                break;
            case 'S':
                parseSpellPacket(packet);
                break;
            //quests
            case 'Q':
                parseQuestPacket(packet);
                break;
            case 'C':
                parseConquestPacket(packet);
                break;
        }
    }

    public void parseDocPacket(String packet) {
        switch (packet.charAt(1)) {
            case 'V':
                SocketManager.GAME_SEND_LEAVE_DOC(player);
                break;
        }
    }

    //Prisme
    public void parseConquestPacket(String packet) {
        switch (packet.charAt(1)) {

//			case 'b':
//				SocketManager.SEND_Cb_CONQUETE(player, World.getRate(player.getAlignement()) + ";"+ World.getBalanceArea(player.getMap().getSubArea().getArea(), player.getAlignement()));
//				break;
//			case 'B':
//				double rate = World.getRate(player.getAlignement());
//				double xrate = Math.rint( (player.getGrade() / 2.5) + 1);
//				SocketManager.SEND_CB_BONUS_CONQUETE(player, rate + "," + rate + "," + rate + ";" + xrate + "," + xrate + ","+ xrate + ";" + rate + "," + rate + "," + rate);
//				break;
//            case 'W':
//                geoConquest(packet);
//                break;
//            case 'I':
//                protectConquest(packet);
//                break;
//            case 'F':
//                joinProtectorsOfPrism(packet);
//                break;
        }
    }

    //quests
    private void parseQuestPacket(String packet) {
        switch (packet.charAt(1)) {
            case 'L': //Liste quête
                SocketManager.SEND_QUESTS_LIST_PACKET(player);
                break;
            case 'S'://Etapes d'une quete
                SocketManager.SEND_QUEST_STEPS_PACKET(player, Integer.parseInt(packet.substring(2)));
                break;
        }
    }

//    private void parseHousePacket(String packet) {
//        switch (packet.charAt(1)) {
//            case 'B'://Acheter la maison
//                packet = packet.substring(2);
//                House.HouseAchat(player);
//                break;
//            case 'G'://Maison de guilde
//                packet = packet.substring(2);
//                if (packet.isEmpty()) {
//                    packet = null;
//                }
//                House.parseHG(player, packet);
//                break;
//            case 'Q'://Quitter/Expulser de la maison
//                packet = packet.substring(2);
//                House.Leave(player, packet);
//                break;
//            case 'S'://Modification du prix de vente
//                packet = packet.substring(2);
//                House.SellPrice(player, packet);
//                break;
//            case 'V'://Fermer fenetre d'achat
//                House.closeBuy(player);
//                break;
//        }
//    }
//
//    private void parseHouseKodePacket(String packet) {
//        switch (packet.charAt(1)) {
//            case 'V'://Fermer fenetre du code
//                House.closeCode(player);
//                break;
//            case 'K'://Envoi du code
//                House_code(packet);
//                break;
//        }
//    }
//
//    private void House_code(String packet) {
//        switch (packet.charAt(2)) {
//            case '0'://Envoi du code
//                packet = packet.substring(4);
//                if (player.getInTrunk() != null) {
//                    Trunk.OpenTrunk(player, packet, false);
//                } else {
//                    House.OpenHouse(player, packet, false);
//                }
//                break;
//            case '1'://Changement du code
//                packet = packet.substring(4);
//                if (player.getInTrunk() != null) {
//                    Trunk.LockTrunk(player, packet);
//                } else {
//                    House.LockHouse(player, packet);
//                }
//                break;
//        }
//    }

    private void parseGuildPacket(String packet) {
        switch (packet.charAt(1)) {
//            case 'h'://Téléportation maison de guilde
//                guild_house(packet.substring(2));
//                break;
            case 'I'://Infos
                guild_infos(packet.charAt(2));
                break;
        }
    }

//    private void guild_house(String packet) {
//        if (player.getGuild() == null) {
//            SocketManager.GAME_SEND_Im_PACKET(player, "1135");
//            return;
//        }
//
//        if (player.getFight() != null || player.isAway()) {
//            return;
//        }
//        int HouseID = Integer.parseInt(packet);
//        House h = World.getHouses().get(HouseID);
//        if (h == null) {
//            return;
//        }
//        if (player.getGuild().getId() != h.get_guild_id()) {
//            SocketManager.GAME_SEND_Im_PACKET(player, "1135");
//            return;
//        }
//        if (!h.canDo(Constants.H_GTELE)) {
//            SocketManager.GAME_SEND_Im_PACKET(player, "1136");
//            return;
//        }
//        if (player.hasItemTemplate(8883, 1)) {
//            player.removeByTemplateID(8883, 1);
//            player.teleport((short) h.get_mapid(), h.get_caseid());
//        } else {
//            SocketManager.GAME_SEND_Im_PACKET(player, "1137");
//            return;
//        }
//    }

    private void guild_infos(char c) {
        switch (c) {
//            case 'H'://House
//                SocketManager.GAME_SEND_gIH_PACKET(player, House.parseHouseToGuild(player));
//                break;
        }
    }

//    public void protectConquest(String packet) {
//        switch (packet.charAt(2)) {
//            case 'J':
//                String str = player.parsePrisme();
//
//                Prism Prismes = World.getPrisme(player.getMap().getSubArea().getPrismeID());
//                if (Prismes != null) {
//                    Prism.parseAttack(player);
//                    Prism.parseDefense(player);
//                }
//                SocketManager.SEND_CIJ_INFO_JOIN_PRISME(player, str);
//                break;
//            case 'V':
//                SocketManager.SEND_CIV_INFOS_CONQUETE(player);
//                break;
//        }
//    }

//    public void joinProtectorsOfPrism(String packet) {
//        switch (packet.charAt(2)) {
//            case 'J':
//                int PrismeID = player.getMap().getSubArea().getPrismeID();
//                Prism Prismes = World.getPrisme(PrismeID);
//                if (Prismes == null) {
//                    return;
//                }
//                int FightID = -1;
//                try {
//                    FightID = Prismes.getFightID();
//                } catch (Exception e) {
//                }
//                short CarteID = -1;
//                try {
//                    CarteID = Prismes.getMap();
//                } catch (Exception e) {
//                }
//                int cellID = -1;
//                try {
//                    cellID = Prismes.getCell();
//                } catch (Exception e) {
//                }
//                if (PrismeID == -1 || FightID == -1 || CarteID == -1 || cellID == -1) {
//                    return;
//                }
//                if (Prismes.getalignement() != player.getAlignement()) {
//                    return;
//                }
//                if (player.getFight() != null) {
//                    return;
//                }
//                if (player.getMap().getId() != CarteID) {
//                    player.getMap();
//                    player.getCell();
//                    try {
//                        Thread.sleep(200);
//                        player.teleport(CarteID, cellID);
//                        Thread.sleep(400);
//                    } catch (Exception e) {
//                    }
//                }
//
//                World.getCarte(CarteID).getFight(FightID).joinPrismeFight(player, player.getSpriteId(), PrismeID);
//                for (Player z : World.getOnlinePersos()) {
//                    if (z == null) {
//                        continue;
//                    }
//                    if (z.getAlignement() != player.getAlignement()) {
//                        continue;
//                    }
//                    Prism.parseDefense(z);
//                }
//                break;
//        }
//    }

    private void parseChanelPacket(String packet) {
        switch (packet.charAt(1)) {
            case 'C'://Changement des Canaux
                Chanels_change(packet);
                break;
        }
    }

    private void Chanels_change(String packet) {
        String chan = packet.charAt(3) + "";
        switch (packet.charAt(2)) {
            case '+'://Ajout du Canal
                player.addChanel(chan);
                break;
            case '-'://Desactivation du canal
                player.removeChanel(chan);
                break;
        }
//        Database.SAVE_PERSONNAGE(player, false);
    }

    private void parseMountPacket(String packet) {
        switch (packet.charAt(1)) {
            case 'v'://Fermeture panneau d'achat
                SocketManager.GAME_SEND_R_PACKET(player, "v");
                break;
            case 'x'://Change l'xp donner a la dinde
                Mount_changeXpGive(packet);
                break;
        }
    }

    private void Mount_changeXpGive(String packet) {
        try {
            int xp = Integer.parseInt(packet.substring(2));
            if (xp < 0) {
                xp = 0;
            }
            if (xp > 90) {
                xp = 90;
            }
            player.setMountGiveXp(xp);
            SocketManager.GAME_SEND_Rx_PACKET(player);
        } catch (Exception e) {
        };
    }

    private void parse_friendPacket(String packet) {
        switch (packet.charAt(1)) {
//            case 'J': //Wife
//                FriendLove(packet);
//                break;
        }
    }

//    private void FriendLove(String packet) {
//        Player Wife = World.getPersonnage(player.getWife());
//        if (Wife == null) {
//            return;
//        }
//        if (!Wife.isOnline()) {
//            if (Wife.getGender() == 0) {
//                SocketManager.GAME_SEND_Im_PACKET(player, "140");
//            } else {
//                SocketManager.GAME_SEND_Im_PACKET(player, "139");
//            }
//
//            SocketManager.GAME_SEND_FRIENDLIST_PACKET(player);
//            return;
//        }
//        switch (packet.charAt(2)) {
//            case 'S'://Teleportation
//                if (player.getFight() != null) {
//                    return;
//                } else {
//                    //player.meetWife(Wife);
//                }
//                break;
//            case 'C'://Suivre le deplacement
//                if (packet.charAt(3) == '+') {//Si lancement de la traque
//                    if (player._Follows != null) {
//                        player._Follows._Follower.remove(player.getSpriteId());
//                    }
//                    SocketManager.GAME_SEND_FLAG_PACKET(player, Wife);
//                    player._Follows = Wife;
//                    Wife._Follower.put(player.getSpriteId(), player);
//                } else {//On arrete de suivre
//                    SocketManager.GAME_SEND_DELETE_FLAG_PACKET(player);
//                    player._Follows = null;
//                    Wife._Follower.remove(player.getSpriteId());
//                }
//                break;
//        }
//    }

    private void parseGroupPacket(String packet) {
        switch (packet.charAt(1)) {
            case 'F'://Suivre membre du groupe PF+GUID
                Group g = player.getGroup();
                if (g == null) {
                    return;
                }

                int pGuid = -1;
                try {
                    pGuid = Integer.parseInt(packet.substring(3));
                } catch (NumberFormatException e) {
                    return;
                }

                if (pGuid == -1) {
                    return;
                }

                Player P = World.getPersonnage(pGuid);

                if (P == null || !P.isOnline()) {
                    return;
                }

                if (packet.charAt(2) == '+')//Suivre
                {
                    if (player._Follows != null) {
                        player._Follows._Follower.remove(player.getSpriteId());
                    }
                    SocketManager.GAME_SEND_FLAG_PACKET(player, P);
                    SocketManager.GAME_SEND_PF(player, "+" + P.getSpriteId());
                    player._Follows = P;
                    P._Follower.put(player.getSpriteId(), player);
                } else if (packet.charAt(2) == '-')//Ne plus suivre
                {
                    SocketManager.GAME_SEND_DELETE_FLAG_PACKET(player);
                    SocketManager.GAME_SEND_PF(player, "-");
                    player._Follows = null;
                    P._Follower.remove(player.getSpriteId());
                }
                break;
            case 'G'://Suivez le tous PG+GUID
                Group g2 = player.getGroup();
                if (g2 == null) {
                    return;
                }

                int pGuid2 = -1;
                try {
                    pGuid2 = Integer.parseInt(packet.substring(3));
                } catch (NumberFormatException e) {
                    return;
                }

                if (pGuid2 == -1) {
                    return;
                }

                Player P2 = World.getPersonnage(pGuid2);

                if (P2 == null || !P2.isOnline()) {
                    return;
                }

                if (packet.charAt(2) == '+')//Suivre
                {
                    for (Player T : g2.getPlayers()) {
                        if (T.getSpriteId() == P2.getSpriteId()) {
                            continue;
                        }
                        if (T._Follows != null) {
                            T._Follows._Follower.remove(player.getSpriteId());
                        }
                        SocketManager.GAME_SEND_FLAG_PACKET(T, P2);
                        SocketManager.GAME_SEND_PF(T, "+" + P2.getSpriteId());
                        T._Follows = P2;
                        P2._Follower.put(T.getSpriteId(), T);
                    }
                } else if (packet.charAt(2) == '-')//Ne plus suivre
                {
                    for (Player T : g2.getPlayers()) {
                        if (T.getSpriteId() == P2.getSpriteId()) {
                            continue;
                        }
                        SocketManager.GAME_SEND_DELETE_FLAG_PACKET(T);
                        SocketManager.GAME_SEND_PF(T, "-");
                        T._Follows = null;
                        P2._Follower.remove(T.getSpriteId());
                    }
                }
                break;
            case 'V'://Quitter
                group_quit(packet);
                break;
            case 'W'://Localisation du groupe
                group_locate();
                break;
        }
    }

    private void group_locate() {
        if (player == null) {
            return;
        }
        Group g = player.getGroup();
        if (g == null) {
            return;
        }
        String str = "";
        boolean isFirst = true;
        for (Player GroupP : player.getGroup().getPlayers()) {
            if (!isFirst) {
                str += "|";
            }
            str += GroupP.getMap().getX() + ";" + GroupP.getMap().getY() + ";" + GroupP.getMap().getId() + ";2;" + GroupP.getSpriteId() + ";" + GroupP.getName();
            isFirst = false;
        }
        SocketManager.GAME_SEND_IH_PACKET(player, str);
    }

    private void group_quit(String packet) {
        if (player == null) {
            return;
        }
        Group g = player.getGroup();
        if (g == null) {
            return;
        }
        if (packet.length() == 2)//Si aucun guid est spécifié, alors c'est que le joueur quitte
        {
            g.leave(player);
            SocketManager.GAME_SEND_PV_PACKET(this, "");
            SocketManager.GAME_SEND_IH_PACKET(player, "");
        } else if (g.isChief(player.getSpriteId()))//Sinon, c'est qu'il kick un joueur du groupe
        {
            int guid = -1;
            try {
                guid = Integer.parseInt(packet.substring(2));
            } catch (NumberFormatException e) {
                return;
            };
            if (guid == -1) {
                return;
            }
            Player t = World.getPersonnage(guid);
            g.leave(t);
            SocketManager.GAME_SEND_PV_PACKET(t.getAccount().getGameThread(), "" + player.getSpriteId());
            SocketManager.GAME_SEND_IH_PACKET(t, "");
        }
    }

    private void parseObjectPacket(String packet) {
        switch (packet.charAt(1)) {
            case 'x':
                Object_obvijevan_desassocier(packet);
                break;
            case 'f':
                Object_obvijevan_feed(packet);
                break;
            case 's':
                Object_obvijevan_changeApparence(packet);
        }
    }

    private void Object_obvijevan_changeApparence(String packet) {
        int guid = -1;
        int pos = -1;
        int val = -1;
        try {
            guid = Integer.parseInt(packet.substring(2).split("\\|")[0]);
            pos = Integer.parseInt(packet.split("\\|")[1]);
            val = Integer.parseInt(packet.split("\\|")[2]);
        } catch (Exception e) {
            return;
        }
        if ((guid == -1) || (!this.player.hasItemGuid(guid))) {
            return;
        }
        Item obj = player.getItems().get(guid);

        if ((val >= 21) || (val <= 0)) {
            return;
        }

        obj.obvijevanChangeStat(Effect.OBVI_SKIN, val);
        SocketManager.send(this.player, obj.obvijevanOCO_Packet(pos));
        if (pos != -1) {
            SocketManager.GAME_SEND_ON_EQUIP_ITEM(this.player.getMap(), this.player);
        }
    }

    private void Object_obvijevan_feed(String packet) {
        int guid = -1;
        int pos = -1;
        int victime = -1;
        try {
            guid = Integer.parseInt(packet.substring(2).split("\\|")[0]);
            pos = Integer.parseInt(packet.split("\\|")[1]);
            victime = Integer.parseInt(packet.split("\\|")[2]);
        } catch (Exception e) {
            return;
        }
        if ((guid == -1) || (!this.player.hasItemGuid(guid))) {
            return;
        }

        Item obj = player.getItems().get(guid);
        Item objVictime = player.getItems().get(victime);

        obj.obvijevanNourir(objVictime, this.player);
        int qua = objVictime.getQuantity();
        if (qua <= 1) {
            this.player.removeItem(objVictime.getGuid());
            SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(this.player, objVictime.getGuid());
        } else {
            objVictime.setQuantity(qua - 1);
            SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(this.player, objVictime);
        }

//        SocketManager.send(this.player, obj.obvijevanOCO_Packet(pos));
    }

    private void Object_obvijevan_desassocier(String packet) {
//        int guid = -1;
//        int pos = -1;
//        try {
//            guid = Integer.parseInt(packet.substring(2).split("\\|")[0]);
//            pos = Integer.parseInt(packet.split("\\|")[1]);
//        } catch (Exception e) {
//            return;
//        }
//        if ((guid == -1) || (!this.player.hasItemGuid(guid))) {
//            return;
//        }
//        Item obj = player.getItems().get(guid);
//        if (obj == null) {
//            return;
//        }
//        int idOBVI = 0;
//
//        switch (obj.getTemplate().getType()) {
//            case AMULETTE:
//                idOBVI = 9255;
//                break;
//            case ANNEAU:
//                idOBVI = 9256;
//                break;
//            case COIFFE:
//                idOBVI = 9234;
//                break;
//            case CAPE:
//                idOBVI = 9233;
//                break;
//            default:
//                SocketManager.GAME_SEND_MESSAGE(this.player, "Erreur d'obvijevan numero: 4. Merci de nous le signaler si le probleme est grave.", "000000");
//                return;
//        }
//
//        ItemTemplate t = World.getObjTemplate(idOBVI);
//        Item obV = t.createNewItem(1, true);
//        String obviStats = obj.getObvijevanStatsOnly();
//
//        if (obviStats == "") {
//            SocketManager.GAME_SEND_MESSAGE(this.player, "Erreur d'obvijevan numero: 3. Merci de nous le signaler si le probleme est grave.", "000000");
//            return;
//        }

        //obV.clearStats();
        //obV.parseStringToStats(obviStats);
//        if (this.player.addObjet(obV, true)) {
//        }
//
//        obj.removeAllObvijevanStats();
//        SocketManager.send(this.player, obj.obvijevanOCO_Packet(pos));
//
//        SocketManager.GAME_SEND_ON_EQUIP_ITEM(this.player.getMap(), this.player);
    }

//    private void Dialog_start(String packet) {
//        try {
//            int npcID = Integer.parseInt(packet.substring(2).split((char) 0x0A + "")[0]);
//            NPC npc = player.getMap().getNPC(npcID);
//            Collector taxCollector = World.getPercepteur(npcID);
//            if (taxCollector != null && taxCollector.get_mapID() == player.getMap().getId()) {
//                SocketManager.GAME_SEND_DCK_PACKET(this, npcID);
//                SocketManager.GAME_SEND_QUESTION_PACKET(this, World.getGuild(taxCollector.get_guildID()).parseQuestionTaxCollector());
//                return;
//            }
//            if (npc == null) {
//                return;
//            }
//            SocketManager.GAME_SEND_DCK_PACKET(this, npcID);
//
//            // Objectif quêtes : Aller voir x
//            player.confirmObjective(1, npc.get_template().getId() + "", null);
//
//            int qID = npc.get_template().get_initQuestionID();
//
//            // Quests
//            boolean pendingStep = false;
//            // Quest Master
//            String quests = npc.get_template().get_quests(); // Quetes du npc
//            if (quests != null) // Npc a des quetes
//            {
//                String[] splitQuests = quests.split(";");
//                for (String curQuest : splitQuests) // Boucle chaque quete du npc
//                {
//                    Map<String, String> questPerso = player.get_quest(Integer.parseInt(curQuest));
//                    if (questPerso != null) // Le perso à la quête
//                    {
//                        String curStep = questPerso.get("curStep");
//                        if (!curStep.equals("-1")) // Si quete non terminee
//                        {
//                            Map<String, String> stepDetails = World.getStep(Integer.parseInt(curStep));
//                            if (!questPerso.get("objectivesToDo").isEmpty() && !questPerso.get("objectivesToDo").equals(" ")) // Etape en cours
//                            {
//                                pendingStep = true;
//                            }
//                            qID = Integer.parseInt(stepDetails.get("question"));
//                            break;
//                        } else { // Quete terminee
//                            Map<String, String> questSteps = World.getQuest(Integer.parseInt(curQuest)); // Template quete
//                            qID = Integer.parseInt(questSteps.get("endQuestion")); // Question de fin de quete
//                        }
//                    } else {
//                        Map<String, String> questTmpl = World.getQuest(Integer.parseInt(curQuest)); // Template quete
//                        qID = Integer.parseInt(questTmpl.get("startQuestion"));
//                        break;
//                    }
//
//                }
//            }
//            // Pnj quete secondaire
//            int newAnswer = -1;
//            Map<Integer, Map<String, String>> objectives = World.getObjectiveByNpcTarget(npc.get_template().getId());
//            if (objectives != null) // Il y a un objectif lié à ce pnj
//            {
//                for (Entry<Integer, Map<String, String>> objective : objectives.entrySet()) {
//                    if (player.hasObjective(objective.getKey())) // Si le perso doit faire cet objectif
//                    {
//                        qID = Integer.parseInt(objective.getValue().get("optQuestion"));
//                        // Le perso ne peut pas accomplir l'objectif
//                        if (!player.canDoObjective(Integer.parseInt(objective.getValue().get("type")), objective.getValue().get("args"))) {
//                            pendingStep = true;
//                        } else {
//                            pendingStep = false;
//                            newAnswer = Integer.parseInt(objective.getValue().get("optAnswer"));
//                            break;
//                        }
//                    }
//                }
//            }
//					  // End quests
//
//            NPC_question quest = World.getNPCQuestion(qID, player);
//            if (quest == null) {
//                SocketManager.GAME_SEND_END_DIALOG_PACKET(this);
//                return;
//            }
//
//            String DQPacket = quest.parseToDQPacket(player);
//            if (pendingStep) // Etape de quete en cours: on remplace la réponse par "terminer la discussion"
//            {
//                DQPacket = DQPacket.split("\\|")[0] + "|4840"; // 4840 = "Terminer la discussion"
//            } else if (newAnswer != -1) {
//                DQPacket = DQPacket.split("\\|")[0] + "|" + newAnswer;
//            }
//
//            SocketManager.GAME_SEND_QUESTION_PACKET(this, DQPacket);
//            player.set_isTalkingWith(npcID);
//        } catch (NumberFormatException e) {
//        };
//    }

    private void parseExchangePacket(String packet) {
        switch (packet.charAt(1)) {
//            case 'A'://Accepter demande d'échange
//                Exchange_accept();
//                break;
//            case 'B'://Achat
//                Exchange_onBuyItem(packet);
//                break;

//            case 'H'://Demande prix moyen + catégorie
//                Exchange_HDV(packet);
//                break;

//            case 'K'://Ok
//                Exchange_isOK();
//                break;
            //Metier
            case 'L'://jobAction : Refaire le craft précedent
                Exchange_doAgain();
                break;

//            case 'M'://Move (Ajouter//retirer un objet a l'échange)
//                Exchange_onMoveItem(packet);
//                break;
//            case 'r'://Rides => Monture
//                Exchange_mountPark(packet);
//                break;
        }
    }

//    private void Exchange_HDV(String packet) {
//        if (player.isDead() > 0 || player.get_isTradingWith() > 0 || player.getFight() != null || player.isAway()) {
//            return;
//        }
//        int templateID;
//        switch (packet.charAt(2)) {
//            case 'B': //Confirmation d'achat
//                String[] info = packet.substring(3).split("\\|");//ligneID|amount|price
//
//                HDV curHdv = World.getHdv(Math.abs(player.get_isTradingWith()));
//
//                int ligneID = Integer.parseInt(info[0]);
//                byte amount = Byte.parseByte(info[1]);
//
//                if (curHdv.buyItem(ligneID, amount, Integer.parseInt(info[2]), player)) {
//                    SocketManager.GAME_SEND_EHm_PACKET(player, "-", ligneID + "");//Enleve la ligne
//                    if (curHdv.getLigne(ligneID) != null && !curHdv.getLigne(ligneID).isEmpty()) {
//                        SocketManager.GAME_SEND_EHm_PACKET(player, "+", curHdv.getLigne(ligneID).parseToEHm());//Réajoute la ligne si elle n'est pas vide
//                    }
//                    /*if(curHdv.getLigne(ligneID) != null)
//                     {
//                     String str = curHdv.getLigne(ligneID).parseToEHm();
//                     SocketManager.GAME_SEND_EHm_PACKET(player,"+",str);
//                     }*/
//
//                    player.refreshStats();
//                    SocketManager.GAME_SEND_Ow_PACKET(player);
//                    SocketManager.GAME_SEND_Im_PACKET(player, "068");//Envoie le message "Lot acheté"
//                } else {
//                    SocketManager.GAME_SEND_Im_PACKET(player, "172");//Envoie un message d'erreur d'achat
//                }
//                break;
//            case 'l'://Demande listage d'un template (les prix)
//                templateID = Integer.parseInt(packet.substring(3));
//                try {
//                    SocketManager.GAME_SEND_EHl(player, World.getHdv(Math.abs(player.get_isTradingWith())), templateID);
//                } catch (NullPointerException e)//Si erreur il y a, retire le template de la liste chez le client
//                {
//                    SocketManager.GAME_SEND_EHM_PACKET(player, "-", templateID + "");
//                }
//
//                break;
//            case 'P'://Demande des prix moyen
//                templateID = Integer.parseInt(packet.substring(3));
//                SocketManager.GAME_SEND_EHP_PACKET(player, templateID);
//                break;
//            case 'T'://Demande des template de la catégorie
//                int categ = Integer.parseInt(packet.substring(3));
//                String allTemplate = World.getHdv(Math.abs(player.get_isTradingWith())).parseTemplate(categ);
//                SocketManager.GAME_SEND_EHL_PACKET(player, categ, allTemplate);
//                break;
//        }
//    }

    //Metier

    private void Exchange_doAgain() {
        //Metier
//        if (player.getCurJobAction() != null) {
//            player.getCurJobAction().putLastCraftIngredients();
//        }
    }

	//metier
    private void Exchange_isOK() {
//        if (player.getCurJobAction() != null) {
//            //Si pas action de craft, on s'arrete la
//            if (!player.getCurJobAction().isCraft()) {
//                return;
//            }
//            player.getCurJobAction().startCraft(player);
//        }
        if (player.getCurExchange() != null) {
//            player.getCurExchange().toogleOK(player.getSpriteId());
            return;
        } else if (player.get_curNpcExchange() != null) { // NPC_Exchange
            player.get_curNpcExchange().toogleOK(false);
            return;
        }
        //Baskwo:Concasseur
        if (player.get_isTradingWith() == -18) {
            if (player.getIsBrakingItem() == 0) {
                return;
            }
            int Guid = player.getIsBrakingItem();
            Item Obj = player.getItems().get(Guid);
            if (Obj == null) {
                return;
            }
            String RuneId = Item.getRunes(Obj);
            String[] ObjList = RuneId.split(";");
            boolean Created = false;
            //SocketManager.GAME_SEND_EV_PACKET(this);
//            try {
//                for (String i : ObjList) {
//                    Created = true;
//                    ItemTemplate ObjTemp = World.getObjTemplate(Integer.parseInt(i.split(",")[0]));
//                    Item Obji = ObjTemp.createNewItem(Integer.parseInt(i.split(",")[1]), true);
//                    if (player.addObjet(Obji, true)) {
//                    }
//
//                }
//            } catch (Exception e) {
//            }
            Item Obj2 = player.getItems().get(player.getIsBrakingItem());
            if (Obj2.getQuantity() == 1) {
                player.removeItem(player.getIsBrakingItem());
                SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(player, player.getIsBrakingItem());
            } else {
                Obj2.setQuantity((Obj2.getQuantity() - 1));
                SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(player, Obj2);
            }
            if (!Created) {
                SocketManager.GAME_SEND_Ec_PACKET(player, "EF");
                SocketManager.GAME_SEND_IO_PACKET_TO_MAP(player.getMap(), player.getSpriteId(), "-");
            } else {

                SocketManager.GAME_SEND_Ow_PACKET(player);
                SocketManager.GAME_SEND_IO_PACKET_TO_MAP(player.getMap(), player.getSpriteId(), "+8378");
            }
            if (player.getCurExchange() == null) {
                return;
            }
//            player.getCurExchange().toogleOK(player.getSpriteId());
        }

        return;
    }

    //@SuppressWarnings("unused")
    @SuppressWarnings("unused")
    private void Exchange_start(String packet) {
        /*if (packet.substring(2, 4).equals("11"))//Ouverture HDV achat
        {
            if (player.isDead() > 0 || player.get_isTradingWith() < 0)//Si déjà ouvert
            {
                SocketManager.GAME_SEND_EV_PACKET(this);
            }

            if (player.getDeshonor() >= 5) {
                SocketManager.GAME_SEND_Im_PACKET(player, "183");
                return;
            }

            HDV toOpen = World.getHdv(player.getMap().getId());

            if (toOpen == null) {
                return;
            }

            String info = "1,10,100;"
                    + toOpen.getStrCategories()
                    + ";" + toOpen.parseTaxe()
                    + ";" + toOpen.getLvlMax()
                    + ";" + toOpen.getMaxItemCompte()
                    + ";-1;"
                    + toOpen.getSellTime();
//            SocketManager.GAME_SEND_ECK_PACKET(player, 11, info);
            player.set_isTradingWith(0 - player.getMap().getId());	//Récupère l'ID de la map et rend cette valeur négative
            return;
        } else if (packet.substring(2, 4).equals("10"))//Ouverture HDV vente
        {
            if (player.get_isTradingWith() < 0)//Si déjà ouvert
            {
                SocketManager.GAME_SEND_EV_PACKET(this);
            }

            if (player.getDeshonor() >= 5) {
                SocketManager.GAME_SEND_Im_PACKET(player, "183");
                return;
            }

            HDV toOpen = World.getHdv(player.getMap().getId());

            if (toOpen == null) {
                return;
            }

            String info = "1,10,100;"
                    + toOpen.getStrCategories()
                    + ";" + toOpen.parseTaxe()
                    + ";" + toOpen.getLvlMax()
                    + ";" + toOpen.getMaxItemCompte()
                    + ";-1;"
                    + toOpen.getSellTime();
  //          SocketManager.GAME_SEND_ECK_PACKET(player, 10, info);
            player.set_isTradingWith(0 - player.getMap().getId());	//Récupère l'ID de la map et rend cette valeur négative

            SocketManager.GAME_SEND_HDVITEM_SELLING(player);
            return;
        } else*/
//        switch (packet.charAt(2)) {
//            case '2':// Start Npc_Exchange
//                if (player.get_isTradingWith() > 0 || player.getFight() != null || player.isAway()) {
//                    return;
//                }
//                int npcId = Integer.parseInt(packet.substring(4));
//                NPC npc = player.getMap().getNPC(npcId);
//                if (!World.isNpcExchange(npc.get_template().getId())) {
//                    return;
//                }
//                if (npc == null) {
//                    return;
//                }
//                int npcTemplateID = npc.get_template().getId();
//
//                NPC_Exchange exchange = new NPC_Exchange(player, npc.get_template());
//                player.setCurNpcExchange(exchange);
//                player.set_isTradingWith(npcTemplateID);
//                player.setAway(true);
////                SocketManager.GAME_SEND_ECK_PACKET(player, 2, npcId + "");
//                break;
//        }
    }

    private void parse_environementPacket(String packet) {
        switch (packet.charAt(1)) {
            case 'D'://Change direction
                Environement_change_direction(packet);
                break;

            case 'U'://Emote
                Environement_emote(packet);
                break;
        }
    }

    private void Environement_emote(String packet) {
        int emote = -1;
        try {
            emote = Integer.parseInt(packet.substring(2));
        } catch (Exception e) {
        };
        if (emote == -1) {
            return;
        }
        if (player == null) {
            return;
        }
        if (player.getFight() != null) {
            return;//Pas d'émote en combat
        }
        switch (emote)//effets spéciaux des émotes
        {
            case 19://s'allonger 
            case 1:// s'asseoir
                player.setSitted(!player.isSitted());
                break;
        }
        if (player.emoteActive() == emote) {
            player.setEmoteActive(0);
        } else {
            player.setEmoteActive(emote);
        }

        System.out.println("Set Emote " + player.emoteActive());
        System.out.println("Is sitted " + player.isSitted());

        SocketManager.GAME_SEND_eUK_PACKET_TO_MAP(player.getMap(), player.getSpriteId(), player.emoteActive());
    }

    private void Environement_change_direction(String packet) {
        try {
            if (player.getFight() != null) {
                return;
            }
            int dir = Integer.parseInt(packet.substring(2));
            if (player.isForView()) {
                dir = OldPathfinding.ViewOrientation(dir);
            }
            player.setOrientation(dir);
            SocketManager.GAME_SEND_eD_PACKET_TO_MAP(player.getMap(), player.getSpriteId(), dir);
        } catch (NumberFormatException e) {
            return;
        }
        ;
    }

    private void parseSpellPacket(String packet) {
        switch (packet.charAt(1)) {
            case 'F'://Oublie de sort
                forgetSpell(packet);
                break;
        }
    }

    private void forgetSpell(String packet) {
//        if (!player.isForgetingSpell()) {
//            return;
//        }
//
//        int id = Integer.parseInt(packet.substring(2));
//
//        if (Ancestra.CONFIG_DEBUG) {
//            GameServer.addToLog("Info: " + player.getName() + ": Tente Oublie sort id=" + id);
//        }
//
//        if (player.forgetSpell(id)) {
//            if (Ancestra.CONFIG_DEBUG) {
//                GameServer.addToLog("Info: " + player.getName() + ": OK pour Oublie sort id=" + id);
//            }
//            SocketManager.GAME_SEND_SPELL_UPGRADE_SUCCED(this, id, player.getSpellById(id).getLevel());
//            SocketManager.GAME_SEND_STATS_PACKET(player);
//            player.setisForgetingSpell(false);
//        }
    }

    private void parseFightPacket(String packet) {
        try {
            switch (packet.charAt(1)) {
                case 'D'://Détails d'un combat (liste des combats)
                    int key = -1;
                    try {
                        key = Integer.parseInt(packet.substring(2).replace(((int) 0x0) + "", ""));
                    } catch (Exception e) {
                    }
                    if (key == -1) {
                        return;
                    }
                    SocketManager.GAME_SEND_FIGHT_DETAILS(this, player.getMap().get_fights().get(key));
                    break;

                case 'L'://Lister les combats
                    SocketManager.GAME_SEND_FIGHT_LIST_PACKET(this, player.getMap());
                    break;

            }
        } catch (Exception e) {
            Peak.errorLog.addToLog(e);
        };
    }

    private void parseBasicsPacket(String packet) {
        switch (packet.charAt(1)) {
            case 'D':
                Basic_send_Date_Hour();
                break;
//            case 'S':
//                player.emoticone(packet.substring(2));
//                break;
            case 'Y':
                Basic_state(packet);
                break;
        }
    }

    public void Basic_state(String packet) {
        switch (packet.charAt(2)) {
            case 'A': //Absent
                if (player._isAbsent) {

                    SocketManager.GAME_SEND_Im_PACKET(player, "038");

                    player._isAbsent = false;
                } else {
                    SocketManager.GAME_SEND_Im_PACKET(player, "037");
                    player._isAbsent = true;
                }
                break;
            case 'I': //Invisible
                if (player._isInvisible) {
                    SocketManager.GAME_SEND_Im_PACKET(player, "051");
                    player._isInvisible = false;
                } else {
                    SocketManager.GAME_SEND_Im_PACKET(player, "050");
                    player._isInvisible = true;
                }
                break;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private void Basic_send_Date_Hour() {
        SocketManager.GAME_SEND_SERVER_DATE(this);
        SocketManager.GAME_SEND_SERVER_HOUR(this);
    }

    private void parseGamePacket(String packet) {
        switch (packet.charAt(1)) {
            case 'F':
                if (player == null) {
                    return;
                }
                player.setGhost();
                break;
            case 'P'://PvP Toogle
                player.toggleWings(packet.charAt(2));
                break;
            case 't':
                if (player.getFight() == null) {
                    return;
                }
                player.getFight().playerPass(player);
                break;
        }
    }

    public void kick() {
        close();
    }

    private void parseAccountPacket(String packet) {
        switch (packet.charAt(1)) {
            case 'g'://Cadeaux à la connexion
                int present = getAccount().getCadeau();
                if (present != 0) {
                    String idModObjeto = Integer.toString(present, 16);
                    String effets = World.getObjTemplate(present).getStrTemplate();
                    SocketManager.GAME_SEND_Ag_PACKET(this, present, "1~" + idModObjeto + "~1~~" + effets);
                }
                break;
            case 'G':
                compte_present(packet.substring(2));
                break;
            //Fin des cadeaux
            case 'f':
                int queueID = 1;
                int position = 1;
                SocketManager.MULTI_SEND_Af_PACKET(this, position, 1, 1, "" + 1, queueID);
                break;

            case 'i':
                getAccount().setClientKey(packet.substring(2));
                break;

            case 'P':
                SocketManager.REALM_SEND_REQUIRED_APK(this);
                break;

        }
    }

    //Cadeau a la connexion
    private void compte_present(String packet) {
        String[] info = packet.split("\\|");
        int idObjet = Integer.parseInt(info[0]);
        int idPj = Integer.parseInt(info[1]);
        Player perso = null;
        Item objet = null;
//        try {
//            perso = World.getPersonnage(idPj);
//            objet = World.getObjTemplate(idObjet).createNewItem(1, false); // Si true = jet parfait --"
//        } catch (Exception e) {
//        }
        if (perso == null || objet == null) {
            return;
        }
        perso.addObjet(objet, false);
        getAccount().setCadeau();
        Database.CADEAU_ACTUALISE(getAccount());
        SocketManager.GAME_SEND_AGK_PACKET(this);
    }
}
