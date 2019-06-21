/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.config;

import fr.quatrevieux.crisis.config.AbstractConfigPackage;
import fr.quatrevieux.crisis.config.DefaultValue;
import fr.quatrevieux.crisis.config.item.IntConfigItem;
import fr.quatrevieux.crisis.config.item.StringConfigItem;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class WorldConfig extends AbstractConfigPackage{
    @DefaultValue("3600")
    private IntConfigItem saveDelay;
    
    @DefaultValue("15")
    private IntConfigItem inactivityDelay;
    
    @DefaultValue("15")
    private IntConfigItem maxIncarnamLevel;
    
    @DefaultValue("512")
    private IntConfigItem maxClients;
    
    @DefaultValue("4")
    private IntConfigItem maxIpPerClient;
    
    @DefaultValue("8")
    private IntConfigItem maxGroupSize;
    
    @DefaultValue("")
    private StringConfigItem welcomeMessage;
    
    @DefaultValue("60")
    private IntConfigItem reloadLiveActionsDelay;
    
    final private CharacterCreationConfig characterCreation = new CharacterCreationConfig();
    final private LifeConfig life = new LifeConfig();
    final private ChatConfig chat = new ChatConfig();
    final private StoreConfig store = new StoreConfig();
    final private MountConfig mount = new MountConfig();
    final private JobConfig job = new JobConfig();
    final private RateConfig rate = new RateConfig();
    final private FightConfig fight = new FightConfig();
    final private PlayerConfig player = new PlayerConfig();
    final private GuildConfig guild = new GuildConfig();

    public int getSaveDelay() {
        return saveDelay.getValue();
    }

    public int getInactivityDelay() {
        return inactivityDelay.getValue();
    }

    public CharacterCreationConfig getCharacterCreation() {
        return characterCreation;
    }

    public ChatConfig getChat() {
        return chat;
    }

    public int getMaxIncarnamLevel() {
        return maxIncarnamLevel.getValue();
    }

    public LifeConfig getLife() {
        return life;
    }

    public int getMaxClients() {
        return maxClients.getValue();
    }

    public int getMaxIpPerClient() {
        return maxIpPerClient.getValue();
    }

    public int getMaxGroupSize() {
        return maxGroupSize.getValue();
    }

    public StoreConfig getStore() {
        return store;
    }

    public MountConfig getMount() {
        return mount;
    }

    public String getWelcomeMessage() {
        return welcomeMessage.getValue();
    }

    public JobConfig getJob() {
        return job;
    }

    public RateConfig getRate() {
        return rate;
    }

    public int getReloadLiveActionsDelay() {
        return reloadLiveActionsDelay.getValue();
    }

    public FightConfig getFight() {
        return fight;
    }

    public PlayerConfig getPlayer() {
        return player;
    }

    public GuildConfig getGuild() {
        return guild;
    }
    
}
