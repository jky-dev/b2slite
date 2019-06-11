package net.runelite.client.plugins.menuentryswapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DigsiteMode
{
	DIGSITE("Digsite"),
	ISLAND("Fossil Island"),
	DUNGEON("Lithkren Dungeon"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
