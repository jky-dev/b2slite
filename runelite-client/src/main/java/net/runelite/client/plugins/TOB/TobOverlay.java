package net.runelite.client.plugins.TOB;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GraphicsObject;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class TobOverlay extends Overlay
{
	private final Client client;
	private final TobPlugin plugin;
	private TobConfig config;

	@Inject
	public TobOverlay(Client client, TobPlugin plugin, TobConfig config)
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
		if (config.showMaidenPools() && client.getMapRegions()[0] == 12613) renderMaidenPools(graphics);

		if (config.showFallingObjects() && client.getMapRegions()[0] == 13125) renderBloatObjects(graphics);

		return null;
	}

	// renders maiden pools
	private void renderMaidenPools(Graphics2D graphics)
	{
		List<GraphicsObject> graphicsObjects = client.getGraphicsObjects();

		for (GraphicsObject graphicsObject : graphicsObjects)
		{
			if (graphicsObject.getId() != 1579) continue;

			drawPolygon(graphicsObject, graphics, config.maidenColor());
		}
	}

	// renders falling objects in bloat room
	private void renderBloatObjects(Graphics2D graphics)
	{
		List<GraphicsObject> graphicsObjects = client.getGraphicsObjects();

		for (GraphicsObject graphicsObject : graphicsObjects)
		{
			if (graphicsObject.getId() < 1570 || graphicsObject.getId() > 1573) continue;

			drawPolygon(graphicsObject, graphics, config.bloatColor());
		}
	}

	private void drawPolygon(GraphicsObject graphicsObject, Graphics2D graphics, Color color)
	{
		LocalPoint lp = graphicsObject.getLocation();
		Polygon poly = Perspective.getCanvasTilePoly(client, lp);

		if (poly != null)
		{
			OverlayUtil.renderPolygon(graphics, poly, color);
		}
	}
}

