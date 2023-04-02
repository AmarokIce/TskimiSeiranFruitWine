package club.someoneice.vine.common.item;

import club.someoneice.vine.core.Data;
import club.someoneice.vine.core.TskimiSeiranVine;
import club.someoneice.vine.init.ItemInit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

public class Wine {
    public RegistryObject<Item> bucket;
    public RegistryObject<Item> bottle;
    public RegistryObject<Item> cup;
    public RegistryObject<Item> wineBottle;
    public String name;


    // TODO - CUP and WINEBOTTLE is null!
    public Wine(String name, int hunger) {
        this.name   = "tsfWine." + name;
        bucket      = ItemInit.ITEMS.register(name + "_bucket",      () -> new WineItem(WineEnum.BUCKET, name, WineItem.propertiesHelper(hunger * 4), Items.BUCKET));
        wineBottle  = ItemInit.ITEMS.register(name + "_wine",        () -> new WineItem(WineEnum.WINE, name, WineItem.propertiesHelper(hunger * 4), ItemInit.WineBottle.get()));
        bottle      = ItemInit.ITEMS.register(name + "_bottle",      () -> new WineItem(WineEnum.BOTTLE, name, WineItem.propertiesHelper(hunger), Items.GLASS_BOTTLE));
        cup         = ItemInit.ITEMS.register(name + "_cup",         () -> new WineItem(WineEnum.CUP, name, WineItem.propertiesHelper(hunger), ItemInit.Cup.get()));
        Data.wineMap.put("tsfWine." + name, this);
    }

    public static Wine getWineByItem(WineItem item) {
        return Data.wineMap.get(item.name);
    }

    public enum WineEnum {
        BUCKET,
        BOTTLE,
        CUP,
        WINE
    }


    public class WineItem extends Item {
        public Item returnItem;
        public String name;
        public WineEnum wineEnum;

        public WineItem(WineEnum wine, String name, Properties properties, Item returnItem) {
            super(properties.craftRemainder(returnItem));
            this.returnItem = returnItem;
            this.name = "tsfWine." + name;
            this.wineEnum = wine;
        }

        @Override
        public int getUseDuration(ItemStack item) {
            return 32;
        }

        @Override
        public UseAnim getUseAnimation(ItemStack item) {
            return UseAnim.DRINK;
        }

        @Override
        public ItemStack finishUsingItem(ItemStack item, Level world, LivingEntity entity) {
            super.finishUsingItem(item, world, entity);
            if (entity instanceof Player player) player.addItem(new ItemStack(this.returnItem));
            return item;
        }

        private static Item.Properties propertiesHelper(int hunger) {
            var properties = new Item.Properties();
            var builder = new FoodProperties.Builder();
            builder.nutrition(hunger);
            builder.saturationMod(Math.max(hunger - 2, 0));
            builder.alwaysEat();
            properties.food(builder.build());
            properties.tab(TskimiSeiranVine.WINE_TAB);
            properties.stacksTo(8);
            return properties;
        }
    }
}
