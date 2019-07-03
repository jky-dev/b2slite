/*
 * Copyright (c) 2018, Damen <https://github.com/basicDamen>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *	list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *	this list of conditions and the following disclaimer in the documentation
 *	and/or other materials provided with the distribution.
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
package net.runelite.client.plugins.grotesqueguardians;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

@PluginDescriptor(
	name = "Grotesque Guardians",
	description = "Display tile indicators for the Grotesque Guardian special attacks",
	tags = {"grotesque", "guardians", "gargoyle", "garg"}
)

@Slf4j
public class GrotesqueGuardiansPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private GrotesqueGuardiansOverlay overlay;

	@Inject
	private Client client;

	@Inject
	private GrotesqueGuardiansConfig config;

	@Getter
	private boolean hideAttackOption;

	@Getter
	private boolean inGGArea;

	@Getter
	private NPC dusk;

	private static final int[] GUARDIAN_AREA = { 6727 };

	@Provides
	GrotesqueGuardiansConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GrotesqueGuardiansConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		hideAttackOption = false;
		dusk = null;
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		hideAttackOption = false;
		dusk = null;
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		if (!inGGArea) return;
		if (event.getNpc().getId() == NpcID.DUSK_7851 || // initial DUSK spawn at the start
			event.getNpc().getId() == NpcID.DAWN_7853) // DAWN comes back
		{
			hideAttackOption = true;
			for (NPC npc : client.getNpcs())
			{
				if (npc.getName().toLowerCase().contains("dusk"))
				{
					dusk = npc;
					return;
				}
			}
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		if (!inGGArea) return;
		if (event.getNpc().getId() == NpcID.DAWN_7853 || event.getNpc().getId() == NpcID.DAWN_7885) // dawn disappears
		{
			hideAttackOption = false;
			dusk = null;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		inGGArea = client.isInInstancedRegion() && Arrays.equals(client.getMapRegions(), GUARDIAN_AREA);
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (config.hideAttackOption() && hideAttackOption && inGGArea) removeAttackOption();
	}

	private void removeAttackOption()
	{
		int entryLength = 0;
		List<MenuEntry> entryList = new ArrayList<>();
		for (MenuEntry entry : client.getMenuEntries()) {
			if (Text.removeTags(entry.getTarget()).contains("Dusk") && entry.getOption().equals("Attack"))
			{

			}
			else
			{
				entryList.add(entry);
				entryLength++;
			}
		}

		if (entryLength != 0)
		{
			MenuEntry[] newEntries = new MenuEntry[entryLength];
			for (int i = 0; i < (entryLength); i++)
			{
				newEntries[i] = entryList.get(i);
			}
			client.setMenuEntries(newEntries);
		}
	}
}
