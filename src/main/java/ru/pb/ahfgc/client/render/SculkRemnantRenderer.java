package ru.pb.ahfgc.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import ru.pb.ahfgc.client.model.SculkRemnantModel;
import ru.pb.ahfgc.entity.custom.SculkRemnantEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SculkRemnantRenderer extends GeoEntityRenderer<SculkRemnantEntity> {
    public SculkRemnantRenderer(EntityRendererProvider.Context context) {
        super(context, new SculkRemnantModel());
        this.shadowRadius = 2f;
    }
}
