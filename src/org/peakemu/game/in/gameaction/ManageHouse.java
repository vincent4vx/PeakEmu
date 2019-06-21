/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.in.gameaction;

//import org.peakemu.objects.House;
import org.peakemu.objects.player.Player;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ManageHouse implements GameAction{
    final public static int ACTION_ID = 507;

    @Override
    public void start(GameActionArg arg) {
//        Player player = arg.getClient().getPlayer();
//        
//        int actionID = Integer.parseInt(arg.getArg());
//        House h = player.getInHouse();
//        if (h == null) {
//            return;
//        }
//        switch (actionID) {
//            case 81://Vérouiller maison
//                h.Lock(player);
//                break;
//            case 97://Acheter maison
//                h.BuyIt(player);
//                break;
//            case 98://Vendre
//            case 108://Modifier prix de vente
//                h.SellIt(player);
//                break;
//        }
    }

    @Override
    public void end(GameActionArg arg, boolean success, String args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int actionId() {
        return ACTION_ID;
    }
    
}
