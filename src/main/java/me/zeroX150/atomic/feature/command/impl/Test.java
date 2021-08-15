package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.gui.ImGuiScreen;

public class Test extends Command {
    public Test() {
        super("Test", "deletes imgui screen to try and fix it fuck you im not doing more than this", "among", "sus", "test");
    }

    @Override
    public void onExecute(String[] args) {
            if (ImGuiScreen.INSTANCE != null) ImGuiScreen.INSTANCE = null;
    }
}
