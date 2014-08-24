package com.comandante.creeper.Items;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.CreeperSession;

public class ItemUseHandler {

    private final Item item;
    private final CreeperSession creeperSession;
    private final GameManager gameManager;
    private final String playerId;
    private final ItemType itemType;

    public ItemUseHandler(Item item, CreeperSession creeperSession, GameManager gameManager, String playerId) {
        this.item = item;
        this.creeperSession = creeperSession;
        this.gameManager = gameManager;
        this.playerId = playerId;
        this.itemType = ItemType.itemTypeFromCode(item.getItemTypeId());

    }

    private void processKey() {
        //If no doors
        writeToPlayer("There's no doors here to use this [key] on.\r\n");
    }

    private void processBeer() {
        writeToPlayer("You drink a cold [coors light] and feel better because of it.\r\n");
    }

    private void processBook() {
        writeToPlayer("You crack open the [book] and immediately realize that you aren't familiar with it's written language.\r\n");
    }

    public void handle() {
        switch (itemType) {
            case KEY:
                processKey();
                break;
            case BOOK:
                processBook();
                break;
            case BEER:
                processBeer();
                break;
            case UNKNOWN:
                writeToPlayer("Item not found.\r\n");
                break;
        }

        if (itemType.isDisposable()) {
            gameManager.getPlayerManager().removeInventoryId(playerId, item.getItemId());
            gameManager.getEntityManager().removeItem(item);
        }
    }

    private void writeToPlayer(String message) {
        gameManager.getPlayerManager().getPlayer(playerId).getChannel().write(message);
    }
}
