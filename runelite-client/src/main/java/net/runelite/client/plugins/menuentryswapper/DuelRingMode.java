package net.runelite.client.plugins.menuentryswapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DuelRingMode
{
	DUEL_ARENA("Duel Arena"),
	CASTLE_WARS("Castle Wars"),
	CLAN_WARS("Clan Wars"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
