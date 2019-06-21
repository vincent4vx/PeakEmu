package org.peakemu.common;

import org.peakemu.world.World;
import org.peakemu.Ancestra;
import org.peakemu.game.GameServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Map.Entry;
import org.peakemu.network.DofusClient;

import org.peakemu.world.GameMap;
import org.peakemu.objects.Mount;
import org.peakemu.objects.Guild;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.fight.Fight;

import org.peakemu.realm.RealmServer;
import org.peakemu.objects.player.Group;
import org.peakemu.world.InteractiveObject;
import org.peakemu.world.MapCell;
import org.peakemu.objects.player.JobStats;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.fight.fighter.Fighter;

public class SocketManager {

    public static void send(Player p, String packet) {
        if (p == null || p.getAccount() == null) {
            return;
        }
        if (p.getAccount().getGameThread() == null) {
            return;
        }
        DofusClient out = p.getAccount().getGameThread();
        send(out, packet);
    }

    public static void send(DofusClient out, String packet) {
        if (out != null && !packet.equals("") && !packet.equals("" + (char) 0x00)) {
            out.send(packet);
        }
    }

    public static String REALM_SEND_HC_PACKET(DofusClient out) {

        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder hashkey = new StringBuilder();

        Random rand = new Random();

        for (int i = 0; i < 32; i++) {
            hashkey.append(alphabet.charAt(rand.nextInt(alphabet.length())));
        }
        String packet = "HC" + hashkey;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            RealmServer.addToSockLog("Realm: Send>>" + packet);
        }
        return hashkey.toString();
    }

    public static void REALM_SEND_REQUIRED_VERSION(DofusClient out) {
        String packet = "AlEv" + Constants.CLIENT_VERSION;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            RealmServer.addToSockLog("Conn: Send>>" + packet);
        }
    }

    public static void REALM_SEND_LOGIN_ERROR(DofusClient out) {
        String packet = "AlEf";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            RealmServer.addToSockLog("Conn: Send>>" + packet);
        }
    }

    public static void MULTI_SEND_Af_PACKET(DofusClient out, int position, int totalAbo, int totalNonAbo, String subscribe,
            int queueID) {
        StringBuilder packet = new StringBuilder();
        packet.append("Af").append(position).append("|").append(totalAbo).append("|").append(totalNonAbo).append("|").append(subscribe).append("|").append(queueID);
        send(out, packet.toString());
        if (Ancestra.CONFIG_DEBUG) {
            RealmServer.addToSockLog("Serv: Send>>" + packet.toString());
        }
    }

    public static void REALM_SEND_BANNED(DofusClient out) {
        String packet = "AlEb";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            RealmServer.addToSockLog("Conn: Send>>" + packet);
        }
    }

    public static void REALM_SEND_ALREADY_CONNECTED(DofusClient out) {
        String packet = "AlEc";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            RealmServer.addToSockLog("Conn: Send>>" + packet);
        }
    }

    public static void REALM_SEND_POLICY_FILE(DofusClient out) {
        String packet = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<cross-domain-policy>"
                + "<allow-access-from domain=\"*\" to-ports=\"*\" secure=\"false\" />"
                + "<site-control permitted-cross-domain-policies=\"master-only\" />"
                + "</cross-domain-policy>";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void REALM_SEND_PERSO_LIST(DofusClient out, int number) {
        String packet = "AxK31536000000";//Temps d'abonnement
        if (number > 0) {
            packet += "|1," + number;//ServeurID
        }
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            RealmServer.addToSockLog("Conn: Send>>" + packet);
        }
    }

    public static void REALM_SEND_GAME_SERVER_IP(DofusClient out, int guid, boolean isHost) {
        String packet = "A";
        if (Ancestra.CONFIG_USE_IP) {
            String ip = Ancestra.CONFIG_IP_LOOPBACK && isHost ? CryptManager.CryptIP("127.0.0.1") + CryptManager.CryptPort(Ancestra.CONFIG_GAME_PORT) : Ancestra.GAMESERVER_IP;
            packet += "XK" + ip + guid;
        } else {
            String ip = Ancestra.CONFIG_IP_LOOPBACK && isHost ? "127.0.0.1" : "";
            packet += "YK" + ip + ":" + Ancestra.CONFIG_GAME_PORT + ";" + guid;
        }
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            RealmServer.addToSockLog("Conn: Send>>" + packet);
        }
    }

    public static void GAME_SEND_HELLOGAME_PACKET(DofusClient out) {
        String packet = "HG";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_ATTRIBUTE_FAILED(DofusClient out) {
        String packet = "ATE";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_ATTRIBUTE_SUCCESS(DofusClient out) {
        String packet = "ATK0";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_AV0(DofusClient out) {
        String packet = "AV0";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_HIDE_GENERATE_NAME(DofusClient out) {
        String packet = "APE2";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_STATS_PACKET(Player perso) {
        String packet = perso.getAsPacket();
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_Rx_PACKET(Player out) {
        String packet = "Rx" + out.getMountXpGive();
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_ALIGNEMENT(DofusClient out, int alliID) {
        String packet = "ZS" + alliID;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_ADD_CANAL(DofusClient out, String chans) {
        String packet = "cC+" + chans;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_SEESPELL_OPTION(DofusClient out, boolean spells) {
        String packet = "SLo" + (spells ? "+" : "-");
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_Ow_PACKET(Player perso) {
        String packet = "Ow" + perso.getPodUsed() + "|" + perso.getMaxPod();
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_OT_PACKET(DofusClient out, int id) {
        String packet = "OT";
        if (id > 0) {
            packet += id;
        }
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_SEE_FRIEND_CONNEXION(DofusClient out, boolean see) {
        String packet = "FO" + (see ? "+" : "-");
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GAME_CREATE(DofusClient out, String _name) {
        String packet = "GCK|1|" + _name;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_SERVER_HOUR(DofusClient out) {
        String packet = GameServer.getServerTime();
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_SERVER_DATE(DofusClient out) {
        String packet = GameServer.getServerDate();
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_MAPDATA(DofusClient out, int id, String date, String key) {
        String packet = "GDM|" + id + "|" + date + "|" + key;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GDK_PACKET(DofusClient out) {
        String packet = "GDK";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_MAP_OBJECTS_GDS_PACKETS(DofusClient out, GameMap carte) {
        String packet = carte.getObjectsGDsPackets();
        if (packet.equals("")) {
            return;
        }
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    // IOs walkables en combat
    public static void GAME_SEND_FIGHT_OBJECTS_GDS_PACKETS(GameMap carte, Fight target) {
        String packet = carte.getObjectsGDsPacketsInFight();
        if (packet.equals("")) {
            return;
        }

        for (Fighter f : target.getFighters(1)) {
            send(f.getPlayer(), packet.toString());
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToSockLog("Game: Send>>" + packet);
            }
        }
        for (Fighter f : target.getFighters(2)) {
            send(f.getPlayer(), packet.toString());
            if (Ancestra.CONFIG_DEBUG) {
                GameServer.addToSockLog("Game: Send>>" + packet);
            }
        }
    }

    public static void GAME_SEND_DUEL_Y_AWAY(DofusClient out, int guid) {
        String packet = "GA;903;" + guid + ";o";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_DUEL_E_AWAY(DofusClient out, int guid) {
        String packet = "GA;903;" + guid + ";z";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_MAP_NEW_DUEL_TO_MAP(GameMap map, int guid, int guid2) {
        String packet = "GA;900;" + guid + ";" + guid2;
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map " + map.getId() + ": Send>>" + packet);
        }
    }

    public static void GAME_SEND_CANCEL_DUEL_TO_MAP(GameMap map, int guid, int guid2) {
        String packet = "GA;902;" + guid + ";" + guid2;
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_MAP_START_DUEL_TO_MAP(GameMap map, int guid, int guid2) {
        String packet = "GA;901;" + guid + ";" + guid2;
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_MAP_FIGHT_COUNT(DofusClient out, GameMap map) {
        String packet = "fC" + map.getNbrFight();
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_FIGHT_PLACES_PACKET_TO_FIGHT(Fight fight, int teams, String places, int team) {
        String packet = "GP" + places + "|" + team;
        for (Fighter f : fight.getFighters(teams)) {
            if (f.getPlayer() == null || !f.getPlayer().isOnline()) {
                continue;
            }
            send(f.getPlayer(), packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GAME_ADDFLAG_PACKET_TO_MAP(GameMap map, int arg1, int guid1, int guid2, int cell1, String str1, int cell2, String str2) {
        StringBuilder packet = new StringBuilder();
        packet.append("Gc+").append(guid1).append(";").append(arg1).append("|").append(guid1).append(";").append(cell1).append(";").append(str1).append("|").append(guid2).append(";").append(cell2).append(";").append(str2);
        for (Player z : map.getPersos()) {
            send(z, packet.toString());
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet.toString());
        }
    }

    public static void GAME_SEND_GAME_ADDFLAG_PACKET_TO_PLAYER(Player p, GameMap map, int arg1, int guid1, int guid2, int cell1, String str1, int cell2, String str2) {
        StringBuilder packet = new StringBuilder();
        packet.append("Gc+").append(guid1).append(";").append(arg1).append("|").append(guid1).append(";").append(cell1).append(";").append(str1).append("|").append(guid2).append(";").append(cell2).append(";").append(str2);
        send(p, packet.toString());
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet.toString());
        }
    }

    public static void GAME_SEND_GAME_REMFLAG_PACKET_TO_MAP(GameMap map, int guid) {
        String packet = "Gc-" + guid;
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_ON_EQUIP_ITEM(GameMap map, Player _perso) {
        String packet = _perso.parseToOa();
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_ON_EQUIP_ITEM_FIGHT(Player _perso, Fighter f, Fight F) {
        String packet = _perso.parseToOa();
//        for (Fighter z : F.getFighters(f.getTeam2())) {
//            if (z.getPlayer() == null) {
//                continue;
//            }
//            send(z.getPlayer(), packet);
//        }
//        for (Fighter z : F.getFighters(f.getOtherTeam())) {
//            if (z.getPlayer() == null) {
//                continue;
//            }
//            send(z.getPlayer(), packet);
//        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_FIGHT_CHANGE_PLACE_PACKET_TO_FIGHT(Fight fight, int teams, GameMap map, int guid, int cell) {
        String packet = "GIC|" + guid + ";" + cell + ";1";
        for (Fighter f : fight.getFighters(teams)) {
            if (f.getPlayer() == null || !f.getPlayer().isOnline()) {
                continue;
            }
            send(f.getPlayer(), packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_FIGHT_CHANGE_OPTION_PACKET_TO_MAP(GameMap map, char s, char option, int guid) {
        String packet = "Go" + s + option + guid;
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GJK_PACKET(Player out, int state, int cancelBtn, int duel, int spec, int time, FightType unknown) {
        StringBuilder packet = new StringBuilder();
        packet.append("GJK").append(state).append("|").append(cancelBtn).append("|").append(duel).append("|").append(spec).append("|").append(time).append("|").append(unknown.ordinal());
        send(out, packet.toString());
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet.toString());
        }
    }

    public static void GAME_SEND_FIGHT_PLACES_PACKET(DofusClient out, String places, int team) {
        String packet = "GP" + places + "|" + team;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_Im_PACKET(Player out, String str) {
        String packet = "Im" + str;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_ILS_PACKET(Player out, int i) {
        String packet = "ILS" + i;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_ILF_PACKET(Player P, int i) {
        String packet = "ILF" + i;
        send(P, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_Im_PACKET_TO_MAP(GameMap map, String id) {
        String packet = "Im" + id;
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_eUK_PACKET_TO_MAP(GameMap map, int guid, int emote) {
        String packet = "eUK" + guid + "|" + emote;
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_Im_PACKET_TO_FIGHT(Fight fight, int teams, String id) {
        String packet = "Im" + id;
        for (Fighter f : fight.getFighters(teams)) {
            if (f.getPlayer() == null || !f.getPlayer().isOnline()) {
                continue;
            }
            send(f.getPlayer(), packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_MESSAGE(Player out, String mess, String color) {
        String packet = "cs<font color='#" + color + "'>" + mess + "</font>";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_MESSAGE_TO_MAP(GameMap map, String mess, String color) {
        String packet = "cs<font color='#" + color + "'>" + mess + "</font>";
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GA903_ERROR_PACKET(DofusClient out, char c, int guid) {
        String packet = "GA;903;" + guid + ";" + c;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GIC_PACKET_TO_FIGHT(Fight fight, int teams, Fighter f) {
        StringBuilder packet = new StringBuilder();
        packet.append("GIC|").append(f.getSpriteId()).append(";").append(f.getCell().getID()).append(";1|");

        for (Fighter perso : fight.getFighters(teams)) {
            if (perso.getPlayer() == null || !perso.getPlayer().isOnline()) {
                continue;
            }
            send(perso.getPlayer(), packet.toString());
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Fight: Send>>" + packet.toString());
        }
    }

    public static void GAME_SEND_GS_PACKET(Player out) {
        String packet = "GS";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Fight : Send>>" + packet);
        }
    }

    public static void GAME_SEND_GTL_PACKET(Player out, Fight fight) {
        String packet = fight.getGTL();
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Fight : Send>>" + packet);
        }
    }

    public static void GAME_SEND_GV_PACKET(Player P) {
        String packet = "GV";
        send(P, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Fight : Send>>" + packet);
        }
    }

    public static void GAME_SEND_PONG(DofusClient out) {
        String packet = "pong";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_QPONG(DofusClient out) {
        String packet = "qpong";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GA_PACKET(DofusClient out, String actionID, String s0, String s1, String s2) {
        String packet = "GA" + actionID + ";" + s0;
        if (!s1.equals("")) {
            packet += ";" + s1;
        }
        if (!s2.equals("")) {
            packet += ";" + s2;
        }

        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_BN(Player out) {
        String packet = "BN";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_BN(DofusClient out) {
        String packet = "BN";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EMOTICONE_TO_MAP(GameMap map, int guid, int id) {
        String packet = "cS" + guid + "|" + id;
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_FIGHT_GIE_TO_FIGHT(Fight fight, int teams, int mType, int cible, int value, String mParam2, String mParam3, String mParam4, int turn, int spellID) {
        StringBuilder packet = new StringBuilder();
        packet.append("GIE").append(mType).append(";").append(cible).append(";").append(value).append(";").append(mParam2).append(";").append(mParam3).append(";").append(mParam4).append(";").append(turn).append(";").append(spellID);
        for (Fighter f : fight.getFighters(teams)) {
            if (f.getPlayer() == null) {
                continue;
            }
            if (f.getPlayer().isOnline()) {
                send(f.getPlayer(), packet.toString());
            }
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Fight : Send>>" + packet.toString());
        }
    }

    public static void GAME_SEND_FIGHT_LIST_PACKET(DofusClient out, GameMap map) {
        StringBuilder packet = new StringBuilder();
        packet.append("fL");
        for (Entry<Integer, Fight> entry : map.get_fights().entrySet()) {
            if (packet.length() > 2) {
                packet.append("|");
            }
            packet.append(entry.getValue().parseFightInfos());
        }
        send(out, packet.toString());
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet.toString());
        }
    }

    public static void GAME_SEND_GDZ_PACKET_TO_FIGHT(Fight fight, int teams, String suffix, int cell, int size, int unk) {
        String packet = "GDZ" + suffix + cell + ";" + size + ";" + unk;

        for (Fighter f : fight.getFighters(teams)) {
            if (f.getPlayer() == null || !f.getPlayer().isOnline()) {
                continue;
            }
            send(f.getPlayer(), packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Fight: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GDC_PACKET_TO_FIGHT(Fight fight, int teams, int cell) {
        String packet = "GDC" + cell;

        for (Fighter f : fight.getFighters(teams)) {
            if (f.getPlayer() == null || !f.getPlayer().isOnline()) {
                continue;
            }
            send(f.getPlayer(), packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Fight: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GA2_PACKET(DofusClient out, int guid) {
        String packet = "GA;2;" + guid + ";";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_CHAT_ERROR_PACKET(DofusClient out, String name) {
        String packet = "cMEf" + name;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_eD_PACKET_TO_MAP(GameMap map, int guid, int dir) {
        String packet = "eD" + guid + "|" + dir;
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EV_PACKET(DofusClient out) {
        String packet = "EV";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_CONSOLE_MESSAGE_PACKET(DofusClient out, String mess) {
        String packet = "BAT2" + mess;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_BUY_ERROR_PACKET(DofusClient out) {
        String packet = "EBE";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_SELL_ERROR_PACKET(DofusClient out) {
        String packet = "ESE";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_BUY_OK_PACKET(DofusClient out) {
        String packet = "EBK";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_OBJECT_QUANTITY_PACKET(Player out, Item obj) {
        String packet = "OQ" + obj.getGuid() + "|" + obj.getQuantity();
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_ESK_PACKEt(Player out) {
        String packet = "ESK";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_REMOVE_ITEM_PACKET(Player out, int guid) {
        String packet = "OR" + guid;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_DELETE_OBJECT_FAILED_PACKET(DofusClient out) {
        String packet = "ODE";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EMOTICONE_TO_FIGHT(Fight fight, int teams, int guid, int id) {
        String packet = "cS" + guid + "|" + id;
        for (Fighter f : fight.getFighters(teams)) {
            if (f.getPlayer() == null || !f.getPlayer().isOnline()) {
                continue;
            }
            send(f.getPlayer(), packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Fight: Send>>" + packet);
        }
    }

    public static void GAME_SEND_OAEL_PACKET(DofusClient out) {
        String packet = "OAEL";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EXCHANGE_REQUEST_OK(DofusClient out, int guid, int guidT, int msgID) {
        String packet = "ERK" + guid + "|" + guidT + "|" + msgID;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EXCHANGE_REQUEST_ERROR(DofusClient out, char c) {
        String packet = "ERE" + c;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EXCHANGE_CONFIRM_OK(DofusClient out, int type) {
        String packet = "ECK" + type;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EXCHANGE_MOVE_OK(Player out, char type, String signe, String s1) {
        String packet = "EMK" + type + signe;
        if (!s1.equals("")) {
            packet += s1;
        }
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EXCHANGE_OTHER_MOVE_OK(DofusClient out, char type, String signe, String s1) {
        String packet = "EmK" + type + signe;
        if (!s1.equals("")) {
            packet += s1;
        }
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EXCHANGE_OK(DofusClient out, boolean ok, int guid) {
        String packet = "EK" + (ok ? "1" : "0") + guid;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EXCHANGE_OK(DofusClient out, boolean ok) {
        String packet = "EK" + (ok ? "1" : "0");
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EXCHANGE_VALID(DofusClient out, char c) {
        String packet = "EV" + c;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GROUP_INVITATION_ERROR(DofusClient out, String s) {
        String packet = "PIE" + s;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GROUP_INVITATION(DofusClient out, String n1, String n2) {
        String packet = "PIK" + n1 + "|" + n2;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GROUP_CREATE(DofusClient out, Group g) {
        String packet = "PCK" + g.getChief().getName();
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Groupe: Send>>" + packet);
        }
    }

    public static void GAME_SEND_PL_PACKET(DofusClient out, Group g) {
        String packet = "PL" + g.getChief().getSpriteId();
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Groupe: Send>>" + packet);
        }
    }

    public static void GAME_SEND_PR_PACKET(Player out) {
        String packet = "PR";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_PV_PACKET(DofusClient out, String s) {
        String packet = "PV" + s;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_ALL_PM_ADD_PACKET(DofusClient out, Group g) {
        StringBuilder packet = new StringBuilder();
        packet.append("PM+");
        boolean first = true;
        for (Player p : g.getPlayers()) {
            if (!first) {
                packet.append("|");
            }
            packet.append(p.parseToPM());
            first = false;
        }
        send(out, packet.toString());
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet.toString());
        }
    }

    public static void GAME_SEND_PM_ADD_PACKET_TO_GROUP(Group g, Player p) {
        String packet = "PM+" + p.parseToPM();
        for (Player P : g.getPlayers()) {
            send(P, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Groupe: Send>>" + packet);
        }
    }

    public static void GAME_SEND_PM_MOD_PACKET_TO_GROUP(Group g, Player p) {
        String packet = "PM~" + p.parseToPM();
        for (Player P : g.getPlayers()) {
            send(P, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Groupe: Send>>" + packet);
        }
    }

    public static void GAME_SEND_PM_DEL_PACKET_TO_GROUP(Group g, int guid) {
        String packet = "PM-" + guid;
        for (Player P : g.getPlayers()) {
            send(P, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Groupe: Send>>" + packet);
        }
    }

    public static void GAME_SEND_cMK_PACKET_TO_GROUP(Group g, String s, int guid, String name, String msg) {
        String packet = "cMK" + s + "|" + guid + "|" + name + "|" + msg + "|";
        for (Player P : g.getPlayers()) {
            send(P, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Groupe: Send>>" + packet);
        }
    }

    public static void GAME_SEND_FIGHT_DETAILS(DofusClient out, Fight fight) {
        if (fight == null) {
            return;
        }
        StringBuilder packet = new StringBuilder();
        packet.append("fD").append(fight.get_id()).append("|");
        for (Fighter f : fight.getFighters(1)) {
            packet.append(f.getPacketsName()).append("~").append(f.get_lvl()).append(";");
        }
        packet.append("|");
        for (Fighter f : fight.getFighters(2)) {
            packet.append(f.getPacketsName()).append("~").append(f.get_lvl()).append(";");
        }
        send(out, packet.toString());
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet.toString());
        }
    }

    public static void GAME_SEND_IQ_PACKET(Player perso, int guid, int qua) {
        String packet = "IQ" + guid + "|" + qua;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_JN_PACKET(Player perso, int jobID, int lvl) {
        String packet = "JN" + jobID + "|" + lvl;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GDF_PACKET_TO_MAP(GameMap map, MapCell cell) {
        int cellID = cell.getID();
        InteractiveObject object = cell.getObject();
        String packet = "GDF|" + cellID + ";" + object.getState().getValue() + ";" + (object.isInteractive() ? "1" : "0");
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    @Deprecated
    public static void GAME_SEND_GA_PACKET_TO_MAP(GameMap map, String gameActionID, int actionID, String s1, String s2) {
        String packet = "GA" + gameActionID + ";" + actionID + ";" + s1;
        if (!s2.equals("")) {
            packet += ";" + s2;
        }

        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }
	//Metier

    public static void GAME_SEND_JO_PACKET(Player perso, Collection<JobStats> SMs) {
//        for (JobStats sm : SMs) {
//            StringBuilder packet = new StringBuilder("JO").append(sm.getID()).append("|").append(sm.getOptBinValue()).append("|2"); //= "JO"+sm.getSpriteId()+"|"+sm.getOptBinValue()+"|2";//FIXME 2=?
//            send(player, packet.toString());
//        }
    }

    public static void GAME_SEND_EsK_PACKET(Player perso, String str) {
        String packet = "EsK" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_FIGHT_SHOW_CASE(ArrayList<DofusClient> PWs, int guid, int cellID) {
        String packet = "Gf" + guid + "|" + cellID;
        for (DofusClient PW : PWs) {
            send(PW, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Fight: Send>>" + packet);
        }
    }

    public static void GAME_SEND_Ea_PACKET(Player perso, String str) {
        String packet = "Ea" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EA_PACKET(Player perso, String str) {
        String packet = "EA" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_Ec_PACKET(Player perso, String str) {
        String packet = "Ec" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_Em_PACKET(Player perso, String str) {
        String packet = "Em" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_IO_PACKET_TO_MAP(GameMap map, int guid, String str) {
        String packet = "IO" + guid + "|" + str;
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_FA_PACKET(Player perso, String str) {
        String packet = "FA" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_FD_PACKET(Player perso, String str) {
        String packet = "FD" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_Rr_PACKET(Player perso, String str) {
        String packet = "Rr" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_cC_PACKET(Player perso, char c, String s) {
        String packet = "cC" + c + s;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GDO_PACKET_TO_MAP(GameMap map, char c, int cell, int itm, int i) {
        String packet = "GDO" + c + cell + ";" + itm + ";" + i;
        for (Player z : map.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GDO_PACKET(Player p, char c, int cell, int itm, int i) {
        String packet = "GDO" + c + cell + ";" + itm + ";" + i;
        send(p, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_ZC_PACKET(Player p, int a) {
        String packet = "ZC" + a;
        send(p, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GIP_PACKET(Player p, int a) {
        String packet = "GIP" + a;
        send(p, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_gn_PACKET(Player p) {
        String packet = "gn";
        send(p, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_gC_PACKET(Player p, String s) {
        String packet = "gC" + s;
        send(p, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_gV_PACKET(Player p) {
        String packet = "gV";
        send(p, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_gIB_PACKET(Player p, String infos) {
        String packet = "gIB" + infos;
        send(p, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_gIH_PACKET(Player p, String infos) {
        String packet = "gIH" + infos;
        send(p, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_gJ_PACKET(Player p, String str) {
        String packet = "gJ" + str;
        send(p, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_gK_PACKET(Player p, String str) {
        String packet = "gK" + str;
        send(p, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    @Deprecated
    public static void REALM_SEND_MESSAGE(DofusClient out, String args) {
        String packet = "M" + args;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    @Deprecated
    public static void GAME_SEND_MESSAGE_SERVER(Player out, String args) {
        String packet = "M1" + args;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EMOTE_LIST(Player perso, String s, String s1) {
        String packet = "eL" + s + "|" + s1;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_NO_EMOTE(Player out) {
        String packet = "eUE";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void REALM_SEND_TOO_MANY_PLAYER_ERROR(DofusClient out) {
        String packet = "AlEw";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void REALM_SEND_REQUIRED_APK(DofusClient out) {
        String chars = "abcdefghijklmnopqrstuvwxyz"; // Tu supprimes les lettres dont tu ne veux pas
        String pass = "";
        for (int x = 0; x < 5; x++) {
            int i = (int) Math.floor(Math.random() * 26); // Si tu supprimes des lettres tu diminues ce nb
            pass += chars.charAt(i);
        }
        System.out.println(pass);

        String packet = "APK" + pass;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_BWK(Player perso, String str) {
        String packet = "BWK" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_KODE(Player perso, String str) {
        String packet = "K" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_hOUSE(Player perso, String str) {
        String packet = "h" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void GAME_SEND_FORGETSPELL_INTERFACE(char sign, Player perso) {
        String packet = "SF" + sign;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_R_PACKET(Player perso, String str) {
        String packet = "R" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_gIF_PACKET(Player perso, String str) {
        String packet = "gIF" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_gITM_PACKET(Player perso, String str) {
        String packet = "gITM" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_gITp_PACKET(Player perso, String str) {
        String packet = "gITp" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_gITP_PACKET(Player perso, String str) {
        String packet = "gITP" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_IH_PACKET(Player perso, String str) {
        String packet = "IH" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_FLAG_PACKET(Player perso, Player cible) {
        String packet = "IC" + cible.getMap().getX() + "|" + cible.getMap().getY();
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_DELETE_FLAG_PACKET(Player perso) {
        String packet = "IC|";
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GUILDHOUSE_PACKET(Player perso) {
        String packet = "gUT";
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GUILDENCLO_PACKET(Player perso) {
        String packet = "gUF";
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    /**
     * HDV*
     */
    public static void GAME_SEND_EHm_PACKET(Player out, String sign, String str) {
        String packet = "EHm" + sign + str;

        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EHM_PACKET(Player out, String sign, String str) {
        String packet = "EHM" + sign + str;

        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EHP_PACKET(Player out, int templateID) //Packet d'envoie du prix moyen du template (En r�ponse a un packet EHP)
    {

        String packet = "EHP" + templateID + "|" + World.getObjTemplate(templateID).getAvgPrice();

        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EHL_PACKET(Player out, int categ, String templates) //Packet de listage des templates dans une cat�gorie (En r�ponse au packet EHT)
    {
        String packet = "EHL" + categ + "|" + templates;

        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_EHL_PACKET(Player out, String items) //Packet de listage des objets en vente
    {
        String packet = "EHL" + items;

        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

//    public static void GAME_SEND_WEDDING(GameMap c, int action, int homme, int femme, int parlant) {
//        String packet = "GA;" + action + ";" + homme + ";" + homme + "," + femme + "," + parlant;
//        Player Homme = World.getPersonnage(homme);
//        send(Homme, packet);
//        if (Ancestra.CONFIG_DEBUG) {
//            GameServer.addToSockLog("Game: Send>>" + packet);
//        }
//    }

    public static void GAME_SEND_PF(Player perso, String str) {
        String packet = "PF" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void REALM_SEND_RESULT_SEARCH(DofusClient out, String packet) {
        String toSend = (new StringBuilder("AF").append(packet)).toString();
        send(out, toSend);
    }

    public static void REALM_SEND_MESSAGE(Player P, int MSG_ID, String args) {
        String packet = "M0" + MSG_ID + "|" + args;
        send(P, packet);
    }

    public static void GAME_SEND_GA_ANIM_PACKET(Player perso, String packet_prepared, short cellid) {
        String packet = "GA0;228;" + perso.getSpriteId() + ";" + cellid + "," + packet_prepared;
        GameMap Map = perso.getMap();
        for (Player P : Map.getPersos()) {
            if (P.getFight() != null) {
                continue;
            }
            send(P, packet);
        }
    }

    public static void GAME_SEND_Ag_PACKET(DofusClient out, int idObjeto, String codObjeto) { //Cadeau � la connexion
        String packet = "Ag1|" + idObjeto
                + "|Cadeau Dofus| Voil� un joli cadeau pour vous ! "
                + "Un jeune aventurier comme vous saura s'en servir de la meilleure fa�on ! "
                + "Bonne continuation! |DOFUS|"
                + codObjeto;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            System.out.println("Game: Send>> " + packet);
        }
    }

    public static void GAME_SEND_AGK_PACKET(DofusClient out) { //Cadeau � la connexion
        String packet = "AGK";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            System.out.println("Game: Send>> " + packet);
        }
    }

    public static void SEND_QUESTS_LIST_PACKET(Player P) {
        //String packet = "QL|209;0;1|92;1;1";
//        String packet = P.parseQuestsList();
//        if (packet.isEmpty()) {
//            return;
//        }
//
//        send(P, packet);
//
//        if (Ancestra.CONFIG_DEBUG) {
//            GameServer.addToSockLog("Game: Send>> " + packet);
//        }
    }

    public static void SEND_QUEST_STEPS_PACKET(Player P, int questId) {
        //String packet = "QS209|386|830,1;831,1;832,0;833,0;834,0||387|417";

//        String packet = P.parseQuestStep(questId);
//        if (packet.isEmpty()) {
//            return;
//        }
//
//        send(P, packet);
//
//        if (Ancestra.CONFIG_DEBUG) {
//            GameServer.addToSockLog("Game: Send>> " + packet);
//        }
    }

    public static void GAME_SEND_GA_CLEAR_PACKET_TO_FIGHT(final Fight fight, final int teams) {
        String packet = "GA;0";
        for (final Fighter f : fight.getFighters(teams)) {
            if (f.getPlayer() == null
                    || !f.getPlayer().isOnline()) {
                continue;
            }
            send(f.getPlayer(), packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Fight: Send>>" + packet.toString());
        }
    }

    /**
     * energie *
     */
    public static void GAME_SEND_INFO_HIGHLIGHT_PACKET(Player perso, String args) {
        String packet = "IH" + args;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void MESSAGE_BOX(DofusClient out, String args) {
        String packet = "M" + args;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GDC_TO_MAP(GameMap target, String cellids, boolean isWalkable) // Interrupteur : rend cellule marchable
    {
        String[] cellSplit = cellids.split(",");
        String packet = "GDC|";

        for (String cell : cellSplit) {
            String datas = isWalkable ? "aaGaaaaaaa801" : "aaaaaaaaaa801";
            packet += cell + ";" + datas + ";1|";
        }

        for (Player p : target.getPersos()) {
            send(p, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_GDC_TO_PERSO(Player target, String cellids, boolean isWalkable) // Interrupteur : rend cellule marchable
    {
        String[] cellSplit = cellids.split(",");
        String packet = "GDC|";

        for (String cell : cellSplit) {
            String datas = isWalkable ? "aaGaaaaaaa801" : "aaaaaaaaaa801";
            packet += cell + ";" + datas + ";1|";
        }

        send(target, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_SWITCH_GDF_TO_MAP(MapCell caseTarget, GameMap mapTarget) // Interrupteur : ouvre ou ferme une porte
    {
        String packet = "GDF|" + caseTarget.getID() + ";" + caseTarget.getStatus() + ";0";

        for (Player p : mapTarget.getPersos()) {
            send(p, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    public static void GAME_SEND_SWITCH_GDF_TO_PERSO(MapCell caseTarget, Player target) // Interrupteur : ouvre ou ferme une porte
    {
        String packet = "GDF|" + caseTarget.getID() + ";3;0";

        send(target, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + packet);
        }
    }

    /**
     * prisme *
     */

    public static void SEND_CP_INFO_DEFENSEURS_PRISME(Player perso, String str) {
        String packet = "CP" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void SEND_Cp_INFO_ATTAQUANT_PRISME(Player perso, String str) {
        String packet = "Cp" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void GAME_SEND_am_ALIGN_PACKET_TO_SUBAREA(Player perso, String str) {
        String packet = "am" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void GAME_SEND_aM_ALIGN_PACKET_TO_AREA(Player perso, String str) {
        String packet = "aM" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void SEND_Cb_CONQUETE(Player perso, String str) {
        String packet = "Cb" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void SEND_GA_ALL_MAPS(GameMap Carte, String gameActionID, int actionID, String s1, String s2) {
        String packet = "GA" + gameActionID + ";" + actionID + ";" + s1;
        if (!s2.equals("")) {
            packet += ";" + s2;
        }
        for (Player z : Carte.getPersos()) {
            send(z, packet);
        }
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void SEND_CIV_INFOS_CONQUETE(Player perso) {
        String packet = "CIV";
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void SEND_CW_INFO_CONQUETE(Player perso, String str) {
        String packet = "CW" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void SEND_CB_BONUS_CONQUETE(Player perso, String str) {
        String packet = "CB" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void SEND_CA_ATTAQUE_MESSAGE_PRISME(Player perso, String str) {
        String packet = "CA" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void SEND_CS_SURVIVRE_MESSAGE_PRISME(Player perso, String str) {
        String packet = "CS" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void SEND_CD_MORT_MESSAGE_PRISME(Player perso, String str) {
        String packet = "CD" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void SEND_CIJ_INFO_JOIN_PRISME(Player perso, String str) {
        String packet = "CIJ" + str;
        send(perso, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }

    }

    public static void GAME_SEND_OCO_PACKET_REMOVE(Player out, Item obj) {
//        String packet = "OCO" + obj.parseItem() + "*" + obj.getGuid();
//        send(out, packet);
//        if (Ancestra.CONFIG_DEBUG) {
//            GameServer.addToLog((new StringBuilder("Game: Send>>")).append(packet).toString());
//        }
    }

    public static void GAME_SEND_CREATE_DOC(Player out, String doc) {
        String packet = "dC|" + doc;
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GAME_SEND_LEAVE_DOC(Player out) {
        String packet = "dV";
        send(out, packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Send>>" + packet);
        }
    }

    public static void GameSendGDFToMap(Player perso, GameMap map, String Inf) {
        String Packet = "GDF|" + Inf;
        send(perso, Packet);
        if (Ancestra.CONFIG_DEBUG) {
            GameServer.addToSockLog("Game: Map: Send>>" + Packet);
        }

    }

}
