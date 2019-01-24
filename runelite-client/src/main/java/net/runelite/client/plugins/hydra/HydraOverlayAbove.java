package net.runelite.client.plugins.hydra;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GraphicsObject;
import net.runelite.api.HeadIcon;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class HydraOverlayAbove extends Overlay
{
	private final Client client;
	private final HydraPlugin plugin;

	private static Color COLOR_ICON_BACKGROUND = new Color(0, 0, 0, 128);
	private static final Color COLOR_ICON_BORDER = new Color(0, 0, 0, 255);
	private static final Color COLOR_ICON_BORDER_FILL = new Color(219, 175, 0, 255);
	private static final int OVERLAY_ICON_DISTANCE = 50;

	@Inject
	private SkillIconManager iconManager;

	@Inject
	public HydraOverlayAbove(Client client, HydraPlugin plugin)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		this.client = client;
		this.plugin = plugin;
	}

	private BufferedImage getIcon(int attackStyle)
	{
		switch (attackStyle)
		{
			case 1:
				return iconManager.getSkillImage(Skill.RANGED);
			case 0:
				return iconManager.getSkillImage(Skill.MAGIC);
		}
		return null;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!client.isInInstancedRegion() && plugin.getHydra() == null) return null;
		updateIconBackground();
		renderIcon(graphics);

		renderGroundObject(graphics);

		renderNPCSize(graphics);
		
		return null;
	}

	// renders special attack ground objects
	private void renderGroundObject(Graphics2D graphics)
	{
		List<GraphicsObject> graphicsObjects = client.getGraphicsObjects();

		for (GraphicsObject graphicsObject : graphicsObjects)
		{
			if (graphicsObject.getId() == 1668) continue;
			LocalPoint lp = graphicsObject.getLocation();
			Polygon poly = Perspective.getCanvasTilePoly(client, lp);

			if (poly != null)
			{
				OverlayUtil.renderPolygon(graphics, poly, Color.cyan);
			}
		}
	}

	private void renderNPCSize(Graphics2D graphics)
	{
		if (plugin.getHydra() == null) return;

		Color color = Color.orange;
		int size = 1;
		NPCComposition composition = plugin.getHydra().getTransformedComposition();
		if (composition != null)
		{
			size = composition.getSize();
		}
		LocalPoint lp = plugin.getHydra().getLocalLocation();
		Polygon tilePoly = Perspective.getCanvasTileAreaPoly(client, lp, size);

		renderPoly(graphics, color, tilePoly);
	}

	// renders the rectangle around hydra
	private void renderPoly(Graphics2D graphics, Color color, Polygon polygon)
	{
		if (polygon != null)
		{
			graphics.setColor(color);
			graphics.setStroke(new BasicStroke(1));
			graphics.draw(polygon);
			graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
		}
	}

	// updates the overhead icon of hydra if not praying correctly
	private void updateIconBackground()
	{
		if (client.getLocalPlayer().getOverheadIcon() == null)
		{
			COLOR_ICON_BACKGROUND = Color.PINK;
			return;
		}

		if (plugin.getAttackStyle() == 0)
		{
			if (client.getLocalPlayer().getOverheadIcon() == HeadIcon.MAGIC)
			{
				COLOR_ICON_BACKGROUND = new Color(0, 0, 0, 128);
				return;
			}
		}
		else if	(plugin.getAttackStyle() == 1)
		{
			if (client.getLocalPlayer().getOverheadIcon() == HeadIcon.RANGED)
			{
				COLOR_ICON_BACKGROUND = new Color(0, 0, 0, 128);
				return;
			}
		}

		COLOR_ICON_BACKGROUND = Color.PINK;
	}

	// renders the overhead icon
	private void renderIcon(Graphics2D graphics)
	{
		NPC npc = plugin.getHydra();
		if (npc == null) return;
		LocalPoint lp = npc.getLocalLocation();
		if (lp != null)
		{
			Point point = Perspective.localToCanvas(client, lp, client.getPlane(),
				npc.getLogicalHeight() - 600);
			if (point != null)
			{
				int bgPadding = 4;
				int currentPosX = 0;
				point = new Point(point.getX(), point.getY());
				BufferedImage icon = getIcon(plugin.getAttackStyle());
				int totalWidth = icon.getWidth();
				graphics.setStroke(new BasicStroke(2));
				graphics.setColor(COLOR_ICON_BACKGROUND);
				graphics.fillOval(
					point.getX() - totalWidth / 2 + currentPosX - bgPadding,
					point.getY() - icon.getHeight() / 2 - OVERLAY_ICON_DISTANCE - bgPadding,
					icon.getWidth() + bgPadding * 2,
					icon.getHeight() + bgPadding * 2);

				graphics.setColor(COLOR_ICON_BORDER);
				graphics.drawOval(
					point.getX() - totalWidth / 2 + currentPosX - bgPadding,
					point.getY() - icon.getHeight() / 2 - OVERLAY_ICON_DISTANCE - bgPadding,
					icon.getWidth() + bgPadding * 2,
					icon.getHeight() + bgPadding * 2);

				graphics.drawImage(
					icon,
					point.getX() - totalWidth / 2 + currentPosX,
					point.getY() - icon.getHeight() / 2 - OVERLAY_ICON_DISTANCE,
					null);

				graphics.setColor(COLOR_ICON_BORDER_FILL);
				Arc2D.Double arc = new Arc2D.Double(
					point.getX() - totalWidth / 2 + currentPosX - bgPadding,
					point.getY() - icon.getHeight() / 2 - OVERLAY_ICON_DISTANCE - bgPadding,
					icon.getWidth() + bgPadding * 2,
					icon.getHeight() + bgPadding * 2,
					90.0,
					-360.0 * (plugin.getAttackCount()) / plugin.getTotalAttacks(),
					Arc2D.OPEN);
				graphics.draw(arc);
			}
		}
	}
}

