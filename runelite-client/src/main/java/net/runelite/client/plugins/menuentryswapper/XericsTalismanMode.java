package net.runelite.client.plugins.menuentryswapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum XericsTalismanMode
{
	GLADE("Xeric's Glade"),
	LOOKOUT("Xeric's Lookout"),
	INFERNO("Xeric's Inferno"),
	HEART("Xeric's Heart"),
	HONOUR("Xeric's Honour"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
