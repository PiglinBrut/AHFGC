package ru.pb.ahfgc.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.pb.ahfgc.AHFGCMod;
import ru.pb.ahfgc.entity.custom.CursedEyeEntity;
import ru.pb.ahfgc.entity.custom.SunEyeItemEntity;
import ru.pb.ahfgc.entity.spells.dread_land_portal.DreadLandPortal;

import java.util.function.Supplier;

public class EntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, AHFGCMod.MOD_ID);
    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static final DeferredHolder<EntityType<?>, EntityType<DreadLandPortal>> DREAD_LAND_PORTAL =
            ENTITIES.register("dread_land_portal", () -> Builder.<DreadLandPortal>of(DreadLandPortal::new, MobCategory.MISC)
                    .sized(11.0F, 11.0F)
                    .clientTrackingRange(64)
                    .build("dread_land_portal"));
    public static final DeferredHolder<EntityType<?>, EntityType<CursedEyeEntity>> CURSED_EYE =
            ENTITIES.register("cursed_eye", () -> Builder.<CursedEyeEntity>of(CursedEyeEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(64)
                    .build("cursed_eye"));

    public static final DeferredHolder<EntityType<?>, EntityType<SunEyeItemEntity>> SUN_EYE_ITEM_ENTITY =
            ENTITIES.register("sun_eye_item", () -> Builder.<SunEyeItemEntity>of(SunEyeItemEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(10)
                    .updateInterval(1)
                    .build("sun_eye_item")
            );
}
