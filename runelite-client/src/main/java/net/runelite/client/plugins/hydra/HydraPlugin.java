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
package net.runelite.client.plugins.hydra;

import com.google.inject.Provides;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
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
	name = "Hydra Boss",
	description = "Count Hydra Boss attack styles",
	tags = {"combat", "overlay", "pve", "pvm"}
)
public class HydraPlugin extends Plugin
{
	// 0 for mage, 1 for range
	@Getter
	private int attackStyle = 0;

	private int lastAttackStyle = 0;

	@Getter
	@Setter
	private int attackCount = 0;

	@Getter
	private int totalAttacks = 6;

	@Getter
	private int specialAttackCount = 7;

	private boolean mageTransition = false;
	private boolean rangeTransition = false;
	private boolean finalTransition = false;

	private int previousID = -1;

	@Getter
	private NPC hydra = null;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private HydraOverlay hydraOverlay;

	@Inject
	private HydraOverlayAbove hydraOverlayAbove;

	@Inject
	private HydraConfig config;


	@Provides
	HydraConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HydraConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(hydraOverlay);
		overlayManager.add(hydraOverlayAbove);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(hydraOverlay);
		overlayManager.remove(hydraOverlayAbove);
		reset();
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		if (event.getNpc().getId() == NpcID.ALCHEMICAL_HYDRA_8622)
		{
			reset();
		}
	}

	private void reset()
	{
		totalAttacks = 6;
		attackCount = 0;
		mageTransition = false;
		rangeTransition = false;
		finalTransition = false;
		specialAttackCount = 7;
		previousID = -1;
	}

	private void updateHydraState()
	{
		List<NPC> npcs = client.getNpcs();

		for (NPC npc : npcs)
		{
			if (npc.getName().equals("Alchemical Hydra"))
			{
				hydra = npc;

				if (npc.getId() == NpcID.ALCHEMICAL_HYDRA)
				{
					checkFormChange(npc.getId());
					totalAttacks = 6;
				}

				// mage is double attacking, ranged is single attacking
				if (npc.getId() == NpcID.ALCHEMICAL_HYDRA_8619)
				{
					checkFormChange(npc.getId());
					if (attackStyle == 0)
					{
						totalAttacks = 6;
					}
					else if (attackStyle == 1)
					{
						if (!rangeTransition)
						{
							attackCount /= 2;
						}
						totalAttacks = 3;
					}
					rangeTransition = true;
				}

				// both are single attacking
				if (npc.getId() == NpcID.ALCHEMICAL_HYDRA_8620)
				{
					checkFormChange(npc.getId());
					if (!mageTransition && attackStyle == 0)
					{
						attackCount /= 2;
					}
					totalAttacks = 3;
					mageTransition = true;
				}

				// final phase, jad phase
				if (npc.getId() == NpcID.ALCHEMICAL_HYDRA_8621)
				{
					checkFormChange(npc.getId());
					if (!finalTransition)
					{
						if (attackCount != 0)
						{
							attackCount = 0;
							attackStyle = (attackStyle == 1) ? 0 : 1;
						}
					}
					totalAttacks = 1;
					finalTransition = true;
				}
			}
			else
			{
				hydra = null;
			}
		}
	}

	private void checkFormChange(int id)
	{
		if (id != previousID)
		{
			previousID = id;
			specialAttackCount = 7;
		}
	}

	private void updateAttacks(int projectile)
	{
		// if it is a maged projectile
		if (projectile == 1662)
		{
			specialAttackCount++;
			// already mage
			if (attackStyle == 0)
			{
				attackCount++;
				if (attackCount == totalAttacks)
				{
					attackCount = 0;
					attackStyle = 1;
					lastAttackStyle = 0;
				}
			}
			else
			{
				// set to attacking maged
				attackCount = 1;
				attackStyle = 0;
				lastAttackStyle = 1;
			}
		}
		else if (projectile == 1663)
		{
			specialAttackCount++;
			// already ranging
			if (attackStyle == 1)
			{
				attackCount++;
				if (attackCount == totalAttacks)
				{
					attackCount = 0;
					attackStyle = 0;
					lastAttackStyle = 1;
				}
			}
			else
			{
				// set to attacking ranging
				attackCount = 1;
				attackStyle = 1;
				lastAttackStyle = 0;
			}
		}
	}

	private boolean fired = false;

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (!client.isInInstancedRegion())
		{
			reset();
			return;
		}

		updateHydraState();

		List<Projectile> projectiles = client.getProjectiles();

		if (fired)
		{
			for (Projectile projectile : projectiles)
			{
				if (projectile.getId() == 1662 || projectile.getId() == 1663)
				{
					return;
				}
			}

			fired = false;
			return;
		}

		for (Projectile projectile : projectiles)
		{
			if (projectile.getId() == 1662 || projectile.getId() == 1663)
			{
				fired = true;
				updateAttacks(projectile.getId());
			}
		}
	}


}
