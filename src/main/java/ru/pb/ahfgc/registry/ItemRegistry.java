package ru.pb.ahfgc.registry;

import com.teamremastered.endrem.item.EREnderEye;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.pb.ahfgc.AHFGCMod;
import ru.pb.ahfgc.util.Food;
import ru.pb.ahfgc.util.itemtools.CursedHorn;
import ru.pb.ahfgc.util.itemtools.HydraEssence;
import ru.pb.ahfgc.util.itemtools.HydraPoison;
import ru.pb.ahfgc.util.itemtools.LibraryDoorItem;


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
    public static final DeferredItem<Item> DRAGON_EYE = EYES.register("dragon_eye", () -> new EREnderEye(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BURNING_BREW = ITEMS.register("burning_brew", () -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(Food.BURNING_BREW).stacksTo(16)));
    public static final DeferredItem<Item> COLD_BREW = ITEMS.register("cold_brew", () -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(Food.COLD_BREW).stacksTo(16)));
    public static final DeferredItem<Item> STATIC_BREW = ITEMS.register("static_brew", () -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(Food.STATIC_BREW).stacksTo(16)));
    public static final DeferredItem<Item> HYDRA_ESSENCE = ITEMS.register("hydra_essence", () -> new HydraEssence(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DRAGON_BREW = ITEMS.register("dragon_brew", () -> new Item(new Item.Properties().craftRemainder(Items.BOWL).food(Food.DRAGON_BREW).stacksTo(16)));
    public static final DeferredItem<Item> HYDRA_POISON = ITEMS.register("hydra_poison", () -> new HydraPoison(new Item.Properties().stacksTo(1)));

    // ==== Sun Eye ====
    public static final DeferredItem<Item> SUN_EYE = EYES.register("sun_eye", () -> new EREnderEye(new Item.Properties().stacksTo(1)));

    // ==== Cursed Eye ====
    public static final DeferredItem<Item> CURSED_HORN = ITEMS.register("cursed_horn", () -> new CursedHorn(new Item.Properties().stacksTo(1)));

    // ==== Pumpkin Eye ====
    public static final DeferredItem<Item> PUMPKIN_EYE = EYES.register("pumpkin_eye", () -> new EREnderEye(new Item.Properties().stacksTo(1)));

    // ==== Life Eye ====
    public static final DeferredItem<Item> LIFE_EYE = EYES.register("life_eye", () -> new EREnderEye(new Item.Properties().stacksTo(1)));

    // ==== Power Eye ====
    public static final DeferredItem<Item> POWER_EYE = EYES.register("power_eye", () -> new EREnderEye(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<LibraryDoorItem> LIBRARY_DOOR =
            ITEMS.registerItem("library_door",
                    props -> new LibraryDoorItem(BlockRegistry.LIBRARY_DOOR.get(), props),
                    new Item.Properties().stacksTo(64)
            );
}
