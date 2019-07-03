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
package net.runelite.client.plugins.playerindicators;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Instant;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class PlayerFriendFoeOverlay extends Overlay
{
	private final PanelComponent panelComponent = new PanelComponent();
	private PlayerIndicatorsConfig config;
	private PlayerIndicatorsPlugin plugin;

	@Inject
	public PlayerFriendFoeOverlay(PlayerIndicatorsConfig config, PlayerIndicatorsPlugin plugin)
	{
		setPosition(OverlayPosition.TOP_LEFT);
		this.config = config;
		this.plugin = plugin;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().clear();

		if (!config.showFriendsOrFoes()) return null;

		if (config.showInfoBox()) return null;

		if (plugin.getFoes() > 0 || plugin.getFriends() > 0)
		{
			panelComponent.getChildren().add(TitleComponent.builder()
				.text("Friends / Enemies")
				.color(Color.GREEN)
				.build());

			if (plugin.getFriends() > 0 || plugin.getFoes() > 0)
			{
				panelComponent.getChildren().add(LineComponent.builder()
					.left(Integer.toString(plugin.getFriends()))
					.leftColor(Color.GREEN)
					.right(Integer.toString(plugin.getFoes()))
					.rightColor(Color.RED)
					.build());
			}
		}

		// cant seem to get skulls of other players - would need to unblock
		if (plugin.getFoesSkulled() > 0 || plugin.getFriendsSkulled() > 0)
		{
			panelComponent.getChildren().add(TitleComponent.builder()
				.text("Skulled")
				.color(Color.GREEN)
				.build());

			if (plugin.getFriendsSkulled() > 0 || plugin.getFoesSkulled() > 0)
			{
				panelComponent.getChildren().add(LineComponent.builder()
					.left(Integer.toString(plugin.getFriendsSkulled()))
					.leftColor(Color.GREEN)
					.right(Integer.toString(plugin.getFoesSkulled()))
					.rightColor(Color.RED)
					.build());
			}
		}

		return panelComponent.render(graphics);
	}
}
