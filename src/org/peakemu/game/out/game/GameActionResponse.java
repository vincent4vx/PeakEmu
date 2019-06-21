/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.game.out.game;

import java.util.List;
import org.peakemu.common.util.StringUtil;
import org.peakemu.game.in.gameaction.GameAction;
import org.peakemu.game.in.gameaction.JoinFight;
import org.peakemu.game.in.gameaction.Move;
import org.peakemu.maputil.Compressor;
import org.peakemu.maputil.PathException;
import org.peakemu.objects.player.Player;
import org.peakemu.world.MapCell;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.Sprite;
import org.peakemu.world.enums.Effect;
import org.peakemu.world.enums.FighterState;
import org.peakemu.world.fight.fighter.Fighter;
import org.peakemu.world.fight.object.Glyphe;
import org.peakemu.world.fight.object.Trap;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class GameActionResponse {
    private String gaId;
    private int actionId;
    
    private Object[] args;

    public GameActionResponse(String gaId, int actionId, Object... args) {
        this.gaId = gaId;
        this.actionId = actionId;
        this.args = args;
    }

    public GameActionResponse(int actionId, Object... args) {
        gaId = "";
        this.actionId = actionId;
        this.args = args;
    }

    public String getGaId() {
        return gaId;
    }

    public void setGaId(String gaId) {
        this.gaId = gaId;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    @Override
    public String toString() {
        String ret = "GA" + gaId + ";" + actionId + ";" + StringUtil.join(args, ";");
        
        return ret;
    }
    
    static public GameActionResponse noGameAction(){
        return new GameActionResponse("", 0);
    }
    
    static public GameActionResponse joinFightError(String teamId, String error){
        return new GameActionResponse(teamId, JoinFight.ACTION_ID, error);
    }
    
    static public GameActionResponse loosePAOnAction(Fighter fighter, int qte){
        return new GameActionResponse("", GameAction.ALTER_PA_ON_ACTION, fighter.getSpriteId(), fighter.getSpriteId() + ",-" + qte);
    }
    
    static public GameActionResponse loosePAOnDebuff(Fighter fighter, int qte){
        return new GameActionResponse(GameAction.ALTER_PA_ON_BUFF, fighter.getSpriteId(), fighter.getSpriteId() + ",-" + qte);
    }
    
    static public GameActionResponse loosePMOnAction(Fighter fighter, int qte){
        return new GameActionResponse("", GameAction.ALTER_PM_ON_ACTION, fighter.getSpriteId(), fighter.getSpriteId() + ",-" + qte);
    }
    
    static public GameActionResponse loosePMOnDebuff(Fighter fighter, int qte){
        return new GameActionResponse(GameAction.ALTER_PM_ON_BUFF, fighter.getSpriteId(), fighter.getSpriteId() + ",-" + qte);
    }
    
    static public GameActionResponse fighterTackled(Fighter fighter){
        return new GameActionResponse(GameAction.TACKLE, fighter.getSpriteId());
    }
    
    static public GameActionResponse criticalMiss(Fighter fighter, int spellId){
        return new GameActionResponse(GameAction.CRITICAL_MISS, fighter.getSpriteId(), spellId);
    }
    
    static public GameActionResponse jobAction(Player player, MapCell cell, int duration, int animation){
        return new GameActionResponse(GameAction.JOB_ACTION, player.getId(), cell.getID() + "," + duration + (animation == -1 ? "" : "," + animation));
    }
    
    static public GameActionResponse removeLifePoints(Fighter caster, Fighter target, int dammages){
        return new GameActionResponse(GameAction.ALTER_LIFE_POINTS, caster.getSpriteId(), target.getSpriteId() + ",-" + dammages);
    }
    
    static public GameActionResponse addLifePoints(Fighter caster, Fighter target, int dammages){
        return new GameActionResponse(GameAction.ALTER_LIFE_POINTS, caster.getSpriteId(), target.getSpriteId() + "," + dammages);
    }
    
    static public GameActionResponse reducedDammages(Fighter caster, Fighter target, int dmg){
        return new GameActionResponse(GameAction.REDUCED_DAMMAGES, caster.getSpriteId(), target.getSpriteId() + "," + dmg);
    }
    
    static public GameActionResponse fightEffect(Effect effect, Fighter caster, Fighter target, int value){
        return new GameActionResponse(effect.getId(), caster.getSpriteId(), target.getSpriteId() + "," + value);
    }
    
    static public GameActionResponse fightEffect(Effect effect, Fighter caster, Fighter target, int value, int turns){
        return new GameActionResponse(effect.getId(), caster.getSpriteId(), target.getSpriteId() + "," + value + "," + turns);
    }
    
    static public GameActionResponse invocation(Effect effect, Fighter caster, Fighter invoc){
        return new GameActionResponse(effect.getId(), caster.getSpriteId(), "|+" + invoc.getSpritePacket());
    }
    
    static public GameActionResponse fakePacket(Fighter fighter, Object packet){
        return new GameActionResponse(GameAction.FAKE_PACKET, fighter.getSpriteId(), packet.toString());
    }
    
    static public GameActionResponse triggerTrap(Fighter fighter, Trap trap){
        return new GameActionResponse(GameAction.TRIGGER_TRAP, fighter.getSpriteId(), trap.getLayingSpell().getSpellID() + "," + trap.getCell().getID() + "," + trap.getLayingSpell().getSpell().getSpriteID() + "," + trap.getTriggerSpell().getLevel() + ",1," + trap.getCaster().getSpriteId());
    }
    
    static public GameActionResponse triggerGlyphe(Fighter fighter, Glyphe trap){
        return new GameActionResponse(GameAction.TRIGGER_GLYPHE, fighter.getSpriteId(), trap.getLayingSpell().getSpellID() + "," + trap.getCell().getID() + "," + trap.getLayingSpell().getLevel() + "," + trap.getTriggerSpell().getLevel() + ",1," + trap.getCaster().getSpriteId());
    }
    
    static public GameActionResponse move(Sprite sprite, List<MapCell> path) throws PathException{
        return new GameActionResponse("0", Move.ACTION_ID, sprite.getSpriteId(), Compressor.compressPath(path));
    }
    
    static public GameActionResponse launchSpell(Fighter caster, SpellLevel spell, MapCell target){
        return new GameActionResponse(GameAction.LAUNCH_SPELL, caster.getSpriteId(), spell.getSpellID() + "," + target.getID() + "," + spell.getSpriteID() + "," + spell.getLevel() + "," + spell.getSpriteInfos());
    }
    
    static public GameActionResponse criticalHit(Fighter caster, SpellLevel spell, MapCell target){
        return new GameActionResponse(GameAction.CRITICAL_HIT, caster.getSpriteId(), spell.getSpellID() + "," + target.getID() + "," + spell.getSpellID() + "," + spell.getLevel() + "," + spell.getSpriteInfos());
    }
    
    static public GameActionResponse weaponCriticalMiss(Fighter caster){
        return new GameActionResponse(GameAction.WEAPON_CRITICAL_MISS, caster.getSpriteId());
    }
    
    static public GameActionResponse weaponCriticalHit(Fighter caster){
        return new GameActionResponse(GameAction.CRITICAL_HIT, caster.getSpriteId(), 0);
    }
    
    static public GameActionResponse useWeapon(Fighter caster, MapCell target){
        return new GameActionResponse(GameAction.USE_WEAPON, caster.getSpriteId(), target.getID());
    }
    
    static public GameActionResponse alterState(Fighter caster, Fighter target, FighterState state, boolean active){
        return new GameActionResponse(GameAction.ALTER_STATE, caster.getSpriteId(), target.getSpriteId() + "," + state.getId() + "," + (active ? "1" : "0"));
    }
    
    static public GameActionResponse fighterDie(Fighter caster, Fighter target){
        return new GameActionResponse(GameAction.FIGHTER_DIE, caster.getSpriteId(), target.getSpriteId());
    }
    
    static public GameActionResponse changeApparence(Fighter caster, Fighter target, int defaultApparence, int newApparence, int turns){
        return new GameActionResponse(Effect.CHANGE_SKIN.getId(), caster.getSpriteId(), target.getSpriteId() + "," + defaultApparence + "," + newApparence + "," + turns);
    }
}
