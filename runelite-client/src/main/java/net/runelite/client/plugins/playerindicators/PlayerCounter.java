/*
 * Copyright (c) 2018 Sebastiaan <https://github.com/SebastiaanVanspauwen>
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

import java.awt.*;
import java.awt.image.BufferedImage;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.infobox.InfoBox;

@Slf4j
class PlayerCounter extends InfoBox
{
	private final PlayerCounterType type;
	private PlayerIndicatorsConfig config;
	private PlayerIndicatorsPlugin plugin;

	PlayerCounter(BufferedImage image, PlayerIndicatorsPlugin plugin, PlayerCounterType type, PlayerIndicatorsConfig config)
	{
		super(image, plugin);
		this.config = config;
		this.plugin = plugin;
		this.type = type;
	}

	@Override
	public String getText()
	{
		if (type == PlayerCounterType.ENEMY) return Integer.toString(plugin.getFoes());
		if (type == PlayerCounterType.ENEMYSKULLED) return Integer.toString(plugin.getFoesSkulled());
		if (type == PlayerCounterType.FRIEND) return Integer.toString(plugin.getFriends());
		if (type == PlayerCounterType.FRIENDSKULLED) return Integer.toString(plugin.getFriendsSkulled());
		return null;
	}

	@Override
	public Color getTextColor() {
		return Color.WHITE;
	}

	@Override
	public boolean render()
	{
		if (config.showFriendsOrFoes() && config.showInfoBox())
		{
			if (config.showCounterType() != PlayerShowCounterType.SKULLED)
			{
				if (this.type == PlayerCounterType.FRIEND && plugin.getFriends() > 0) return true;
				if (this.type == PlayerCounterType.ENEMY && plugin.getFoes() > 0) return true;
			}
			if (config.showCounterType() != PlayerShowCounterType.ALL)
			{
				if (this.type == PlayerCounterType.FRIENDSKULLED && plugin.getFriendsSkulled() > 0) return true;
				if (this.type == PlayerCounterType.ENEMYSKULLED && plugin.getFoesSkulled() > 0) return true;
			}
		}
		return false;
	}
}
