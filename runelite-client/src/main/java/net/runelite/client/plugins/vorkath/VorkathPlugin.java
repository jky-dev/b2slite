/*
 * Copyright (c) 2019, Jacky <https://github.com/jkybtw>
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
package net.runelite.client.plugins.vorkath;

import com.google.inject.Provides;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Projectile;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Vorkath",
	description = "Count vorkath attack styles",
	tags = {"combat", "overlay", "pve", "pvm"}
)
public class VorkathPlugin extends Plugin
{
	@Getter
	private int attackCount = 0;

	@Getter
	private NPC vorkath = null;

	@Inject
	private Client client;

	@Inject
	private VorkathOverlay vorkathOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private VorkathConfig config;


	@Provides
	VorkathConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(VorkathConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(vorkathOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(vorkathOverlay);
		reset();
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		if (event.getNpc().getId() == NpcID.VORKATH_8061)
		{
			reset();
		}
	}

	private void reset()
	{
		attackCount = 0;
		vorkath = null;
	}

	private void updateVorkath()
	{
		List<NPC> npcs = client.getNpcs();

		for (NPC npc : npcs)
		{
			if (npc.getName().equals("Vorkath") && npc.getId() == 8601)
			{
				vorkath = npc;
				return;
			}
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (!client.isInInstancedRegion())
		{
			reset();
			return;
		}

		updateVorkath();

		if (vorkath == null) return;

		// count the attacks
		if (vorkath.getAnimation() != -1) attackCount++;

		if (attackCount == 8)
		{
			attackCount = 1;
		}
	}

}
