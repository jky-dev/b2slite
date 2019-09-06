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
package net.runelite.client.plugins.raids;

import java.awt.event.KeyEvent;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;
import net.runelite.api.VarClientStr;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.input.KeyListener;
import net.runelite.client.plugins.raids.solver.Room;

@Slf4j
@Singleton
public class RaidsKeyboardListener implements KeyListener
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private RaidsPlugin plugin;

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{

	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_T && plugin.getRaid() != null)
		{
			String chatbox = client.getVar(VarClientStr.CHATBOX_TYPED_TEXT);
			String pm = client.getVar(VarClientStr.INPUT_TEXT);
			if (chatbox != null || pm != null)
			{
				String rotation = "";
				for (Room layoutRoom : plugin.getRaid().getLayout().getRooms())
				{
					int position = layoutRoom.getPosition();
					RaidRoom room = plugin.getRaid().getRoom(position);

					if (room == null)
					{
						continue;
					}

					switch (room.getType())
					{
						case COMBAT:
							String bossName = room.getBoss().getShortName();
							rotation += bossName + ", ";

							break;

						case PUZZLE:
							String puzzleName = room.getPuzzle().getShortName();
							rotation += puzzleName + ", ";
							break;
					}
				}
				rotation += "w" + client.getWorld();
				final String replacement;
				if (chatbox != null && chatbox.contains("!rot"))
				{
					replacement = chatbox.replace("!rot", rotation);

					clientThread.invoke(() ->
					{
						client.setVar(VarClientStr.CHATBOX_TYPED_TEXT, replacement);
						client.runScript(ScriptID.CHAT_PROMPT_INIT);
					});
				}
				else
				{
					replacement = pm.replace("!rot", rotation);

					clientThread.invoke(() ->
					{
						client.setVar(VarClientStr.INPUT_TEXT, replacement);
						client.runScript(ScriptID.CHAT_TEXT_INPUT_REBUILD, "");
					});
				}

			}
		}
	}
}
