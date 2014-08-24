package com.comandante.creeper.managers;

import com.comandante.creeper.model.CreeperEntity;
import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemSerializer;
import com.comandante.creeper.model.Room;
import com.comandante.creeper.npc.Npc;
import org.mapdb.DB;
import org.mapdb.HTreeMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EntityManager {

    private final ConcurrentHashMap<String, Npc> npcs = new ConcurrentHashMap<>();
    private final HTreeMap<String, Item> items;
    private final ConcurrentHashMap<String, CreeperEntity> entities = new ConcurrentHashMap<>();
    private final ExecutorService tickService = Executors.newFixedThreadPool(1);
    private final ExecutorService ticketRunnerService = Executors.newFixedThreadPool(10);
    private final RoomManager roomManager;
    private final DB db;

    public EntityManager(RoomManager roomManager, DB db) {
        this.roomManager = roomManager;
        if (db.exists("itemMap")) {
            this.items = db.get("itemMap");
        } else {
            this.items = db.createHashMap("itemMap").valueSerializer(new ItemSerializer()).make();
        }
        this.db = db;
        tickService.submit(new Ticker());
    }

    public void addEntity(CreeperEntity creeperEntity) {
        if (creeperEntity instanceof Npc) {
            Npc npc = (Npc) creeperEntity;
            roomManager.getRoom(npc.getRoomId()).addPresentNpc(npc.getEntityId());
            npcs.put(creeperEntity.getEntityId(), npc);
        }
        if (creeperEntity instanceof Room) {
            roomManager.addRoom((Room) creeperEntity);
        }
        entities.put(creeperEntity.getEntityId(), creeperEntity);
    }

    public void addItem(Item item){
        items.put(item.getItemId(), item);
        db.commit();
    }

    public void removeItem(Item item) {
        items.remove(item.getItemId());
        db.commit();
    }

    public void deleteNpcEntity(String npcId) {
        roomManager.getRoom(npcs.get(npcId).getRoomId()).removePresentNpc(npcId);
        npcs.remove(npcId);
    }

    public Npc getNpcEntity(String npcId) {
        return npcs.get(npcId);
    }

    public Item getItemEntity(String itemId) {
        return items.get(itemId);
    }

    class Ticker implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10000);
                    for (Map.Entry<String, CreeperEntity> next : entities.entrySet()) {
                        CreeperEntity creeperEntity = next.getValue();
                        ticketRunnerService.submit(creeperEntity);
                    }
                } catch (InterruptedException ie) {
                    throw new RuntimeException("Problem with ticker.");
                }
            }
        }
    }

}
