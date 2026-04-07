package ru.pb.ahfgc.entity.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import ru.pb.ahfgc.AHFGCMod;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CursedEyeRenderer extends GeoEntityRenderer<CursedEyeEntity> {
    public CursedEyeRenderer(EntityRendererProvider.Context context) {
        super(context, new CursedEyeModel());
        this.shadowRadius = 0f;
    }
}