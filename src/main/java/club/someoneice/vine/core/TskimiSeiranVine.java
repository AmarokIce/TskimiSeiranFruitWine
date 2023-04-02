package club.someoneice.vine.core;

import club.someoneice.vine.init.BlockInit;
import club.someoneice.vine.init.ItemInit;
import club.someoneice.vine.init.TileInit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(TskimiSeiranVine.MODID)
public class TskimiSeiranVine {
    public static final String MODID = "tskimi_seiran_fruit_vine";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final boolean isCroptopiaInstall = FMLLoader.getLoadingModList().getModFileById("croptopia") != null;

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

    public static final CreativeModeTab COOKTAIL_TAB = new CreativeModeTab(MODID + ".cocktail") {
        @Nonnull @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.Cider.cup.get());
        } // TODO
    };

    public TskimiSeiranVine() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.init(bus);
        BlockInit.BLOCKS.register(bus);
        TileInit.TILE_ENTITIES.register(bus);

        MinecraftForge.EVENT_BUS.register(new VanillaEvent());
        MinecraftForge.EVENT_BUS.register(this);

        // Data init.
        new TagHelper();
        Data.init();
    }
}
