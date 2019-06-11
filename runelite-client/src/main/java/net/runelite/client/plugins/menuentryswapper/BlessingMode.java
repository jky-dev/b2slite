package net.runelite.client.plugins.menuentryswapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlessingMode
{
	MOUNT("Mount Karuulm"),
	KOUREND("Kourend Woodland"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
