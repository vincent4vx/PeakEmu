/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.action;

import org.peakemu.common.SocketManager;
import org.peakemu.game.GameClient;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.player.Player;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.MapCell;
import org.peakemu.world.World;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.handler.ItemHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Stalking implements ActionPerformer {
    final private ItemHandler itemHandler;

    public Stalking(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public int actionId() {
        return 50;
    }

    @Override
    public boolean apply(Action action, Player caster, Player target, MapCell cell, Item item) {
        if (caster.get_traque() == null) {
            Player.traque traq = new Player.traque(0, null);
            caster.set_traque(traq);
        }
        if (caster.getLevel() < 50) {
            SocketManager.GAME_SEND_Im_PACKET(caster, "13");
            return false;
        }
        if (caster.get_traque().get_time() < System.currentTimeMillis() - 600000 || caster.get_traque().get_time() == 0) {
            Player tempP = null;
            int tmp = 15;
            int diff = 0;
            for (GameClient GT : World.getInstance().getSessionHandler().getGameClients()) {
                Player P = GT.getPlayer();
                if (P == null || P == caster) {
                    continue;
                }
                if (P.getAccount().get_curIP().compareTo(caster.getAccount().get_curIP()) == 0) {
                    continue;
                }
                //SI pas s�riane ni neutre et si alignement oppos�
                if (P.getAlignement() == caster.getAlignement() || P.getAlignement() == 0 || P.getAlignement() == 3) {
                    continue;
                }

                if (P.getLevel() > caster.getLevel()) {
                    diff = P.getLevel() - caster.getLevel();
                }
                if (caster.getLevel() > P.getLevel()) {
                    diff = caster.getLevel() - P.getLevel();
                }
                if (diff < tmp) {
                    tempP = P;
                }
                tmp = diff;
            }
            if (tempP == null) {
                SocketManager.GAME_SEND_MESSAGE(caster, "Nous n'avons pas trouve de cible a ta hauteur. Reviens plus tard.", "000000");
                return false;
            }

            SocketManager.GAME_SEND_MESSAGE(caster, "Vous etes desormais en chasse de " + tempP.getName() + ".", "000000");

            caster.get_traque().set_traqued(tempP);
            caster.get_traque().set_time(System.currentTimeMillis());

            ItemTemplate T = itemHandler.getTemplateById(10085);
            
            if (T == null) {
                return false;
            }
            caster.removeByTemplateID(T.getID(), 100);

            Item newObj = itemHandler.createNewItem(T, 20, false);
					//On ajoute le nom du type � recherch�

            int alignid = tempP.getAlignement();
            String align = "";
            switch (alignid) {
                case 0:
                    align = "Neutre";
                    break;
                case 1:
                    align = "Bontarien";
                    break;
                case 2:
                    align = "Brakmarien";
                    break;
                case 3:
                    align = "Seriane";
                    break;
            }
					// parcho de type:
            //nom
            //alignement
            //grade
            //Niveau
            newObj.getStatsTemplate().setText(Effect.NAME, tempP.getName());
            newObj.getStatsTemplate().setText(Effect.ALIGN, align);
            newObj.getStatsTemplate().setText(Effect.GRADE, Integer.toString(tempP.getGrade()));
            newObj.getStatsTemplate().setText(Effect.LEVEL, Integer.toString(tempP.getLevel()));

            caster.getItems().addItem(newObj);

        } else {
            SocketManager.GAME_SEND_MESSAGE(caster, "Thomas Sacre : Vous venez juste de signer un contrat, vous devez vous reposer.", "000000");
        }

        return true;
    }

}
