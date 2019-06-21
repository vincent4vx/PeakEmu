/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.dao;

import org.peakemu.database.Database;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class DAOFactory {
    final private Database database;
    
    final private AccountDAO accountDAO;
    final private AreaDAO areaDAO;
    final private SubAreaDAO subAreaDAO;
    final private MapDAO mapDAO;
    final private PlayerDAO playerDAO;
    final private ItemDAO itemDAO;
    final private InventoryDAO inventoryDAO;
    final private ItemSetDAO itemSetDAO;
    final private IOTemplateDAO ioTemplateDAO;
    final private TriggerDAO triggerDAO;
    final private EndFightActionDAO endFightActionDAO;
    final private WaypointDAO waypointDAO;
    final private MonsterDAO monsterDAO;
    final private UseItemActionDAO useItemActionDAO;
    final private PrismDAO prismDAO;
    final private GuildDAO guildDAO;
    final private GuildMemberDAO guildMemberDAO;
    final private CollectorDAO collectorDAO;
    final private FriendListDAO friendListDAO;
    final private NPCQuestionDAO npcQuestionDAO;
    final private NPCResponseDAO npcResponseDAO;
    final private NPCTemplateDAO npcTemplateDAO;
    final private NpcDAO npcDAO;
    final private PlayerStoreDAO playerStoreDAO;
    final private MountParkDAO mountParkDAO;
    final private MountDAO mountDAO;
    final private FixedMonsterGroupDAO fixedMonsterGroupDAO;
    final private CraftDAO craftDAO;
    final private JobSkillDAO jobSkillDAO;
    final private JobDAO jobDAO;
    final private LiveActionDAO liveActionDAO;
    final private SpellDAO spellDAO;
    final private ClassDataDAO classDataDAO;
    final private ExpDAO expDAO;
    final private MountTemplateDAO mountTemplateDAO;

    public DAOFactory(Database database) {
        this.database = database;
        
        expDAO = new ExpDAO(database);
        spellDAO = new SpellDAO(database);
        triggerDAO = new TriggerDAO(database);
        itemSetDAO = new ItemSetDAO(database);
        useItemActionDAO = new UseItemActionDAO(database);
        itemDAO = new ItemDAO(database, itemSetDAO, useItemActionDAO);
        inventoryDAO = new InventoryDAO(database, itemDAO);
        accountDAO = new AccountDAO(database, inventoryDAO);
        areaDAO = new AreaDAO(database);
        subAreaDAO = new SubAreaDAO(database, areaDAO);
        ioTemplateDAO = new IOTemplateDAO(database);
        monsterDAO = new MonsterDAO(database, spellDAO);
        mapDAO = new MapDAO(database, subAreaDAO, ioTemplateDAO, triggerDAO, monsterDAO);
        classDataDAO = new ClassDataDAO(database, spellDAO);
        playerStoreDAO = new PlayerStoreDAO(database, inventoryDAO);
        mountTemplateDAO = new MountTemplateDAO(database, itemDAO);
        mountDAO = new MountDAO(database, inventoryDAO, mountTemplateDAO, expDAO);
        jobDAO = new JobDAO(database, itemDAO);
        craftDAO = new CraftDAO(database, itemDAO);
        jobSkillDAO = new JobSkillDAO(database, jobDAO, craftDAO, itemDAO);
        playerDAO = new PlayerDAO(database, accountDAO, inventoryDAO, mapDAO, playerStoreDAO, mountDAO, jobDAO, spellDAO, expDAO);
        endFightActionDAO = new EndFightActionDAO(database);
        waypointDAO = new WaypointDAO(database, mapDAO);
        prismDAO = new PrismDAO(database, mapDAO, spellDAO);
        guildDAO = new GuildDAO(database, spellDAO);
        guildMemberDAO = new GuildMemberDAO(database, playerDAO, guildDAO);
        collectorDAO = new CollectorDAO(database, guildDAO, mapDAO, inventoryDAO);
        friendListDAO = new FriendListDAO(database, accountDAO);
        npcResponseDAO = new NPCResponseDAO(database);
        npcQuestionDAO = new NPCQuestionDAO(database, npcResponseDAO);
        npcTemplateDAO = new NPCTemplateDAO(database, npcQuestionDAO, itemDAO);
        npcDAO = new NpcDAO(database, mapDAO, npcTemplateDAO);
        mountParkDAO = new MountParkDAO(database, mapDAO, guildDAO, mountDAO, playerDAO);
        fixedMonsterGroupDAO = new FixedMonsterGroupDAO(database, mapDAO, monsterDAO);
        liveActionDAO = new LiveActionDAO(database, playerDAO);
    }

    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    public AreaDAO getAreaDAO() {
        return areaDAO;
    }

    public SubAreaDAO getSubAreaDAO() {
        return subAreaDAO;
    }

    public MapDAO getMapDAO() {
        return mapDAO;
    }

    public PlayerDAO getPlayerDAO() {
        return playerDAO;
    }

    public ItemDAO getItemDAO() {
        return itemDAO;
    }

    public InventoryDAO getInventoryDAO() {
        return inventoryDAO;
    }

    public ItemSetDAO getItemSetDAO() {
        return itemSetDAO;
    }

    public IOTemplateDAO getIoTemplateDAO() {
        return ioTemplateDAO;
    }

    public TriggerDAO getTriggerDAO() {
        return triggerDAO;
    }

    public EndFightActionDAO getEndFightActionDAO() {
        return endFightActionDAO;
    }

    public WaypointDAO getWaypointDAO() {
        return waypointDAO;
    }

    public MonsterDAO getMonsterDAO() {
        return monsterDAO;
    }

    public UseItemActionDAO getUseItemActionDAO() {
        return useItemActionDAO;
    }

    public PrismDAO getPrismDAO() {
        return prismDAO;
    }

    public GuildDAO getGuildDAO() {
        return guildDAO;
    }

    public GuildMemberDAO getGuildMemberDAO() {
        return guildMemberDAO;
    }

    public CollectorDAO getCollectorDAO() {
        return collectorDAO;
    }

    public FriendListDAO getFriendListDAO() {
        return friendListDAO;
    }

    public NPCQuestionDAO getNpcQuestionDAO() {
        return npcQuestionDAO;
    }

    public NPCResponseDAO getNpcResponseDAO() {
        return npcResponseDAO;
    }

    public NPCTemplateDAO getNpcTemplateDAO() {
        return npcTemplateDAO;
    }

    public NpcDAO getNpcDAO() {
        return npcDAO;
    }

    public PlayerStoreDAO getPlayerStoreDAO() {
        return playerStoreDAO;
    }

    public MountParkDAO getMountParkDAO() {
        return mountParkDAO;
    }

    public MountDAO getMountDAO() {
        return mountDAO;
    }

    public FixedMonsterGroupDAO getFixedMonsterGroupDAO() {
        return fixedMonsterGroupDAO;
    }

    public CraftDAO getCraftDAO() {
        return craftDAO;
    }

    public JobSkillDAO getJobSkillDAO() {
        return jobSkillDAO;
    }

    public JobDAO getJobDAO() {
        return jobDAO;
    }

    public LiveActionDAO getLiveActionDAO() {
        return liveActionDAO;
    }

    public SpellDAO getSpellDAO() {
        return spellDAO;
    }

    public ClassDataDAO getClassDataDAO() {
        return classDataDAO;
    }

    public ExpDAO getExpDAO() {
        return expDAO;
    }

    public MountTemplateDAO getMountTemplateDAO() {
        return mountTemplateDAO;
    }
    
}
