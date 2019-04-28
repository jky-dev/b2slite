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
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.GraphicID;
import net.runelite.api.ItemID;
import net.runelite.api.Query;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Minnows Stats",
	description = "Show minnows stats",
	tags = {"fishing"}
)
public class MinnowsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MinnowsConfig config;

	@Inject
	private Notifier notifier;

	@Inject
	private MinnowsOverlay minnowsOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Getter(AccessLevel.PACKAGE)
	private int minnowsCaught;

	@Getter(AccessLevel.PACKAGE)
	private int minnowsCount = -1;

	@Getter(AccessLevel.PACKAGE)
	private long startTime;

	@Provides
	private MinnowsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MinnowsConfig.class);
	}

	@Subscribe
	public void configChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("minnows"))
		{
			return;
		}

		if (event.getKey().equals("camera"))
		{
			client.setOculusOrbState(config.detachedCamera() ? 1 : 0);
			client.setOculusOrbNormalSpeed(config.detachedCamera() ? 36 : 12);
		}
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(minnowsOverlay);
		minnowsCount = -1;
		client.setOculusOrbState(config.detachedCamera() ? 1 : 0);
		client.setOculusOrbNormalSpeed(config.detachedCamera() ? 36 : 12);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(minnowsOverlay);
		minnowsCount = -1;
		client.setOculusOrbState(0);
		client.setOculusOrbNormalSpeed(12);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() != GameState.LOGGED_IN)
		{
			minnowsCount = -1;
		}

		if (gameStateChanged.getGameState() == GameState.LOGGED_IN && client.getLocalPlayer().getWorldLocation().getRegionID() == 10293
		&& config.detachedCamera())
		{
			client.setOculusOrbState(config.detachedCamera() ? 1 : 0);
			client.setOculusOrbNormalSpeed(config.detachedCamera() ? 36 : 12);
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (client.getLocalPlayer().getInteracting() != null && client.getLocalPlayer().getInteracting().getGraphic() == GraphicID.FLYING_FISH)
		{
			notifier.notify("Flying fish");
		}
	}
}
