package net.runelite.client.plugins.menuentryswapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GloryMode
{
	EDGE("Edgeville"),
	KARAM("Karamja"),
	DRAYNOR("Draynor Village"),
	ALKHA("Al Kharid"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
