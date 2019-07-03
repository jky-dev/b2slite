package net.runelite.client.plugins.grotesqueguardians;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("grotesqueguardians")
public interface GrotesqueGuardiansConfig extends Config
{
	@ConfigItem(
		keyName = "hideAttack",
		name = "Hide Dusk attack option",
		description = "Hides the option to attack Dusk during irrelevant times",
		position = 1
	)
	default boolean hideAttackOption()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showDuskTile",
		name = "Show Dusk tile",
		description = "Draws Dusk's tile",
		position = 2
	)
	default boolean showDuskTile()
	{
		return false;
	}
}
