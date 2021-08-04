package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.clickgui.Themes;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.helper.AttackManager;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import me.zeroX150.atomic.helper.Transitions;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TargetHud extends Module {
    BooleanValue renderPing = (BooleanValue) this.config.create("Render ping", true).description("Shows the ping of the enemy");
    BooleanValue renderHP = (BooleanValue) this.config.create("Render health", true).description("Shows the current HP");
    BooleanValue renderMaxHP = (BooleanValue) this.config.create("Render max health", true).description("Shows the max HP");
    BooleanValue renderDistance = (BooleanValue) this.config.create("Render distance", true).description("Shows the distance to the player");
    BooleanValue renderLook = (BooleanValue) this.config.create("Render look", false).description("Shows if the player is looking at you");
    BooleanValue renderLoseWin = (BooleanValue) this.config.create("Render lose / win", true).description("Shows if you're losing or winning, if in battle");

    double wX = 0;
    double renderWX1 = 0;
    Entity e = null;
    Entity re = null;
    double trackedHp = 0;

    public TargetHud() {
        super("TargetHud", "the bruh", ModuleType.RENDER);
    }

    boolean isApplicable(Entity check) {
        if (check == Atomic.client.player) return false;
        if (check.distanceTo(Atomic.client.player) > 64) return false;
        int l = check.getEntityName().length();
        if (l < 3 || l > 16) return false;
        boolean isValidEntityName = Client.isPlayerNameValid(check.getEntityName());
        if (!isValidEntityName) return false;
        return check instanceof PlayerEntity;
    }

    @Override
    public void tick() {
        if (AttackManager.getLastAttackInTimeRange() != null) {
            e = AttackManager.getLastAttackInTimeRange();
            return;
        }
        List<Entity> entitiesQueue = StreamSupport.stream(Atomic.client.world.getEntities().spliterator(), false).filter(this::isApplicable).sorted(Comparator.comparingDouble(value -> value.getPos().distanceTo(Atomic.client.player.getPos()))).collect(Collectors.toList());
        if (entitiesQueue.size() > 0) {
            e = entitiesQueue.get(0);
        } else e = null;
        if (e instanceof LivingEntity ev) {
            if (ev.isDead()) e = null;
        }
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void onFastTick() {
        renderWX1 = Transitions.transition(renderWX1, wX, 10);
        if (re instanceof LivingEntity e) {
            trackedHp = Transitions.transition(trackedHp, e.getHealth(), 15, 0.002);
        }
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
        MatrixStack stack = new MatrixStack();
        int w = Atomic.client.getWindow().getScaledWidth();
        int h = Atomic.client.getWindow().getHeight();
        int modalWidth = 160;
        int modalHeight = 70;
        if (e != null) {
            wX = 100;
            re = e;
        } else wX = 0;
        if (re != null) {
            if (!(re instanceof LivingEntity entity)) return;

            float yOffset = 5;
            double renderWX = renderWX1 / 100d;
            double renderPosX = w / 2d + 10;
            double renderPosY = h / 4d + 10;
            stack.translate(renderPosX, renderPosY, 0);
            stack.push();
            double rwxI = Math.abs(1 - renderWX);
            double x = rwxI * (modalWidth / 2d);
            double y = rwxI * (modalHeight / 2d);
            stack.translate(x, y, 0);
            stack.scale((float) renderWX, (float) renderWX, 1);
            Renderer.fill(stack, Renderer.modify(Themes.Theme.ATOMIC.getPalette().active(), -1, -1, -1, 200), 0, 0, modalWidth, modalHeight);
            Atomic.fontRenderer.drawString(stack, entity.getEntityName(), 40, yOffset, 0xFFFFFF);
            yOffset += 10;
            PlayerListEntry ple = Atomic.client.getNetworkHandler().getPlayerListEntry(entity.getUuid());
            if (ple != null && renderPing.getValue()) {
                int ping = ple.getLatency();
                String v = ping + " ms";
                float ww = Atomic.fontRenderer.getStringWidth(v);
                Atomic.fontRenderer.drawString(stack, v, modalWidth - ww - 5, 5, 0xFFFFFF);
            }
            float mhealth = entity.getMaxHealth();
            float health = (float) trackedHp;
            float hPer = health / mhealth;
            double renderToX = modalWidth * hPer;
            Color GREEN = new Color(100, 255, 20);
            Color RED = new Color(255, 50, 20);
            Color MID_END = Renderer.lerp(GREEN, RED, hPer);
            Renderer.fillGradientH(stack, RED, MID_END, 0, modalHeight - 2, renderToX, modalHeight);
            if (renderHP.getValue()) {
                Atomic.fontRenderer.drawString(stack, Client.roundToN(trackedHp, 2) + " HP", 40, yOffset, MID_END.getRGB());
                yOffset += 10;
            }
            if (renderDistance.getValue()) {
                Atomic.fontRenderer.drawString(stack, Client.roundToN(entity.getPos().distanceTo(Atomic.client.player.getPos()), 1) + " D", 40, yOffset, 0xFFFFFF);
                yOffset += 10;
            }
            float mhP = Atomic.fontRenderer.getStringWidth(mhealth + "");
            if (renderMaxHP.getValue())
                Atomic.fontRenderer.drawString(stack, mhealth + "", (modalWidth - mhP - 3), (modalHeight - 3 - 10), 0xFFFFFF);

            HitResult bhr = entity.raycast(entity.getPos().distanceTo(Atomic.client.player.getPos()), 0f, false);
            if (bhr.getPos().distanceTo(Atomic.client.player.getPos().add(0, 1, 0)) < 1.5 && renderLook.getValue()) {
                Atomic.fontRenderer.drawString(stack, "Looks at you", 40, yOffset, 0xFFFFFF);
                yOffset += 10;
            }

            if (AttackManager.getLastAttackInTimeRange() != null && renderLoseWin.getValue()) {
                String st = entity.getHealth() > Atomic.client.player.getHealth() ? "Losing" : entity.getHealth() == Atomic.client.player.getHealth() ? "Stalemate" : "Winning";
                Atomic.fontRenderer.drawString(stack, st, 40, yOffset, 0xFFFFFF);
            }

            Text cname = re.getCustomName();
            re.setCustomName(Text.of("DoNotRenderThisUsernamePlease"));
            stack.pop();
            Renderer.drawEntity((20 * renderWX) + x, (modalHeight - 11) * renderWX + y, renderWX * 27, -10, -10, entity, stack);
            re.setCustomName(cname);
        }
    }

}

