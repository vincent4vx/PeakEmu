package org.peakemu.objects;

import org.peakemu.objects.item.Item;
import org.peakemu.world.NPCTemplate;
import org.peakemu.objects.player.Player;
import org.peakemu.game.GameServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.peakemu.database.Database;
import org.peakemu.common.SocketManager;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.World;
import org.peakemu.world.World.Couple;

public class NPC_Exchange
{
		private Player perso;
		private NPCTemplate npc;
		private long kamasPerso = 0;
		private long kamasNpc = 0;
		private ArrayList<Couple<Integer,Integer>> itemsPerso = new ArrayList<Couple<Integer,Integer>>();
		private ArrayList<Couple<Integer,Integer>> itemsNpc = new ArrayList<Couple<Integer,Integer>>();
		private boolean okPerso;
		private boolean okNpc;

		public NPC_Exchange(Player p, NPCTemplate npcTmpl)
		{
			perso = p;
			npc = npcTmpl;
		}
		
		synchronized public long getKamas(boolean isNpc)
		{			
			if(isNpc)
				return kamasNpc;
			else
				return kamasPerso;
		}
		
		synchronized public void toogleOK(boolean isNpc)
		{
			if(isNpc)
			{
				okNpc = !okNpc;
				SocketManager.GAME_SEND_EXCHANGE_OK(perso.getAccount().getGameThread(),okNpc);
			} else
			{
				okPerso = !okPerso;
				SocketManager.GAME_SEND_EXCHANGE_OK(perso.getAccount().getGameThread(),okPerso,perso.getSpriteId());
			}

			if(okNpc && okPerso)
				apply();
		}
		
		synchronized public void setKamas(boolean isNpc, long k)
		{
			if(k < 0) return;
			okNpc = okPerso = false;
			
			SocketManager.GAME_SEND_EXCHANGE_OK(perso.getAccount().getGameThread(),okPerso,perso.getSpriteId());
			SocketManager.GAME_SEND_EXCHANGE_OK(perso.getAccount().getGameThread(),okNpc);
			
			if(isNpc)
			{
				kamasNpc = k;
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(perso.getAccount().getGameThread(), 'G', "", k+"");
			}else 
			{
				if(k > perso.getKamas()) return;
				kamasPerso = k;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(perso, 'G', "", k+"");	
			}
		}
		
		synchronized public void cancel()
		{
			if(perso.getAccount() != null)
				if(perso.getAccount().getGameThread() != null)
					SocketManager.GAME_SEND_EV_PACKET(perso.getAccount().getGameThread());
			
			perso.set_isTradingWith(0);
			perso.setCurNpcExchange(null);
		}
		
		public synchronized void apply()
		{
			//Gestion des Kamas
			perso.addKamas((-kamasPerso+kamasNpc));
			for(Couple<Integer, Integer> couple : itemsPerso)
			{
				if(couple.second == 0)continue;
				if(!perso.hasItemGuid(couple.first))//Si le perso n'a pas l'item (Ne devrait pas arriver)
				{
					couple.second = 0;//On met la quantit� a 0 pour �viter les problemes
					continue;
				}	
				Item obj = perso.getItems().get(couple.first);
				if((obj.getQuantity() - couple.second) <1)//S'il ne reste plus d'item apres l'�change
				{
					perso.removeItem(couple.first);
					couple.second = obj.getQuantity();
					SocketManager.GAME_SEND_REMOVE_ITEM_PACKET(perso, couple.first);
				}else
				{
					obj.setQuantity(obj.getQuantity()-couple.second);
					SocketManager.GAME_SEND_OBJECT_QUANTITY_PACKET(perso, obj);
					//Objet newObj = Item.getCloneObjet(obj, couple.second);
				}
			}
			for(Couple<Integer, Integer> couple : itemsNpc) // Ajout des items du npc
			{
//				if(couple.second == 0)continue;
//				ItemTemplate T = World.getObjTemplate(couple.first);
//				if(T == null)return;
//				Item O = T.createNewItem(couple.second, false);
//				//Si retourne true, on l'ajoute au monde
//				perso.addObjet(O, true);
//				
//				SocketManager.GAME_SEND_Im_PACKET(perso, "021;"+couple.second+"~"+couple.first);
			}
			//Fin
			perso.set_isTradingWith(0);
			perso.setCurNpcExchange(null);
			SocketManager.GAME_SEND_EXCHANGE_VALID(perso.getAccount().getGameThread(),'a');
			SocketManager.GAME_SEND_Ow_PACKET(perso);	
//			Database.SAVE_PERSONNAGE(perso,true);
		}
		

		synchronized public void addItem(int guid, int qua)
		{
			if(qua < 1) return;
			Item obj = perso.getItems().get(guid);
			if(obj == null)return;
			okNpc = okPerso = false;
			SocketManager.GAME_SEND_EXCHANGE_OK(perso.getAccount().getGameThread(),okPerso,perso.getSpriteId());
			SocketManager.GAME_SEND_EXCHANGE_OK(perso.getAccount().getGameThread(),okNpc);
			
			String str = guid+"|"+qua;
		
			Couple<Integer,Integer> couple = getCoupleInList(itemsPerso,guid);
			if(couple != null) // Item déjà dans l'échange, on augmente sa quantité
			{
				couple.second += qua;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(perso, 'O', "+", ""+guid+"|"+couple.second);
				isItCraft();
				return;
			}
			itemsPerso.add(new Couple<Integer,Integer>(guid,qua));
			SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(perso, 'O', "+", str);
			
			isItCraft(); // On check si on peut faire un craft
		}

		
		synchronized public void removeItem(int guid, int qua) // Uniqumenet perso
		{
			if(qua < 0) return;
			okPerso = okNpc = false;
			SocketManager.GAME_SEND_EXCHANGE_OK(perso.getAccount().getGameThread(),okPerso,perso.getSpriteId());
			SocketManager.GAME_SEND_EXCHANGE_OK(perso.getAccount().getGameThread(),okNpc);

			Item obj = perso.getItems().get(guid);
			if(obj == null) { GameServer.addToLog("Erreur Npc_Exchange : Delete d'un item inexistant "); return; }
				
			Couple<Integer,Integer> couple = getCoupleInList(itemsPerso,guid);
			int newQua = couple.second - qua;
				
			if(newQua <1)//Si il n'y a pu d'item
			{
				itemsPerso.remove(couple);
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(perso, 'O', "-", ""+guid);
			}else
			{
				couple.second = newQua;
				SocketManager.GAME_SEND_EXCHANGE_MOVE_OK(perso, 'O', "+", ""+guid+"|"+newQua);
			}
			
			isItCraft();
			
		}

		synchronized private Couple<Integer, Integer> getCoupleInList(ArrayList<Couple<Integer, Integer>> items,int guid)
		{
			for(Couple<Integer, Integer> couple : items)
			{
				if(couple.first == guid)
					return couple;
			}
			return null;
		}

		public synchronized int getQuaItem(int itemID, boolean isNpc)
		{
			ArrayList<Couple<Integer, Integer>> items;
			if(isNpc)
				items = itemsNpc;
			else
				items = itemsPerso;
			
			for(Couple<Integer, Integer> curCoupl : items)
			{
				if(curCoupl.first == itemID)
				{
					return curCoupl.second;
				}
			}
			
			return 0;
		}
		
		synchronized public void isItCraft()
		{ 
			if(itemsPerso.size() < 1) return;
			Map<Integer,Integer> ingredients = new HashMap<Integer, Integer>();
			for(Couple<Integer, Integer> ingredient : itemsPerso) // Transformation itemsPerso en Map
			{
				Item obj = perso.getItems().get(ingredient.first);
				if(obj == null ) continue;
				ingredients.put(obj.getTemplate().getID(),ingredient.second);
			}
			Couple<Integer, Integer> craftResult = World.getObjectByIngredient(ingredients,npc.get_id());
			//int tID = World.getObjectByIngredient(ingredients,npc.get_id());
			clearNpcItems(); // Dans tous les cas on retire les items du npc
			
			if(craftResult != null) // Un objet correspond
			{
				// Ajout des nouveaux items
				itemsNpc.add(new Couple<Integer, Integer>(craftResult.first, craftResult.second));
				for(Couple<Integer, Integer> curNpcItem : itemsNpc) // Envoi packet d'ajout d'item
				{
					ItemTemplate newObj = World.getObjTemplate(curNpcItem.first);
					String str = curNpcItem.first+"|"+curNpcItem.second;
					String add = "|"+newObj.getID()+"|"+newObj.getStrTemplate();
					SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(perso.getAccount().getGameThread(), 'O', "+", str+add);
				}
				
				toogleOK(true); // Pnj accepte
			}
		}
		
		synchronized public void clearNpcItems()
		{
			for(Couple<Integer, Integer> curItem : itemsNpc)
			{
				SocketManager.GAME_SEND_EXCHANGE_OTHER_MOVE_OK(perso.getAccount().getGameThread(), 'O', "-", ""+curItem.first);
			}
			itemsNpc.clear();
		}
}