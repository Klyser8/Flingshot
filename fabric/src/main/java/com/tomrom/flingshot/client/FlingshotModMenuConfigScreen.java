package com.tomrom.flingshot.client;

import com.tomrom.flingshot.config.FlingshotConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class FlingshotModMenuConfigScreen extends Screen {

    private static final int LABEL_WIDTH = 190;
    private static final int FIELD_WIDTH = 210;
    private static final int ROW_HEIGHT = 24;

    private final Screen parent;

    private CycleButton<Boolean> enableGlimmerGooWorldgen;
    private EditBox glimmerGooPatchRarity;
    private EditBox glimmerGooPatchMinBlocks;
    private EditBox glimmerGooPatchMaxBlocks;
    private CycleButton<Boolean> recoverableCopperBuckPickup;
    private CycleButton<Boolean> enableVersatilityItemFlinging;
    private EditBox versatilityItemFlingingItemBlacklist;
    private EditBox versatilityItemFlingingItemTagBlacklist;
    private CycleButton<Boolean> enableAmmoTooltips;

    public FlingshotModMenuConfigScreen(Screen parent) {
        super(Component.translatable("config.flingshot.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int labelX = centerX - LABEL_WIDTH - 12;
        int fieldX = centerX + 8;
        int y = 24;

        addRenderableWidget(new StringWidget(centerX - 100, 8, 200, 12, title, font));

        enableGlimmerGooWorldgen = addToggle(labelX, fieldX, y, "enable_glimmer_goo_worldgen", FlingshotConfig.enableGlimmerGooWorldgen());
        y += ROW_HEIGHT;

        glimmerGooPatchRarity = addIntegerField(labelX, fieldX, y, "glimmer_goo_patch_rarity", FlingshotConfig.glimmerGooPatchRarity());
        y += ROW_HEIGHT;

        glimmerGooPatchMinBlocks = addIntegerField(labelX, fieldX, y, "glimmer_goo_patch_min_blocks", FlingshotConfig.glimmerGooPatchMinBlocks());
        y += ROW_HEIGHT;

        glimmerGooPatchMaxBlocks = addIntegerField(labelX, fieldX, y, "glimmer_goo_patch_max_blocks", FlingshotConfig.glimmerGooPatchMaxBlocks());
        y += ROW_HEIGHT;

        recoverableCopperBuckPickup = addToggle(labelX, fieldX, y, "recoverable_copper_buck_pickup", FlingshotConfig.recoverableCopperBuckPickup());
        y += ROW_HEIGHT;

        enableAmmoTooltips = addToggle(labelX, fieldX, y, "enable_ammo_tooltips", FlingshotConfig.enableAmmoTooltips());
        y += ROW_HEIGHT;

        enableVersatilityItemFlinging = addToggle(labelX, fieldX, y, "enable_versatility_item_flinging", FlingshotConfig.enableVersatilityItemFlinging());
        y += ROW_HEIGHT;

        versatilityItemFlingingItemBlacklist = addListField(labelX, fieldX, y, "versatility_item_flinging_item_blacklist", FlingshotConfig.versatilityItemFlingingItemBlacklist());
        y += ROW_HEIGHT;

        versatilityItemFlingingItemTagBlacklist = addListField(labelX, fieldX, y, "versatility_item_flinging_item_tag_blacklist", FlingshotConfig.versatilityItemFlingingItemTagBlacklist());

        int buttonY = height - 28;
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> {
            save();
            onClose();
        }).bounds(centerX - 154, buttonY, 96, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("controls.reset"), button -> {
            FlingshotConfig.resetToDefaultsAndSave();
            rebuildWidgets();
        }).bounds(centerX - 48, buttonY, 96, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                .bounds(centerX + 58, buttonY, 96, 20)
                .build());
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    private CycleButton<Boolean> addToggle(int labelX, int fieldX, int y, String key, boolean value) {
        addLabel(labelX, y, key);
        return addRenderableWidget(CycleButton.onOffBuilder(value)
                .create(fieldX, y - 4, FIELD_WIDTH, 20, Component.translatable("config.flingshot." + key)));
    }

    private EditBox addIntegerField(int labelX, int fieldX, int y, String key, int value) {
        addLabel(labelX, y, key);
        EditBox editBox = new EditBox(font, fieldX, y - 4, FIELD_WIDTH, 20, Component.translatable("config.flingshot." + key));
        editBox.setValue(Integer.toString(value));
        editBox.setMaxLength(6);
        editBox.setResponder(text -> {
            if (!text.matches("\\d*")) {
                editBox.setValue(text.replaceAll("\\D", ""));
            }
        });
        return addRenderableWidget(editBox);
    }

    private EditBox addListField(int labelX, int fieldX, int y, String key, List<String> values) {
        addLabel(labelX, y, key);
        EditBox editBox = new EditBox(font, fieldX, y - 4, FIELD_WIDTH, 20, Component.translatable("config.flingshot." + key));
        editBox.setValue(String.join(", ", values));
        editBox.setMaxLength(512);
        editBox.setSuggestion(Component.translatable("config.flingshot.list_hint").getString());
        return addRenderableWidget(editBox);
    }

    private void addLabel(int x, int y, String key) {
        addRenderableWidget(new StringWidget(x, y, LABEL_WIDTH, 12, Component.translatable("config.flingshot." + key), font));
    }

    private void save() {
        FlingshotConfig.updateAndSave(
                enableGlimmerGooWorldgen.getValue(),
                parseInt(glimmerGooPatchRarity, FlingshotConfig.glimmerGooPatchRarity()),
                parseInt(glimmerGooPatchMinBlocks, FlingshotConfig.glimmerGooPatchMinBlocks()),
                parseInt(glimmerGooPatchMaxBlocks, FlingshotConfig.glimmerGooPatchMaxBlocks()),
                recoverableCopperBuckPickup.getValue(),
                enableVersatilityItemFlinging.getValue(),
                parseList(versatilityItemFlingingItemBlacklist.getValue()),
                parseList(versatilityItemFlingingItemTagBlacklist.getValue()),
                enableAmmoTooltips.getValue()
        );
    }

    private static int parseInt(EditBox editBox, int fallback) {
        try {
            return Integer.parseInt(editBox.getValue());
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private static List<String> parseList(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(entry -> !entry.isBlank())
                .toList();
    }
}
