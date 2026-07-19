package com.clemdefrance.market;

import com.clemdefrance.Main;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Hdv implements CommandExecutor {

    public void sendRemoteOpenMarketPacket(Player player, int packetId, String category, String targetTab, int inventorySlot, String searchType, String searchQuery, boolean isCatalog) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeInt(packetId);

        out.writeUTF(category == null ? "" : category);
        out.writeUTF(targetTab == null ? "" : targetTab);
        out.writeInt(inventorySlot);
        out.writeUTF(searchType == null ? "" : searchType);
        out.writeUTF(searchQuery == null ? "" : searchQuery);
        out.writeBoolean(isCatalog);

        player.sendPluginMessage(Main.getInstance(), "nationsgui", out.toByteArray());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        int marketPacketId = 374;

        if (args.length == 0) {
            sendRemoteOpenMarketPacket(player, marketPacketId, "", "", -1, "", "", false);
        } else {
            StringBuilder builder = new StringBuilder();
            for (String arg : args) {
                builder.append(arg).append(" ");
            }

            String search = builder.toString().trim();

            sendRemoteOpenMarketPacket(player, marketPacketId, "", "", -1, "", search, false);
        }

        player.sendMessage("§aOuverture du market...");
        return true;
    }
}