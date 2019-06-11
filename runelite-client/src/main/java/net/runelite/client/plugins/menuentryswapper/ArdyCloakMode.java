package net.runelite.client.plugins.menuentryswapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArdyCloakMode
{
	MONASTERY("Monastery Teleport"),
	FARM("Farm Teleport"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
