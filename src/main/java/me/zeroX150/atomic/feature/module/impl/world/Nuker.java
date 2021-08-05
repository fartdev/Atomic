package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.feature.module.impl.misc.AutoTool;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import me.zeroX150.atomic.helper.Rotations;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Nuker extends Module {
    final List<BlockPos> renders = new ArrayList<>();
    final SliderValue range = (SliderValue) this.config.create("Range", 3, 0, 4, 1).description("The range to nuke by");
    final SliderValue blocksPerTick = (SliderValue) this.config.create("Blocks per tick", 1, 1, 20, 0).description("The amount of blocks to destroy per tick");
    final SliderValue delay = (SliderValue) this.config.create("Delay", 5, 0, 20, 0).description("The delay before breaking blocks");
    final BooleanValue ignoreXray = (BooleanValue) this.config.create("Ignore xray", true).description("Whether or not to ignore xray blocks");
    MultiValue mode = (MultiValue) this.config.create("Mode", "Everything", "Everything", "Torches", "Fire", "Wood").description("What to nuke");
    BooleanValue autoTool = (BooleanValue) this.config.create("Auto tool", true).description("Automatically picks the best tool from your inventory, for the block being broken");
    int delayPassed = 0;

    public Nuker() {
        super("Nuker", "breaking block", ModuleType.WORLD);
    }

    Block[] WOOD = new Block[]{
            Blocks.ACACIA_LOG,
            Blocks.BIRCH_LOG,
            Blocks.DARK_OAK_LOG,
            Blocks.JUNGLE_LOG,
            Blocks.OAK_LOG,
            Blocks.SPRUCE_LOG,
            Blocks.STRIPPED_ACACIA_LOG,
            Blocks.STRIPPED_BIRCH_LOG,
            Blocks.STRIPPED_DARK_OAK_LOG,
            Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_SPRUCE_LOG
    };

    boolean isBlockApplicable(Block b) {
        if (mode.getValue().equalsIgnoreCase("everything")) return true;
        else if (mode.getValue().equalsIgnoreCase("torches")) return b == Blocks.TORCH || b == Blocks.WALL_TORCH;
        else if (mode.getValue().equalsIgnoreCase("fire")) return b == Blocks.FIRE;
        else if (mode.getValue().equalsIgnoreCase("wood")) {
            return Arrays.stream(WOOD).anyMatch(block -> block == b);
        }
        return false;
    }

    @Override
    public void tick() {
        if (Atomic.client.player == null || Atomic.client.world == null || Atomic.client.interactionManager == null || Atomic.client.getNetworkHandler() == null)
            return;
        if (delayPassed < delay.getValue()) {
            delayPassed++;
            return;
        }
        delayPassed = 0;
        BlockPos ppos1 = Atomic.client.player.getBlockPos();
        int blocksBroken = 0;
        renders.clear();
        for (double y = range.getValue(); y > -range.getValue() - 1; y--) {
            for (double x = -range.getValue(); x < range.getValue() + 1; x++) {
                for (double z = -range.getValue(); z < range.getValue() + 1; z++) {
                    if (blocksBroken >= blocksPerTick.getValue()) break;
                    BlockPos vp = new BlockPos(x, y, z);
                    BlockPos np = ppos1.add(vp);
                    Vec3d vp1 = new Vec3d(np.getX(), np.getY(), np.getZ());
                    if (vp1.distanceTo(Atomic.client.player.getPos()) >= Atomic.client.interactionManager.getReachDistance() - 0.2)
                        continue;
                    BlockState bs = Atomic.client.world.getBlockState(np);
                    boolean b = !ignoreXray.getValue() || !XRAY.blocks.contains(bs.getBlock());
                    if (!bs.isAir() && bs.getBlock() != Blocks.WATER && bs.getBlock() != Blocks.LAVA && bs.getBlock() != Blocks.BEDROCK && b && Atomic.client.world.getWorldBorder().contains(np) && isBlockApplicable(bs.getBlock())) {
                        renders.add(np);
                        if (autoTool.getValue()) AutoTool.pick(bs);
                        Atomic.client.player.swingHand(Hand.MAIN_HAND);
                        if (!Atomic.client.player.getAbilities().creativeMode) {
                            Atomic.client.interactionManager.updateBlockBreakingProgress(np, Direction.DOWN);
                        } else Atomic.client.interactionManager.attackBlock(np, Direction.DOWN);
                        Rotations.lookAtV3(new Vec3d(np.getX() + .5, np.getY() + .5, np.getZ() + .5));
                        blocksBroken++;
                    }
                }
            }
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
        for (BlockPos render : renders) {
            Vec3d vp = new Vec3d(render.getX(), render.getY(), render.getZ());
            Renderer.renderFilled(vp, new Vec3d(1, 1, 1), Renderer.modify(Client.getCurrentRGB(), -1, -1, -1, 50), matrices);
        }
    }

    @Override
    public void onHudRender() {

    }
}

