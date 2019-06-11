package net.runelite.client.plugins.menuentryswapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KaramGloveMode
{
	DURADEL("Duradel"),
	GEM("Gem Mine"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
