package net.runelite.client.plugins.ztob;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class BloatTimerOverlay extends Overlay {

	private final Client client;
	private final TheatrePlugin plugin;
	private final TheatreConfig config;

	@Inject
	private BloatTimerOverlay(Client client, TheatrePlugin plugin, TheatreConfig config) {
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGH);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	public Dimension render(Graphics2D graphics)
	{

		if (config.bloatTimer())
		{
			final String tickCounter = String.valueOf(plugin.bloatTimer);
			int secondConversion = (int) (plugin.bloatTimer * .6);
			if (plugin.getBloat_NPC() != null)
			{
				Point canvasPoint = plugin.getBloat_NPC().getCanvasTextLocation(graphics, tickCounter, 60);
				if (plugin.bloatTimer <= 37)
				{
					renderTextLocation(graphics, tickCounter + "( " + secondConversion + " )", 15, Font.BOLD, Color.WHITE, canvasPoint);
				}
				else
				{
					renderTextLocation(graphics, tickCounter + "( " + secondConversion + " )", 15, Font.BOLD, Color.RED, canvasPoint);
				}
			}
		}
		return null;
	}

	private void renderTextLocation(Graphics2D graphics, String txtString, int fontSize, int fontStyle, Color fontColor, net.runelite.api.Point canvasPoint)
	{
		graphics.setFont(new Font("Arial", fontStyle, fontSize));
		if (canvasPoint != null)
		{
			final net.runelite.api.Point canvasCenterPoint = new net.runelite.api.Point(
				canvasPoint.getX(),
				canvasPoint.getY());
			final net.runelite.api.Point canvasCenterPoint_shadow = new Point(
				canvasPoint.getX() + 1,
				canvasPoint.getY() + 1);
			OverlayUtil.renderTextLocation(graphics, canvasCenterPoint_shadow, txtString, Color.BLACK);
			OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, txtString, fontColor);
		}
	}
}