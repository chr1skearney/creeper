package com.comandante.creeper.merchant.lockers;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.PlayerMetadata;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;


public class RetrieveCommand extends LockerCommand {

    final static List<String> validTriggers = Arrays.asList("retrieve", "r");
    final static String description = "Retrieve an item from your locker.";

    public RetrieveCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            configure(e);
            originalMessageParts.remove(0);
            String desiredRetrieveOption = Joiner.on(" ").join(originalMessageParts);
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            for (String entityId: playerMetadata.getLockerInventory()) {
                Item itemEntity = gameManager.getEntityManager().getItemEntity(entityId);
                if (itemEntity.getItemTriggers().contains(desiredRetrieveOption)) {
                    gameManager.transferItemFromLocker(player, entityId);
                    write(itemEntity.getItemName() + " retrieved from locker.\r\n");
                    return;
                }
            }
            write("Item not found in locker.\r\n");
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}