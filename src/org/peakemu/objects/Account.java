package org.peakemu.objects;

import org.peakemu.objects.player.Player;
import org.peakemu.common.CryptManager;
import org.peakemu.game.GameClient;

import java.util.ArrayList;
import java.util.Collection;


import org.peakemu.realm.RealmClient;

public class Account {

    private int id;
    private String name;
    private String pass;
    private String pseudo;
    private String key;
    private String _lastIP = "";
    private String answer;
    private String response;
    private boolean banned = false;
    private int gmLvl = 0;
    private int _vip = 0;
    private String _curIP = "";
    private String _lastConnectionDate = "";
    private GameClient _gameThread;
    private RealmClient _realmThread;
    final private FriendList friendList = new FriendList(this);
    private long endMuteTime = 0;
    public int _position = -1;//Position du joueur
	//public int _muteTime;
    //public long _muteTime2 = 0;
    private int _cadeau; //Cadeau � la connexion
    final private Bank bank;

    //private Map<Integer,ArrayList<HdvEntry>> _hdvsItems;// Contient les items des HDV format : <hdvID,<cheapestID>>
    final private Collection<Player> players = new ArrayList<>();

    public Account(int aGUID, String aName, String aPass, String aPseudo, String aQuestion, String aReponse, int aGmLvl, int vip, boolean aBanned, String aLastIp, String aLastConnectionDate, Bank bank, int cadeau) {
        this.id = aGUID;
        this.name = aName;
        this.pass = aPass;
        this.pseudo = aPseudo;
        this.answer = aQuestion;
        this.response = aReponse;
        this.gmLvl = aGmLvl;
        this._vip = vip;
        this.banned = aBanned;
        this._lastIP = aLastIp;
        this._lastConnectionDate = aLastConnectionDate;
        this.bank = bank;
        this._cadeau = cadeau;
    }

    public boolean isMuted() {
        return endMuteTime < System.currentTimeMillis();
    }
    
    public void mute(int sec){
        endMuteTime = System.currentTimeMillis() + sec * 1000;
    }

    public int getCadeau() {
        return _cadeau;
    }

    public void setCadeau() {
        _cadeau = 0;
    }

    public void setCadeau(int cadeau) {
        _cadeau = cadeau;
    }

    public void setGameThread(GameClient t) {
        _gameThread = t;
    }

    public void setCurIP(String ip) {
        _curIP = ip;
    }

    public String getLastConnectionDate() {
        return _lastConnectionDate;
    }

    public void setLastIP(String _lastip) {
        _lastIP = _lastip;
    }

    public void setLastConnectionDate(String connectionDate) {
        _lastConnectionDate = connectionDate;
    }

    public GameClient getGameThread() {
        return _gameThread;
    }

    public RealmClient getRealmThread() {
        return _realmThread;
    }

    public Bank getBank() {
        return bank;
    }

    public int get_GUID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String get_pass() {
        return pass;
    }

    public String get_pseudo() {
        return pseudo;
    }

    public String get_key() {
        return key;
    }

    public void setClientKey(String aKey) {
        key = aKey;
    }

    public Collection<Player> gePlayers() {
        return players;
    }
    
    public Player getCharacterById(int id){
        for(Player player : players){
            if(player.getId() == id)
                return player;
        }
        return null;
    }
    
    public void removePlayer(Player player){
        players.remove(player);
    }

    public String get_lastIP() {
        return _lastIP;
    }

    public String get_question() {
        return answer;
    }

    public Player getCurPlayer() {
        if (getGameThread() == null) {
            return null;
        }

        return getGameThread().getPlayer();
    }

    public String get_reponse() {
        return response;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean isOnline() {
        if (_gameThread != null) {
            return true;
        }
        if (_realmThread != null) {
            return true;
        }
        return false;
    }

    public int get_gmLvl() {
        return gmLvl;
    }

    public String get_curIP() {
        return _curIP;
    }

    public boolean isValidPass(String pass, String hash) {
        String clientPass = CryptManager.decryptPass(pass.substring(2), hash);

        clientPass = CryptManager.CryptSHA512(clientPass);

        return clientPass.equals(pass);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void setRealmThread(RealmClient thread) {
        _realmThread = thread;
    }

    public void updateInfos(int aGUID, String aName, String aPass, String aPseudo, String aQuestion, String aReponse, int aGmLvl, boolean aBanned) {
        this.id = aGUID;
        this.name = aName;
        this.pass = aPass;
        this.pseudo = aPseudo;
        this.answer = aQuestion;
        this.response = aReponse;
        this.gmLvl = aGmLvl;
        this.banned = aBanned;
    }

//	public void deconnexion()
//	{
//		_curPerso = null;
//		_gameThread = null;
//		_realmThread = null;
//		_curIP = "";
//		Database.LOG_OUT(getSpriteId(), 0);
//		resetAllChars(true);
//		Database.UPDATE_ACCOUNT_DATA(this);
//	}
//	public void resetAllChars(boolean save)
//	{
//		for(Player P : _persos.values())
//		{
//			//Si Echange avec un joueur
//			if(P.getCurExchange() != null)P.getCurExchange().cancel();
//			//Si en groupe
//			if(P.getGroup() != null)P.getGroup().leave(P);
//			
//			//Si en combat
//			if(P.getFight() != null)P.getFight().leftFight(P, null);
//			else//Si hors combat
//			{
//				P.getCell().removePlayer(P.getSpriteId());
//				if(P.getMap() != null && P.isOnline())SocketManager.GAME_SEND_ERASE_ON_MAP_TO_MAP(P.getMap(), P.getSpriteId());
//			}
//			P.set_Online(false);
//			//Reset des vars du perso
//			P.resetVars();
///*			if(save)
//				Database.SAVE_PERSONNAGE(P,true);*/
//			World.unloadPerso(P.getSpriteId());
//		}
//		_persos.clear();
//	}
    public void setGmLvl(int gmLvl) {
        this.gmLvl = gmLvl;
    }

    public int get_vip() {
        return _vip;
    }

//    @Deprecated
//	public boolean recoverItem(int ligneID, int amount)
//	{
//		if(_curPerso == null)
//			return false;
//		if(_curPerso.get_isTradingWith() >= 0)
//			return false;
//		
//		int hdvID = Math.abs(_curPerso.get_isTradingWith());//R�cup�re l'ID de l'HDV
//		
//		HdvEntry entry = null;
//		for(HdvEntry tempEntry : _hdvsItems.get(hdvID))//Boucle dans la liste d'entry de l'HDV pour trouver un entry avec le meme cheapestID que sp�cifi�
//		{
//			if(tempEntry.getLigneID() == ligneID)//Si la boucle trouve un objet avec le meme cheapestID, arrete la boucle
//			{
//				entry = tempEntry;
//				break;
//			}
//		}
//		if(entry == null)//Si entry == null cela veut dire que la boucle s'est effectu� sans trouver d'item avec le meme cheapestID
//			return false;
//		
//		_hdvsItems.get(hdvID).remove(entry);//Retire l'item de la liste des objets a vendre du compte
//
//		Item obj = entry.getObjet();
//		
//		boolean OBJ = _curPerso.addItem(obj,true);//False = Meme item dans l'inventaire donc augmente la qua
//		if(!OBJ)
//		{
//		}
//		
//		World.getHdv(hdvID).delEntry(entry);//Retire l'item de l'HDV
//			
//		return true;
//		//Hdv curHdv = World.getHdv(hdvID);
//		
//	}
//	
//	public HdvEntry[] getHdvItems(int hdvID)
//	{
//		if(_hdvsItems.get(hdvID) == null)
//			return new HdvEntry[1];
//		
//		HdvEntry[] toReturn = new HdvEntry[20];
//		for (int i = 0; i < _hdvsItems.get(hdvID).size(); i++)
//		{
//			toReturn[i] = _hdvsItems.get(hdvID).get(i);
//		}
//		return toReturn;
//	}
//	
//	public int countHdvItems(int hdvID)
//	{
//		if(_hdvsItems.get(hdvID) == null)
//			return 0;
//		
//		return _hdvsItems.get(hdvID).size();
//	}
    public FriendList getFriendList() {
        return friendList;
    }

    public void send(Object packet) {
        if (getGameThread() != null) {
            getGameThread().send(packet);
        }
    }
}
