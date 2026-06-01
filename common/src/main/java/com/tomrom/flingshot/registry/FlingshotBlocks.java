package com.tomrom.flingshot.registry;

import com.tomrom.flingshot.block.GlimmerGooSplatBlock;
import com.tomrom.flingshot.platform.CommonPlatformHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class FlingshotBlocks {

    public static final Supplier<GlimmerGooSplatBlock> GLIMMER_GOO_SPLAT = CommonPlatformHelper.registerBlock(
            "glimmer_goo_splat",
            GlimmerGooSplatBlock::new,
            properties -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLOW_LICHEN)
                    .lightLevel(GlimmerGooSplatBlock.emission(7))
                    .friction(1.0f)
                    .sound(SoundType.HONEY_BLOCK)
    );

    public static void init() {
    }
}
