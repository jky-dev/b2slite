package net.runelite.client.plugins.pricechecker;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.util.StackFormatter;

public class PriceCheckerOverlay extends Overlay
{
	private final Client client;
	private final PriceCheckerPlugin plugin;
	private final PriceCheckerConfig config;

	private final PanelComponent panelComponent = new PanelComponent();

	@Inject
	public PriceCheckerOverlay(Client client, PriceCheckerConfig config, PriceCheckerPlugin plugin)
	{
		this.client = client;
		this.config = config;
		this.plugin = plugin;
		setPosition(OverlayPosition.TOP_LEFT);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().clear();

		panelComponent.getChildren().add(LineComponent.builder()
			.left("Price Check: ")
			.right(StackFormatter.quantityToStackSize(plugin.getTotalProfit()))
			.build());

		return panelComponent.render(graphics);
	}
}
