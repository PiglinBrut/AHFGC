package ru.pb.ahfgc.util.Tools;

import net.mcreator.borninchaosv.init.BornInChaosV1ModSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.Random;

public class HydraEssence extends Item {
    private static final Random RANDOM = new Random();
    private static final int DRINK_DURATION = 32;
    public static final int EFFECT_DURATION = 2000;
    public static final int AMPLIFIER = 4;

    public HydraEssence(Properties properties) {
        super(properties);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            level.playSound((Player)null, entity.blockPosition(), BornInChaosV1ModSounds.PUMPKIN_STAFF_SHOOT.get(), entity.getSoundSource(), 1.0F, 1.0F);
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, EFFECT_DURATION + (int)((RANDOM.nextDouble() - 0.2)*200), AMPLIFIER, false, false, true));
            entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, EFFECT_DURATION + (int)((RANDOM.nextDouble() - 0.2)*200), AMPLIFIER, false, false, true));
            entity.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 0, false, false, true));
        }

        stack.consume(1, entity);
        return stack;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return DRINK_DURATION;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

//    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
//        super.appendHoverText(stack, context, components, flag);
//        List<MobEffectInstance> list = List.of(new MobEffectInstance(MobEffects.POISON, EFFECT_DURATION, AMPLIFIER, false, false, true));
//        Objects.requireNonNull(components);
//        PotionContents.addPotionTooltip(list, components::add, 1.0F, context.tickRate());
//    }
}
