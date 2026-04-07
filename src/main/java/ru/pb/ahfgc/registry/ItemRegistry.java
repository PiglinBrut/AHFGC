package ru.pb.ahfgc.registry;

import com.teamremastered.endrem.item.EREnderEye;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.pb.ahfgc.AHFGCMod;
import ru.pb.ahfgc.util.Food;
import ru.pb.ahfgc.util.tools.CursedHorn;
import ru.pb.ahfgc.util.tools.HydraEssence;
import ru.pb.ahfgc.util.tools.HydraPoison;


public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AHFGCMod.MOD_ID);
    public static final DeferredRegister.Items EYES = DeferredRegister.createItems("endrem");
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        EYES.register(eventBus);
    }

    // ==== Ocean Eye ====
    public static final DeferredItem<Item> ESSENCE_OF_THE_SEA = ITEMS.register("essence_of_the_sea", () -> new Item(new Item.Properties().stacksTo(64)));

    // ==== Dragon Eye ====
    public static final DeferredItem<Item> BURNING_BREW = ITEMS.register("burning_brew", () -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(Food.BURNING_BREW).stacksTo(16)));
    public static final DeferredItem<Item> COLD_BREW = ITEMS.register("cold_brew", () -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(Food.COLD_BREW).stacksTo(16)));
    public static final DeferredItem<Item> STATIC_BREW = ITEMS.register("static_brew", () -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(Food.STATIC_BREW).stacksTo(16)));
    public static final DeferredItem<Item> HYDRA_ESSENCE = ITEMS.register("hydra_essence", () -> new HydraEssence(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DRAGON_BREW = ITEMS.register("dragon_brew", () -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(Food.DRAGON_BREW).stacksTo(16)));
    public static final DeferredItem<HydraPoison> HYDRA_POISON = ITEMS.register("hydra_poison", () -> new HydraPoison(new Item.Properties().stacksTo(1)));

    // ==== Sun Eye ====
    public static final DeferredItem<Item> SUN_EYE = EYES.register("sun_eye", () -> new EREnderEye(new Item.Properties().stacksTo(1)));

    // ==== Cursed Eye ====
    public static final DeferredItem<Item> CURSED_HORN = ITEMS.register("cursed_horn", () -> new CursedHorn(new Item.Properties().stacksTo(1)));

}
