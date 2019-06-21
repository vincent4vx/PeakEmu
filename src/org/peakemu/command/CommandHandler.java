/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command;

import java.util.ArrayList;
import org.peakemu.command.level1.Who;
import org.peakemu.command.level1.Unmute;
import org.peakemu.command.level1.Unjail;
import org.peakemu.command.level1.Teleport;
import org.peakemu.command.level1.NameGo;
import org.peakemu.command.level1.Mute;
import org.peakemu.command.level1.MapInfo;
import org.peakemu.command.level1.Jail;
import org.peakemu.command.level0.Infos;
import org.peakemu.command.level1.GoName;
import org.peakemu.command.level1.GoMap;
import org.peakemu.command.level1.All;
import org.peakemu.command.level2.AddStat;
import org.peakemu.command.level2.AddItem;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.peakemu.Peak;
import org.peakemu.command.level0.Help;
import org.peakemu.command.level1.MP;
import org.peakemu.command.level2.AddGuildXp;
import org.peakemu.command.level2.Capital;
import org.peakemu.command.level3.DoAction;
import org.peakemu.command.level2.HPPer;
import org.peakemu.command.level2.Honor;
import org.peakemu.command.level2.Kamas;
import org.peakemu.command.level2.Kick;
import org.peakemu.command.level2.LearnJob;
import org.peakemu.command.level2.LearnSpell;
import org.peakemu.command.level2.LevelUp;
import org.peakemu.command.level2.Morph;
import org.peakemu.command.level2.SetAlign;
import org.peakemu.command.level2.Size;
import org.peakemu.command.level2.SpellPoints;
import org.peakemu.command.level2.Title;
import org.peakemu.command.level3.AddFightPos;
import org.peakemu.command.level3.AddNPC;
import org.peakemu.command.level3.AddTrigger;
import org.peakemu.command.level3.Ban;
import org.peakemu.command.level3.CreateGuild;
import org.peakemu.command.level3.DeleteFightPos;
import org.peakemu.command.level3.DeleteNPC;
import org.peakemu.command.level3.DeleteTrigger;
import org.peakemu.command.level3.Demorph;
import org.peakemu.command.level3.Exit;
import org.peakemu.command.level3.HideFightPos;
import org.peakemu.command.level3.Save;
import org.peakemu.command.level3.ShowFightPos;
import org.peakemu.command.level3.Shutdown;
import org.peakemu.command.level3.SpawnFix;
import org.peakemu.command.level3.ToogleAggro;
import org.peakemu.command.level3.Unban;
import org.peakemu.command.level3.UnsetMutant;
import org.peakemu.command.level4.AddBuffItem;
import org.peakemu.command.level4.BanIP;
import org.peakemu.command.level4.Block;
import org.peakemu.command.level3.FreeActionId;
import org.peakemu.command.level3.GetActionId;
import org.peakemu.command.level4.Gift;
import org.peakemu.command.level4.Lock;
import org.peakemu.command.level4.Move;
import org.peakemu.command.level4.RemAllBuffs;
import org.peakemu.command.level4.Send;
import org.peakemu.command.level4.SetAdmin;
import org.peakemu.command.level4.SetSeller;
import org.peakemu.command.level4.TestPath;
import org.peakemu.common.util.StringUtil;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
final public class CommandHandler {

    final private Map<String, Command> commands = new HashMap<>();

    public CommandHandler(Peak peak) {

        /**
         * lvl 0 *
         */
        registerCommand(new Infos(peak.getWorld().getSessionHandler()));
        registerCommand(new Help(this));

        /**
         * lvl 1
         */
        registerCommand(new All(peak.getWorld().getSessionHandler()));
        registerCommand(new GoMap(peak.getWorld().getSessionHandler(), peak.getWorld().getMapHandler(), peak.getWorld().getPlayerHandler()));
        GoName goName = new GoName(peak.getWorld().getSessionHandler(), peak.getWorld().getPlayerHandler());
        registerCommand(goName);
        registerCommand(new Alias("JOIN", goName));
        registerCommand(new Jail(peak.getWorld().getSessionHandler(), peak.getWorld().getPlayerHandler()));
        registerCommand(new MP(peak.getWorld().getSessionHandler()));
        registerCommand(new MapInfo());
        registerCommand(new Mute(peak.getWorld().getSessionHandler()));
        registerCommand(new NameGo(peak.getWorld().getSessionHandler(), peak.getWorld().getPlayerHandler()));
        registerCommand(new Teleport(peak.getWorld().getSessionHandler(), peak.getWorld().getMapHandler(), peak.getWorld().getPlayerHandler()));
        registerCommand(new Unjail(peak.getWorld().getSessionHandler(), peak.getWorld().getPlayerHandler()));
        registerCommand(new Unmute(peak.getWorld().getSessionHandler()));
        registerCommand(new Who(peak.getWorld().getSessionHandler()));

        /**
         * lvl 2 *
         */
        registerCommand(new AddItem(peak.getWorld().getItemHandler()));
        registerAlias("ITEM", "ADDITEM"); //Ancestra <3
        registerAlias("!getitem", "ADDITEM"); //offi
        //registerCommand(new AddJobXP()); // TODO
        registerCommand(new AddStat());
        registerCommand(new Capital(peak.getWorld().getSessionHandler()));
        registerCommand(new HPPer(peak.getWorld().getSessionHandler()));
        registerCommand(new Honor(peak.getWorld().getSessionHandler(), peak.getWorld().getPlayerHandler()));
        //registerCommand(new AddItemSet()); //TODO
        registerCommand(new Kamas((peak.getWorld().getSessionHandler())));
        registerCommand(new Kick((peak.getWorld().getSessionHandler())));
        registerCommand(new LearnJob(peak.getWorld().getJobHandler(), peak.getWorld().getSessionHandler()));
        registerCommand(new LearnSpell(peak.getWorld().getSessionHandler(), peak.getWorld().getSpellHandler()));
        registerCommand(new LevelUp(peak.getWorld().getSessionHandler(), peak.getDao().getPlayerDAO(), peak.getWorld().getPlayerHandler()));
        registerCommand(new Morph(peak.getWorld().getSessionHandler()));
        registerCommand(new SetAlign(peak.getWorld().getSessionHandler()));
        registerCommand(new Size(peak.getWorld().getSessionHandler()));
        registerCommand(new SpellPoints(peak.getWorld().getSessionHandler()));
        registerCommand(new Title(peak.getWorld().getSessionHandler()));
        registerCommand(new AddGuildXp(peak.getDao().getGuildDAO()));
        
        /** lvl 3 **/
        registerCommand(new AddFightPos());
        registerCommand(new AddNPC(peak.getDao().getNpcTemplateDAO(), peak.getDao().getNpcDAO()));
        registerCommand(new AddTrigger());
        registerCommand(new Ban(peak.getWorld().getSessionHandler(), peak.getDao().getAccountDAO()));
        registerCommand(new CreateGuild(peak.getWorld().getSessionHandler()));
        registerCommand(new DeleteFightPos());
        registerCommand(new DeleteNPC(peak.getDao().getNpcDAO()));
        registerCommand(new DeleteTrigger());
        registerCommand(new Demorph(peak.getWorld().getSessionHandler()));
        registerCommand(new Exit());
        registerCommand(new ShowFightPos());
        registerCommand(new HideFightPos());
        registerCommand(new Shutdown(peak.getWorld().getSessionHandler()));
        registerCommand(new SpawnFix(peak.getDao().getMonsterDAO(), peak.getDao().getFixedMonsterGroupDAO()));
        registerCommand(new ToogleAggro(peak.getWorld().getSessionHandler()));
        registerCommand(new Unban(peak.getWorld().getSessionHandler(), peak.getDao().getAccountDAO(), peak.getDao().getPlayerDAO()));
        registerCommand(new Save(peak.getWorld()));
        registerCommand(new UnsetMutant(peak.getWorld().getSessionHandler()));
        registerCommand(new DoAction(peak.getWorld().getSessionHandler(), peak.getWorld().getActionHandler()));
        registerCommand(new FreeActionId(peak.getWorld().getActionHandler()));
        registerCommand(new GetActionId(peak.getWorld().getActionHandler()));
        
        /** lvl 4 **/
        registerCommand(new BanIP(peak.getWorld().getSessionHandler()));
        registerCommand(new Block(peak.getWorld().getSessionHandler()));
        registerCommand(new Gift(peak.getWorld().getSessionHandler()));
        registerCommand(new Lock());
        registerCommand(new SetAdmin(peak.getWorld().getSessionHandler(), peak.getDao().getAccountDAO()));
        registerCommand(new Send());
        registerCommand(new SetSeller());
        registerCommand(new Move());
        registerCommand(new TestPath(peak.getWorld().getPlayerHandler()));
        registerCommand(new AddBuffItem(peak.getWorld().getItemHandler()));
        registerCommand(new RemAllBuffs());
    }

    public void registerCommand(Command command) {
        commands.put(command.name().toUpperCase(), command);
    }

    public void registerAlias(String alias, String cmd) {
        if (!commands.containsKey(cmd)) {
            System.err.println("Alias : Commande introuvable " + cmd);
        }

        registerCommand(new Alias(alias, commands.get(cmd)));
    }

    public void perform(CommandPerformer performer, String commandLine) {
        String[] args = commandLine.split("\\s+");

        String cmd = args[0].toUpperCase();

        if (!commands.containsKey(cmd)) {
            performer.displayError("Commande introuvable : " + cmd);
            performer.displayError("Vous cherchez " + searchCommand(cmd, performer).name().toUpperCase() + " ?");
            return;
        }

        Command command = commands.get(cmd);

        if (command.minLevel() > performer.getLevel()) {
            performer.displayError("Non autorisé");
            return;
        }

        try {
            command.perform(performer, Arrays.copyOfRange(args, 1, args.length));
        } catch (Exception e) {
            performer.displayError(e.toString());
            Peak.errorLog.addToLog(e);
        }
    }

    public Command searchCommand(String command, CommandPerformer performer) {
        command = command.toUpperCase();

        Command best = null;
        int levenshtein = Integer.MAX_VALUE;

        for (Command c : commands.values()) {
            if(performer.getLevel() < c.minLevel())
                continue;
            
            int curLevenshtein = StringUtil.levenshteinDistance(command, c.name().toUpperCase());

            if (curLevenshtein < levenshtein) {
                best = c;
                levenshtein = curLevenshtein;
            }
        }

        return best;
    }

    public Collection<Command> getCommands(CommandPerformer performer) {
        Collection<Command> authorized = new ArrayList<>();
        
        for(Command command : commands.values()){
            if(performer.getLevel() >= command.minLevel())
                authorized.add(command);
        }
        
        return authorized;
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }
}
