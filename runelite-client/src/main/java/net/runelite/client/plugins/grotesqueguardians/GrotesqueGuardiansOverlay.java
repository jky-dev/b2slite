/*
 * Copyright (c) 2018, Damen <https://github.com/basicDamen>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *	list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *	this list of conditions and the following disclaimer in the documentation
 *	and/or other materials provided with the distribution.
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
package net.runelite.client.plugins.grotesqueguardians;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GraphicsObject;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

class GrotesqueGuardiansOverlay extends Overlay
{
	private final Client client;
	private GrotesqueGuardiansPlugin plugin;
	private GrotesqueGuardiansConfig config;
	private static final int GROTESQUE_GUARDIANS_LIGHTNING_START = 1416;
	private static final int GROTESQUE_GUARDIANS_LIGHTNING_END = 1431;
	private static final int GROTESQUE_GUARDIANS_FALLING_ROCKS = 1436;
	private static final int GROTESQUE_GUARDIANS_STONE_ORB = 160;

	@Inject
	private GrotesqueGuardiansOverlay(Client client, GrotesqueGuardiansConfig config, GrotesqueGuardiansPlugin plugin)
	{
		this.client = client;
		this.config = config;
		this.plugin = plugin;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(OverlayPriority.LOW);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.isInGGArea())
		{
			return null;
		}

		if (plugin.isHideAttackOption() && config.showDuskTile())
		{
			int size = 1;
			NPCComposition composition = plugin.getDusk().getTransformedComposition();
			if (composition != null)
			{
				size = composition.getSize();
			}
			LocalPoint lp = plugin.getDusk().getLocalLocation();
			Polygon tilePoly = Perspective.getCanvasTileAreaPoly(client, lp, size);

			renderPoly(graphics, Color.CYAN, tilePoly, 2);
		}

		// TODO: Awaiting GraphicsObjectDespawn event to be tracked to make this more efficient.
		for (GraphicsObject graphicsObject : client.getGraphicsObjects())
		{
			Color color = null;

			if (graphicsObject.getId() >= GROTESQUE_GUARDIANS_LIGHTNING_START && graphicsObject.getId() <= GROTESQUE_GUARDIANS_LIGHTNING_END)
			{
				color = Color.ORANGE;
			}
			else if (graphicsObject.getId() == GROTESQUE_GUARDIANS_STONE_ORB)
			{
				color = Color.RED;
			}
			else if (graphicsObject.getId() == GROTESQUE_GUARDIANS_FALLING_ROCKS)
			{
				color = Color.YELLOW;
			}
			else
			{
				continue;
			}

			LocalPoint lp = graphicsObject.getLocation();
			Polygon poly = Perspective.getCanvasTilePoly(client, lp);


			if (poly != null)
			{
				if (graphicsObject.getId() == GROTESQUE_GUARDIANS_STONE_ORB)
				{
					poly = Perspective.getCanvasTileAreaPoly(client, lp, 3);
					renderPoly(graphics, Color.RED, poly, 0);
				}
				else
				{
					OverlayUtil.renderPolygon(graphics, poly, color);
				}
			}
		}
		return null;
	}

	private void renderPoly(Graphics2D graphics, Color color, Polygon polygon, int size)
	{
		if (polygon != null)
		{
			graphics.setColor(color);
			graphics.setStroke(new BasicStroke(size));
			graphics.draw(polygon);
			graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
			if (size == 0) graphics.fill(polygon);
		}
	}
}
