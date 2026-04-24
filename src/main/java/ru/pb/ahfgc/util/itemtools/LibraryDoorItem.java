package ru.pb.ahfgc.util.itemtools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import ru.pb.ahfgc.block.LibraryDoorBlock;

public class LibraryDoorItem extends Item {
    private final LibraryDoorBlock block;

    public LibraryDoorItem(LibraryDoorBlock block, Properties properties) {
        super(properties);
        this.block = block;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        BlockPos placePos = clickedPos.relative(clickedFace);

        Direction facing = context.getHorizontalDirection();

        if (canPlaceDoor(level, placePos, facing)) {
            if (!level.isClientSide) {
                BlockState state = block.defaultBlockState()
                        .setValue(LibraryDoorBlock.FACING, facing)
                        .setValue(LibraryDoorBlock.PART, LibraryDoorBlock.LibraryDoorPart.CENTER)
                        .setValue(LibraryDoorBlock.Y_OFFSET, 0)
                        .setValue(LibraryDoorBlock.LIT, false)
                        .setValue(LibraryDoorBlock.OPEN, false);

                level.setBlock(placePos, state, 3);

                block.setPlacedBy(level, placePos, state, context.getPlayer(), context.getItemInHand());
            }

            context.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    private boolean canPlaceDoor(Level level, BlockPos pos, Direction facing) {
        for (int y = 0; y < 13; y++) {
            BlockPos levelPos = pos.above(y);
            for (int x = -3; x <= 3; x++) {
                BlockPos checkPos = levelPos.relative(facing.getClockWise(), x);
                BlockState state = level.getBlockState(checkPos);
                if (!state.canBeReplaced()) {
                    return false;
                }
            }
        }
        return true;
    }
}
