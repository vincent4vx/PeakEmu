/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.peakemu.command.Command;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.Formulas;
import org.peakemu.common.SocketManager;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.objects.player.Player;
import org.peakemu.world.World;
import org.peakemu.world.handler.SessionHandler;

/**
 *
 * @author Ludovic Sanctorum <ludovic.sanctorum@gmail.com>
 */
public class Shutdown implements Command {
    final private SessionHandler sessionHandler;

    public Shutdown(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    private Timer timer;
    private boolean timerStart = false;

    @Override
    public String name() {
        return "SHUTDOWN";
    }

    @Override
    public String shortDescription() {
        return "Eteint le serveur après X minutes";
    }

    @Override
    public String help() {
        return "SHUTDOWN [1|0] [time]";
    }

    @Override
    public int minLevel() {
        return 3;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        int time = 30, OffOn = 0;
        try {
            OffOn = Integer.parseInt(args[0]);
            time = Integer.parseInt(args[1]);
        } catch (Exception e) {
            performer.displayError("Erreur d'arguments");
            return;
        }

        if (OffOn == 1 && timerStart)// demande de dÃ©marer le reboot
        {
            performer.displayError("Un shutdown est deja programmé.");
        } else if (OffOn == 1 && !timerStart) {
            timer = createTimer(time);
            timer.start();
            timerStart = true;
            String timeMSG = "minutes";
            if (time <= 1) {
                timeMSG = "minute";
            }
            
            sessionHandler.sendToOnline(new InfoMessage(InfoMessage.Type.ERROR).addMessage(15, time + " " + timeMSG));
            
            performer.displayMessage("Shutdown lance.");
        } else if (OffOn == 0 && timerStart) {
            timer.stop();
            timerStart = false;
            performer.displayMessage("Shutdown arrete.");
        } else if (OffOn == 0 && !timerStart) {
            performer.displayMessage("Aucun shutdown n'est lance.");
        }
    }

    public Timer createTimer(final int time) {
        ActionListener action = new ActionListener() {
            int Time = (time * 60); //secondes
            int LastTime = 0;

            @Override
            public void actionPerformed(ActionEvent event) {
                Time--; //par seconde
                World._time = Time;
                LastTime++;
                if (Time > 0 && Time < (13 * 60 * 60) && LastTime >= Formulas.getShutDownTime(Time)) //Si il reste plus de 0 secondes, qu'il y a moins de 13h, et qu'il a attendu assez
                {
                    sessionHandler.sendToOnline(new InfoMessage(InfoMessage.Type.ERROR).addMessage(15, Formulas.getShutDownName(Time)));
                    LastTime = 0;
                }
                if (Time <= 0) {
                    System.exit(0);
                }
            }
        };
        return new Timer(1000, action);//1000
    }

}
