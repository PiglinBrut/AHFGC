package ru.pb.ahfgc.util.Tools;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import ru.pb.ahfgc.screen.skill_tree.SkillTreeScreen;
import ru.pb.ahfgc.util.network.OpenSkillTreeScreenPacket;

public class SkillManuscript extends Item {
    public SkillManuscript(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new OpenSkillTreeScreenPacket(pUsedHand));
        }

        return super.use(level, player, pUsedHand);
    }

    public static void openSkillTreeScreen(InteractionHand hand) {
        Minecraft.getInstance().setScreen(new SkillTreeScreen(Component.empty(), hand));
    }
}
