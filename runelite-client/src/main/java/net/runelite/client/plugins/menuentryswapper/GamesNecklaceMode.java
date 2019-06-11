package net.runelite.client.plugins.menuentryswapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GamesNecklaceMode
{
	BURTHORPE("Burthorpe"),
	BARB("Barbarian Outpost"),
	CORP("Corporeal Beast"),
	TEARS("Tears of Guthix"),
	TOTD("Wintertotd Camp"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
