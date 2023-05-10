package club.someoneice.vine.core;

import club.someoneice.vine.init.BlockInit;
import club.someoneice.vine.init.ItemInit;
import club.someoneice.vine.init.PotionInit;
import club.someoneice.vine.init.TileInit;
import club.someoneice.vine.json.JsonCore;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;

@Mod(TskimiSeiranVine.MODID)
public class TskimiSeiranVine {
    public static final String MODID = "tksrwine";
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

    public static final CreativeModeTab COCKTAIL_TAB = new CreativeModeTab(MODID + ".cocktail") {
        @Nonnull @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.NoneCocktail.get());
        }
    };

    public TskimiSeiranVine() {
        JsonCore.readFromJson();

        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        PotionInit.POTIONS.register(bus);
        ItemInit.init(bus);
        BlockInit.BLOCKS.register(bus);
        TileInit.TILE_ENTITIES.register(bus);

        MinecraftForge.EVENT_BUS.register(new VanillaEvent());
        MinecraftForge.EVENT_BUS.register(this);

        // WineData init.
        new TagHelper();
        Data.init();
    }

    @SubscribeEvent
    public void serverinit(ServerStartedEvent event) {
        // Delay incoming in, or make a crash.
        data();
    }

    private void data() {
        Data.cocktailMap.put(map(
                item(ItemInit.Peach.bottle.get(), 3),
                item(ItemInit.Whiskey.bottle.get(), 3),
                item(ItemInit.Vodka.bottle.get(), 3)
                ),
                ItemInit.TskimiSeiran_S_Mystery.get()
        );

        Data.cocktailMap.put(map(
                item(ItemInit.Peach.bottle.get(), 3),
                item(Items.RABBIT, 2),
                item(Items.CARROT, 2),
                item(ItemInit.Vodka.bottle.get(), 2)),
                ItemInit.TskimiSeiran_SOUP.get()
        );

        if (JsonCore.DATA_LIST == null || JsonCore.DATA_LIST.isEmpty()) return;
        for (var wine : JsonCore.DATA_LIST) {
            List<ItemStack> list = Lists.newArrayList();
            for (var item : wine.materials)
                list.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(item)).getDefaultInstance());

            Data.cocktailMap.put(map(list), ItemInit.COCKTAIL_LIST.get(wine.name).get());
        }
    }

    private DataMap map(ItemStack ... items) {
        var map = new DataMap();
        for (var item : items) map.put(item);
        return map;
    }

    private DataMap map(List<ItemStack> items) {
        var map = new DataMap();
        for (var item : items) map.put(item);
        return map;
    }

    private ItemStack item(Item item, int i) {
        return new ItemStack(item, i);
    }
}
