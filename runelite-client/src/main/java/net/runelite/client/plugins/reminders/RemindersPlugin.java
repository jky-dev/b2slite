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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RemindersPlugin extends Plugin
{

	private Instant loginTime;
	private boolean ready;

	@Inject
	private Client client;

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
		switch (client.getGameState())
		{
			case LOGIN_SCREEN:
			case LOGIN_SCREEN_AUTHENTICATOR:
			case LOGGING_IN:
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

	@Override
	public void shutDown()
	{
		loginTime = null;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		GameState state = event.getGameState();

		switch (state)
		{
			case LOGIN_SCREEN:
			case LOGIN_SCREEN_AUTHENTICATOR:
			case LOGGING_IN:
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
		period = 1,
		unit = ChronoUnit.MINUTES
	)
	public void reminders()
    {
    	if (loginTime == null) return;
    	int minutes = (int)Math.floor((Duration.between(loginTime, Instant.now()).getSeconds())/60);
		log.debug("mins: {}", minutes);
		if (config.customReminderTime() != 0)
		{
			if (minutes % config.customReminderTime() == 0 && minutes > 0 && !config.customReminder().isEmpty())
			{
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "<col=ef1020>" + config.customReminder() + "</col>", "");
			}
		}

        if (minutes % 60 == 0 && config.hydrationReminder() && minutes > 0)
        {
            int hours = minutes / 60;
            if (hours == 1)
            {
                client.addChatMessage(ChatMessageType.BROADCAST, "", "You have been gaming for 1 hour.", "");
            }
            else
            {
                client.addChatMessage(ChatMessageType.BROADCAST, "", "You have been gaming for " + hours + " hours.", "");
            }
            client.addChatMessage(ChatMessageType.BROADCAST, "", "Remember to drink water and stand up for a bit :)", "");
        }
    }
}
