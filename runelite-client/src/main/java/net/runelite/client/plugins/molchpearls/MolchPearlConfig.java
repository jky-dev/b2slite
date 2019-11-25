/*
 * Copyright (c) 2019, Jacky <liangj97@gmail.com>
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
package net.runelite.client.plugins.molchpearls;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("molchpearl")
public interface MolchPearlConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName ="showCurrent",
		name = "Show Current Fish stat",
		description = "Shows the current fish caught between pearls"
	)
	default boolean showCurrent() { return true; }

	@ConfigItem(
		position = 1,
		keyName ="showMin",
		name = "Show Min Fish stat",
		description = "Shows the min fish caught between pearls"
	)
	default boolean showMin() { return true; }

	@ConfigItem(
		position = 2,
		keyName ="showMax",
		name = "Show Max Fish stat",
		description = "Shows the max fish caught between pearls"
	)
	default boolean showMax() { return true; }

	@ConfigItem(
		position = 3,
		keyName ="showAvg",
		name = "Show Avg Fish stat",
		description = "Shows the avg fish caught between pearls"
	)
	default boolean showAvg() { return true; }

	@ConfigItem(
		position = 4,
		keyName ="showTotal",
		name = "Show Total Fish stat",
		description = "Shows the total fish caught"
	)
	default boolean showTotal() { return true; }


}
