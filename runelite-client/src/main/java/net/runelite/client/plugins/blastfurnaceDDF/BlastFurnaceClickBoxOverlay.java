/*
 * Copyright (c) 2018, Seth <Sethtroll3@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.blastfurnaceDDF;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;

import com.sun.javafx.geom.Curve;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameObject;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.Point;
import net.runelite.api.Varbits;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;

class BlastFurnaceClickBoxOverlay extends Overlay {
    private static final int MAX_DISTANCE = 2350;

    private final Client client;
    private final BlastFurnaceDDFPlugin plugin;
    private final BlastFurnaceConfigDDF config;

    @Inject
    private BlastFurnaceClickBoxOverlay(Client client, BlastFurnaceDDFPlugin plugin, BlastFurnaceConfigDDF config) {
        setPosition(OverlayPosition.DYNAMIC);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        int dispenserState = client.getVar(Varbits.BAR_DISPENSER);

        if (config.showConveyorBelt() && plugin.getConveyorBelt() != null) {
            Color color = dispenserState == 1 ? Color.RED : Color.GREEN;
            renderObject(plugin.getConveyorBelt(), graphics, color);
        }

        if (config.showBarDispenser() && plugin.getBarDispenser() != null) {
            boolean hasIceGloves = hasIceGloves();
            Color color = dispenserState == 2 && hasIceGloves ? Color.GREEN : (dispenserState == 3 ? Color.GREEN : Color.RED);

            renderObject(plugin.getBarDispenser(), graphics, color);
        }
        Color color = Color.GREEN;
        if (plugin.getBankChest() != null)
        {
            renderObject(plugin.getBankChest(), graphics, color);
        }
        if (plugin.getCoffer() != null)
        {
            renderObject(plugin.getCoffer(), graphics, color);
        }
        return null;
    }

    private boolean hasIceGloves() {
        ItemContainer equipmentContainer = client.getItemContainer(InventoryID.EQUIPMENT);
        if (equipmentContainer == null) {
            return false;
        }

        Item[] items = equipmentContainer.getItems();
        int idx = EquipmentInventorySlot.GLOVES.getSlotIdx();

        if (items == null || idx >= items.length) {
            return false;
        }

        Item glove = items[idx];
        return glove != null && glove.getId() == ItemID.ICE_GLOVES;
    }

    private void renderObject(GameObject object, Graphics2D graphics, Color color) {
        LocalPoint localLocation = client.getLocalPlayer().getLocalLocation();
        Point mousePosition = client.getMouseCanvasPosition();

        LocalPoint location = object.getLocalLocation();

        if (localLocation.distanceTo(location) <= MAX_DISTANCE) {
            Area objectClickbox = object.getClickbox();
            writeArea(objectClickbox, "C:\\Users\\dan\\Documents\\ahk\\objectLocation\\" + Integer.toString(object.getId()) + ".txt");
            if (objectClickbox != null) {
                if (objectClickbox.contains(mousePosition.getX(), mousePosition.getY())) {
                    graphics.setColor(color.darker());
                } else {
                    graphics.setColor(color);
                }
                graphics.draw(objectClickbox);
                graphics.setColor(color);
                graphics.fill(objectClickbox);
            }
        }
    }

    private void writeArea(Area area, String fileName) {
        FileWriter fileWriter = null;
        int NUM_COORDS[] = { 2, 2, 4, 6, 0 };
        try {
            fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            PathIterator it = area.getPathIterator(new AffineTransform());
            while(!it.isDone())
            {
                double coords[] = new double[6];
                int ret = it.currentSegment(coords);
                for (int i = 0; i < NUM_COORDS[ret]; i++)
                {
                    printWriter.print(coords[i]);
                    printWriter.printf(",");
                }
                printWriter.printf("\r\n");

                it.next();
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}