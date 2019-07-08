package net.runelite.client.plugins.ztob;

import java.awt.Color;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;

public class Nylo
{
	@Getter
	private NPC npc;

	@Getter
	private int ticks;

	@Getter
	@Setter
	private boolean ragger;

	public Nylo(NPC npc)
	{
		this.npc = npc;
		ticks = 52;
		ragger = false;
	}

	public Color getColor()
	{
		String name = this.npc.getName().toLowerCase();
		if (name.contains("hagios"))
		{
			return Color.CYAN;
		}
		else if (name.contains("toxobolos"))
		{
			return Color.GREEN;
		}
		else if (name.contains("ischyros"))
		{
			return Color.WHITE;
		}
		else
		{
			return Color.BLACK;
		}
	}

	public void updateTick()
	{
		this.ticks--;
	}
}
