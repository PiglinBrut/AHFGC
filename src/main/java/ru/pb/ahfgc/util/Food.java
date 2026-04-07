package ru.pb.ahfgc.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

public class Food {

    public static final FoodProperties BURNING_BREW = (new FoodProperties.Builder())
            .nutrition(8)
            .saturationModifier(0.8F)
            .alwaysEdible()
            .usingConvertsTo(Items.BOWL)
            .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1800, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.HARM, 1, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.ABSORPTION, 1800, 1), 1.0F)
            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 1800, 0), 1.0F)
            .build();
    public static final FoodProperties COLD_BREW = (new FoodProperties.Builder())
            .nutrition(8)
            .saturationModifier(0.8F)
            .alwaysEdible()
            .usingConvertsTo(Items.BOWL)
            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1800, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1800, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.HARM, 1, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1800, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.ABSORPTION, 1800, 1), 1.0F)
            .build();
    public static final FoodProperties STATIC_BREW = (new FoodProperties.Builder())
            .nutrition(8)
            .saturationModifier(0.8F)
            .alwaysEdible()
            .usingConvertsTo(Items.BOWL)
            .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1800, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.HARM, 1, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.ABSORPTION, 1800, 1), 1.0F)
            .build();
    public static final FoodProperties DRAGON_BREW = (new FoodProperties.Builder())
            .nutrition(8)
            .saturationModifier(0.8F)
            .alwaysEdible()
            .usingConvertsTo(Items.BOWL)
            .effect(new MobEffectInstance(MobEffects.REGENERATION, 1800, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1800, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1800, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.HARM, 1, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.ABSORPTION, 1800, 1), 1.0F)
            .effect(new MobEffectInstance(MobEffects.WATER_BREATHING, 1800, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 1800, 1), 1.0F)
            .build();

}
