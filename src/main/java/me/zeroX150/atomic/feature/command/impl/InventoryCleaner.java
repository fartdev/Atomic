package me.zeroX150.atomic.feature.command.impl;

import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class InventoryCleaner extends Command {
    public InventoryCleaner() {
        super("InventoryCleaner", "Config for the inventory cleaner", "inventorycleaner", "invcleaner", "icleaner");
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            Client.notifyUser("You gotta give me a subcommand");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "list" -> {
                Client.notifyUser("All items currently in:");
                for (Item item : ((me.zeroX150.atomic.feature.module.impl.misc.InventoryCleaner) ModuleRegistry.getByClass(me.zeroX150.atomic.feature.module.impl.misc.InventoryCleaner.class)).getItems()) {
                    Client.notifyUser(" - " + Registry.ITEM.getId(item).getPath());
                }
            }
            case "remove" -> {
                if (args.length < 2) {
                    Client.notifyUser("I need an item to remove please");
                    return;
                }
                List<Item> i = ((me.zeroX150.atomic.feature.module.impl.misc.InventoryCleaner) ModuleRegistry.getByClass(me.zeroX150.atomic.feature.module.impl.misc.InventoryCleaner.class)).getItems();
                Item a = Registry.ITEM.get(new Identifier(args[1]));
                if (!i.contains(a)) {
                    Client.notifyUser("That item isnt in the list");
                    return;
                }
                ((me.zeroX150.atomic.feature.module.impl.misc.InventoryCleaner) ModuleRegistry.getByClass(me.zeroX150.atomic.feature.module.impl.misc.InventoryCleaner.class)).remove(a);
                Client.notifyUser("Removed item");
            }
            case "add" -> {
                if (args.length < 2) {
                    Client.notifyUser("I need an item to add please");
                    return;
                }
                List<Item> i = ((me.zeroX150.atomic.feature.module.impl.misc.InventoryCleaner) ModuleRegistry.getByClass(me.zeroX150.atomic.feature.module.impl.misc.InventoryCleaner.class)).getItems();
                Item a = Registry.ITEM.get(new Identifier(args[1]));
                if (i.contains(a)) {
                    Client.notifyUser("Item already in the list.");
                    return;
                }
                if (a == Items.AIR) {
                    Client.notifyUser("Not sure if i can add that");
                    return;
                }
                ((me.zeroX150.atomic.feature.module.impl.misc.InventoryCleaner) ModuleRegistry.getByClass(me.zeroX150.atomic.feature.module.impl.misc.InventoryCleaner.class)).add(a);
                Client.notifyUser("Added item " + args[1]);
            }
        }
    }
}
