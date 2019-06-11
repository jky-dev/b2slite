/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.menuentryswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("menuentryswapper")
public interface MenuEntrySwapperConfig extends Config
{
	@ConfigItem(
		position = -2,
		keyName = "shiftClickCustomization",
		name = "Customizable shift-click",
		description = "Allows customization of shift-clicks on items"
	)
	default boolean shiftClickCustomization()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapAdmire",
		name = "Admire",
		description = "Swap Admire with Teleport, Spellbook and Perks (max cape) for mounted skill capes."
	)
	default boolean swapAdmire()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapAssignment",
		name = "Assignment",
		description = "Swap Talk-to with Assignment for Slayer Masters. This will take priority over swapping Trade."
	)
	default boolean swapAssignment()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapBanker",
		name = "Bank",
		description = "Swap Talk-to with Bank on Bank NPC<br>Example: Banker"
	)
	default boolean swapBank()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapBirdhouseEmpty",
		name = "Birdhouse",
		description = "Swap Interact with Empty for birdhouses on Fossil Island"
	)
	default boolean swapBirdhouseEmpty()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapBones",
		name = "Bury",
		description = "Swap Bury with Use on Bones"
	)
	default boolean swapBones()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapContract",
		name = "Contract",
		description = "Swap Talk-to with Contract on Guildmaster Jane"
	)
	default boolean swapContract()
	{
		return true; 
	}

	@ConfigItem(
		keyName = "swapChase",
		name = "Chase",
		description = "Allows to left click your cat to chase"
	)
	default boolean swapChase()
	{
		return true;
	}

	@ConfigItem(
		keyName = "claimSlime",
		name = "Claim Slime",
		description = "Swap Talk-to with Claim Slime from Morytania diaries"
	)
	default boolean claimSlime()
	{
		return true;
	}

	@ConfigItem(		
		keyName = "swapDarkMage",
		name = "Repairs",
		description = "Swap Talk-to with Repairs for Dark Mage"
	)
	default boolean swapDarkMage()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapDecant",
		name = "Decant",
		description = "Swap Talk-to with Decant for Bob Barter and Murky Matt at the Grand Exchange."
	)
	default boolean swapDecant()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapExchange",
		name = "Exchange",
		description = "Swap Talk-to with Exchange on NPC<br>Example: Grand Exchange Clerk, Tool Leprechaun, Void Knight"
	)
	default boolean swapExchange()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapFairyRing",
		name = "Fairy ring",
		description = "Swap Zanaris with Last-destination or Configure on Fairy rings"
	)
	default FairyRingMode swapFairyRing()
	{
		return FairyRingMode.LAST_DESTINATION;
	}

	@ConfigItem(
		keyName = "swapHardWoodGrove",
		name = "Hardwood Grove",
		description =  "Swap Quick-Pay(100) and Send-Parcel at Hardwood Grove"
	)
	default boolean swapHardWoodGrove()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapHarpoon",
		name = "Harpoon",
		description = "Swap Cage, Big Net with Harpoon on Fishing spot"
	)
	default boolean swapHarpoon()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapHomePortal",
		name = "Home",
		description = "Swap Enter with Home or Build or Friend's house on Portal"
	)
	default HouseMode swapHomePortal()
	{
		return HouseMode.HOME;
	}

	@ConfigItem(
		keyName = "swapPickpocket",
		name = "Pickpocket on H.A.M.",
		description = "Swap Talk-to with Pickpocket on H.A.M members"
	)
	default boolean swapPickpocket()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapPay",
		name = "Pay",
		description = "Swap Talk-to with Pay on NPC<br>Example: Elstan, Heskel, Fayeth"
	)
	default boolean swapPay()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapPrivate",
		name = "Private",
		description = "Swap Shared with Private on the Chambers of Xeric storage units."
	)
	default boolean swapPrivate()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapPick",
		name = "Pick",
		description = "Swap Pick with Pick-lots of the Gourd tree in the Chambers of Xeric"
	)
	default boolean swapPick()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapQuick",
		name = "Quick Pass/Open/Start/Travel",
		description = "Swap Pass with Quick-Pass, Open with Quick-Open, Ring with Quick-Start and Talk-to with Quick-Travel"
	)
	default boolean swapQuick()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapBoxTrap",
		name = "Reset",
		description = "Swap Check with Reset on box trap"
	)
	default boolean swapBoxTrap()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapTeleportItem",
		name = "Teleport item",
		description = "Swap Wear, Wield with Rub, Teleport on teleport item<br>Example: Amulet of glory, Explorer's ring, Chronicle"
	)
	default boolean swapTeleportItem()
	{
		return false;
	}

	@ConfigItem(
		keyName = "TeleFromEquipped",
		name = "Teleport from Equipped Screen",
		description = "Allows you to teleport from equipped items (used with Teleport Item)"
	)
	default boolean swapTeleportFromEquipped()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapAbyssTeleport",
		name = "Teleport to Abyss",
		description = "Swap Talk-to with Teleport for the Mage of Zamorak"
	)
	default boolean swapAbyssTeleport()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapTrade",
		name = "Trade",
		description = "Swap Talk-to with Trade on NPC<br>Example: Shop keeper, Shop assistant"
	)
	default boolean swapTrade()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapTravel",
		name = "Travel",
		description = "Swap Talk-to with Travel, Take-boat, Pay-fare, Charter on NPC<br>Example: Squire, Monk of Entrana, Customs officer, Trader Crewmember"
	)
	default boolean swapTravel()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapDuelRing",
		name = "Swap Duel Arena",
		description = "One click duel arena",
		position = 25
	)
	default DuelRingMode swapDA()
	{
		return DuelRingMode.CASTLE_WARS;
	}

	@ConfigItem(
		keyName = "swapMaxCape",
		name = "Max Cape",
		description = "Swap tele option on max cape",
		position = 26
	)
	default MaxCapeMode swapMaxCape()
	{
		return MaxCapeMode.CRAFTING;
	}

	@ConfigItem(
		keyName = "swapDesert",
		name = "Desert Amulet",
		description = "Swap tele option on desert amulet",
		position = 27
	)
	default DesertAmuletMode swapDesert()
	{
		return DesertAmuletMode.NARDAH;
	}

	@ConfigItem(
		keyName = "swapArdy",
		name = "Ardourgne Cloak",
		description = "Swap tele option on Ardougne Cloak",
		position = 28
	)
	default ArdyCloakMode swapArdy()
	{
		return ArdyCloakMode.FARM;
	}

	@ConfigItem(
		keyName = "swapMory",
		name = "Morytania Legs",
		description = "Swap tele option on legs",
		position = 29
	)
	default MoryLegsMode swapMoryLegs()
	{
		return MoryLegsMode.BURGH;
	}

	@ConfigItem(
		keyName = "swapGloves",
		name = "Karamja Gloves",
		description = "Swap tele option on gloves",
		position = 30
	)
	default KaramGloveMode swapKaramGloves()
	{
		return KaramGloveMode.GEM;
	}

	@ConfigItem(
		keyName = "swapGlory",
		name = "Amulet of Glory",
		description = "Swap tele option on glory",
		position = 31
	)
	default GloryMode swapGlory()
	{
		return GloryMode.EDGE;
	}

	@ConfigItem(
		keyName = "swapXerics",
		name = "Xerics Talisman",
		description = "Swap tele option on xeric's talisman",
		position = 32
	)
	default XericsTalismanMode swapXerics()
	{
		return XericsTalismanMode.HONOUR;
	}

	@ConfigItem(
		keyName = "swapGames",
		name = "Games Necklace",
		description = "Swap tele option on Games Necklace",
		position = 33
	)
	default GamesNecklaceMode swapGames()
	{
		return GamesNecklaceMode.TEARS;
	}

	@ConfigItem(
		keyName = "swapBlessing",
		name = "Rada's Blessing",
		description = "Swap tele option on Rada's Blessing",
		position = 34
	)
	default BlessingMode swapBlessing()
	{
		return BlessingMode.MOUNT;
	}

	@ConfigItem(
		keyName = "swapDigsite",
		name = "Digsite Pendant",
		description = "Swap tele option on Digsite Pendant",
		position = 35
	)
	default DigsiteMode swapDigsite()
	{
		return DigsiteMode.ISLAND;
	}

	@ConfigItem(
		keyName = "swapFishingCape",
		name = "Fishing Cape",
		description = "Swap tele option on Fishing Cape",
		position = 36
	)
	default FishingCapeMode swapFishingCape()
	{
		return FishingCapeMode.GUILD;
	}

	@ConfigItem(
		keyName = "swapMemoirs",
		name = "Kharedst's Memoirs",
		description = "Swap tele option on Kharedst's Memoirs",
		position = 37
	)
	default MemoirsMode swapMemoirs()
	{
		return MemoirsMode.OFF;
	}
}
