package me.zeroX150.atomic.mixin.chat;

import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.feature.command.Command;
import me.zeroX150.atomic.feature.command.CommandRegistry;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.external.CleanGUI;
import me.zeroX150.atomic.feature.module.impl.external.ClientConfig;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Arrays;

@Mixin(Screen.class)
public class ScreenMixin extends DrawableHelper {
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    @Nullable
    protected MinecraftClient client;

    @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    public void sendMessage(String message, boolean toHud, CallbackInfo ci) {
        if (this.client == null) return;
        if (message.toLowerCase().startsWith(ClientConfig.chatPrefix.getValue().toLowerCase())) {
            ci.cancel();
            this.client.inGameHud.getChatHud().addToMessageHistory(message);
            String[] args = message.substring(ClientConfig.chatPrefix.getValue().length()).split(" +");
            String command = args[0].toLowerCase();
            args = Arrays.copyOfRange(args, 1, args.length);
            Command c = CommandRegistry.getByAlias(command);
            if (c == null) Client.notifyUser("Command not found.");
            else {
                Client.notifyUser(command);
                c.onExecute(args);
            }
        }
    }

    @Redirect(method = "renderBackground(Lnet/minecraft/client/util/math/MatrixStack;I)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;fillGradient(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
    ))
    public void renderBackgroundOverride(Screen screen, MatrixStack matrices, int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
        if (ModuleRegistry.getByClass(CleanGUI.class).isEnabled()) {
            int i = CleanGUI.mode.getIndex();
            switch (i) {
                case 0 -> {
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.enableBlend();
                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                    BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
                    Matrix4f matrices4 = matrices.peek().getModel();
                    float offset = (float) ((System.currentTimeMillis() % 3000) / 3000d);
                    float hsv2p = 0.25f + offset;
                    float hsv3p = 0.5f + offset;
                    float hsv4p = 0.75f + offset;
                    Color hsv1 = Color.getHSBColor(offset % 1, 0.6f, 1f);
                    Color hsv2 = Color.getHSBColor(hsv2p % 1, 0.6f, 1f);
                    Color hsv3 = Color.getHSBColor(hsv3p % 1, 0.6f, 1f);
                    Color hsv4 = Color.getHSBColor(hsv4p % 1, 0.6f, 1f);
                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                    bufferBuilder.vertex(matrices4, startX, startY, 0).color(hsv1.getRed(), hsv1.getGreen(), hsv1.getBlue(), 60).next();
                    bufferBuilder.vertex(matrices4, startX, endY, 0).color(hsv2.getRed(), hsv2.getGreen(), hsv2.getBlue(), 60).next();
                    bufferBuilder.vertex(matrices4, endX, endY, 0).color(hsv3.getRed(), hsv3.getGreen(), hsv3.getBlue(), 60).next();
                    bufferBuilder.vertex(matrices4, endX, startY, 0).color(hsv4.getRed(), hsv4.getGreen(), hsv4.getBlue(), 60).next();
                    bufferBuilder.end();
                    BufferRenderer.draw(bufferBuilder);
                    RenderSystem.disableBlend();
                }
                case 1 -> {
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.enableBlend();
                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
                    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                    BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
                    Matrix4f matrices4 = matrices.peek().getModel();
                    float offset = (float) ((System.currentTimeMillis() % 3000) / 3000d);
                    float hsv2p = 0.5f + offset;
                    Color hsv1 = Color.getHSBColor(offset % 1, 0.6f, 1f);
                    Color hsv2 = Color.getHSBColor(hsv2p % 1, 0.6f, 1f);
                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                    bufferBuilder.vertex(matrices4, startX, startY, 0).color(hsv1.getRed(), hsv1.getGreen(), hsv1.getBlue(), 60).next();
                    bufferBuilder.vertex(matrices4, startX, endY, 0).color(hsv2.getRed(), hsv2.getGreen(), hsv2.getBlue(), 60).next();
                    bufferBuilder.vertex(matrices4, endX, endY, 0).color(hsv2.getRed(), hsv2.getGreen(), hsv2.getBlue(), 60).next();
                    bufferBuilder.vertex(matrices4, endX, startY, 0).color(hsv1.getRed(), hsv1.getGreen(), hsv1.getBlue(), 60).next();
                    bufferBuilder.end();
                    BufferRenderer.draw(bufferBuilder);
                    RenderSystem.disableBlend();
                }
                case 2 -> DrawableHelper.fill(matrices, startX, startY, endX, endY, new Color(0, 0, 0, 60).getRGB());
            }
        } else this.fillGradient(matrices, startX, startY, endX, endY, colorStart, colorEnd);
    }

    @Inject(method = "renderBackgroundTexture", at = @At("HEAD"), cancellable = true)
    public void renderBackgroundTexture(int vOffset, CallbackInfo ci) {
        ci.cancel();
        Renderer.renderBackgroundTexture();
    }
}
