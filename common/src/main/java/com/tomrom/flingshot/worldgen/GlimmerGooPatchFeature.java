package com.tomrom.flingshot.worldgen;

import com.mojang.serialization.Codec;
import com.tomrom.flingshot.block.GlimmerGooSplatBlock;
import com.tomrom.flingshot.config.FlingshotConfig;
import com.tomrom.flingshot.registry.FlingshotBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GlimmerGooPatchFeature extends Feature<GlimmerGooPatchConfiguration> {

    private static final int PLACEMENT_FLAGS = Block.UPDATE_CLIENTS;

    public GlimmerGooPatchFeature(Codec<GlimmerGooPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<GlimmerGooPatchConfiguration> context) {
        if (!FlingshotConfig.enableGlimmerGooWorldgen()) {
            return false;
        }

        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        int rarity = FlingshotConfig.glimmerGooPatchRarity();
        if (rarity > 1 && random.nextInt(rarity) != 0) {
            return false;
        }

        GlimmerGooPatchConfiguration config = context.config();
        GlimmerGooSplatBlock gooBlock = FlingshotBlocks.GLIMMER_GOO_SPLAT.get();
        SurfaceAnchor anchor = findSurface(level, context.origin(), random, gooBlock, config.searchRange());

        if (anchor == null) {
            return false;
        }

        int minBlocks = FlingshotConfig.glimmerGooPatchMinBlocks();
        int maxBlocks = FlingshotConfig.glimmerGooPatchMaxBlocks();
        int targetBlocks = minBlocks + random.nextInt(maxBlocks - minBlocks + 1);
        int maxDistance = Math.max(8, (int) Math.ceil(Math.sqrt(targetBlocks) * 1.75));
        List<SurfaceAnchor> frontier = new ArrayList<>();
        Set<SurfaceAnchor> visited = new HashSet<>();
        Set<BlockPos> placedPositions = new HashSet<>();

        if (!tryPlaceAt(level, anchor.pos(), anchor.face(), gooBlock)) {
            return false;
        }

        frontier.add(anchor);
        visited.add(anchor);
        placedPositions.add(anchor.pos());

        while (!frontier.isEmpty() && placedPositions.size() < targetBlocks) {
            int frontierIndex = random.nextInt(frontier.size());
            SurfaceAnchor current = frontier.get(frontierIndex);
            List<SurfaceAnchor> candidates = adjacentSurfaces(level, current, anchor.pos(), gooBlock, visited, maxDistance);

            if (candidates.isEmpty()) {
                frontier.remove(frontierIndex);
                continue;
            }

            SurfaceAnchor next = candidates.get(random.nextInt(candidates.size()));
            visited.add(next);

            if (tryPlaceAt(level, next.pos(), next.face(), gooBlock)) {
                frontier.add(next);
                placedPositions.add(next.pos());
            }
        }

        return !placedPositions.isEmpty();
    }

    private static SurfaceAnchor findSurface(WorldGenLevel level, BlockPos origin, RandomSource random, GlimmerGooSplatBlock gooBlock, int searchRange) {
        SurfaceAnchor anchor = findSurfaceAt(level, origin, random, gooBlock);

        if (anchor != null) {
            return anchor;
        }

        int attempts = Math.max(64, searchRange * searchRange);
        for (int attempt = 0; attempt < attempts; attempt++) {
            BlockPos pos = origin.offset(
                    random.nextInt(searchRange * 2 + 1) - searchRange,
                    random.nextInt(searchRange * 2 + 1) - searchRange,
                    random.nextInt(searchRange * 2 + 1) - searchRange
            );
            anchor = findSurfaceAt(level, pos, random, gooBlock);

            if (anchor != null) {
                return anchor;
            }
        }

        return null;
    }

    private static SurfaceAnchor findSurfaceAt(WorldGenLevel level, BlockPos pos, RandomSource random, GlimmerGooSplatBlock gooBlock) {
        List<Direction> faces = new ArrayList<>();

        for (Direction face : Direction.values()) {
            if (canPlaceFace(level, pos, face, gooBlock)) {
                faces.add(face);
            }
        }

        if (faces.isEmpty()) {
            return null;
        }

        return new SurfaceAnchor(pos, faces.get(random.nextInt(faces.size())));
    }

    private static boolean tryPlaceAt(WorldGenLevel level, BlockPos pos, Direction face, GlimmerGooSplatBlock gooBlock) {
        if (!canPlaceFace(level, pos, face, gooBlock)) {
            return false;
        }

        BlockState oldState = level.getBlockState(pos);
        BlockState newState = gooBlock.withFace(oldState, level, pos, face);

        return newState != null && level.setBlock(pos, newState, PLACEMENT_FLAGS);
    }

    private static List<SurfaceAnchor> adjacentSurfaces(WorldGenLevel level, SurfaceAnchor current, BlockPos origin, GlimmerGooSplatBlock gooBlock, Set<SurfaceAnchor> visited, int maxDistance) {
        List<SurfaceAnchor> candidates = new ArrayList<>();

        for (Direction face : Direction.values()) {
            addSurfaceCandidate(level, candidates, visited, origin, current.pos(), face, gooBlock, maxDistance);
        }

        for (Direction moveDirection : Direction.values()) {
            BlockPos neighborPos = current.pos().relative(moveDirection);
            for (Direction face : Direction.values()) {
                addSurfaceCandidate(level, candidates, visited, origin, neighborPos, face, gooBlock, maxDistance);
            }
        }

        return candidates;
    }

    private static void addSurfaceCandidate(
            WorldGenLevel level,
            List<SurfaceAnchor> candidates,
            Set<SurfaceAnchor> visited,
            BlockPos origin,
            BlockPos pos,
            Direction face,
            GlimmerGooSplatBlock gooBlock,
            int maxDistance
    ) {
        SurfaceAnchor candidate = new SurfaceAnchor(pos, face);

        if (!visited.contains(candidate) && withinDistance(origin, pos, maxDistance) && canPlaceFace(level, pos, face, gooBlock)) {
            candidates.add(candidate);
        }
    }

    private static boolean canPlaceFace(WorldGenLevel level, BlockPos pos, Direction face, GlimmerGooSplatBlock gooBlock) {
        if (!level.ensureCanWrite(pos)) {
            return false;
        }

        BlockState oldState = level.getBlockState(pos);

        if (oldState.is(gooBlock) && GlimmerGooSplatBlock.hasFace(oldState, face)) {
            return false;
        }

        BlockPos supportPos = pos.relative(face);
        if (!MultifaceBlock.canAttachTo(level, face, supportPos, level.getBlockState(supportPos))) {
            return false;
        }

        if (!oldState.is(gooBlock) && !oldState.canBeReplaced() && !oldState.getFluidState().isSourceOfType(Fluids.WATER)) {
            return false;
        }

        return gooBlock.withFace(oldState, level, pos, face) != null;
    }

    private static boolean withinDistance(BlockPos origin, BlockPos pos, int maxDistance) {
        int x = pos.getX() - origin.getX();
        int y = pos.getY() - origin.getY();
        int z = pos.getZ() - origin.getZ();
        return x * x + y * y + z * z <= maxDistance * maxDistance;
    }

    private record SurfaceAnchor(BlockPos pos, Direction face) {
    }
}
