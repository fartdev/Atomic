package me.zeroX150.atomic.feature.command.impl;

import baritone.api.BaritoneAPI;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.helper.Client;

public class Baritone extends Command {
    public Baritone() {
        super("Baritone", "Runs baritone commands", "baritone", "b");
    }

    @Override
    public void onExecute(String[] args) {
        String v = Client.getValueFromBaritoneSetting(BaritoneAPI.getSettings().prefix); // what the fuck is the issue?
        Atomic.client.player.sendChatMessage(v + String.join(" ", args));
    }
}
