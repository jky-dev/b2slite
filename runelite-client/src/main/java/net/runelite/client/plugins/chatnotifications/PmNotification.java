package net.runelite.client.plugins.chatnotifications;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PmNotification
{
	STANDARD("Standard"),
	SOUND_ONLY("Only sound"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
