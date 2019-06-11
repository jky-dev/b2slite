package net.runelite.client.plugins.menuentryswapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FishingCapeMode
{
	GUILD("Fishing Guild"),
	GROTTO("Otto's Grotto"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
