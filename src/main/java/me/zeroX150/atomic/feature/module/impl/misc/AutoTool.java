package me.zeroX150.atomic.feature.module.impl.misc;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.impl.world.Nuker;
import me.zeroX150.atomic.mixin.game.ClientPlayerInteractionManagerAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTool", "Automatically selects the best tool for the job", ModuleType.MISC);
    }

    public static void pick(BlockState state) {
        float best = 1f;
        int index = -1;
        int optAirIndex = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = Atomic.client.player.getInventory().getStack(i);
            if (stack.getItem() == Items.AIR) optAirIndex = i;
            float s = stack.getMiningSpeedMultiplier(state);
            if (s > best) {
                index = i;
            }
        }
        if (index != -1) {
            Atomic.client.player.getInventory().selectedSlot = index;
        } else {
            if (optAirIndex != -1)
                Atomic.client.player.getInventory().selectedSlot = optAirIndex; // to prevent tools from getting damaged by accident, switch to air if we didnt find anything
        }
    }

    @Override
    public void tick() {
        if (Atomic.client.interactionManager.isBreakingBlock() && !ModuleRegistry.getByClass(Nuker.class).isEnabled()) {
            BlockPos breaking = ((ClientPlayerInteractionManagerAccessor) Atomic.client.interactionManager).getCurrentBreakingPos();
            BlockState bs = Atomic.client.world.getBlockState(breaking);
            pick(bs);
        }
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

