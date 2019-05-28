package net.runelite.client.plugins.debug;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Debug",
	description = "Debugger",
	tags = {"debug"}
)
public class DebugPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private DebugConfig config;

	@Inject
	private DebugSceneOverlay debugSceneOverlay;

	@Provides
	DebugConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DebugConfig.class);
	}

	@Getter
	private List<NPC> npcs;

	@Override
	protected void startUp() throws Exception
	{
		npcs = new ArrayList<>();
		overlayManager.add(debugSceneOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(debugSceneOverlay);
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		if (checkNPC(event.getNpc()))
		{
			npcs.add(event.getNpc());
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		if (npcs.contains(event.getNpc())) npcs.remove(event.getNpc());
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{

	}

	private boolean checkNPC(NPC npc)
	{
		if (npc == null) return false;

		String[] npcs = config.npcString().split(",");

		if (npcs.length == 0) return false;

		for (String name : npcs)
		{
			if (name == null) return false;

			if (name.equals("*")) return true;

			if (npc.getName().equals(name)) return true;
		}

		return false;
	}
}
