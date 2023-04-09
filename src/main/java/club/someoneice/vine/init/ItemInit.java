package club.someoneice.vine.init;

import club.someoneice.vine.common.shaker.CocktailBase;
import club.someoneice.vine.common.item.Flagon;
import club.someoneice.vine.common.item.Wine;
import club.someoneice.vine.common.shaker.Cocktail;
import club.someoneice.vine.common.shaker.ShakerItem;
import club.someoneice.vine.core.TskimiSeiranVine;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TskimiSeiranVine.MODID);

    public static RegistryObject<Item> Shaker = ITEMS.register("shaker", ShakerItem::new);
    public static RegistryObject<Item> BrewingBarrelItem = ITEMS.register("brewing_barrel_item", () -> new BlockItem(BlockInit.BrewingBarrelBlock.get(), new Item.Properties().tab(TskimiSeiranVine.TAB)));
    public static RegistryObject<Item> DistillationBoilerItem = ITEMS.register("distillation_boiler_item", () -> new BlockItem(BlockInit.DistillationBoilerBlock.get(), new Item.Properties().tab(TskimiSeiranVine.TAB)));
    public static RegistryObject<Item> Goblet = ITEMS.register("goblet_item", () -> new BlockItem(BlockInit.GobletBlock.get(), new Item.Properties().tab(TskimiSeiranVine.TAB)));

    public static RegistryObject<Item> Flagon = ITEMS.register("flagon", Flagon::new);
    public static RegistryObject<Item> Gourd = ITEMS.register("gourd", Flagon::new);
    public static RegistryObject<Item> Cup = ITEMS.register("cup", () -> new Item(new Item.Properties().tab(TskimiSeiranVine.TAB)));
    public static RegistryObject<Item> WineBottle = ITEMS.register("wine_bottle", () -> new Item(new Item.Properties().tab(TskimiSeiranVine.TAB)));

    // 酿造
    public static Wine Cider = new Wine("apple_cider",3);
    public static Wine MilkWine = new Wine("milk_wine",3);
    public static Wine HoneyWine = new Wine("honey_wine",3);
    public static Wine BerriesWine = new Wine("berries_wine",3);
    public static Wine GlowBerriesWine = new Wine("glow_berries_wine",3);
    public static Wine Beer = new Wine("beer",3);
    public static Wine Cherry = new Wine("cherry",2);
    public static Wine Peach = new Wine("peach",2);
    public static Wine Pineapple = new Wine("pineapple",2);
    public static Wine Lemon = new Wine("lemon",2);
    public static Wine Coconut = new Wine("coconut",2);
    public static Wine Pear = new Wine("pear",2);
    public static Wine Strawberry = new Wine("strawberry",2);

    // 蒸馏
    public static Wine Vodka = new Wine("vodka", 4);
    public static Wine Whiskey = new Wine("whiskey", 4);
    public static Wine Rum = new Wine("rum", 3);
    public static Wine Kwas = new Wine("kwas", 3);

    public static RegistryObject<Item> NoneCocktail = ITEMS.register("none_cocktail_item", () -> new Cocktail.CocktailItem((Cocktail.CocktailBlock) BlockInit.NoneCocktail.get()));
    public static RegistryObject<Item> TskimiSeiran_S_Mystery = ITEMS.register("tskimi_seiran_mystery", () -> itemCocktailBase(10, MobEffects.JUMP, MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED));

    private static Item itemCocktailBase(int hunger, MobEffect... effects) {
        Item.Properties properties = new Item.Properties();
        FoodProperties.Builder builder = new FoodProperties.Builder();
        builder.nutrition(hunger);
        builder.saturationMod((float) (hunger - 2));
        builder.alwaysEat();
        properties.tab(TskimiSeiranVine.COCKTAIL_TAB).stacksTo(1).food(builder.build());
        return new CocktailBase(properties, effects);
    }

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
    }
}
