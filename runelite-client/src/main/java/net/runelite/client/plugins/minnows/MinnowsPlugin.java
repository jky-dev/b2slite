/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
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

import com.google.common.eventbus.Subscribe;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.Query;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.queries.InventoryWidgetItemQuery;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.QueryRunner;

@PluginDescriptor(
	name = "Minnows",
	description = "Shows how many minnows you've caught in the session",
	tags = {"skilling", "fishing", "minnows", "overlay"}
)
public class MinnowsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MinnowsOverlay minnowsOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private QueryRunner queryRunner;

	@Getter(AccessLevel.PACKAGE)
	private int minnowsCaught;

	@Getter(AccessLevel.PACKAGE)
	private int minnowsCount = -1;

	@Getter(AccessLevel.PACKAGE)
	private long startTime;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(minnowsOverlay);
		minnowsCount = -1;
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(minnowsOverlay);
		minnowsCount = -1;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() != GameState.LOGGED_IN)
		{
			minnowsCount = -1;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		Query inventoryQuery = new InventoryWidgetItemQuery().idEquals(ItemID.MINNOW);
		WidgetItem[] inventoryWidgetItems = queryRunner.runQuery(inventoryQuery);

		if (inventoryWidgetItems.length == 1 && minnowsCount == -1)
		{
			minnowsCount = inventoryWidgetItems[0].getQuantity();
			startTime = System.currentTimeMillis();
		}
		else if (inventoryWidgetItems.length == 1)
		{
			minnowsCaught = inventoryWidgetItems[0].getQuantity() - minnowsCount;
		}
	}
}
