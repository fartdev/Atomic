package me.zeroX150.atomic.mixin.game.screen;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.external.InventoryWalk;
import me.zeroX150.atomic.feature.module.impl.external.SlotSpammer;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class GenericContainerScreenMixin<T extends ScreenHandler> extends Screen {
    @Shadow
    @Final
    protected T handler;
    @Shadow
    protected int x;
    @Shadow
    protected int y;
    boolean isSelecting = false;
    ButtonWidget bw;
    KeyBinding arrowRight = new KeyBinding("", GLFW.GLFW_KEY_RIGHT, "");
    KeyBinding arrowLeft = new KeyBinding("", GLFW.GLFW_KEY_LEFT, "");
    KeyBinding arrowUp = new KeyBinding("", GLFW.GLFW_KEY_UP, "");
    KeyBinding arrowDown = new KeyBinding("", GLFW.GLFW_KEY_DOWN, "");

    protected GenericContainerScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract boolean isPointOverSlot(Slot slot, double pointX, double pointY);

    @Inject(method = "init", at = @At("TAIL"))
    public void initCustom(CallbackInfo ci) {
        int w = Atomic.client.getWindow().getScaledWidth();
        bw = new ButtonWidget(w / 2 - (150 / 2), 11, 150, 20, Text.of("Slot spammer"), button -> {
            if (ModuleRegistry.getByClass(SlotSpammer.class).isEnabled()) {
                ModuleRegistry.getByClass(SlotSpammer.class).setEnabled(false);
            } else isSelecting = !isSelecting;
        });
        this.addDrawableChild(bw);
    }

    boolean keyPressed(KeyBinding bind) {
        return InputUtil.isKeyPressed(Atomic.client.getWindow().getHandle(), bind.getDefaultKey().getCode());
    }

    void setState(KeyBinding bind) {
        bind.setPressed(keyPressed(bind));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (!ModuleRegistry.getByClass(InventoryWalk.class).isEnabled()) return;
        GameOptions go = Atomic.client.options;
        setState(go.keyForward);
        setState(go.keyRight);
        setState(go.keyBack);
        setState(go.keyLeft);
        setState(go.keyJump);
        setState(go.keySprint);

        float yawOffset = 0f;
        float pitchOffset = 0f;
        if (keyPressed(arrowRight)) yawOffset += 5f;
        if (keyPressed(arrowLeft)) yawOffset -= 5f;
        if (keyPressed(arrowUp)) pitchOffset -= 5f;
        if (keyPressed(arrowDown)) pitchOffset += 5f;
        Atomic.client.player.setYaw(Atomic.client.player.getYaw() + yawOffset);
        Atomic.client.player.setPitch(Atomic.client.player.getPitch() + pitchOffset);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void oMC(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (isSelecting) {
            Slot s = null;
            for (Slot slot : this.handler.slots) {
                if (this.isPointOverSlot(slot, mouseX, mouseY)) {
                    s = slot;
                    break;
                }
            }
            isSelecting = false;
            if (s != null) {
                SlotSpammer.slotToSpam = s;
                cir.cancel();
                cir.setReturnValue(true);
                ModuleRegistry.getByClass(SlotSpammer.class).setEnabled(true);
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        String t = "disabled";
        if (ModuleRegistry.getByClass(SlotSpammer.class).isEnabled()) {
            bw.setMessage(Text.of("Click to disable"));
            t = "running";
        } else if (isSelecting) {
            bw.setMessage(Text.of("Click a slot..."));
            t = "selecting";
        } else bw.setMessage(Text.of("Slot spammer"));
        DrawableHelper.drawCenteredText(matrices, textRenderer, "Slot spammer " + t, width / 2, 1, 0xFFFFFF);
        if (SlotSpammer.slotToSpam != null) {
            Renderer.fill(Renderer.modify(Client.getCurrentRGB(), -1, -1, -1, 100), this.x + SlotSpammer.slotToSpam.x, this.y + SlotSpammer.slotToSpam.y, this.x + SlotSpammer.slotToSpam.x + 16, this.y + SlotSpammer.slotToSpam.y + 16);
        }
    }

}
