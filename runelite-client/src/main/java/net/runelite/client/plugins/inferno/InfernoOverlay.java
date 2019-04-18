package net.runelite.client.plugins.inferno;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;

import net.runelite.client.ui.overlay.components.PanelComponent;

public class InfernoOverlay extends Overlay
{
	private final Client client;
	private final InfernoPlugin plugin;
	private final InfernoConfig config;
	private final PanelComponent panelComponent = new PanelComponent();

	@Inject
	public InfernoOverlay(Client client, InfernoConfig config, InfernoPlugin plugin)
	{
		this.client = client;
		this.config = config;
		this.plugin = plugin;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{

		return panelComponent.render(graphics);
	}
}
