/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.handler;

import org.peakemu.Peak;
import org.peakemu.database.dao.InventoryDAO;
import org.peakemu.database.dao.ItemDAO;
import org.peakemu.database.dao.ItemSetDAO;
import org.peakemu.database.dao.UseItemActionDAO;
import org.peakemu.objects.item.Item;
import org.peakemu.objects.item.factory.BuffItemFactory;
import org.peakemu.objects.item.factory.ItemFactory;
import org.peakemu.objects.item.factory.MutationItemFactory;
import org.peakemu.objects.item.factory.WeaponFactory;
import org.peakemu.world.ItemSet;
import org.peakemu.world.ItemTemplate;
import org.peakemu.world.StatsTemplate;
import org.peakemu.world.enums.InventoryPosition;
import org.peakemu.world.enums.ItemType;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class ItemHandler {
    final private ItemDAO itemDAO;
    final private ItemSetDAO itemSetDAO;
    final private UseItemActionDAO useItemActionDAO;
    final private InventoryDAO inventoryDAO;
    final private Peak peak;

    public ItemHandler(ItemDAO itemDAO, ItemSetDAO itemSetDAO, UseItemActionDAO useItemActionDAO, InventoryDAO inventoryDAO, Peak peak) {
        this.itemDAO = itemDAO;
        this.itemSetDAO = itemSetDAO;
        this.useItemActionDAO = useItemActionDAO;
        this.inventoryDAO = inventoryDAO;
        this.peak = peak;
    }
    
    public void load(){
        loadFactories();
        
        System.out.print("Chargement des actions des items : ");
        System.out.println(useItemActionDAO.getAll().size() + " actions chargées");
        
        System.out.print("Chargement des panoplies : ");
        System.out.println(itemSetDAO.getAll().size() + " panoplies chargées");
        
        System.out.print("Chargement des items : ");
        int nb = itemDAO.getAll().size();
        System.out.println(nb + " items chargées");
    }
    
    private void loadFactories(){
        final WeaponFactory weaponFactory = new WeaponFactory();
        
        for(ItemType type : ItemType.WEAPONS){
            inventoryDAO.setFactory(type, weaponFactory);
        }
        
        final BuffItemFactory buffItemFactory = new BuffItemFactory();
        
        for(ItemType type : ItemType.BUFFS){
            inventoryDAO.setFactory(type, buffItemFactory);
        }
        
        inventoryDAO.setFactory(ItemType.TRANSFORM, new MutationItemFactory(peak.getWorld().getMonsterHandler()));
    }

    public ItemFactory getFactory(ItemType type) {
        return inventoryDAO.getFactory(type);
    }

    public ItemTemplate getTemplateById(int id) {
        return itemDAO.getById(id);
    }

    public ItemSet getItemSetById(int id) {
        return itemSetDAO.getItemSetById(id);
    }
    
    public Item createNewItem(ItemTemplate itemTemplate, int qte, boolean useMax){
        return getFactory(itemTemplate.getType()).createItem(
            "", -1, itemTemplate, qte, 
            InventoryPosition.NO_EQUIPED, 
            useMax ? itemTemplate.getStatsTemplate().maximizeStats() : itemTemplate.getStatsTemplate().randomizeStats()
        );
    }
    
    public Item createNewItem(ItemTemplate template, int qua, StatsTemplate stats){
        return getFactory(template.getType()).createItem("", -1, template, qua, InventoryPosition.NO_EQUIPED, stats);
    }
}
