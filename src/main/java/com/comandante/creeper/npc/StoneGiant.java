package com.comandante.creeper.npc;


import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.world.Area;
import com.google.common.base.Optional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StoneGiant extends Npc {
    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "stone giant";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"s", "stone", "giant", "g", NAME}
    ));

    private final static String colorName = "stone" + Color.BOLD_ON + Color.MAGENTA + " giant"  + Color.RESET ;
    private final static String dieMessage = "a " + colorName + " breathes his last breath in a pool of " + Color.BOLD_ON + Color.RED + "blood" + Color.RESET + ".";

    public StoneGiant(GameManager gameManager, Loot loot) {
        super(gameManager, NAME, colorName, 0, NpcStats.STONE_GIANT.createStats(), dieMessage, Optional.<HashSet<Area>>absent(), validTriggers, loot);
    }

    @Override
    public StoneGiant create(GameManager gameManager, Loot loot) {
        return new StoneGiant(gameManager, loot);
    }
}