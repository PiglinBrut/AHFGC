package ru.pb.ahfgc.entity.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Position;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class SunEyeRenderer extends EntityRenderer<SunEyeEntity> {

    private final ItemRenderer itemRenderer;

    public SunEyeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SunEyeEntity entity, float yaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int light) {

        poseStack.pushPose();
        poseStack.translate(0, 0.25, 0);

        double bob = Math.sin((entity.tickCount + partialTicks) * 0.1) * 0.08;
        poseStack.translate(0, 0.35 + bob, 0);

        float spin = entity.getSpin();
        poseStack.mulPose(Axis.YP.rotationDegrees(spin));

        float sway = (float) Math.sin((entity.tickCount + partialTicks) * 0.15) * 4.0f;
        poseStack.mulPose(Axis.XP.rotationDegrees(sway * 0.8f));

        poseStack.scale(1.5f, 1.5f, 1.5f);

        itemRenderer.renderStatic(new net.minecraft.world.item.ItemStack(Items.ENDER_EYE),
                ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY,
                poseStack, buffer, entity.level(), 0);

        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(SunEyeEntity entity) {
        return null;
    }
}