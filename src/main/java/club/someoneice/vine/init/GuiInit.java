package club.someoneice.vine.init;

import club.someoneice.vine.common.gui.ContainerBarrel;
import club.someoneice.vine.common.gui.ContainerBoilers;
import club.someoneice.vine.common.gui.ContainerShaker;
import club.someoneice.vine.core.TskimiSeiranVine;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GuiInit {
    public static final DeferredRegister<MenuType<?>> GuiList = DeferredRegister.create(ForgeRegistries.CONTAINERS, TskimiSeiranVine.MODID);

    public static final RegistryObject<MenuType<ContainerBarrel>> BARREL_GUI = GuiList.register("barrel_gui", () -> IForgeMenuType.create(ContainerBarrel::new));
    public static final RegistryObject<MenuType<ContainerBoilers>> BOILER_GUI = GuiList.register("boiler_gui", () -> IForgeMenuType.create(ContainerBoilers::new));
    public static final RegistryObject<MenuType<ContainerShaker>> SHAKER_GUI = GuiList.register("shaker_gui", () -> IForgeMenuType.create(ContainerShaker::new));

}
