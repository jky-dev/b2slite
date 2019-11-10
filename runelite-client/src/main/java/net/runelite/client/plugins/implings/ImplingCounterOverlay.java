package net.runelite.client.plugins.implings;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class ImplingCounterOverlay extends Overlay
{
    private final Client client;
    private final ImplingsPlugin plugin;
    private final ImplingsConfig config;

    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    public ImplingCounterOverlay(Client client, ImplingsConfig config, ImplingsPlugin plugin)
    {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.showCounter() || plugin.getImplings().isEmpty())
            return null;

        panelComponent.getChildren().clear();

        for (Map.Entry<ImplingType, Integer> entry : plugin.getImplingCounterMap().entrySet())
        {
            if (plugin.showImplingType(entry.getKey()) != ImplingsConfig.ImplingMode.NONE && entry.getValue() != 0)
            {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left(entry.getKey().getName())
                        .right(entry.getValue().toString())
                        .leftColor(plugin.typeToColor(entry.getKey()))
                        .build());
            }
        }

        return panelComponent.render(graphics);
    }
}
