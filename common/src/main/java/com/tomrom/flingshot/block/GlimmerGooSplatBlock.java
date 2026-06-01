package com.tomrom.flingshot.block;

import com.tomrom.flingshot.registry.FlingshotParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.ToIntFunction;

public class GlimmerGooSplatBlock extends MultifaceBlock {

    public GlimmerGooSplatBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static ToIntFunction<BlockState> emission(int lightLevel) {
        return state -> hasAnyFace(state) ? lightLevel : 0;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return state.getFluidState().isEmpty();
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        int faceCount = availableFaces(state).size();

        if (faceCount > 1) {
            for (ItemStack drop : drops) {
                drop.setCount(drop.getCount() * faceCount);
            }
        }

        return drops;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        Vec3 velocity = entity.getDeltaMovement();
        if (!level.isClientSide() || level.getGameTime() % 5L != 0L || (velocity.x == 0.0 && velocity.z == 0.0)) {
            return;
        }

        level.addParticle(
                FlingshotParticles.GREASE_POP.get(),
                entity.getRandomX(0.5),
                entity.getY() + 0.05,
                entity.getRandomZ(0.5),
                -velocity.x / 4.0,
                0.01,
                -velocity.z / 4.0
        );
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (Direction direction : availableFaces(state)) {
            Vec3 particlePos = calculateParticlePos(direction, pos, random);
            if (particlePos != null) {
                level.addParticle(GREASE_CHUNK(), particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
            }
        }
    }

    public BlockState withFace(BlockState currentState, BlockGetter level, BlockPos pos, Direction face) {
        return getStateForPlacement(currentState, level, pos, face);
    }

    private static ParticleOptions GREASE_CHUNK() {
        return FlingshotParticles.GREASE_CHUNK.get();
    }

    private Vec3 calculateParticlePos(Direction direction, BlockPos pos, RandomSource random) {
        return switch (direction) {
            case UP -> Vec3.atLowerCornerOf(pos).add(random.nextDouble(), 1.1, random.nextDouble());
            case NORTH -> Vec3.atLowerCornerOf(pos).add(random.nextDouble(), random.nextDouble(), -0.1);
            case EAST -> Vec3.atLowerCornerOf(pos).add(1.1, random.nextDouble(), random.nextDouble());
            case SOUTH -> Vec3.atLowerCornerOf(pos).add(random.nextDouble(), random.nextDouble(), 1.1);
            case WEST -> Vec3.atLowerCornerOf(pos).add(-0.1, random.nextDouble(), random.nextDouble());
            case DOWN -> null;
        };
    }
}
