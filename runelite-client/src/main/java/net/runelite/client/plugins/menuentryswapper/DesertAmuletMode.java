package net.runelite.client.plugins.menuentryswapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DesertAmuletMode
{
	NARDAH("Nardah"),
	KALPHITE_CAVE("Kalphite cave"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
