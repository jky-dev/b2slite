package net.runelite.client.plugins.menuentryswapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemoirsMode
{
	LUNCH("Lunch by the Lancalliums"),
	FLUTE("The Fisher's Flute"),
	HISTORY("History and Hearsay"),
	JEWELLERY("Jewellery of Jubilation"),
	DISPOSITION("A Dark Disposition"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
