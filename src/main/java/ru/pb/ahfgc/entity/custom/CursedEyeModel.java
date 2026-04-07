package ru.pb.ahfgc.entity.custom;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CursedEyeModel extends GeoModel<CursedEyeEntity> {
    public CursedEyeModel() {
    }

    @Override
    public ResourceLocation getModelResource(CursedEyeEntity cursedEye) {
        return ResourceLocation.fromNamespaceAndPath("ahfgc", "geo/cursed_eye.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CursedEyeEntity cursedEye) {
        return ResourceLocation.fromNamespaceAndPath("ahfgc", "textures/entity/cursed_eye.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CursedEyeEntity cursedEye) {
        return ResourceLocation.fromNamespaceAndPath("ahfgc", "animations/cursed_eye.animation.json");
    }
}