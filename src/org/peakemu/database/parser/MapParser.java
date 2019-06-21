/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.database.parser;

import static org.peakemu.common.CryptManager.getIntByHashedValue;
import org.peakemu.database.dao.IOTemplateDAO;
import org.peakemu.world.GameMap;
import org.peakemu.world.IOTemplate;
import org.peakemu.world.MapCell;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public class MapParser {
    static public void parseCells(GameMap map, String dData, String cellsData, IOTemplateDAO ioTemplateDAO) {
        if (!dData.isEmpty()) {
            for (int f = 0; f < dData.length(); f += 10) {
                String CellData = dData.substring(f, f + 10);
                int Type = (((byte) getIntByHashedValue(CellData.charAt(2))) & 56) >> 3;
                boolean IsSightBlocker = (((byte) getIntByHashedValue(CellData.charAt(0))) & 1) != 0;
                int layerObject2 = ((((byte) getIntByHashedValue(CellData.charAt(0))) & 2) << 12) + ((((byte) getIntByHashedValue(CellData.charAt(7))) & 1) << 12) + (((byte) getIntByHashedValue(CellData.charAt(8))) << 6) + (byte) getIntByHashedValue(CellData.charAt(9));
                boolean layerObject2Interactive = ((((byte) getIntByHashedValue(CellData.charAt(7))) & 2) >> 1) != 0;
                int obj = (layerObject2Interactive ? layerObject2 : -1);
                IOTemplate ioTemplate = obj == -1 ? null : ioTemplateDAO.getIOTemplateById(obj);
                
                map.addCell(new MapCell(map, f / 10, Type != 0, IsSightBlocker, obj, ioTemplate));
            }
        } else {
            String[] cellsDataArray = cellsData.split("\\|");

            for (String o : cellsDataArray) {

                boolean Walkable = true;
                boolean LineOfSight = true;
                int Number = -1;
                int obj = -1;
                String[] cellInfos = o.split(",");
                try {
                    Walkable = cellInfos[2].equals("1");
                    LineOfSight = cellInfos[1].equals("1");
                    Number = Integer.parseInt(cellInfos[0]);
                    if (!cellInfos[3].trim().equals("")) {
                        obj = Integer.parseInt(cellInfos[3]);
                    }
                } catch (Exception d) {
                };
                if (Number == -1) {
                    continue;
                }
                
                
                IOTemplate ioTemplate = obj == -1 ? null : ioTemplateDAO.getIOTemplateById(obj);

                map.addCell(new MapCell(map, Number, Walkable, LineOfSight, obj, ioTemplate));
            }
        }
    }
}
