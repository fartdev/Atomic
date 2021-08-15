package me.zeroX150.atomic.feature.gui;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.DynamicValue;
import me.zeroX150.atomic.feature.module.config.MultiValue;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.feature.module.impl.external.ClientConfig;
import me.zeroX150.atomic.feature.module.impl.render.ImGUI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.*;

@Environment(EnvType.CLIENT)
public class ImGuiScreen extends Screen {

    public static ImGuiScreen INSTANCE;

    private final ImGuiImplGlfw implGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 implGl3 = new ImGuiImplGl3();
    private final long windowPtr;
    // god i miss javascript
    private final Map<Module, ImBoolean> enabledMap = new HashMap<>();
    private final Map<DynamicValue, Object> configMap = new HashMap<>();
    private final Map<ModuleType, Boolean> catLoaded = new HashMap<>();
    int x = 25;

    public ImGuiScreen() {
        super(Text.of(" "));
        windowPtr = MinecraftClient.getInstance().getWindow().getHandle();
        ImGui.createContext();
        implGlfw.init(windowPtr, true);
        implGl3.init("#version 150");

        INSTANCE = this;

        for (ModuleType type : ModuleType.values()) {
            catLoaded.put(type, false);
        }
        for (Module module : ModuleRegistry.getModules()) {
            List<DynamicValue<?>> vals = module.config.getAll();
            enabledMap.put(module, new ImBoolean(module.isEnabled()));
            for (DynamicValue val : vals) {
                if (val.getKey().equalsIgnoreCase("keybind")) {
                    configMap.put(val, new ImString(val.getValue().toString()));
                } else if (val instanceof BooleanValue) {
                    configMap.put(val, new ImBoolean((Boolean) val.getValue()));
                } else if (val instanceof SliderValue) {
                    configMap.put(val, new float[]{new Float((double) val.getValue())});
                } else if (val instanceof MultiValue) {
                    configMap.put(val, new ImInt(((MultiValue) val).getPossible().indexOf(val.getValue())));
                }
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        implGlfw.newFrame();
        ImGui.newFrame();
        ImGui.getIO().setConfigWindowsMoveFromTitleBarOnly(true);
        ImGui.getStyle().setWindowMenuButtonPosition(-1);
        ImGui.getStyle().setColor(ImGuiCol.Button, 0, 0, 0, 127);
        ImGui.getStyle().setColor(ImGuiCol.Header, 50, 50, 50, 255);
        ImGui.getStyle().setColor(ImGuiCol.Border, 50, 50, 50, 255);
        ImGui.getStyle().setColor(ImGuiCol.ButtonHovered, 80, 80, 80, 255);
        ImGui.getStyle().setColor(ImGuiCol.TitleBg, 50, 50, 50, 255);
        ImGui.getStyle().setColor(ImGuiCol.CheckMark, 255, 255, 255, 255);
        ImGui.getStyle().setColor(ImGuiCol.SliderGrab, 255, 255, 255, 255);
        ImGui.getStyle().setColor(ImGuiCol.SliderGrabActive, 200, 200, 200, 255);
        ImGui.getStyle().setColor(ImGuiCol.FrameBgActive, 200, 200, 200, 255);
        ImGui.getStyle().setColor(ImGuiCol.ButtonActive, 100, 100, 100, 255);
        ImGui.getStyle().setColor(ImGuiCol.Header, 50, 50, 50, 255);
        ImGui.getStyle().setColor(ImGuiCol.HeaderHovered, 75, 75, 75, 255);
        ImGui.getStyle().setColor(ImGuiCol.Text, 255, 255, 255, 255);
        ImGui.getStyle().setColor(ImGuiCol.WindowBg, 25, 25, 25, 255);
        ImGui.getStyle().setColor(ImGuiCol.Border, 0, 0, 0, 255);
        for (ModuleType category : ModuleType.values()) {
            if (category == ModuleType.HIDDEN) {
                continue;
            }
            ImGui.begin(category.getName());
            if (!catLoaded.get(category)) {
                ImGui.setWindowPos(x, 50);
                x += 325;
                ImGui.setWindowSize(250, 450);
                catLoaded.put(category, true);
            }
            ArrayList<Module> modulesByCategoryArray = new ArrayList<>();
            for (Module module : ModuleRegistry.getModules()) {
                if (module.getModuleType().equals(category)) modulesByCategoryArray.add(module);
            }
            for (Module module : modulesByCategoryArray) {
                if (ImGui.collapsingHeader(module.isEnabled() ? module.getName() + " 0" : module.getName() + " X")) {
                    ImGui.checkbox("Enable", enabledMap.get(module));
                    List<DynamicValue<?>> vals = module.config.getAll();
                    for (DynamicValue val : vals) {
                        // This shit doesnt work men
                        // I tried to skid osiris' keybind thing and it too hard an shit
                        if (val.getKey().equalsIgnoreCase("keybind")) {
                            ImGui.text(val.getKey() + ": " + configMap.get(val) + " - unbothered, just use clickgui :)");
                        } else if (val instanceof BooleanValue) {
                            ImGui.checkbox(val.getKey(), (ImBoolean) configMap.get(val));
                            if ((boolean) val.getValue() != ((ImBoolean) configMap.get(val)).get()) {
                                val.setValue(((ImBoolean) configMap.get(val)).get());
                            }
                        } else if (val instanceof SliderValue) {
                            ImGui.sliderFloat(val.getKey(), (float[]) configMap.get(val), (float) ((SliderValue) val).getMin(), (float) ((SliderValue) val).getMax());
                            float[] floatArr = (float[]) configMap.get(val);
                            if (floatArr[0] != new Float((double) val.getValue())) {
                                val.setValue(floatArr[0]);
                            }
                        } else if (val instanceof MultiValue) {
                            String[] switchArray = ((MultiValue) val).getPossible().toArray(new String[((MultiValue) val).getPossible().size()]);
                            ImGui.combo(val.getKey(), (ImInt) configMap.get(val), switchArray);
                            if (((ImInt) configMap.get(val)).get() != ((MultiValue) val).getPossible().indexOf(val.getValue())) {
                                val.setValue(((MultiValue) val).getPossible().get(((ImInt) configMap.get(val)).get()));
                            }
                        }
                    }
                }
                if (enabledMap.get(module).get() != module.isEnabled()) {
                    module.toggle();
                }
            }
            ImGui.end();
        }
        ImGui.render();
        implGl3.renderDrawData(Objects.requireNonNull(ImGui.getDrawData()));
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}

