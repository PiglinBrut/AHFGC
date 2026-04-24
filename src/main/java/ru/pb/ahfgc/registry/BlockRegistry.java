package ru.pb.ahfgc.registry;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.pb.ahfgc.AHFGCMod;
import ru.pb.ahfgc.block.LibraryDoorBlock;

public class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AHFGCMod.MOD_ID);
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    public static final DeferredBlock<LibraryDoorBlock> LIBRARY_DOOR = BLOCKS.register("library_door",
            () -> new LibraryDoorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .noOcclusion()
                    .dynamicShape()
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()
                    .sound(SoundType.METAL)));
}
