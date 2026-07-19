/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.ilexiconn.nationsgui.forge.server.packet.PacketRegistry
 *  net.ilexiconn.nationsgui.forge.server.packet.impl.GetGroupAndPrimePacket
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 */
package com.clemdefrance.cmd;

import net.ilexiconn.nationsgui.forge.server.packet.PacketRegistry;
import net.ilexiconn.nationsgui.forge.server.packet.impl.GetGroupAndPrimePacket;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GetGroup
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        sender.sendMessage("ID GET_GROUP_AND_PRIME = " + PacketRegistry.INSTANCE.packetList.indexOf(GetGroupAndPrimePacket.class));
        return true;
    }
}

