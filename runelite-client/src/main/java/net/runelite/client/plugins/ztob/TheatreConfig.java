/*
 * THIS SOFTWARE WRITTEN BY A KEYBOARD-WIELDING MONKEY BOI
 * No rights reserved. Use, redistribute, and modify at your own discretion,
 * and in accordance with Yagex and RuneLite guidelines.
 * However, aforementioned monkey would prefer if you don't sell this plugin for profit.
 * Good luck on your raids!
 */

package net.runelite.client.plugins.ztob;

import java.awt.Color;
import java.awt.event.KeyEvent;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ModifierlessKeybind;
import net.runelite.client.config.Range;

@ConfigGroup("Theatre")

public interface TheatreConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "MaidenBlood",
		name = "Maiden blood attack",
		description = ""
	)
	default boolean MaidenBlood()
	{
		return true;
	}

	@ConfigItem(
		position = 1,
		keyName = "maidenPoolColor",
		name = "Maiden Pool Color",
		description = "Configures the color for Maiden pools"
	)
	default Color maidenColor()
	{
		return Color.WHITE;
	}

	@ConfigItem(
		position = 2,
		keyName = "MaidenSpawns",
		name = "Maiden blood spawns",
		description = ""
	)
	default boolean MaidenSpawns()
	{
		return true;
	}

	@ConfigItem(
		position = 3,
		keyName = "BloatIndicator",
		name = "Bloat Indicator",
		description = ""
	)
	default boolean BloatIndicator()
	{
		return true;
	}

	@ConfigItem(
		position = 4,
		keyName = "bloat Timer",
		name = "Bloat Timer",
		description = ""
	)
	default boolean bloatTimer()
	{
		return true;
	}

	@ConfigItem(
		position = 5,
		keyName = "bloatFeet",
		name = "Bloat Feet",
		description = ""
	)
	default boolean bloatFeetIndicator()
	{
		return true;
	}

	@ConfigItem(
		position = 6,
		keyName = "bloatColor",
		name = "Bloat Feet Color",
		description = "Configures the color for falling feet"
	)
	default Color bloatColor()
	{
		return Color.WHITE;
	}

	@ConfigItem(
		position = 7,
		keyName = "NyloPillars",
		name = "Nylocas pillar health",
		description = ""
	)
	default boolean NyloPillars()
	{
		return true;
	}



	@ConfigItem(
		position = 8,
		keyName = "NyloBlasts",
		name = "Nylocas explosions",
		description = ""
	)
	default boolean NyloBlasts()
	{
		return true;
	}

	@ConfigItem(
		position = 9,
		keyName = "NyloMenu",
		name = "Hide Attack options for Nylocas",
		description = ""
	)

	default boolean NyloMenu() {
		return true;
	}

	@ConfigItem(
		position = 10,
		keyName = "highlightSote",
		name = "Sote Missiles",
		description = "Highlight Sotetseg's Missiles with pray type"
	)
	default boolean highlightSote()
	{
		return true;
	}

	@ConfigItem(
		position = 11,
		keyName = "SotetsegMaze1",
		name = "Sotetseg maze",
		description = ""
	)
	default boolean SotetsegMaze1()
	{
		return true;
	}

	@ConfigItem(
		position = 12,
		keyName = "SotetsegMaze2",
		name = "Sotetseg maze (solo mode)",
		description = ""
	)
	default boolean SotetsegMaze2()
	{
		return true;
	}

	@ConfigItem(
		position = 13,
		keyName = "XarpusExhumed",
		name = "Xarpus Exhumed",
		description = ""
	)
	default boolean XarpusExhumed()
	{
		return true;
	}

	@ConfigItem(
		position = 14,
		keyName = "XarpusTick",
		name = "Xarpus Tick",
		description = ""
	)
	default boolean XarpusTick()
	{
		return false;
	}

	@ConfigItem(
		position = 15,
		keyName = "xarpusExhumes",
		name = "Xarpus Exhume Counter",
		description = ""
	)
	default boolean XarpusExhumeOverlay()
	{
		return false;
	}

	@ConfigItem(
		position = 16,
		keyName = "VerzikCupcakes",
		name = "Verzik Projectile Markers",
		description = ""
	)
	default boolean VerzikCupcakes()
	{
		return false;
	}

	@ConfigItem(
		position = 17,
		keyName = "VerzikTick",
		name = "Verzik P3 Tick",
		description = ""
	)
	default boolean VerzikTick()
	{
		return false;
	}

	@ConfigItem(
		position = 18,
		keyName = "VerzikMelee",
		name = "Verzik P3 Melee Range",
		description = ""
	)
	default boolean VerzikMelee()
	{
		return false;
	}

	@ConfigItem(
		position = 19,
		keyName = "VerzikYellow",
		name = "Verzik Yellow Timing",
		description = ""
	)
	default boolean VerzikYellow()
	{
		return false;
	}

	@ConfigItem(
		position = 20,
		keyName = "Verzik Nylo",
		name = "Verzik Nylo Overlay",
		description = ""
	)
	default boolean NyloTargetOverlay()
	{
		return false;
	}

	@ConfigItem(
		position = 21,
		keyName = "VerzikTankTile",
		name = "Verzik P3 Tile Overlay",
		description = ""
	)
	default boolean verzikTankTile()
	{
		return true;
	}

	@ConfigItem(
		position = 22,
		keyName = "VerzikP3TimerKey",
		name = "Verzik P3 Manual Timer Key",
		description = ""
	)
	default ModifierlessKeybind verzikTimerKey()
	{
		return new ModifierlessKeybind(KeyEvent.VK_S, 0);
	}



}