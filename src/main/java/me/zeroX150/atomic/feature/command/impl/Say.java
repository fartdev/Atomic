package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.helper.Client;

public class Say extends Command {
    public Say() {
        super("Say", "Says something", "say", "tell");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            Client.notifyUser("not sure if i can say nothing");
            return;
        }
        Atomic.client.player.sendChatMessage(String.join(" ", args));
    }
}
