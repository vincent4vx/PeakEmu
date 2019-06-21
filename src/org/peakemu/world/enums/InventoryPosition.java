/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.world.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public enum InventoryPosition {
    NO_EQUIPED(-1){
        @Override
        public boolean isValidPosition(ItemType type) {
            return true;
        }

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    AMULETTE(0){

        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.AMULETTE;
        }
        
    },
    ARME(1){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isWeapon(type);
        }
        
    },
    ANNEAU1(2){

        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.ANNEAU;
        }
        
    },
    CEINTURE(3){

        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.CEINTURE;
        }
        
    },
    ANNEAU2(4){

        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.ANNEAU;
        }
        
    },
    BOTTES(5){

        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.BOTTES;
        }
        
    },
    COIFFE(6){

        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.COIFFE;
        }
        
    },
    CAPE(7){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isAMantle(type);
        }
        
    },
    FAMILIER(8){

        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.FAMILIER;
        }
        
    },
    DOFUS1(9){

        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.DOFUS;
        }
        
    },
    DOFUS2(10){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.DOFUS;
        }
        
    },
    DOFUS3(11){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.DOFUS;
        }
        
    },
    DOFUS4(12){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.DOFUS;
        }
        
    },
    DOFUS5(13){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.DOFUS;
        }
        
    },
    DOFUS6(14){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.DOFUS;
        }
        
    },
    BOUCLIER(15){

        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.BOUCLIER;
        }
        
    },
    DRAGODINDE(16){

        @Override
        public boolean isValidPosition(ItemType type) {
            return type == ItemType.POISSON || type == ItemType.VIANDE;
        }

        @Override
        public boolean isEquipment() {
            return false;
        }
        
        
    },
    //Bonbons
    CANDY(20){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isBuff(type);
        }
        
    },
    CANDY1(21){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isBuff(type);
        }
        
    },
    CANDY2(22){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isBuff(type);
        }
        
    },
    CANDY3(23){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isBuff(type);
        }
        
    },
    CANDY4(24){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isBuff(type);
        }
        
    },
    CANDY5(25){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isBuff(type);
        }
        
    },
    CANDY6(26){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isBuff(type);
        }
        
    },
    CANDY7(27){
        
        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isBuff(type);
        }
        
    },
    
    //Usable items
    USABLE35(35){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE36(36){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE37(37){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE38(38){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE39(39){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE40(40){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE41(41){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE42(42){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE43(43){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE44(44){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE45(45){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE46(46){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE47(47){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    USABLE48(48){

        @Override
        public boolean isValidPosition(ItemType type) {
            return ItemType.isUsable(type);
        }
        

        @Override
        public boolean isEquipment() {
            return false;
        }
    },
    ;
    
    final static private Map<Integer, InventoryPosition> positions = new HashMap<>();
    
    final static public InventoryPosition[] BUFF_POSITIONS = new InventoryPosition[]{
        CANDY, CANDY1, CANDY2, CANDY3, CANDY4, CANDY5, CANDY6, CANDY7
    };
    
    final static public InventoryPosition[] STUFF_POSITIONS = new InventoryPosition[]{
        AMULETTE, ARME, ANNEAU1, ANNEAU2, CEINTURE, BOTTES, COIFFE, FAMILIER,
        DOFUS1, DOFUS2, DOFUS3, DOFUS4, DOFUS5, DOFUS6, BOUCLIER, CAPE
    };
    
    static{
        for(InventoryPosition position : values())
            positions.put(position.id, position);
    }
    
    final private int id;

    private InventoryPosition(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public boolean isEquipment(){
        return true;
    }
    
    abstract public boolean isValidPosition(ItemType type);
    
    static public InventoryPosition fromId(int pos){
        return positions.get(pos);
    }
}
