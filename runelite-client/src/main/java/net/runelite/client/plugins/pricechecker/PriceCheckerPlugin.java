/*
 * Copyright (c) 2019 Jacky <https://github.com/jkybtw>
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

package net.runelite.client.plugins.pricechecker;

import com.google.inject.Provides;
import java.util.HashSet;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Inventory Price Checker",
	description = "Automatic price checker",
	tags = {"inventory", "price", "check"},
	enabledByDefault = false
)
public class PriceCheckerPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PriceCheckerOverlay overlay;

	@Inject
	private PriceCheckerConfig config;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ItemManager itemManager;

	@Getter
	private long totalProfit = 0;

	private HashSet<Integer> itemIds;

	private String[] ignoredNames;

	@Provides
	PriceCheckerConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PriceCheckerConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		itemIds = new HashSet<>();
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		clientThread.invoke(() -> calculatePrice());
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		itemIds.clear();

		String items[] = config.ignoreItemIdString().split(",");
		for (String id : items)
		{
			if (id.isEmpty()) continue;
			try
			{
				itemIds.add(Integer.valueOf(id));
			}
			catch (Exception e)
			{
			}
		}

		ignoredNames = config.ignoreItemNameString().split(",");
		for (String s : ignoredNames)
		{
			if (s.isEmpty()) continue;
			s = s.trim().toLowerCase();
		}

		clientThread.invoke(() -> calculatePrice());
	}

	private long getItemStackValue(Item item, ItemComposition itemComposition)
	{
		int id = item.getId();
		int qty = item.getQuantity();

		// Special case for coins and platinum tokens
		if (id == ItemID.COINS_995)
		{
			return qty;
		}
		else if (id == ItemID.PLATINUM_TOKEN)
		{
			return qty * 1000;
		}

		if (itemComposition.getNote() != -1)
		{
			id = itemComposition.getLinkedNoteId();
		}

		// Only check prices for things with store prices
		if (itemComposition.getPrice() <= 0)
		{
			return 0;
		}

		return itemManager.getItemPrice(id) * qty;
	}

	private void calculatePrice()
	{
		totalProfit = 0;

		final ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);

		if (itemContainer == null)
		{
			return;
		}

		final Item[] invItems = itemContainer.getItems();

		for (int i = 0; i < 28; i++)
		{
			if (i < invItems.length)
			{
				final Item item = invItems[i];
				final ItemComposition itemComposition = itemManager.getItemComposition(item.getId());
				if (!isItemIgnored(item, itemComposition))
				{
					totalProfit += getItemStackValue(item, itemComposition);
				}
			}
		}
	}

	private boolean isItemIgnored(Item item, ItemComposition itemComposition)
	{
		String itemName = itemComposition.getName().toLowerCase();

		if (item.getQuantity() == 0) return true;

		if (itemIds.contains(item.getId())) return true;

		for (String s : ignoredNames)
		{
			if (s.charAt(s.length()-1) == '*')
			{
				if (itemName.contains(s.substring(0, s.length()-2))) return true;
			}
			else
			{
				if (s.equals(itemName)) return true;
			}
		}

		return false;
	}
}
