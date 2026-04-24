package ru.pb.ahfgc.client.model;

import net.minecraft.resources.ResourceLocation;
import ru.pb.ahfgc.entity.custom.CursedEyeEntity;
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
        return ResourceLocation.fromNamespaceAndPath("ahfgc", "textures/entity/cursed_portal.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CursedEyeEntity cursedEye) {
        return ResourceLocation.fromNamespaceAndPath("ahfgc", "animations/cursed_eye.animation.json");
    }
}