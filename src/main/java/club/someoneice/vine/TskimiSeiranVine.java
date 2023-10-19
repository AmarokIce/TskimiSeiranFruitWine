package club.someoneice.vine;

import club.someoneice.vine.client.gui.GuiBarrel;
import club.someoneice.vine.client.gui.GuiBoiler;
import club.someoneice.vine.client.gui.GuiShaker;
import club.someoneice.vine.core.Data;
import club.someoneice.vine.core.TagHelper;
import club.someoneice.vine.core.VanillaEvent;
import club.someoneice.vine.init.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(TskimiSeiranVine.MODID)
public class TskimiSeiranVine {
    public static final String MODID = "tksrwine";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final boolean isCroptopiaInstalled = ModList.get().isLoaded("croptopia");

    public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {
        @Nonnull @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.Flagon.get());
        }
    };

    public static final CreativeModeTab WINE_TAB = new CreativeModeTab(MODID + ".wine") {
        @Nonnull @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.Cider.cup.get());
        }
    };

    public static final CreativeModeTab COCKTAIL_TAB = new CreativeModeTab(MODID + ".cocktail") {
        @Nonnull @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.NoneCocktail.get());
        }
    };

    public TskimiSeiranVine() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        PotionInit.POTIONS.register(bus);
        ItemInit.init(bus);
        BlockInit.BLOCKS.register(bus);
        TileInit.TILE_ENTITIES.register(bus);
        ContainerInit.CONTAINERS.register(bus);
        RecipeInit.RecipesList.register(bus);

        MinecraftForge.EVENT_BUS.register(new VanillaEvent());
        MinecraftForge.EVENT_BUS.register(this);
        bus.addListener(this::clientSetup);

        // WineData init.
        TagHelper.init();
        Data.init();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(ContainerInit.BARREL_GUI.get(), GuiBarrel::new);
        MenuScreens.register(ContainerInit.BOILER_GUI.get(), GuiBoiler::new);
        MenuScreens.register(ContainerInit.SHAKER_GUI.get(), GuiShaker::new);
    }


}
