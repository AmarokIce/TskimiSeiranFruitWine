package club.someoneice.vine.init;

import club.someoneice.vine.TskimiSeiranVine;
import club.someoneice.vine.common.item.Flagon;
import club.someoneice.vine.common.item.PotionSoup;
import club.someoneice.vine.common.item.ShakerItem;
import club.someoneice.vine.common.item.Wine;
import club.someoneice.vine.common.item.cocktail.Cocktail;
import club.someoneice.vine.common.item.cocktail.CocktailBase;
import club.someoneice.vine.common.item.group.CreativeModeTabDef;
import com.google.common.collect.Maps;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

public class ItemInit {
    public static final Map<String, RegistryObject<Item>> COCKTAIL_LIST = Maps.newHashMap();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TskimiSeiranVine.MODID);

    public static RegistryObject<Item> Shaker = registerWithTab("shaker", ShakerItem::new, CreativeModeTabDef.TAB);
    public static RegistryObject<Item> BrewingBarrelItem = registerWithTab("brewing_barrel_item", () -> new BlockItem(BlockInit.BrewingBarrelBlock.get(), new Item.Properties()), CreativeModeTabDef.TAB);
    public static RegistryObject<Item> DistillationBoilerItem = registerWithTab("distillation_boiler_item", () -> new BlockItem(BlockInit.DistillationBoilerBlock.get(), new Item.Properties()), CreativeModeTabDef.TAB);
    public static RegistryObject<Item> Goblet = registerWithTab("goblet_item", () -> new BlockItem(BlockInit.GobletBlock.get(), new Item.Properties()), CreativeModeTabDef.TAB);

    public static RegistryObject<Item> Flagon = registerWithTab("flagon", Flagon::new, CreativeModeTabDef.TAB);
    public static RegistryObject<Item> Gourd = registerWithTab("gourd", Flagon::new, CreativeModeTabDef.TAB);
    public static RegistryObject<Item> Cup = registerWithTab("cup", () -> new Item(new Item.Properties()), CreativeModeTabDef.TAB);
    public static RegistryObject<Item> GlassBottle = registerWithTab("glass_bottle", () -> new Item(new Item.Properties()), CreativeModeTabDef.TAB);
    public static RegistryObject<Item> WineBottle = registerWithTab("wine_bottle", () -> new Item(new Item.Properties()), CreativeModeTabDef.TAB);

    // 酿造
    public static Wine Cider = new Wine("apple_cider", 3);
    public static Wine MilkWine = new Wine("milk_wine", 3);
    public static Wine HoneyWine = new Wine("honey_wine", 3);
    public static Wine BerriesWine = new Wine("berries_wine", 3);
    public static Wine GlowBerriesWine = new Wine("glow_berries_wine", 3);
    public static Wine Beer = new Wine("beer", 3);
    public static Wine Cherry = new Wine("cherry", 2);
    public static Wine Peach = new Wine("peach", 2);
    public static Wine Pineapple = new Wine("pineapple", 2);
    public static Wine Lemon = new Wine("lemon", 2);
    public static Wine Coconut = new Wine("coconut", 2);
    public static Wine Pear = new Wine("pear", 2);
    public static Wine Strawberry = new Wine("strawberry", 2);

    // 蒸馏
    public static Wine Vodka = new Wine("vodka", 4);
    public static Wine Whiskey = new Wine("whiskey", 4);
    public static Wine Rum = new Wine("rum", 3);
    public static Wine Kwas = new Wine("kwas", 3);
    public static Wine Spirits = new Wine("spirits", 5);
    public static Wine Grape = new Wine("grape", 3);
    public static Wine Plum = new Wine("plum", 3);
    public static Wine Sake = new Wine("sake", 3);


    public static RegistryObject<Item> NoneCocktail = registerWithTab("none_cocktail_item", () -> new Cocktail.CocktailItem((Cocktail.CocktailBlock) BlockInit.NoneCocktail.get()), CreativeModeTabDef.COCKTAIL_TAB);
    public static RegistryObject<Item> TskimiSeiran_S_Mystery = ITEMS.register("tskimi_seiran_mystery", () -> itemCocktailBase(10, MobEffects.JUMP, MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED));
    public static RegistryObject<Item> TskimiSeiran_SOUP = ITEMS.register("tskimi_seiran_soup", PotionSoup::new);

    public static RegistryObject<Item> registerWithTab(String id, Supplier<Item> sup, CreativeModeTabDef def) {
        final RegistryObject<Item> reg = ITEMS.register(id, sup);
        def.addIntoTab(() -> reg.get().getDefaultInstance());
        return reg;
    }

    private static Item itemCocktailBase(int hunger, MobEffect... effects) {
        Item.Properties properties = new Item.Properties();
        FoodProperties.Builder builder = new FoodProperties.Builder();
        builder.nutrition(hunger);
        builder.saturationMod((hunger - 2) / 10.0F);
        builder.alwaysEat();
        properties.stacksTo(1).food(builder.build());
        final Item cocktail = new CocktailBase(properties, effects);
        CreativeModeTabDef.COCKTAIL_TAB.addIntoTab(cocktail::getDefaultInstance);
        return cocktail;
    }

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
    }
}
