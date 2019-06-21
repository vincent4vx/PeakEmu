/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.config;

import java.util.Collection;
import java.util.HashSet;
import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.ConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.data.ConfigNode;
import fr.quatrevieux.crisis.config.item.IntConfigItem;
import fr.quatrevieux.crisis.config.item.LongConfigItem;
import fr.quatrevieux.crisis.config.item.StringConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class CharacterCreationConfig extends AbstractConfigPackage{
    @DefaultValue("^[a-zA-Z]+\\-?[a-zA-Z]+$")
    private StringConfigItem nameRegex;
    
    @DefaultValue("3")
    private IntConfigItem minLength;
    
    @DefaultValue("20")
    private IntConfigItem maxLength;
    
    @DefaultValue("5")
    private IntConfigItem maxCharPerAccount;
    
    @DefaultValue("0")
    private LongConfigItem startKamas;
    
    @DefaultValue("1")
    private IntConfigItem startLevel;
    
    final private Forbidden forbidden = new Forbidden();
    
    static public class Forbidden implements ConfigPackage{
        final private Collection<String> start = new HashSet<>();
        final private Collection<String> end = new HashSet<>();
        final private Collection<String> contains = new HashSet<>();
        final private Collection<String> equals = new HashSet<>();

        @Override
        public void parse(ConfigNode node) {
            for(ConfigNode elem : node.getNodes()){
                switch(elem.getName()){
                    case "start":
                        start.add(elem.getValue().toLowerCase());
                        break;
                    case "end":
                        end.add(elem.getValue().toLowerCase());
                        break;
                    case "contains":
                        contains.add(elem.getValue().toLowerCase());
                        break;
                    case "equals":
                        equals.add(elem.getValue().toLowerCase());
                        break;
                    default:
                        System.err.println("WorldConfig.characterCreate.forbidden : invalid element " + elem.getName());
                }
            }
        }

        public Collection<String> getStart() {
            return start;
        }

        public Collection<String> getEnd() {
            return end;
        }

        public Collection<String> getContains() {
            return contains;
        }

        public Collection<String> getEquals() {
            return equals;
        }
    }

    public String getNameRegex() {
        return nameRegex.getValue();
    }

    public int getMinLength() {
        return minLength.getValue();
    }

    public int getMaxLength() {
        return maxLength.getValue();
    }

    public Forbidden getForbidden() {
        return forbidden;
    }

    public int getMaxCharPerAccount() {
        return maxCharPerAccount.getValue();
    }

    public long getStartKamas() {
        return startKamas.getValue();
    }

    public int getStartLevel() {
        return startLevel.getValue();
    }
}
