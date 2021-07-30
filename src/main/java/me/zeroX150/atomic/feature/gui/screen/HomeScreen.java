package me.zeroX150.atomic.feature.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.clickgui.ClickGUI;
import me.zeroX150.atomic.feature.gui.overlay.WelcomeOverlay;
import me.zeroX150.atomic.feature.gui.particles.ParticleManager;
import me.zeroX150.atomic.feature.module.impl.external.ClientConfig;
import me.zeroX150.atomic.helper.Client;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.lwjgl.opengl.GL11;

public class HomeScreen extends Screen {
    static boolean shownWelcome = false;
    boolean isMeteorLoaded = false;
    ParticleManager pm = new ParticleManager(200);
    String t = "Atomic, made by ";

    public HomeScreen() {
        super(Text.of("a"));
    }

    @Override
    protected void init() {
        if (!shownWelcome) {
            shownWelcome = true;
            Atomic.client.setOverlay(new WelcomeOverlay());
        }
        addDrawableChild(createCentered("Singleplayer", height / 2 - 20 - 20 - 10, button -> Atomic.client.openScreen(new SelectWorldScreen(this))));
        addDrawableChild(createCentered("Multiplayer", height / 2 - 25, button -> Atomic.client.openScreen(new MultiplayerScreen(this))));
        addDrawableChild(createCentered("Realms", height / 2, button -> Atomic.client.openScreen(new RealmsMainScreen(this))));
        addDrawableChild(new ButtonWidget(width / 2 - 75, height / 2 + 25, 70, 20, Text.of("Options"), button -> Atomic.client.openScreen(new OptionsScreen(this, Atomic.client.options))));
        addDrawableChild(new ButtonWidget(width / 2 + 5, height / 2 + 25, 70, 20, Text.of("Quit"), button -> Atomic.client.stop()));
        addDrawableChild(new ButtonWidget(width / 2 - (150 / 2), height / 2 + 25 + 25, 150, 20, Text.of("Alts"), button -> Atomic.client.openScreen(new AltManager())));
        addDrawableChild(new ButtonWidget(1, 1, 130, 20, Text.of("Vanilla home screen"), button -> {
            ClientConfig.customMainMenu.setValue(false);
            Atomic.client.openScreen(null);
        }));
        super.init();
        isMeteorLoaded = FabricLoader.getInstance().isModLoaded("meteor-client");
    }

    @Override
    public void tick() {
        pm.tick();
        super.tick();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(0);
        pm.render();
        double logoSize = 0.3;
        RenderSystem.setShaderTexture(0, ClickGUI.LOGO);
        RenderSystem.enableBlend();
        RenderSystem.blendEquation(32774);
        RenderSystem.blendFunc(770, 1);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager._texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        drawTexture(matrices, (int) (width / 2 - (504 * logoSize / 2)), 10, 0, 0, 0, (int) (504 * logoSize), (int) (130 * logoSize), (int) (130 * logoSize), (int) (504 * logoSize));
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        if (isMeteorLoaded) {
            Atomic.fontRenderer.drawString(matrices, "Fuck meteor edition", 1, 1, 0xFFFFFF);
        }
        Atomic.fontRenderer.drawString(matrices, t, 1, height - 10, 0xFFFFFF);
        Atomic.fontRenderer.drawString(matrices, "0x150", 1 + Atomic.fontRenderer.getStringWidth(t), height - 10, Client.getCurrentRGB().getRGB());
        super.render(matrices, mouseX, mouseY, delta);
    }

    ButtonWidget createCentered(String t, int y, ButtonWidget.PressAction action) {
        return new ButtonWidget(width / 2 - (150 / 2), y, 150, 20, Text.of(t), action);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float width = Atomic.fontRenderer.getStringWidth(t);
        float mwidth = width + Atomic.fontRenderer.getStringWidth("0x150");
        float h = height - 10;
        float m = height - 1;
        if (mouseX >= width && mouseX <= mwidth && mouseY >= h && mouseY <= m) {
            Util.getOperatingSystem().open("https://0x150.cf");
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
