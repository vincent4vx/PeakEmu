package org.peakemu.database;

import org.peakemu.Ancestra;
import org.peakemu.objects.Fireworks;
import org.peakemu.objects.Account;
import org.peakemu.world.GameMap;
import org.peakemu.world.MonsterTemplate;
import org.peakemu.objects.player.Player;
import org.peakemu.objects.item.Item;
import org.peakemu.game.GameServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;

import org.peakemu.common.Constants;
import org.peakemu.common.SocketManager;
import org.peakemu.world.World;
import org.peakemu.world.World.*;
import fr.quatrevieux.crisis.config.ConfigException;
import fr.quatrevieux.crisis.config.RootConfig;
import org.peakemu.realm.RealmServer;
import org.peakemu.world.ItemTemplate;

public class Database {

    final private DatabaseConfig config;
    final private Connection connection;

    private static Connection othCon;
    private static Connection statCon;

    private static Timer timerCommit;
    private static boolean needCommit;

    public Database(RootConfig rootConfig) throws InstantiationException, IllegalAccessException, ConfigException, SQLException {
        config = rootConfig.getPackage(DatabaseConfig.class);

        connection = DriverManager.getConnection(
                "jdbc:mysql://" + config.getHost() + "/" + config.getDbname(),
                config.getUser(),
                config.getPassword()
        );
        connection.setAutoCommit(true);

        //for ancestra compatibility
        othCon = connection;
        statCon = connection;
    }
    
    synchronized public PreparedStatement prepare(String query) throws SQLException{
        return connection.prepareStatement(query);
    }
    
    synchronized public PreparedStatement prepareInsert(String query) throws SQLException{
        return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }
    
    synchronized public ResultSet query(String sql) throws SQLException{
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    @Deprecated
    public synchronized static ResultSet executeQuery(String query) throws SQLException {
        if (!Ancestra.isInit) {
            return null;
        }

        Connection DB = othCon;

        Statement stat = DB.createStatement();
        ResultSet RS = stat.executeQuery(query);
        stat.setQueryTimeout(300);
        return RS;
    }

    @Deprecated
    public synchronized static PreparedStatement newTransact(String baseQuery, Connection dbCon) throws SQLException {
        PreparedStatement toReturn = (PreparedStatement) dbCon.prepareStatement(baseQuery);

        needCommit = true;
        return toReturn;
    }

    @Deprecated
    private static void closeResultSet(ResultSet RS) {
        try {
            RS.getStatement().close();
            RS.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Deprecated
    private static void closePreparedStatement(PreparedStatement p) {
        try {
            p.clearParameters();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void LOAD_DROPS() {
        try {
            ResultSet RS = Database.executeQuery("SELECT * from drops;");
            while (RS.next()) {
                MonsterTemplate MT = World.getMonstre(RS.getInt("mob"));
                MT.addDrop(new Drop(
                        RS.getInt("item"),
                        RS.getInt("seuil"),
                        RS.getFloat("taux"),
                        RS.getInt("max")
                ));
            }
            closeResultSet(RS);
        } catch (SQLException e) {
            RealmServer.addToLog("SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void UPDATE_LASTCONNECTION_INFO(Account compte) {
        String baseQuery = "UPDATE accounts SET "
                + "`lastIP` = ?,"
                + "`lastConnectionDate` = ?"
                + " WHERE `guid` = ?;";

        try {
            PreparedStatement p = newTransact(baseQuery, othCon);

            p.setString(1, compte.get_curIP());
            p.setString(2, compte.getLastConnectionDate());
            p.setInt(3, compte.get_GUID());

            p.executeUpdate();
            closePreparedStatement(p);
        } catch (SQLException e) {
            RealmServer.addToLog("SQL ERROR: " + e.getMessage());
            RealmServer.addToLog("Query: " + baseQuery);
            e.printStackTrace();
        }
    }

    public static boolean SAVE_TRIGGER(int mapID1, int cellID1, int action, int event, String args, String cond) {
        String baseQuery = "REPLACE INTO `scripted_cells`"
                + " VALUES (?,?,?,?,?,?);";

        try {
            PreparedStatement p = newTransact(baseQuery, statCon);
            p.setInt(1, mapID1);
            p.setInt(2, cellID1);
            p.setInt(3, action);
            p.setInt(4, event);
            p.setString(5, args);
            p.setString(6, cond);

            p.execute();
            closePreparedStatement(p);
            return true;
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
        return false;
    }

    public static boolean REMOVE_TRIGGER(int mapID, int cellID) {
        String baseQuery = "DELETE FROM `scripted_cells` WHERE "
                + "`MapID` = ? AND "
                + "`CellID` = ?;";
        try {
            PreparedStatement p = newTransact(baseQuery, statCon);
            p.setInt(1, mapID);
            p.setInt(2, cellID);

            p.execute();
            closePreparedStatement(p);
            return true;
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
        return false;
    }

    public static boolean SAVE_MAP_DATA(GameMap map) {
        String baseQuery = "UPDATE `maps` SET "
                + "`places` = ?, "
                + "`numgroup` = ? "
                + "WHERE id = ?;";

        try {
            PreparedStatement p = newTransact(baseQuery, statCon);
            p.setString(1, map.get_placesStr());
            p.setInt(2, map.getMaxGroupNumb());
            p.setInt(3, map.getId());

            p.executeUpdate();
            closePreparedStatement(p);
            return true;
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
        return false;
    }

    public static boolean DELETE_NPC_ON_MAP(int m, int c) {
        String baseQuery = "DELETE FROM npcs WHERE mapid = ? AND cellid = ?;";
        try {
            PreparedStatement p = newTransact(baseQuery, statCon);
            p.setInt(1, m);
            p.setInt(2, c);

            p.execute();
            closePreparedStatement(p);
            return true;
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
        return false;
    }

    public static boolean ADD_ENDFIGHTACTION(int mapID, int type, int Aid, String args, String cond) {
        if (!DEL_ENDFIGHTACTION(mapID, type, Aid)) {
            return false;
        }
        String baseQuery = "INSERT INTO `endfight_action` "
                + "VALUES (?,?,?,?,?);";
        try {
            PreparedStatement p = newTransact(baseQuery, statCon);
            p.setInt(1, mapID);
            p.setInt(2, type);
            p.setInt(3, Aid);
            p.setString(4, args);
            p.setString(5, cond);

            p.execute();
            closePreparedStatement(p);
            return true;
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
        return false;
    }

    public static boolean DEL_ENDFIGHTACTION(int mapID, int type, int aid) {
        String baseQuery = "DELETE FROM `endfight_action` "
                + "WHERE map = ? AND "
                + "fighttype = ? AND "
                + "action = ?;";
        try {
            PreparedStatement p = newTransact(baseQuery, statCon);
            p.setInt(1, mapID);
            p.setInt(2, type);
            p.setInt(3, aid);

            p.execute();
            closePreparedStatement(p);
            return true;
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
            return false;
        }
    }

    public static void DEL_GUILD(int id) {
        String baseQuery = "DELETE FROM `guilds` "
                + "WHERE `id` = ?;";
        try {
            PreparedStatement p = newTransact(baseQuery, othCon);
            p.setInt(1, id);

            p.execute();
            closePreparedStatement(p);
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
    }

    public static void DEL_ALL_GUILDMEMBER(int guildid) {
        String baseQuery = "DELETE FROM `guild_members` "
                + "WHERE `guild` = ?;";
        try {
            PreparedStatement p = newTransact(baseQuery, othCon);
            p.setInt(1, guildid);

            p.execute();
            closePreparedStatement(p);
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
    }

    public static void DEL_GUILDMEMBER(int id) {
        String baseQuery = "DELETE FROM `guild_members` "
                + "WHERE `guid` = ?;";
        try {
            PreparedStatement p = newTransact(baseQuery, othCon);
            p.setInt(1, id);

            p.execute();
            closePreparedStatement(p);
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
    }

    public static boolean ADD_REPONSEACTION(int repID, int type, String args) {
        String baseQuery = "DELETE FROM `npc_reponses_actions` "
                + "WHERE `ID` = ? AND "
                + "`type` = ?;";
        PreparedStatement p;
        try {
            p = newTransact(baseQuery, statCon);
            p.setInt(1, repID);
            p.setInt(2, type);

            p.execute();
            closePreparedStatement(p);
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
        baseQuery = "INSERT INTO `npc_reponses_actions` "
                + "VALUES (?,?,?);";
        try {
            p = newTransact(baseQuery, statCon);
            p.setInt(1, repID);
            p.setInt(2, type);
            p.setString(3, args);

            p.execute();
            closePreparedStatement(p);
            return true;
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
        return false;
    }

    public static boolean UPDATE_INITQUESTION(int id, int q) {
        String baseQuery = "UPDATE `npc_template` SET "
                + "`initQuestion` = ? "
                + "WHERE `id` = ?;";
        try {
            PreparedStatement p = newTransact(baseQuery, statCon);
            p.setInt(1, q);
            p.setInt(2, id);

            p.execute();
            closePreparedStatement(p);
            return true;
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
        return false;
    }

    public static boolean UPDATE_NPCREPONSES(int id, String reps) {
        String baseQuery = "UPDATE `npc_questions` SET "
                + "`responses` = ? "
                + "WHERE `ID` = ?;";
        try {
            PreparedStatement p = newTransact(baseQuery, statCon);
            p.setString(1, reps);
            p.setInt(2, id);

            p.execute();
            closePreparedStatement(p);
            return true;
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
        return false;
    }

    public static void LOAD_ACTION() {
        /*Variables repr�sentant les champs de la base*/
        Player perso;
        int action;
        int nombre;
        int id;
        int quantite;
        Ancestra.addToShopLog("Lancement de l'application des Lives Actions ...");
        String sortie;
        String couleur = "DF0101"; //La couleur du message envoyer a l'utilisateur (couleur en code HTML)
        ItemTemplate t;
        Item obj;
        PreparedStatement p;
        /*FIN*/
        try {
            ResultSet RS = executeQuery("SELECT * from live_action;");

            while (RS.next()) {
                perso = World.getPersonnage(RS.getInt("PlayerID"));
                if (perso == null) {
                    Ancestra.addToShopLog("Personnage " + RS.getInt("PlayerID") + " non trouve, personnage non charge ?");
                    continue;
                }
                if (!perso.isOnline()) {
                    Ancestra.addToShopLog("Personnage " + RS.getInt("PlayerID") + " hors ligne");
                    continue;
                }
                if (perso.getAccount() == null) {
                    Ancestra.addToShopLog("Le Personnage " + RS.getInt("PlayerID") + " n'est attribue a aucun compte charge");
                    continue;
                }
                if (perso.getAccount().getGameThread() == null) {
                    Ancestra.addToShopLog("Le Personnage " + RS.getInt("PlayerID") + " n'a pas thread associe, le personnage est il hors ligne ?");
                    continue;
                }
                if (perso.getFight() != null) {
                    continue; // Perso en combat  @ Nami-Doc
                }
                action = RS.getInt("Action");
                nombre = RS.getInt("Nombre");
                id = RS.getInt("ID");
                quantite = RS.getInt("Quantite");
                sortie = "+";

                switch (action) {
//                    case 20:	//Ajouter un item avec des jets al�atoire
//                        t = World.getObjTemplate(nombre);
//                        if (t == null) {
//                            continue;
//                        }
//                        obj = t.createNewItem(quantite, true); //Si mis � "true" l'objet � des jets max. Sinon ce sont des jets al�atoire
//                        //obj = t.createNewItem(1,false); //Si mis � "true" l'objet � des jets max. Sinon ce sont des jets al�atoire
//                        if (obj == null) {
//                            continue;
//                        }
//                        //Si le joueur n'avait pas d'item similaire
//                        if (perso.addItem(obj, true)) {
//                        }
//                        GameServer.addToSockLog("Objet " + nombre + " ajouter a " + perso.getName() + " avec des stats aleatoire");
//                        //SocketManager.GAME_SEND_MESSAGE(perso,"L'objet \""+t.getName()+"\" vient d'�tre ajoute� � votre personnage",couleur);
//                        SocketManager.send(perso, "M1INFO_BOUTIQUE!<br><br> L'objet " + t.getName() + "(x" + quantite + ")" + " vient d'etre ajoute a votre personnage!");
//                        break;
//                    case 21:	//Ajouter un item avec des jets MAX
//                        t = World.getObjTemplate(nombre);
//                        if (t == null) {
//                            continue;
//                        }
//                        obj = t.createNewItem(quantite, true); //Si mis � "true" l'objet � des jets max. Sinon ce sont des jets al�atoire
//                        if (obj == null) {
//                            continue;
//                        }
//                        if (perso.addItem(obj, true))//Si le joueur n'avait pas d'item similaire
//                        {
//                        }
//                        GameServer.addToSockLog("Objet " + nombre + " ajoute a " + perso.getName() + " avec des stats MAX");
//                        //SocketManager.GAME_SEND_MESSAGE(perso,"L'objet \""+t.getName()+"\" avec des stats maximum, vient d'�tre ajoute� � votre personnage",couleur);
//                        SocketManager.send(perso, "M1INFO_BOUTIQUE!<br><br> L'objet " + t.getName() + "(x" + quantite + ")" + " vient d'etre ajoute a votre personnage!");
//                        break;
                }
                SocketManager.GAME_SEND_STATS_PACKET(perso);
                if (action < 20 || action > 100) {
                    SocketManager.GAME_SEND_MESSAGE(perso, sortie + " a votre personnage", couleur); //Si l'action n'est pas un ajout d'objet on envoye un message a l'utilisateur
                }
                Ancestra.addToShopLog("(Commande " + id + ")Action " + action + " Nombre: " + nombre + " appliquee sur le personnage " + RS.getInt("PlayerID") + "(" + perso.getName() + ")");
                try {
                    String query = "DELETE FROM live_action WHERE ID=" + id + ";";
                    p = newTransact(query, othCon);
                    p.execute();
                    closePreparedStatement(p);
                    Ancestra.addToShopLog("Commande " + id + " supprimee.");
                } catch (SQLException e) {
                    GameServer.addToLog("SQL ERROR: " + e.getMessage());
                    Ancestra.addToShopLog("Error Delete From: " + e.getMessage());
                    e.printStackTrace();
                }
                //Database.SAVE_PERSONNAGE(perso, true);
            }
            closeResultSet(RS);
        } catch (Exception e) {
            GameServer.addToLog("ERROR: " + e.getMessage());
            Ancestra.addToShopLog("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void LOG_OUT(int accID, int logged) {
        PreparedStatement p;
        String query = "UPDATE `accounts` SET logged=? WHERE `guid`=?;";
        try {
            p = newTransact(query, othCon);
            p.setInt(1, logged);
            p.setInt(2, accID);

            p.execute();
            closePreparedStatement(p);
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + query);
        }
    }

    public static void LOGGED_ZERO() {
        PreparedStatement p;
        String query = "UPDATE `accounts` SET logged=0;";
        try {
            p = newTransact(query, othCon);

            p.execute();
            closePreparedStatement(p);
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + query);
        }

    }

    public static int LOAD_BANIP() {
        int i = 0;
        try {
            ResultSet RS = executeQuery("SELECT ip from banip;");
            while (RS.next()) {
                Constants.BAN_IP += RS.getString("ip");
                if (!RS.isLast()) {
                    Constants.BAN_IP += ",";
                }
                i++;
            }
            closeResultSet(RS);
        } catch (SQLException e) {
            RealmServer.addToLog("SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return i;
    }

    public static boolean ADD_BANIP(String ip) {
        String baseQuery = "INSERT INTO `banip`"
                + " VALUES (?);";
        try {
            PreparedStatement p = newTransact(baseQuery, othCon);
            p.setString(1, ip);
            p.execute();
            closePreparedStatement(p);
            return true;
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            GameServer.addToLog("Game: Query: " + baseQuery);
        }
        return false;
    }

    public static void LOAD_FULLMORPHS() {
        try {
            ResultSet RS = executeQuery("SELECT * FROM `full_morphs`");

            while (RS.next()) {
                World.addFullMorph(RS.getInt("id"),
                        RS.getInt("stuffId"),
                        RS.getInt("gfxId"),
                        RS.getString("spells"),
                        RS.getString("name"));
            }

            closeResultSet(RS);
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void LOAD_QUESTS() {
        try {
            ResultSet RS = executeQuery("SELECT * FROM `quests`");

            while (RS.next()) {
                World.addQuest(RS.getInt("id"),
                        RS.getString("steps"),
                        RS.getInt("startQuestion"),
                        RS.getInt("endQuestion"),
                        RS.getInt("minLvl"),
                        RS.getInt("questRequired"));
            }

            closeResultSet(RS);
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void LOAD_QUEST_STEPS() {
        try {
            ResultSet RS = executeQuery("SELECT * FROM `quest_steps`");

            while (RS.next()) {
                World.addQuestStep(RS.getInt("id"),
                        RS.getString("objectives"),
                        RS.getInt("question"),
                        RS.getInt("gainExp"),
                        RS.getInt("gainKamas"),
                        RS.getString("gainItems"));
            }

            closeResultSet(RS);
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void LOAD_QUEST_OBJECTIVES() {
        try {
            ResultSet RS = executeQuery("SELECT * FROM `quest_objectives`");

            while (RS.next()) {
                World.addQuestObjective(RS.getInt("id"),
                        RS.getString("type"),
                        RS.getString("args"),
                        RS.getInt("optNpcTarget"),
                        RS.getInt("optQuestion"),
                        RS.getInt("optAnswer"));
            }

            closeResultSet(RS);
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void LOAD_FIREWORKS() {
        try {
            ResultSet RS = executeQuery("SELECT * from fireworks;");
            while (RS.next()) {
                World.addAnimation(new Fireworks(
                        RS.getInt("guid"),
                        RS.getInt("id"),
                        RS.getString("nom"),
                        RS.getInt("area"),
                        RS.getInt("action"),
                        RS.getInt("size")
                ));
            }
            closeResultSet(RS);
        } catch (SQLException e) {
            GameServer.addToLog("Game: SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Cadeau � la connexion
    public static void CADEAU_ACTUALISE(Account compte) {
        String baseQuery = "UPDATE accounts SET `cadeau` = 0 WHERE `guid` = ?;";
        try {
            PreparedStatement p = newTransact(baseQuery, othCon);
            p.setInt(1, compte.get_GUID());
            p.executeUpdate();
            closePreparedStatement(p);
        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
            System.out.println("Query: " + baseQuery);
            e.printStackTrace();
        }
    }// Fin cadeaux

	    //Morph item
    public static String getStatsTemplate(int templateID) {
        try {
            ResultSet RS = Database.executeQuery("SELECT statsTemplate FROM item_template WHERE id = " + templateID + ";");
            if (RS.first()) {
                return RS.getString("statsTemplate");
            }
            closeResultSet(RS);
        } catch (SQLException e) {
        }
        return "";
    }

}
