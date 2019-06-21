/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler.fight;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.peakemu.Peak;
import org.peakemu.common.Constants;
import org.peakemu.common.Formulas;
import org.peakemu.common.Logger;
import org.peakemu.database.dao.CollectorDAO;
import org.peakemu.database.dao.EndFightActionDAO;
import org.peakemu.database.dao.GuildDAO;
import org.peakemu.database.dao.GuildMemberDAO;
import org.peakemu.database.dao.MonsterDAO;
import org.peakemu.database.dao.PlayerDAO;
import org.peakemu.game.in.gameaction.GameAction;
import org.peakemu.game.out.InfoMessage;
import org.peakemu.game.out.guild.CollectorAttacked;
import org.peakemu.game.out.guild.InfosTaxCollectorsMovement;
import org.peakemu.objects.Collector;
import org.peakemu.objects.Guild;
import org.peakemu.objects.player.Player;
import org.peakemu.world.GameMap;
import org.peakemu.objects.MonsterGroup;
import org.peakemu.objects.Prism;
import org.peakemu.world.MapCell;
import org.peakemu.world.SpellEffect;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.action.Action;
import org.peakemu.world.config.FightConfig;
import org.peakemu.world.config.RateConfig;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.FightType;
import org.peakemu.world.fight.AgressionFight;
import org.peakemu.world.fight.ChallengeFight;
import org.peakemu.world.fight.ConquestFight;
import org.peakemu.world.fight.Fight;
import org.peakemu.world.fight.FightRewards;
import org.peakemu.world.fight.MutantFight;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.PvMFight;
import org.peakemu.world.fight.PvTFight;
import org.peakemu.world.fight.effect.AddGlypheEffect;
import org.peakemu.world.fight.effect.AddInvocationEffect;
import org.peakemu.world.fight.effect.AddStateEffect;
import org.peakemu.world.fight.effect.AddTrapEffect;
import org.peakemu.world.fight.effect.ArmorEffect;
import org.peakemu.world.fight.effect.AttackEffect;
import org.peakemu.world.fight.effect.AttractEffect;
import org.peakemu.world.fight.effect.BoostEffect;
import org.peakemu.world.fight.effect.Buff;
import org.peakemu.world.fight.effect.CasterBoostPointsEffect;
import org.peakemu.world.fight.effect.CasterDammageEffect;
import org.peakemu.world.fight.effect.ChangeApparence;
import org.peakemu.world.fight.effect.EffectScope;
import org.peakemu.world.fight.effect.ExchangePlace;
import org.peakemu.world.fight.effect.ExchangePlaceOnDammages;
import org.peakemu.world.fight.effect.FightEffect;
import org.peakemu.world.fight.effect.FlightLife;
import org.peakemu.world.fight.effect.FlightPointsEffect;
import org.peakemu.world.fight.effect.GiftPercentLife;
import org.peakemu.world.fight.effect.HealEffect;
import org.peakemu.world.fight.effect.HealOnDammageEffect;
import org.peakemu.world.fight.effect.ImmunityEffect;
import org.peakemu.world.fight.effect.InvisibilityEffect;
import org.peakemu.world.fight.effect.InvocEffect;
import org.peakemu.world.fight.effect.NoEffect;
import org.peakemu.world.fight.effect.PercentLifeAttack;
import org.peakemu.world.fight.effect.ChastisementEffect;
import org.peakemu.world.fight.effect.KillEffect;
import org.peakemu.world.fight.effect.PunishmentEffect;
import org.peakemu.world.fight.effect.RemovePoints;
import org.peakemu.world.fight.effect.RepulseEffect;
import org.peakemu.world.fight.effect.ReturnsDammagesEffect;
import org.peakemu.world.fight.effect.TeleportEffect;
import org.peakemu.world.fight.effect.UnesquivablePointsLooseEffect;
import org.peakemu.world.fight.effect.UsedAPDammagesEffect;
import org.peakemu.world.fight.fighter.PlayerFighter;
import org.peakemu.world.handler.ActionHandler;
import org.peakemu.world.handler.AlignementHandler;
import org.peakemu.world.handler.ExpHandler;
import org.peakemu.world.handler.PlayerHandler;
import org.peakemu.world.handler.SpellHandler;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class FightHandler {
    final private RewardsHandler rewardsHandler;
    final private ActionHandler actionHandler;
    final private EndFightActionDAO endFightActionDAO;
    final private PlayerDAO playerDAO;
    final private PlayerHandler playerHandler;
    final private CollectorDAO collectorDAO;
    final private GuildDAO guildDAO;
    final private GuildMemberDAO guildMemberDAO;
    final private MonsterDAO monsterDAO;
    final private AlignementHandler alignementHandler;
    final private SpellHandler spellHandler;
    final private RateConfig rates;
    final private FightConfig config;
    final private ExpHandler expHandler;
    
    final private Map<Effect, FightEffect> effects = new HashMap<>();

    public FightHandler(RewardsHandler rewardsHandler, ActionHandler actionHandler, EndFightActionDAO endFightActionDAO, PlayerDAO playerDAO, PlayerHandler playerHandler, CollectorDAO collectorDAO, GuildDAO guildDAO, GuildMemberDAO guildMemberDAO, MonsterDAO monsterDAO, AlignementHandler alignementHandler, SpellHandler spellHandler, RateConfig rates, FightConfig config, ExpHandler expHandler) {
        this.rewardsHandler = rewardsHandler;
        this.actionHandler = actionHandler;
        this.endFightActionDAO = endFightActionDAO;
        this.playerDAO = playerDAO;
        this.playerHandler = playerHandler;
        this.collectorDAO = collectorDAO;
        this.guildDAO = guildDAO;
        this.guildMemberDAO = guildMemberDAO;
        this.monsterDAO = monsterDAO;
        this.alignementHandler = alignementHandler;
        this.spellHandler = spellHandler;
        this.rates = rates;
        this.config = config;
        this.expHandler = expHandler;
    }

    public void load(){
        System.out.print("Chargement des actions de fin de combat : ");
        System.out.println(endFightActionDAO.getAll().size() + " actions chargées");
        
        loadFightEffects();
    }

    public RateConfig getRates() {
        return rates;
    }

    public FightConfig getConfig() {
        return config;
    }

    public void giveRewards(Collection<FightRewards> rewardses) {
        rewardsHandler.giveRewards(rewardses);
    }
    
    public SpellLevel getDefaultMeleeSpell(){
        return spellHandler.getSpellById(0).getLevel(1);
    }
    
    //===============//
    // Fight actions //
    //===============//
    
    public void applyEndFightActions(Fight fight, Collection<Fighter> winners){
        //apply end fight actions
        for(Action action : endFightActionDAO.getByMap(fight.getOldMap().getId()).getActions(fight.get_type())){
            for(Fighter fighter : winners){
                if(fighter.getPlayer() == null)
                    continue;
                
                actionHandler.apply(action, fighter.getPlayer(), null, null, null);
            }
        }
    }
    
    public void kickAllFighters(Collection<Fighter> fighters){
        Set<Guild> guilds = new HashSet<>();
        
        //save all fighters
        for(Fighter fighter : fighters){
            if(fighter instanceof PlayerFighter){
                Player player = fighter.getPlayer();
                playerDAO.save(player);
                player.setFighter(null);
                player.setFight(null);
                player.set_duelID(-1);
                player.setAway(false);
                player.setSitted(false);
                player.refreshStats();

                if(player.getGuild() != null){
                    guilds.add(player.getGuild());
                    guildMemberDAO.save(player.getGuildMember());
                }
            }
        }
        
        for(Guild guild : guilds)
            guildDAO.save(guild);
    }
    
    public void removeEnergy(Fight fight, Collection<Fighter> loosers){
        for(Fighter fighter : loosers){
            Player player = fighter.getPlayer();
            
            if(player == null)
                continue;
            
            int energyLoose = Formulas.getLoosEnergy(fighter.get_lvl(), fight.get_type() == FightType.AGRESSION, fight.get_type() == FightType.PVT);
            playerHandler.removeEnergy(player, energyLoose);
        }
    }
    
    public void teleportLoosers(Collection<Fighter> loosers){
        for(Fighter fighter : loosers){
            if(fighter.getPlayer() == null || fighter.getPlayer().getEnergy() <= 0)
                continue;
            
            playerHandler.setPositionToSavePos(fighter.getPlayer());
        }
    }
    
    public void jailLoosers(Fight fight, Collection<Fighter> loosers){
        for(Fighter fighter : loosers){
            Player player = fighter.getPlayer();
            
            if(player == null || player.getEnergy() <= 0) //not a player, or death
                continue;
            
            if (player.getAlignement() == Constants.ALIGNEMENT_BONTARIEN && (fight.getOldMap().getSubArea().getAlignement() == Constants.ALIGNEMENT_BRAKMARIEN || fight.getOldMap().getSubArea().getArea().getalignement() == Constants.ALIGNEMENT_BRAKMARIEN)) {
                playerHandler.setPosition(player, (short)6171, 366);
                player.setSavePos("6171,366");
            }else if (player.getAlignement() == Constants.ALIGNEMENT_BRAKMARIEN && (fight.getOldMap().getSubArea().getAlignement() == Constants.ALIGNEMENT_BONTARIEN || fight.getOldMap().getSubArea().getArea().getalignement() == Constants.ALIGNEMENT_BONTARIEN)) {
                playerHandler.setPosition(player, (short)6164, 180);
                player.setSavePos("6164,180");
            }else{ //ini other cases, only warp to save pos
                playerHandler.setPositionToSavePos(player);
            }
        }
    }
    
    public void setLifePoints(Collection<Fighter> fighters){
        for(Fighter fighter : fighters){
            Player player = fighter.getPlayer();
            
            if(player == null)
                continue;
            
            if(fighter._hasLevelUp){
                player.fullPDV();
                continue;
            }
            
            if(fighter.isDead()){
                player.setLifePoints(1);
            }else{
                int life = fighter.getPDV();

                if(life > player.getMaxLifePoints())
                    life = player.getMaxLifePoints();

                player.setLifePoints(life);
            }
        }
    }
    
    public void collectorLoose(Collection<Fighter> loosers){
        for(Fighter F : loosers){
            if (F.getCollector() != null) { //collector loose
                Collector collector = F.getCollector();
                collectorDAO.remove(collector);
                collector.getGuild().sendToMembers(new InfosTaxCollectorsMovement(collector.getGuild()));
                collector.getGuild().sendToMembers(new CollectorAttacked(collector, CollectorAttacked.TAX_ATTACKED_DIED));
            }
        }
    }
    
    /**
     * Add dammages on items (like buff turns, etheree...)
     * @param fighters 
     */
    public void inventoryDammages(Collection<Fighter> fighters){
        for (Fighter fighter : fighters) {
            if(fighter instanceof PlayerFighter){
                Player player = fighter.getPlayer();
                
                player.getItems().decrementBuffTurns();
            }
        }
    }
    
    //=================//
    // Fight factories //
    //=================//
    
    public Fight startPvMFight(GameMap map, Player player, MonsterGroup group){
        int id = map.getNextFightId();
        Fight fight = new PvMFight(this, id, map, player, group);
        map.addFight(fight);
        return fight;
    }
    
    public Fight startChallengeFight(GameMap map, Player player1, Player player2){
        int id = map.getNextFightId();
        Fight fight = new ChallengeFight(this, id, map, player1, player2);
        map.addFight(fight);
        return fight;
    }
    
    public Fight startAgressionFight(GameMap map, Player player1, Player player2){
        int id = map.getNextFightId();
        Fight fight = new AgressionFight(expHandler, this, id, map, player1, player2);
        map.addFight(fight);
        return fight;
    }
    
    public Fight startPvTFight(GameMap map, Player init, Collector collector){
        int id = map.getNextFightId();
        Fight fight = new PvTFight(this, id, map, init, collector);
        map.addFight(fight);
        return fight;
    }
    
    public Fight startConquestFight(GameMap map, Player init, Prism prism){
        int id = map.getNextFightId();
        Fight fight = new ConquestFight(expHandler, alignementHandler, this, id, map, init, prism);
        map.addFight(fight);
        return fight;
    }
    
    public Fight startMutantFight(GameMap map, Player mutant, Player other){
        int id = map.getNextFightId();
        Fight fight = new MutantFight(this, id, map, mutant, other);
        map.addFight(fight);
        return fight;
    }
    
    //===============//
    // Fight effects //
    //===============//
    
    private void loadFightEffects(){
        //attack
        addFightEffect(new AttackEffect(Effect.DOMA_AIR));
        addFightEffect(new AttackEffect(Effect.DOMA_EAU));
        addFightEffect(new AttackEffect(Effect.DOMA_FEU));
        addFightEffect(new AttackEffect(Effect.DOMA_NEU));
        addFightEffect(new AttackEffect(Effect.DOMA_TER));
        
        addFightEffect(new FlightLife(Effect.VOL_VIE_AIR));
        addFightEffect(new FlightLife(Effect.VOL_VIE_EAU));
        addFightEffect(new FlightLife(Effect.VOL_VIE_FEU));
        addFightEffect(new FlightLife(Effect.VOL_VIE_NEU));
        addFightEffect(new FlightLife(Effect.VOL_VIE_TER));
        addFightEffect(new FlightLife(Effect.VOL_VIE));
        
        addFightEffect(new PercentLifeAttack(Effect.DOMA_PERCENT_AIR));
        addFightEffect(new PercentLifeAttack(Effect.DOMA_PERCENT_EAU));
        addFightEffect(new PercentLifeAttack(Effect.DOMA_PERCENT_FEU));
        addFightEffect(new PercentLifeAttack(Effect.DOMA_PERCENT_NEU));
        addFightEffect(new PercentLifeAttack(Effect.DOMA_PERCENT_TER));
        
        addFightEffect(new UsedAPDammagesEffect());
        addFightEffect(new PunishmentEffect());
        addFightEffect(new KillEffect());
        
        //objects
        addFightEffect(new AddTrapEffect(spellHandler));
        addFightEffect(new AddGlypheEffect(spellHandler));
        
        //malus
        addFightEffect(new RemovePoints(Effect.REM_PA, Effect.ADD_AFLEE, GameAction.DODGE_REMOVE_AP));
        addFightEffect(new RemovePoints(Effect.REM_PM, Effect.ADD_MFLEE, GameAction.DODGE_REMOVE_MP));
        addFightEffect(new UnesquivablePointsLooseEffect(Effect.REM_PA2));
        addFightEffect(new UnesquivablePointsLooseEffect(Effect.REM_PM2));
        
        addFightEffect(new FlightPointsEffect(Effect.VOL_PA, Effect.ADD_AFLEE, GameAction.DODGE_REMOVE_AP, Effect.REM_PA, Effect.ADD_PA));
        addFightEffect(new FlightPointsEffect(Effect.VOL_PM, Effect.ADD_MFLEE, GameAction.DODGE_REMOVE_MP, Effect.REM_PM, Effect.ADD_PM));
        
        //armor
        addFightEffect(new ArmorEffect());
        addFightEffect(new ImmunityEffect());
        
        //movement
        addFightEffect(new TeleportEffect());
        addFightEffect(new RepulseEffect());
        addFightEffect(new AttractEffect());
        ExchangePlace exchangePlace = new ExchangePlace();
        addFightEffect(exchangePlace);
        
        //on dammages buff
        addFightEffect(new HealOnDammageEffect());
        addFightEffect(new ReturnsDammagesEffect());
        addFightEffect(new ChastisementEffect());
        addFightEffect(new ExchangePlaceOnDammages(exchangePlace));
        
        //boosts
        addFightEffect(new BoostEffect(Effect.ADD_VIE));
        addFightEffect(new BoostEffect(Effect.ADD_DOMA));
        addFightEffect(new BoostEffect(Effect.MULTIPLY_DOMMAGE));
        addFightEffect(new BoostEffect(Effect.ADD_CC));
        addFightEffect(new BoostEffect(Effect.ADD_PO));
        addFightEffect(new BoostEffect(Effect.ADD_AGIL));
        addFightEffect(new BoostEffect(Effect.ADD_FORC));
        addFightEffect(new BoostEffect(Effect.ADD_CHAN));
        addFightEffect(new BoostEffect(Effect.ADD_VITA));
        addFightEffect(new BoostEffect(Effect.ADD_SAGE));
        addFightEffect(new BoostEffect(Effect.ADD_INTE));
        addFightEffect(new BoostEffect(Effect.ADD_DOM2));
        addFightEffect(new BoostEffect(Effect.ADD_PERDOM));
        addFightEffect(new BoostEffect(Effect.ADD_PA));
        addFightEffect(new BoostEffect(Effect.ADD_PM));
        addFightEffect(new BoostEffect(Effect.BOOST_SPELL_DOMA));
        addFightEffect(new BoostEffect(Effect.ADD_RP_AIR));
        addFightEffect(new BoostEffect(Effect.ADD_RP_EAU));
        addFightEffect(new BoostEffect(Effect.ADD_RP_FEU));
        addFightEffect(new BoostEffect(Effect.ADD_RP_NEU));
        addFightEffect(new BoostEffect(Effect.ADD_RP_TER));
        
        addFightEffect(new CasterBoostPointsEffect(Effect.ADD_PA2));
        addFightEffect(new CasterBoostPointsEffect(Effect.ADD_PM2));
        
        addFightEffect(new InvisibilityEffect());
        
        addFightEffect(new InvocEffect(monsterDAO));
        
        //heal
        addFightEffect(new HealEffect());
        addFightEffect(new GiftPercentLife());
        
        //Misc
        addFightEffect(new NoEffect());
        addFightEffect(new ChangeApparence());
        
        //States
        addFightEffect(new AddStateEffect());
        
        //CasterDammage
        addFightEffect(new CasterDammageEffect());
        
        //Add invoc
        addFightEffect(new AddInvocationEffect());
    }
    
    public void addFightEffect(FightEffect fightEffect){
        effects.put(fightEffect.getEffect(), fightEffect);
    }
    
    public void applyEffectToFight(Fight fight, SpellEffect spellEffect, MapCell cell, Fighter caster){
        FightEffect fe = effects.get(spellEffect.getEffect());
        
        if(fe == null){
            Peak.worldLog.addToLog(Logger.Level.INFO, "[applyEffectToFight] Undefined fight effect %s", spellEffect.getEffect());
            return;
        }
        
        fe.applyToFight(fight, spellEffect, cell, caster);
    }
    
    public void applyBuffOnStartTurn(Buff buff){
        FightEffect fe = effects.get(buff.getSpellEffect().getEffect());
        
        if(fe == null){
            Peak.worldLog.addToLog(Logger.Level.INFO, "[applyBuffOnStartTurn] Undefined fight effect %s", buff.getSpellEffect().getEffect());
            return;
        }
        
        fe.applyBuffOnStartTurn(buff);
    }
    
    public void applyBuffOnEndTurn(Buff buff){
        FightEffect fe = effects.get(buff.getSpellEffect().getEffect());
        
        if(fe == null){
            Peak.worldLog.addToLog(Logger.Level.INFO, "[applyBuffOnEndTurn] Undefined fight effect %s", buff.getSpellEffect().getEffect());
            return;
        }
        
        fe.applyBuffOnEndTurn(buff);
    }
    
    public void applyBuffOnDammages(Buff buff, EffectScope scope){
        FightEffect fe = effects.get(buff.getSpellEffect().getEffect());
        
        if(fe == null){
            Peak.worldLog.addToLog(Logger.Level.INFO, "[applyBuffOnDammages] Undefined fight effect %s", buff.getSpellEffect().getEffect());
            return;
        }
        
        fe.applyBuffOnDammages(buff, scope);
    }
    
    public void onBuffEnd(Buff buff){
        FightEffect fe = effects.get(buff.getSpellEffect().getEffect());
        
        if(fe == null){
            Peak.worldLog.addToLog(Logger.Level.INFO, "[onBuffEnd] Undefined fight effect %s", buff.getSpellEffect().getEffect());
            return;
        }
        
        fe.onBuffEnd(buff);
    }
}
