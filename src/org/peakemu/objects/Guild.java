package org.peakemu.objects;

import org.peakemu.objects.player.GuildMember;
import org.peakemu.objects.player.Player;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.peakemu.game.out.guild.JoinGuildResponse;
import org.peakemu.world.Request;
import org.peakemu.world.Spell;
import org.peakemu.world.SpellLevel;
import org.peakemu.world.Stats;
import org.peakemu.world.enums.Effect;

public class Guild {
    final static public String DEFAULT_SPELLS = "462;0|461;0|460;0|459;0|458;0|457;0|456;0|455;0|454;0|453;0|452;0|451;0|";
    final static public String DEFAULT_STATS = "176;100|158;1000|124;100|";
    
    final private int id;
    final private String name;
    final private String emblem;
    final private Map<Integer, GuildMember> members = new HashMap<Integer, GuildMember>();
    private int level;
    private long xp;

    //Percepteur
    private int capital = 0;
    private int maxCollectors = 0;
    final private Map<Integer, SpellLevel> spells;
    final private Stats stats;
    //Stats en combat
    private Stats statsFight = new Stats();
    
    final private Collection<Collector> collectors = new HashSet<>();

    public Guild(int id, String name, String emblem, int lvl, long xp, int capital, int nbrmax, Map<Integer, SpellLevel> spells, Stats stats) {
        this.id = id;
        this.name = name;
        this.emblem = emblem;
        this.xp = xp;
        this.level = lvl;
        this.capital = capital;
        this.maxCollectors = nbrmax;
        this.spells = spells;
        this.stats = stats;
        //Mise en place des stats
        statsFight = new Stats();
        statsFight.addOneStat(Effect.ADD_FORC, this.level);
        statsFight.addOneStat(Effect.ADD_SAGE, get_Stats(Effect.ADD_SAGE));
        statsFight.addOneStat(Effect.ADD_INTE, this.level);
        statsFight.addOneStat(Effect.ADD_CHAN, this.level);
        statsFight.addOneStat(Effect.ADD_AGIL, this.level);
        statsFight.addOneStat(Effect.ADD_RP_NEU, (int) Math.floor(getLevel() / 2));
        statsFight.addOneStat(Effect.ADD_RP_FEU, (int) Math.floor(getLevel() / 2));
        statsFight.addOneStat(Effect.ADD_RP_EAU, (int) Math.floor(getLevel() / 2));
        statsFight.addOneStat(Effect.ADD_RP_AIR, (int) Math.floor(getLevel() / 2));
        statsFight.addOneStat(Effect.ADD_RP_TER, (int) Math.floor(getLevel() / 2));
        statsFight.addOneStat(Effect.ADD_AFLEE, (int) Math.floor(getLevel() / 2));
        statsFight.addOneStat(Effect.ADD_MFLEE, (int) Math.floor(getLevel() / 2));
    }

    public void addMember(GuildMember member) {
        members.put(member.getPlayer().getSpriteId(), member);
    }
    
    public void removeMember(GuildMember member){
        members.remove(member.getPlayer().getSpriteId());
    }

    public int getId() {
        return id;
    }

    public int getMaxCollectors() {
        return maxCollectors;
    }

    public void setMaxCollectors(int nbr) {
        maxCollectors = nbr;
    }

    public int getCapital() {
        return capital;
    }

    public void setCapital(int nbr) {
        capital = nbr;
    }

    public Map<Integer, SpellLevel> getSpells() {
        return spells;
    }

    public Stats getStats() {
        return stats;
    }

    public void addStat(Effect stat, int qte) {
        stats.addOneStat(stat, qte);
    }

    public boolean boostSpell(Spell spell) {
        SpellLevel level = spells.get(spell.getId());
        
        if(level == null){
            return false; //spell should be learned before
        }
        
        if(level.isMaxLevel())
            return false;
        
        spells.put(spell.getId(), level.getNextLevel());
        return true;
    }

    public Stats getStatsFight() {
        return new Stats(statsFight);
    }

    public String getName() {
        return name;
    }

    public String getEmbem() {
        return emblem;
    }

    public long getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int lvl) {
        this.level = lvl;
    }

    public int getSize() {
        return members.size();
    }

    public Collection<GuildMember> getMembers() {
        return members.values();
    }

    public GuildMember getMember(int guid) {
        return members.get(guid);
    }

//    public void removeMember(Player perso) {
//        House h = House.get_HouseByPerso(perso);//On prend ça maison
//        if (h != null) {
//            if (House.HouseOnGuild(id) > 0) {
//                Database.HOUSE_GUILD(h, 0, 0);//On retire de la guilde
//            }
//        }
//        members.remove(perso.getSpriteId());
//        Database.DEL_GUILDMEMBER(perso.getSpriteId());
//    }

    public void addXp(long xp) {
        this.xp += xp;
    }

    public void upgrade_Stats(Effect statsid, int add) {
        stats.addOneStat(statsid, add);
    }

    public int get_Stats(Effect effect) {
        return stats.getEffect(effect);
    }

    public String parseQuestionTaxCollector() {
        StringBuilder packet = new StringBuilder(10);
        packet.append('1').append(';');
        packet.append(name).append(',');
        packet.append(get_Stats(Effect.ADD_PODS)).append(',');
        packet.append(get_Stats(Effect.ADD_PROS)).append(',');
        packet.append(get_Stats(Effect.ADD_SAGE)).append(',');
        packet.append(maxCollectors);
        return packet.toString();
    }

    public void sendToMembers(Object packet) {
        String str = packet.toString();
        for (GuildMember member : members.values()) {
            if(member.getPlayer().isOnline())
                member.getPlayer().send(str);
        }
    }
    
    public void addCollector(Collector collector){
        collectors.add(collector);
    }
    
    public int getMaxMembers(){
        return 40 + (level - 1) * 10;
    }
    
    public boolean isFull(){
        return getMembers().size() >= getMaxMembers();
    }
    
    public int getCollectorsCount(){
        return collectors.size();
    }
    
    public int getCollectorCost(){
        return 1000 + 10 * getLevel();
    }

    public Collection<Collector> getCollectors() {
        return collectors;
    }
    
    public void removeCollector(Collector collector){
        collectors.remove(collector);
    }
    
    public Collector getCollectorById(int id){
        for(Collector collector : collectors){
            if(collector.getGuid() == id)
                return collector;
        }
        
        return null;
    }
    
    public Player getLeader(){
        for(GuildMember member : getMembers()){
            if(member.getRank() == 1)
                return member.getPlayer();
        }
        
        return null;
    }
    
    public int getMaxMountParks(){
        return getLevel() / 10;
    }
    
    static public class InvitationRequest implements Request{
        final private Player performer;
        final private Player target;

        public InvitationRequest(Player performer, Player target) {
            this.performer = performer;
            this.target = target;
        }
        
        @Override
        public Player getPerformer() {
            return performer;
        }

        @Override
        public Player getTarget() {
            return target;
        }

        @Override
        public void accept() {
            target.send(new JoinGuildResponse(JoinGuildResponse.YOUR_R_NEW_IN_GUILD));
            performer.send(new JoinGuildResponse(JoinGuildResponse.A_JOIN_YOUR_GUILD, target.getName()));
            target.setRequest(null);
            performer.setRequest(null);
        }

        @Override
        public void cancel() {
            target.send(new JoinGuildResponse(JoinGuildResponse.GUILD_JOIN_CANCEL));
            performer.send(new JoinGuildResponse(JoinGuildResponse.GUILD_JOIN_CANCEL));
            target.setRequest(null);
            performer.setRequest(null);
        }

        @Override
        public void refuse() {
            performer.send(new JoinGuildResponse(JoinGuildResponse.GUILD_JOIN_REFUSED, target.getName()));
            target.setRequest(null);
            performer.setRequest(null);
        }
        
    }
}
