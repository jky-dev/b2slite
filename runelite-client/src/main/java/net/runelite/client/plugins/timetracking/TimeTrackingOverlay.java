/*
 * Copyright (c) 2017, Seth <Sethtroll3@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
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
package net.runelite.client.plugins.timetracking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Instant;
import javax.inject.Inject;
import net.runelite.client.plugins.timetracking.farming.FarmingTracker;
import net.runelite.client.plugins.timetracking.hunter.BirdHouseTracker;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class TimeTrackingOverlay extends Overlay
{
	private final PanelComponent panelComponent = new PanelComponent();
	private final TimeTrackingConfig config;
	private final FarmingTracker farmingTracker;
	private final BirdHouseTracker birdHouseTracker;

	@Inject
	public TimeTrackingOverlay(BirdHouseTracker birdHouseTracker, FarmingTracker farmingTracker, TimeTrackingConfig config)
	{
		setPosition(OverlayPosition.TOP_LEFT);
		this.birdHouseTracker = birdHouseTracker;
		this.farmingTracker = farmingTracker;
		this.config = config;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().clear();
		if (!config.showOverlay()) return panelComponent.render(graphics);
		{
			if ((birdHouseTracker.getCompletionTime() == 0 && config.showBirdHouse()) || (farmingTracker.getCompletionTime(Tab.HERB) - Instant.now().getEpochSecond() <= 0 && config.showHerbs()))
			{
				panelComponent.getChildren().add(TitleComponent.builder()
					.text("Time Tracking Overlay")
					.color(Color.GREEN)
					.build());

				if (birdHouseTracker.getCompletionTime() <= 0 && config.showBirdHouse())
				{
					panelComponent.getChildren().add(LineComponent.builder()
						.left("Birdhouses:")
						.right("Ready")
						.build());
				}

				if (farmingTracker.getCompletionTime(Tab.HERB) - Instant.now().getEpochSecond() <= 0 && config.showHerbs())
				{
					panelComponent.getChildren().add(LineComponent.builder()
						.left("Herbs:")
						.right("Ready")
						.build());
				}
			}
		}
		return panelComponent.render(graphics);
	}
}
