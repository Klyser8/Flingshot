package com.tomrom.flingshot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tomrom.flingshot.FlingshotConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlingshotConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", FlingshotConstants.MOD_ID + ".json");

    private static ConfigData data = ConfigData.defaults();
    private static Set<ResourceLocation> versatilityItemBlacklist = Set.of();
    private static List<TagKey<Item>> versatilityItemTagBlacklist = List.of();

    public static void init() {
        data = load();
        data.sanitize();
        cacheBlacklists();
        save(data);
    }

    public static boolean enableGlimmerGooWorldgen() {
        return data.worldgen.enableGlimmerGooWorldgen;
    }

    public static int glimmerGooPatchRarity() {
        return data.worldgen.glimmerGooPatchRarity;
    }

    public static int glimmerGooPatchMinBlocks() {
        return Math.min(data.worldgen.glimmerGooPatchMinBlocks, data.worldgen.glimmerGooPatchMaxBlocks);
    }

    public static int glimmerGooPatchMaxBlocks() {
        return Math.max(data.worldgen.glimmerGooPatchMinBlocks, data.worldgen.glimmerGooPatchMaxBlocks);
    }

    public static boolean recoverableCopperBuckPickup() {
        return data.gameplay.recoverableCopperBuckPickup;
    }

    public static boolean enableAmmoTooltips() {
        return data.client.enableAmmoTooltips;
    }

    public static boolean enableVersatilityItemFlinging() {
        return data.gameplay.enableVersatilityItemFlinging;
    }

    public static List<String> versatilityItemFlingingItemBlacklist() {
        return List.copyOf(data.gameplay.versatilityItemFlingingItemBlacklist);
    }

    public static List<String> versatilityItemFlingingItemTagBlacklist() {
        return List.copyOf(data.gameplay.versatilityItemFlingingItemTagBlacklist);
    }

    public static void updateAndSave(
            boolean enableGlimmerGooWorldgen,
            int glimmerGooPatchRarity,
            int glimmerGooPatchMinBlocks,
            int glimmerGooPatchMaxBlocks,
            boolean recoverableCopperBuckPickup,
            boolean enableVersatilityItemFlinging,
            List<String> versatilityItemFlingingItemBlacklist,
            List<String> versatilityItemFlingingItemTagBlacklist,
            boolean enableAmmoTooltips
    ) {
        data.worldgen.enableGlimmerGooWorldgen = enableGlimmerGooWorldgen;
        data.worldgen.glimmerGooPatchRarity = glimmerGooPatchRarity;
        data.worldgen.glimmerGooPatchMinBlocks = glimmerGooPatchMinBlocks;
        data.worldgen.glimmerGooPatchMaxBlocks = glimmerGooPatchMaxBlocks;
        data.gameplay.recoverableCopperBuckPickup = recoverableCopperBuckPickup;
        data.gameplay.enableVersatilityItemFlinging = enableVersatilityItemFlinging;
        data.gameplay.versatilityItemFlingingItemBlacklist = new ArrayList<>(versatilityItemFlingingItemBlacklist);
        data.gameplay.versatilityItemFlingingItemTagBlacklist = new ArrayList<>(versatilityItemFlingingItemTagBlacklist);
        data.client.enableAmmoTooltips = enableAmmoTooltips;
        data.sanitize();
        cacheBlacklists();
        save(data);
    }

    public static void resetToDefaultsAndSave() {
        data = ConfigData.defaults();
        data.sanitize();
        cacheBlacklists();
        save(data);
    }

    public static boolean isVersatilityBlacklisted(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (versatilityItemBlacklist.contains(itemId)) {
            return true;
        }

        for (TagKey<Item> tag : versatilityItemTagBlacklist) {
            if (stack.is(tag)) {
                return true;
            }
        }

        return false;
    }

    private static ConfigData load() {
        if (!Files.exists(CONFIG_PATH)) {
            return ConfigData.defaults();
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            ConfigData loaded = GSON.fromJson(reader, ConfigData.class);
            if (loaded == null) {
                FlingshotConstants.LOG.warn("Config file {} was empty. Recreating it with default values.", CONFIG_PATH);
                return ConfigData.defaults();
            }
            return ConfigData.defaults().merge(loaded);
        } catch (Exception exception) {
            FlingshotConstants.LOG.warn("Failed to read config file {}. Using default values for this launch.", CONFIG_PATH, exception);
            return ConfigData.defaults();
        }
    }

    private static void save(ConfigData config) {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException exception) {
            FlingshotConstants.LOG.warn("Failed to save config file {}.", CONFIG_PATH, exception);
        }
    }

    private static void cacheBlacklists() {
        Set<ResourceLocation> itemBlacklist = new HashSet<>();
        for (String itemId : data.gameplay.versatilityItemFlingingItemBlacklist) {
            ResourceLocation id = ResourceLocation.tryParse(itemId);
            if (id == null) {
                FlingshotConstants.LOG.warn("Ignoring invalid Flingshot versatility item blacklist entry: {}", itemId);
                continue;
            }
            itemBlacklist.add(id);
        }

        List<TagKey<Item>> tagBlacklist = new ArrayList<>();
        for (String tagId : data.gameplay.versatilityItemFlingingItemTagBlacklist) {
            String normalizedTagId = tagId.startsWith("#") ? tagId.substring(1) : tagId;
            ResourceLocation id = ResourceLocation.tryParse(normalizedTagId);
            if (id == null) {
                FlingshotConstants.LOG.warn("Ignoring invalid Flingshot versatility item tag blacklist entry: {}", tagId);
                continue;
            }
            tagBlacklist.add(TagKey.create(Registries.ITEM, id));
        }

        versatilityItemBlacklist = Set.copyOf(itemBlacklist);
        versatilityItemTagBlacklist = List.copyOf(tagBlacklist);
    }

    private static class ConfigData {
        private Worldgen worldgen = new Worldgen();
        private Gameplay gameplay = new Gameplay();
        private Client client = new Client();

        private static ConfigData defaults() {
            return new ConfigData();
        }

        private ConfigData merge(ConfigData loaded) {
            if (loaded.worldgen != null) {
                worldgen = worldgen.merge(loaded.worldgen);
            }
            if (loaded.gameplay != null) {
                gameplay = gameplay.merge(loaded.gameplay);
            }
            if (loaded.client != null) {
                client = client.merge(loaded.client);
            }
            return this;
        }

        private void sanitize() {
            worldgen.sanitize();
            gameplay.sanitize();
        }
    }

    private static class Worldgen {
        private boolean enableGlimmerGooWorldgen = true;
        private int glimmerGooPatchRarity = 12;
        private int glimmerGooPatchMinBlocks = 12;
        private int glimmerGooPatchMaxBlocks = 36;

        private Worldgen merge(Worldgen loaded) {
            enableGlimmerGooWorldgen = loaded.enableGlimmerGooWorldgen;
            glimmerGooPatchRarity = loaded.glimmerGooPatchRarity;
            glimmerGooPatchMinBlocks = loaded.glimmerGooPatchMinBlocks;
            glimmerGooPatchMaxBlocks = loaded.glimmerGooPatchMaxBlocks;
            return this;
        }

        private void sanitize() {
            glimmerGooPatchRarity = clamp(glimmerGooPatchRarity, 1, 100000);
            glimmerGooPatchMinBlocks = clamp(glimmerGooPatchMinBlocks, 1, 2048);
            glimmerGooPatchMaxBlocks = clamp(glimmerGooPatchMaxBlocks, 1, 2048);
            if (glimmerGooPatchMinBlocks > glimmerGooPatchMaxBlocks) {
                int oldMinBlocks = glimmerGooPatchMinBlocks;
                glimmerGooPatchMinBlocks = glimmerGooPatchMaxBlocks;
                glimmerGooPatchMaxBlocks = oldMinBlocks;
            }
        }
    }

    private static class Gameplay {
        private boolean recoverableCopperBuckPickup = true;
        private boolean enableVersatilityItemFlinging = true;
        private List<String> versatilityItemFlingingItemBlacklist = new ArrayList<>();
        private List<String> versatilityItemFlingingItemTagBlacklist = new ArrayList<>();

        private Gameplay merge(Gameplay loaded) {
            recoverableCopperBuckPickup = loaded.recoverableCopperBuckPickup;
            enableVersatilityItemFlinging = loaded.enableVersatilityItemFlinging;
            if (loaded.versatilityItemFlingingItemBlacklist != null) {
                versatilityItemFlingingItemBlacklist = loaded.versatilityItemFlingingItemBlacklist;
            }
            if (loaded.versatilityItemFlingingItemTagBlacklist != null) {
                versatilityItemFlingingItemTagBlacklist = loaded.versatilityItemFlingingItemTagBlacklist;
            }
            return this;
        }

        private void sanitize() {
            versatilityItemFlingingItemBlacklist = sanitizeStringList(versatilityItemFlingingItemBlacklist);
            versatilityItemFlingingItemTagBlacklist = sanitizeStringList(versatilityItemFlingingItemTagBlacklist);
        }
    }

    private static class Client {
        private boolean enableAmmoTooltips = true;

        private Client merge(Client loaded) {
            enableAmmoTooltips = loaded.enableAmmoTooltips;
            return this;
        }
    }

    private static List<String> sanitizeStringList(List<String> values) {
        if (values == null) {
            return new ArrayList<>();
        }

        List<String> sanitized = new ArrayList<>();
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                sanitized.add(value.trim());
            }
        }
        return sanitized;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
