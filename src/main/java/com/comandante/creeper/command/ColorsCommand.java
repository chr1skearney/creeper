package com.comandante.creeper.command;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class ColorsCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("colors");
    final static String description = "Display available color examples.";
    final static String correctUsage = "colors";

    public ColorsCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            write ("BLACK: " + Color.BLACK + "This is an example of the color." + Color.RESET + "\r\n");
            write ("BLUE: " + Color.BLUE + "This is an example of the color." + Color.RESET + "\r\n");
            write ("CYAN: " + Color.CYAN + "This is an example of the color." + Color.RESET + "\r\n");
            write ("GREEN: " + Color.GREEN + "This is an example of the color." + Color.RESET + "\r\n");
            write ("MAGENTA: " + Color.MAGENTA + "This is an example of the color." + Color.RESET + "\r\n");
            write ("RED: " + Color.RED + "This is an example of the color." + Color.RESET + "\r\n");
            write ("WHITE: " + Color.WHITE + "This is an example of the color." + Color.RESET + "\r\n");
            write ("YELLOW: " + Color.YELLOW + "This is an example of the color." + Color.RESET + "\r\n");
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}