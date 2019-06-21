/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.objects;

import org.peakemu.world.MapCell;
import org.peakemu.world.MoveableSprite;
import org.peakemu.world.NPCTemplate;
import org.peakemu.world.Sprite;
import org.peakemu.world.enums.SpriteTypeEnum;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class NPC implements Sprite {
    final private NPCTemplate _template;
    private MapCell cell;
    private byte _orientation;
    
    private int spriteId = 0;

    public NPC(NPCTemplate temp, MapCell cell, byte o) {
        _template = temp;
        this.cell = cell;
        _orientation = o;
    }

    public NPCTemplate getTemplate() {
        return _template;
    }

    @Override
    public MapCell getCell() {
        return cell;
    }

    @Override
    public int getSpriteId() {
        return spriteId;
    }

    public void setSpriteId(int spriteId) {
        this.spriteId = spriteId;
    }

    public int get_orientation() {
        return _orientation;
    }

    @Override
    public SpriteTypeEnum getSpriteType() {
        return SpriteTypeEnum.NPC;
    }

    @Override
    public String getSpritePacket() {
        StringBuilder sock = new StringBuilder();
        sock.append(cell.getID()).append(";");
        sock.append(_orientation).append(";");
        sock.append("0").append(";");
        sock.append(getSpriteId()).append(";");
        sock.append(_template.get_id()).append(";");
        sock.append(getSpriteType().toInt()).append(";");//type = NPC

        StringBuilder taille = new StringBuilder();
        if (_template.get_scaleX() == _template.get_scaleY()) {
            taille.append(_template.get_scaleY());
        } else {
            taille.append(_template.get_scaleX()).append("x").append(_template.get_scaleY());
        }
        sock.append(_template.get_gfxID()).append("^").append(taille.toString()).append(";");
        sock.append(_template.get_sex()).append(";");
        sock.append((_template.get_color1() != -1 ? Integer.toHexString(_template.get_color1()) : "-1")).append(";");
        sock.append((_template.get_color2() != -1 ? Integer.toHexString(_template.get_color2()) : "-1")).append(";");
        sock.append((_template.get_color3() != -1 ? Integer.toHexString(_template.get_color3()) : "-1")).append(";");
        sock.append(_template.get_acces()).append(";");
        sock.append((_template.get_extraClip() != -1 ? (_template.get_extraClip()) : (""))).append(";");
        sock.append(_template.get_customArtWork());
        return sock.toString();
    }

    public void setCell(MapCell cell) {
        this.cell = cell;
    }

    public void setOrientation(byte o) {
        _orientation = o;
    }

}
