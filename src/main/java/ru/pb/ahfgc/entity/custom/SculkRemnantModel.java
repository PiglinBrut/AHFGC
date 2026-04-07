package ru.pb.ahfgc.entity.custom;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SculkRemnantModel extends GeoModel<SculkRemnantEntity> {
    public SculkRemnantModel() {
    }

    @Override
    public ResourceLocation getModelResource(SculkRemnantEntity sculkRemnantEntity) {
        return ResourceLocation.fromNamespaceAndPath("ahfgc", "geo/sculk_remnant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SculkRemnantEntity sculkRemnantEntity) {
        return ResourceLocation.fromNamespaceAndPath("ahfgc", "textures/entity/sculk_remnant.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SculkRemnantEntity sculkRemnantEntity) {
        return ResourceLocation.fromNamespaceAndPath("ahfgc", "animations/sculk_remnant.animation.json");
    }
}
