/*
 * Copyright (c) 2019, Jacky <liangj97@gmail.com>
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
package net.runelite.client.plugins.molchpearls;

import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Molch Pearl Tracker",
	description = "Tracks Molch Pearl stats",
	tags = {"overlay", "skilling", "pearl", "aerial", "fishing"}
)
public class MolchPearlPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private MolchPearlConfig config;

	@Inject
	private MolchPearlOverlay overlay;

	@Getter(AccessLevel.PACKAGE)
	private int currentPearls, totalPearlsFound, minFish, maxFish, currentFish, totalFish, avgFish;

	@Getter(AccessLevel.PACKAGE)
	private boolean isInMolchIsland = false;

	private boolean caught, checkedInventory;

	private static int MOLCH_PEARL_ID = 22820;
	private static int MOLCH_ISLAND_ID = 5432;

	@Provides
	MolchPearlConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MolchPearlConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		avgFish = 0;
		currentFish = 0;
		currentPearls = checkCurrentPearls();
		maxFish = 0;
		minFish = 0;
		totalFish = 0;
		totalPearlsFound = 0;
		caught = false;
		checkedInventory = false;
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		avgFish = 0;
		currentFish = 0;
		currentPearls = 0;
		maxFish = 0;
		minFish = 0;
		totalFish = 0;
		totalPearlsFound = 0;
		caught = false;
		checkedInventory = false;
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		checkedInventory = false;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		isInMolchIsland = checkMolchIsland();

		if (!isInMolchIsland)
		{
			return;
		}

		if (!checkedInventory)
		{
			currentPearls = checkCurrentPearls();
		}
	}

	private int checkCurrentPearls()
	{
		final ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);

		if (itemContainer == null)
		{
			return -1;
		}

		checkedInventory = true;

		final Item[] items = itemContainer.getItems();

		for (Item item : items)
		{
			if (item.getId() == MOLCH_PEARL_ID)
			{
				return item.getQuantity();
			}
		}
		return 0;
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (!isInMolchIsland || event.getType() != ChatMessageType.SPAM)
		{
			return;
		}

		if (event.getMessage().equals("Your cormorant returns with its catch."))
		{
			totalFish++;
			if (caught)
			{
				currentFish = 0;
				caught = false;
			}

			currentFish++;

			if (totalPearlsFound > 0)
			{
				avgFish = totalFish / totalPearlsFound;
			}

			if (caughtPearl())
			{
				totalPearlsFound++;

				if (minFish == 0)
				{
					minFish = currentFish;
				}

				minFish = currentFish < minFish ? currentFish : minFish;
				maxFish = currentFish > maxFish ? currentFish : maxFish;
				caught = true;
			}
		}
	}

	private boolean caughtPearl()
	{
		final ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);

		if (itemContainer == null)
		{
			return false;
		}

		final Item[] items = itemContainer.getItems();

		for (Item item : items)
		{
			if (item.getId() == 22820)
			{
				if (item.getQuantity() > currentPearls)
				{
					currentPearls = item.getQuantity();
					return true;
				}
				return false;
			}
		}
		return false;
	}

	private boolean checkMolchIsland()
	{
		WorldPoint point = client.getLocalPlayer().getWorldLocation();
		if (point == null)
		{
			return false;
		}
		return point.getRegionID() == MOLCH_ISLAND_ID;
	}
}
