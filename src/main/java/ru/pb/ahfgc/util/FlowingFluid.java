package ru.pb.ahfgc.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class FlowingFluid extends BaseFlowingFluid {
    public FlowingFluid(Properties properties) {
        super(properties);
    }

    public Item getBucket() {
        return Items.AIR;
    }

    protected BlockState createLegacyBlock(FluidState state) {
        return Blocks.AIR.defaultBlockState();
    }

    public boolean isSource(FluidState state1) {
        return true;
    }

    public int getAmount(FluidState state2) {
        return 0;
    }
}
