package net.runelite.client.plugins.ztob;

import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import net.runelite.api.Varbits;
import net.runelite.client.ui.overlay.Overlay;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class TheatreReminderOverlay extends Overlay {
	private final TheatrePlugin plugin;
	private final TheatreConfig config;
	PanelComponent panelComponent = new PanelComponent();

	@Inject
	private TheatreReminderOverlay(TheatrePlugin plugin, TheatreConfig config)
	{
		setPosition(OverlayPosition.TOP_RIGHT);
		this.plugin = plugin;
		this.config = config;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (plugin.isInTobLobby() && !plugin.isCorrectSpellbook() && config.ancientsReminder())
		{
			panelComponent.getChildren().clear();
			// Build overlay title
			panelComponent.getChildren().add(TitleComponent.builder()
				.text("ANCIENTS PLEASE")
				.color(Color.RED)
				.build());
			return panelComponent.render(graphics);
		}

		return null;
	}


}