package ru.pb.ahfgc.entity.custom;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SculkRemnantRenderer extends GeoEntityRenderer<SculkRemnantEntity> {
    public SculkRemnantRenderer(EntityRendererProvider.Context context) {
        super(context, new SculkRemnantModel());
        this.shadowRadius = 2f;
    }
}
