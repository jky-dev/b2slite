package net.runelite.client.plugins.debug;

import com.google.common.base.Strings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class DebugSceneOverlay extends Overlay
{
	private final Client client;
	private final DebugPlugin plugin;
	private final DebugConfig config;
	private final PanelComponent panelComponent = new PanelComponent();

	@Inject
	public DebugSceneOverlay(Client client, DebugConfig config, DebugPlugin plugin)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		this.client = client;
		this.config = config;
		this.plugin = plugin;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showAnimationIDs()) return null;

		// renders text above the npc's head
		for (NPC npc : plugin.getNpcs())
		{
			LocalPoint lp = npc.getLocalLocation();
			if (lp != null)
			{
				Point point = Perspective.localToCanvas(client, lp, client.getPlane(), npc.getLogicalHeight() + 40);
				if (point != null)
				{
					renderTextLocation(graphics, npc, Integer.toString(npc.getAnimation()), Color.WHITE);
				}
			}
		}
		return null;
	}

	// renders text location
	public static void renderTextLocation(Graphics2D graphics, NPC actor, String text, Color color)
	{
		Point textLocation = actor.getCanvasTextLocation(graphics, text, actor.getLogicalHeight() + 40);
		if (Strings.isNullOrEmpty(text))
		{
			return;
		}

		int x = textLocation.getX();
		int y = textLocation.getY();

		graphics.setColor(Color.BLACK);
		graphics.drawString(text, x + 1, y + 1);

		graphics.setColor(color);
		graphics.drawString(text, x, y);
	}
}
