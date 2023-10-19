package club.someoneice.vine.init;

import club.someoneice.vine.TskimiSeiranVine;
import club.someoneice.vine.common.container.ContainerBarrel;
import club.someoneice.vine.common.container.ContainerBoilers;
import club.someoneice.vine.common.container.ContainerShaker;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TskimiSeiranVine.MODID);

    public static final RegistryObject<MenuType<ContainerBarrel>> BARREL_GUI = CONTAINERS.register("barrel_gui", () -> IForgeMenuType.create(ContainerBarrel::new));
    public static final RegistryObject<MenuType<ContainerBoilers>> BOILER_GUI = CONTAINERS.register("boiler_gui", () -> IForgeMenuType.create(ContainerBoilers::new));
    public static final RegistryObject<MenuType<ContainerShaker>> SHAKER_GUI = CONTAINERS.register("shaker_gui", () -> IForgeMenuType.create(ContainerShaker::new));

}
