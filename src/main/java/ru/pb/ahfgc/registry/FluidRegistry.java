package ru.pb.ahfgc.registry;

import com.simibubi.create.AllFluids;
import io.redspace.ironsspellbooks.fluids.NoopFluid;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import ru.pb.ahfgc.AHFGCMod;

import java.util.Objects;
import java.util.function.Supplier;

public class FluidRegistry extends AllFluids{

    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, AHFGCMod.MOD_ID);
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, AHFGCMod.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> DRAGON_BREW_TYPE;
//    public static final DeferredHolder<FluidType, FluidType> BURNING_BREW_TYPE;
//    public static final DeferredHolder<FluidType, FluidType> COLD_BREW_TYPE;
//    public static final DeferredHolder<FluidType, FluidType> STATIC_BREW_TYPE;
    public static final DeferredHolder<FluidType, FluidType> HYDRA_ESSENCE_TYPE;
    public static final DeferredHolder<FluidType, FluidType> HYDRA_POISON_TYPE;

    public static final DeferredHolder<Fluid, NoopFluid> DRAGON_BREW;
//    public static final DeferredHolder<Fluid, NoopFluid> BURNING_BREW;
//    public static final DeferredHolder<Fluid, NoopFluid> COLD_BREW;
//    public static final DeferredHolder<Fluid, NoopFluid> STATIC_BREW;
    public static final DeferredHolder<Fluid, NoopFluid> HYDRA_ESSENCE;
    public static final DeferredHolder<Fluid, NoopFluid> HYDRA_POISON;

    public FluidRegistry() {
    }

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }

    private static DeferredHolder<Fluid, NoopFluid> registerFluid(String name, Supplier<FluidType> fluidType) {
        DeferredHolder<Fluid, NoopFluid> holder = DeferredHolder.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(AHFGCMod.MOD_ID, name));
        Objects.requireNonNull(holder);
        Supplier var10003 = holder::value;
        Objects.requireNonNull(holder);
        BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(fluidType, var10003, holder::value).bucket(() -> Items.AIR);
        FLUIDS.register(name, () -> new NoopFluid(properties));
        return holder;
    }

    static {
        DRAGON_BREW_TYPE = FLUID_TYPES.register("dragon_brew", () -> new
                FluidType(FluidType.Properties.create()));
//        BURNING_BREW_TYPE = FLUID_TYPES.register("burning_brew_type", () -> new
//                FluidType(FluidType.Properties.create()));
//        COLD_BREW_TYPE = FLUID_TYPES.register("cold_brew_type", () -> new
//                FluidType(FluidType.Properties.create()));
//        STATIC_BREW_TYPE = FLUID_TYPES.register("static_brew_type", () -> new
//                FluidType(FluidType.Properties.create()));
        HYDRA_ESSENCE_TYPE = FLUID_TYPES.register("hydra_essence", () -> new
                FluidType(FluidType.Properties.create()));
        HYDRA_POISON_TYPE = FLUID_TYPES.register("hydra_poison", () -> new
                FluidType(FluidType.Properties.create()));

        DeferredHolder<FluidType, FluidType> var10001 = DRAGON_BREW_TYPE;
        Objects.requireNonNull(var10001);
        DRAGON_BREW = registerFluid("dragon_brew", var10001::value);
//        var10001 = BURNING_BREW_TYPE;
//        Objects.requireNonNull(var10001);
//        BURNING_BREW = registerFluid("burning_brew", var10001::value);
//        var10001 = COLD_BREW_TYPE;
//        Objects.requireNonNull(var10001);
//        COLD_BREW = registerFluid("cold_brew", var10001::value);
//        var10001 = STATIC_BREW_TYPE;
//        Objects.requireNonNull(var10001);
//        STATIC_BREW = registerFluid("static_brew", var10001::value);
        var10001 = HYDRA_ESSENCE_TYPE;
        Objects.requireNonNull(var10001);
        HYDRA_ESSENCE = registerFluid("hydra_essence", var10001::value);
        var10001 = HYDRA_POISON_TYPE;
        Objects.requireNonNull(var10001);
        HYDRA_POISON = registerFluid("hydra_poison", var10001::value);
    }
}
