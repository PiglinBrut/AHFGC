package ru.pb.ahfgc.client.model;

import net.minecraft.resources.ResourceLocation;
import ru.pb.ahfgc.AHFGCMod;
import ru.pb.ahfgc.block.block_entity.LibraryDoorBlockEntity;
import software.bernie.geckolib.model.GeoModel;

public class LibraryDoorModel extends GeoModel<LibraryDoorBlockEntity> {
    private static final ResourceLocation MODEL_PATH =
            ResourceLocation.fromNamespaceAndPath(AHFGCMod.MOD_ID, "geo/library_door.geo.json");

    @Override
    public ResourceLocation getModelResource(LibraryDoorBlockEntity animatable) {
        return MODEL_PATH;
    }

    @Override
    public ResourceLocation getTextureResource(LibraryDoorBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(AHFGCMod.MOD_ID, "textures/block/library_door.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LibraryDoorBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(AHFGCMod.MOD_ID, "animations/library_door.animation.json");
    }
}