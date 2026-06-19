package com.tomrom.flingshot.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import com.tomrom.flingshot.FlingshotConstants;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class FlingshotTiers {

    public static final FlingshotTier COPPER = new FlingshotTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            192,
            3.0f,
            1.5f,
            15,
            TagKey.create(Registries.ITEM, FlingshotConstants.id("copper_tool_materials"))
    );

    public record FlingshotTier(
            TagKey<Block> incorrectBlocksForDrops,
            int durability,
            float speed,
            float attackDamageBonus,
            int enchantmentValue,
            TagKey<Item> repairItems
    ) implements Tier {

        @Override
        public int getUses() {
            return durability;
        }

        @Override
        public float getSpeed() {
            return speed;
        }

        @Override
        public float getAttackDamageBonus() {
            return attackDamageBonus;
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return incorrectBlocksForDrops;
        }

        @Override
        public int getEnchantmentValue() {
            return enchantmentValue;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(repairItems);
        }
    }
}
