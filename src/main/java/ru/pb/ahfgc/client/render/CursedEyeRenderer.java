package ru.pb.ahfgc.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import ru.pb.ahfgc.client.model.CursedEyeModel;
import ru.pb.ahfgc.entity.custom.CursedEyeEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CursedEyeRenderer extends GeoEntityRenderer<CursedEyeEntity> {
    public CursedEyeRenderer(EntityRendererProvider.Context context) {
        super(context, new CursedEyeModel());
        this.shadowRadius = 0f;
    }

    public Vec3 getPositionOffset(CursedEyeEntity entity, float partialTick) {
        return new Vec3(0, 0, 0); // Смещение для компенсации масштаба
    }

    @Override
    public void render(CursedEyeEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(2.0f, 2.0f, 2.0f);
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}