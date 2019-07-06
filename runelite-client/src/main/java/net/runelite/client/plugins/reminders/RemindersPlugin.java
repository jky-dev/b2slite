/*
 * Copyright (c) 2019, jkybtw <https://github.com/jkybtw>
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
package net.runelite.client.plugins.reminders;

import com.google.inject.Provides;
import lombok.Getter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.task.Schedule;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@PluginDescriptor(
	name = "Reminders",
	description = "Reminders for your gaming session",
	tags = {"gaming", "session", "gamer", "reminder", "custom", "hydration", "rest"},
    enabledByDefault = false
)
public class RemindersPlugin extends Plugin
{

	private Instant loginTime;
	private boolean ready;

	@Getter
	private String gamingSessionTime = "";

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private RemindersConfig config;

	@Provides
    RemindersConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RemindersConfig.class);
	}

	@Override
	public void startUp()
	{
	    clientThread.invoke(this::updateReportButtonTime);
	}

	@Override
	public void shutDown()
	{
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		GameState state = event.getGameState();

		switch (state)
		{
			case LOGIN_SCREEN:
			case LOGIN_SCREEN_AUTHENTICATOR:
				ready = true;
				break;
			case LOGGED_IN:
				if (ready)
				{
					loginTime = Instant.now();
					ready = false;
				}
				break;
		}
	}

	@Schedule(
		period = 500,
		unit = ChronoUnit.MILLIS
	)
    public void update() { updateReportButtonTime(); }

    @Schedule(
        period = 1,
        unit = ChronoUnit.MINUTES
    )
    public void checkReminder() { reminders(); }

	private void updateReportButtonTime()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		gamingSessionTime = getLoginTime();
	}

	private String getLoginTime()
	{
		if (loginTime == null)
		{
			return "unknown";
		}

		Duration duration = Duration.between(loginTime, Instant.now());
		LocalTime time = LocalTime.ofSecondOfDay(duration.getSeconds());
		return time.format(DateTimeFormatter.ofPattern("H:mm:ss"));
	}

	private void reminders()
    {
        if (config.customReminderTime() == 0) return;
        double minutes = Math.floor((Duration.between(loginTime, Instant.now()).getSeconds())/60);
        if (minutes % config.customReminderTime() == 0 && minutes > 0 && !config.customReminder().isEmpty())
        {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>" + config.customReminder() + "</col>", "");
        }
        if (minutes % 60 == 0 && config.hydrationReminder() && minutes > 0)
        {
            int hours = (int)minutes / 60;
            if (hours == 1)
            {
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>You have been gaming for 1 hour.</col>", "");
            }
            else
            {
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>You have been gaming for " + hours + " hours.</col>", "");
            }
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>Remember to drink water and stand up for a bit :)</col>", "");
        }

    }
}
