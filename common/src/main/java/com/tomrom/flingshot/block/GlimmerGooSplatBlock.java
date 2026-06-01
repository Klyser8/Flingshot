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

    public static void spawnStepParticles(Level level, Entity entity) {
        Vec3 velocity = entity.getDeltaMovement();
        if (!level.isClientSide() || level.getGameTime() % 5L != 0L || velocity.horizontalDistanceSqr() < 1.0E-4) {
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

    public static BlockPos findFloorSplat(Level level, Entity entity) {
        BlockPos entityPos = entity.blockPosition();
        BlockPos feetPos = BlockPos.containing(entity.getX(), entity.getBoundingBox().minY + 1.0E-3, entity.getZ());
        BlockPos belowFeetPos = BlockPos.containing(entity.getX(), entity.getBoundingBox().minY - 1.0E-3, entity.getZ());
        BlockPos onPos = entity.getOnPos();
        BlockPos aboveOnPos = onPos.above();

        if (isFloorSplat(level, entityPos)) {
            return entityPos;
        }
        if (isFloorSplat(level, feetPos)) {
            return feetPos;
        }
        if (isFloorSplat(level, belowFeetPos)) {
            return belowFeetPos;
        }
        if (isFloorSplat(level, onPos)) {
            return onPos;
        }
        if (isFloorSplat(level, aboveOnPos)) {
            return aboveOnPos;
        }

        return null;
    }

    public static boolean isFloorSplat(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof GlimmerGooSplatBlock && state.getValue(MultifaceBlock.getFaceProperty(Direction.DOWN));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (Direction direction : availableFaces(state)) {
            if (direction == Direction.DOWN) {
                continue;
            }
            Vec3 particlePos = calculateParticlePos(direction, pos, random);
            level.addParticle(GREASE_CHUNK(), particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
        }
    }

    public BlockState withFace(BlockState currentState, BlockGetter level, BlockPos pos, Direction face) {
        return getStateForPlacement(currentState, level, pos, face);
    }

    private static ParticleOptions GREASE_CHUNK() {
        return FlingshotParticles.GREASE_CHUNK.get();
    }

    private Vec3 calculateParticlePos(Direction direction, BlockPos pos, RandomSource random) {
        Vec3 faceNormal = new Vec3(direction.getStepX(), direction.getStepY(), direction.getStepZ());
        return switch (direction) {
            case UP -> Vec3.atLowerCornerOf(pos).add(random.nextDouble(), 0.88, random.nextDouble()).add(faceNormal.scale(0.02));
            case DOWN -> Vec3.atLowerCornerOf(pos).add(random.nextDouble(), 0.12, random.nextDouble()).add(faceNormal.scale(0.02));
            case NORTH -> Vec3.atLowerCornerOf(pos).add(random.nextDouble(), random.nextDouble(), 0.12).add(faceNormal.scale(0.02));
            case EAST -> Vec3.atLowerCornerOf(pos).add(0.88, random.nextDouble(), random.nextDouble()).add(faceNormal.scale(0.02));
            case SOUTH -> Vec3.atLowerCornerOf(pos).add(random.nextDouble(), random.nextDouble(), 0.88).add(faceNormal.scale(0.02));
            case WEST -> Vec3.atLowerCornerOf(pos).add(0.12, random.nextDouble(), random.nextDouble()).add(faceNormal.scale(0.02));
        };
    }
}
