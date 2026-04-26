package ru.pb.ahfgc.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import ru.pb.ahfgc.block.LibraryDoorBlock;
import ru.pb.ahfgc.block.block_entity.LibraryDoorBlockEntity;
import ru.pb.ahfgc.client.model.LibraryDoorModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class LibraryDoorRenderer extends GeoBlockRenderer<LibraryDoorBlockEntity> {
    private static final GeoModel<LibraryDoorBlockEntity> MODEL = new LibraryDoorModel();

    public LibraryDoorRenderer(BlockEntityRendererProvider.Context context) {
        super(MODEL);
    }

    public boolean shouldRender(LibraryDoorBlockEntity blockEntity, BlockState blockState) {
        BlockState state = blockEntity.getBlockState();
        return state.getValue(LibraryDoorBlock.PART) == LibraryDoorBlock.LibraryDoorPart.CENTER
                && state.getValue(LibraryDoorBlock.Y_OFFSET) == 0;
    }

    @Override
    public void render(LibraryDoorBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        if (state.getValue(LibraryDoorBlock.PART) != LibraryDoorBlock.LibraryDoorPart.CENTER
                || state.getValue(LibraryDoorBlock.Y_OFFSET) != 0) {
            return;
        }

        super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public AABB getRenderBoundingBox(LibraryDoorBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(
                pos.getX() - 3, pos.getY(), pos.getZ() - 3,
                pos.getX() + 4, pos.getY() + 13, pos.getZ() + 4
        );
    }

    @Override
    public boolean shouldRenderOffScreen(LibraryDoorBlockEntity blockEntity) {
        return true;
    }
}