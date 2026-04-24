package ru.pb.ahfgc.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.pb.ahfgc.AHFGCMod;
import ru.pb.ahfgc.block.block_entity.LibraryDoorBlockEntity;
import ru.pb.ahfgc.entity.custom.CursedEyeEntity;
import ru.pb.ahfgc.entity.custom.SunEyeEntity;

public class EntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, AHFGCMod.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AHFGCMod.MOD_ID);
    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }

    public static final DeferredHolder<EntityType<?>, EntityType<CursedEyeEntity>> CURSED_EYE =
            ENTITIES.register("cursed_eye", () -> Builder.<CursedEyeEntity>of(CursedEyeEntity::new, MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .clientTrackingRange(64)
                    .build("cursed_eye"));

    public static final DeferredHolder<EntityType<?>, EntityType<SunEyeEntity>> SUN_EYE =
            ENTITIES.register("sun_eye", () -> Builder.<SunEyeEntity>of(SunEyeEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(10)
                    .updateInterval(1)
                    .build("sun_eye")
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LibraryDoorBlockEntity>> LIBRARY_DOOR =
            BLOCK_ENTITIES.register("library_door",
                    () -> BlockEntityType.Builder.of(
                            LibraryDoorBlockEntity::new,
                            BlockRegistry.LIBRARY_DOOR.get()
                    ).build(null));
}
