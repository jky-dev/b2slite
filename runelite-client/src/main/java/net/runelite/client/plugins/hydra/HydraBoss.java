package net.runelite.client.plugins.hydra;

public class HydraBoss
{
	enum AttackStyle
	{
		RANGED,
		MAGIC
	}

	private int attackStyle = 0;
	private int lastAttackStyle = 0;
	private int attackCount = 0;
	private int totalAttacks = 6;
	private int specialAttackCount = 7;
	private boolean mageTransition = false;
	private boolean rangeTransition = false;
	private boolean finalTransition = false;

	public HydraBoss()
	{

	}

	public void changeForm()
	{

	}

	public void reset()
	{
		totalAttacks = 6;
		attackCount = 0;
		mageTransition = false;
		rangeTransition = false;
		finalTransition = false;
		specialAttackCount = 7;
	}
}
