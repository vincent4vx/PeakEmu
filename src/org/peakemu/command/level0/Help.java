/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.command.level0;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.peakemu.command.Command;
import org.peakemu.command.CommandHandler;
import org.peakemu.command.CommandPerformer;
import org.peakemu.common.util.StringUtil;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class Help implements Command{
    final private CommandHandler commandHandler;

    public Help(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public String name() {
        return "HELP";
    }

    @Override
    public String shortDescription() {
        return "Affiche la liste des commandes, cherche une commande et donne des informations sur les commandes";
    }

    @Override
    public String help() {
        return "HELP\nHELP [commande]";
    }

    @Override
    public int minLevel() {
        return 0;
    }

    @Override
    public void perform(CommandPerformer performer, String[] args) {
        if(args.length == 0)
            listCommands(performer);
        else
            searchCommand(performer, args[0]);
    }
    
    private void listCommands(CommandPerformer performer){
        Map<Integer, SortedSet<String>> commandsByLevel = new TreeMap<>();
        
        for(Command command : commandHandler.getCommands(performer)){
            if(!commandsByLevel.containsKey(command.minLevel()))
                commandsByLevel.put(command.minLevel(), new TreeSet<String>());
            
            commandsByLevel.get(command.minLevel()).add(command.name().toUpperCase() + " - " + command.shortDescription());
        }
        
        performer.displayMessage("Liste des commandes disponibles : ");
        
        for(Map.Entry<Integer, SortedSet<String>> entry : commandsByLevel.entrySet()){
            if(entry.getKey() == 0){
                performer.displayMessage("\nCommandes joueurs");
            }else{
                performer.displayMessage("\nCommandes GM " + entry.getKey());
            }
            
            performer.displayMessage("================");
            
            for(String cmd : entry.getValue())
                performer.displayMessage(cmd);
        }
    }
    
    private void searchCommand(CommandPerformer performer, String terms){
        terms = terms.toUpperCase();
        Command command = commandHandler.getCommand(terms);
        
        if(command != null){
            performer.displayMessage("Commande " + terms.toUpperCase() + " :");
            performer.displayMessage(command.shortDescription());
            performer.displayMessage("Utilisation :");
            performer.displayMessage(command.help());
        }else{
            SortedSet<SearchResult> results = new TreeSet<>();
            
            for(Command cmd : commandHandler.getCommands(performer)){
                if(cmd.minLevel() > performer.getLevel())
                    continue;
                
                results.add(new SearchResult(StringUtil.levenshteinDistance(cmd.name().toUpperCase(), terms), cmd));
            }
            
            performer.displayMessage("Résultat de la recherche : ");
         
            int max = 5;
            for(SearchResult result : results){
                if(max-- == 0)
                    break;
                
                performer.displayMessage(result.command.name().toUpperCase() + " - " + result.command.shortDescription());
            }
        }
    }
    
    private class SearchResult implements Comparable<SearchResult>{
        final private int levenshtein;
        final private Command command;

        public SearchResult(int levenshtein, Command command) {
            this.levenshtein = levenshtein;
            this.command = command;
        }

        @Override
        public int compareTo(SearchResult t) {
            return levenshtein - t.levenshtein;
        }
    }
}
