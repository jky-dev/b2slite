/*
 * Copyright (c) 2017, Seth <Sethtroll3@gmail.com>
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
package net.runelite.client.plugins.minnows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class MinnowsOverlay extends Overlay
{
	private final Client client;
	private final MinnowsPlugin plugin;
	private final PanelComponent panelComponent = new PanelComponent();
	private final ItemComposition rawShark;
	private final ItemManager itemManager;

	@Inject
	public MinnowsOverlay(Client client, ItemManager itemManager, MinnowsPlugin plugin)
	{
		setPosition(OverlayPosition.TOP_LEFT);
		this.client = client;
		this.itemManager = itemManager;
		this.plugin = plugin;
		this.rawShark = itemManager.getItemComposition(ItemID.RAW_SHARK);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		{
		panelComponent.getChildren().clear();
			if (plugin.getMinnowsCaught() > 0)
			{

				panelComponent.getChildren().add(TitleComponent.builder()
					.text("Minnow Stats")
					.color(Color.GREEN)
					.build());

				panelComponent.getChildren().add(LineComponent.builder()
					.left("Caught sharks")
					.right(Integer.toString(plugin.getMinnowsCaught() / 40))
					.build());

				panelComponent.getChildren().add(LineComponent.builder()
					.left("Sharks/hr:")
					.right(Integer.toString(toPerHour(plugin.getMinnowsCaught())))
					.build());

				panelComponent.getChildren().add(LineComponent.builder()
					.left("GP/hr:")
					.right(Integer.toString(toPerHour(plugin.getMinnowsCaught() * rawShark.getPrice()) / 1000) + "K")
					.build());
			}
		}

		return panelComponent.render(graphics);
	}

	private int toPerHour(int value)
	{
		return (int) ((1.0 / (Math.max(60, ((System.currentTimeMillis() - plugin.getStartTime()) / 1000)) / 3600.0)) * (value / 40));
	}
}
