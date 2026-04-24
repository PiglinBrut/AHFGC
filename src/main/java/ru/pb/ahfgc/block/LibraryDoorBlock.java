package ru.pb.ahfgc.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ru.pb.ahfgc.block.block_entity.LibraryDoorBlockEntity;

public class LibraryDoorBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<LibraryDoorPart> PART = EnumProperty.create("door_part", LibraryDoorPart.class);
    public static final IntegerProperty Y_OFFSET = IntegerProperty.create("y_offset", 0, 12);

    private static final VoxelShape FULL_SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public LibraryDoorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false)
                .setValue(OPEN, false)
                .setValue(PART, LibraryDoorPart.CENTER)
                .setValue(Y_OFFSET, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, LIT, PART, Y_OFFSET);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LibraryDoorBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos basePos = getBasePos(state, pos);
        BlockState baseState = level.getBlockState(basePos);

        if (!(baseState.getBlock() instanceof LibraryDoorBlock)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (level.getBlockEntity(basePos) instanceof LibraryDoorBlockEntity doorEntity) {
            if (doorEntity.isAnimating()) {
                if (!level.isClientSide) {
                    level.playSound(null, basePos, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.BLOCKS, 0.1F,
                            level.random.nextFloat() * 0.2F + 0.8F);
                }
                return ItemInteractionResult.SUCCESS;
            }
            if (!level.isClientSide) {
                level.playSound(null, basePos, SoundEvents.IRON_DOOR_OPEN, SoundSource.BLOCKS, 1.0F,
                        level.random.nextFloat() * 0.2F + 0.8F);
            }
            doorEntity.toggleOpen(level, basePos);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private BlockPos getBasePos(BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        int xOffset = switch (state.getValue(PART)) {
            case END_LEFT -> -3;
            case SIDE_LEFT_OUTER -> -2;
            case SIDE_LEFT_INNER -> -1;
            case CENTER -> 0;
            case SIDE_RIGHT_INNER -> 1;
            case SIDE_RIGHT_OUTER -> 2;
            case END_RIGHT -> 3;
        };

        BlockPos centerPos = pos.relative(facing.getClockWise(), -xOffset);

        int yOffset = state.getValue(Y_OFFSET);
        BlockPos basePos = centerPos.below(yOffset);

        return basePos;
    }

    private boolean canPlace(Level level, BlockPos pos, Direction facing) {
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        BlockPos pos = context.getClickedPos();

        if (canPlace(context.getLevel(), pos, facing)) {
            return this.defaultBlockState().setValue(FACING, facing);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);

            for (int yOffset = 0; yOffset < 13; yOffset++) {
                BlockPos levelPos = pos.above(yOffset);

                for (int xOffset = -3; xOffset <= 3; xOffset++) {
                    BlockPos targetPos = levelPos.relative(facing.getClockWise(), xOffset);
                    LibraryDoorPart part = getPartForOffset(xOffset);

                    BlockState partState = this.defaultBlockState()
                            .setValue(FACING, facing)
                            .setValue(PART, part)
                            .setValue(Y_OFFSET, yOffset)
                            .setValue(LIT, false)
                            .setValue(OPEN, false);

                    level.setBlock(targetPos, partState, 3);
                    level.blockUpdated(targetPos, Blocks.AIR);
                }
            }
        }
    }

    private LibraryDoorPart getPartForOffset(int offset) {
        return switch (offset) {
            case -3 -> LibraryDoorPart.END_LEFT;
            case -2 -> LibraryDoorPart.SIDE_LEFT_OUTER;
            case -1 -> LibraryDoorPart.SIDE_LEFT_INNER;
            case 0 -> LibraryDoorPart.CENTER;
            case 1 -> LibraryDoorPart.SIDE_RIGHT_INNER;
            case 2 -> LibraryDoorPart.SIDE_RIGHT_OUTER;
            case 3 -> LibraryDoorPart.END_RIGHT;
            default -> LibraryDoorPart.CENTER;
        };
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            BlockPos basePos = getBasePos(state, pos);
            BlockState baseState = level.getBlockState(basePos);

            if (baseState.is(this)) {
                Direction facing = baseState.getValue(FACING);

                for (int yOffset = 0; yOffset < 13; yOffset++) {
                    BlockPos levelPos = basePos.above(yOffset);

                    for (int xOffset = -3; xOffset <= 3; xOffset++) {
                        BlockPos targetPos = levelPos.relative(facing.getClockWise(), xOffset);
                        BlockState blockState = level.getBlockState(targetPos);

                        if (blockState.is(this)) {
                            level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 35);
                            level.levelEvent(player, 2001, targetPos, Block.getId(blockState));
                        }
                    }
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return FULL_SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        if (state.getValue(PART) == LibraryDoorPart.CENTER && state.getValue(Y_OFFSET) == 0) {
            return RenderShape.ENTITYBLOCK_ANIMATED;
        }
        return RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(OPEN)) {
            return Shapes.empty();
        }
        return FULL_SHAPE;
    }

    public enum LibraryDoorPart implements StringRepresentable {
        END_LEFT("end_left"),
        SIDE_LEFT_OUTER("side_left_outer"),
        SIDE_LEFT_INNER("side_left_inner"),
        CENTER("center"),
        SIDE_RIGHT_INNER("side_right_inner"),
        SIDE_RIGHT_OUTER("side_right_outer"),
        END_RIGHT("end_right");

        private final String name;

        LibraryDoorPart(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
