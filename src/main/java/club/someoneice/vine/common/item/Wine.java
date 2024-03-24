package club.someoneice.vine.common.item;

import club.someoneice.vine.common.item.group.CreativeModeTabDef;
import club.someoneice.vine.init.ItemInit;
import club.someoneice.vine.util.Utilities;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

public class Wine {
    public RegistryObject<Item> bottle;
    public RegistryObject<Item> cup;
    public RegistryObject<Item> glass;
    public RegistryObject<Item> wineBottle;
    public String name;

    public Wine(String name, int hunger) {
        this.name = "tsfWine." + name;
        wineBottle = ItemInit.registerWithTab(name + "_wine",
                () -> new WineItem(WineEnum.WINE, name, WineItem.propertiesHelper(hunger), ItemInit.WineBottle.get()), CreativeModeTabDef.WINE_TAB);
        bottle = ItemInit.registerWithTab(name + "_bottle",
                () -> new WineItem(WineEnum.BOTTLE, name, WineItem.propertiesHelper(hunger), Items.GLASS_BOTTLE), CreativeModeTabDef.WINE_TAB);
        glass = ItemInit.registerWithTab(name + "_glass",
                () -> new WineItem(WineEnum.GLASS, name, WineItem.propertiesHelper(hunger), ItemInit.GlassBottle.get()), CreativeModeTabDef.WINE_TAB);
        cup = ItemInit.registerWithTab(name + "_cup",
                () -> new WineItem(WineEnum.CUP, name, WineItem.propertiesHelper(hunger), ItemInit.Cup.get()), CreativeModeTabDef.WINE_TAB);
    }

    public ItemStack getItemByContainerOnUse(ItemStack item) {
        if (item.is(ItemInit.WineBottle.get()))     return this.wineBottle.get().getDefaultInstance();
        if (item.is(ItemInit.Cup.get()))            return this.cup.get().getDefaultInstance();
        if (item.is(ItemInit.GlassBottle.get()))    return this.bottle.get().getDefaultInstance();
        if (item.is(Items.GLASS_BOTTLE))            return this.glass.get().getDefaultInstance();

        return null;
    }

    public enum WineEnum {
        BOTTLE,
        CUP,
        GLASS,
        WINE
    }


    public class WineItem extends Item {
        public Item returnItem;
        public String name;
        public WineEnum wineEnum;

        public Wine getWineType() {
            return Wine.this;
        }

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
            if (!world.isClientSide && entity instanceof Player player) {
                world.gameEvent(player, GameEvent.EAT, player.getEyePosition());
                world.playSound(player, player.getX(), player.getY(), player.getZ(), this.getDrinkingSound(), SoundSource.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);

                final FoodProperties properties = item.getItem().getFoodProperties(item, player);
                assert properties != null;

                player.getFoodData().eat(properties.getNutrition(), properties.getSaturationModifier());
                var nbt = item.getOrCreateTag();
                if (!nbt.contains("wine")) nbt.putInt("wine", 4);
                if (!player.isCreative()) nbt.putInt("wine", Math.max(nbt.getInt("wine") - 1, 0));
                if (nbt.getInt("wine") != 0) return item;
                item.shrink(1);
                nbt.putInt("wine", 4);
                Utilities.addItem2PlayerOrDrop(player, new ItemStack(this.returnItem));
            }

            return item;
        }

        @Override
        public int getBarWidth(ItemStack itemStack) {
            if ((((WineItem) itemStack.getItem()).wineEnum == WineEnum.WINE) && itemStack.getOrCreateTag().contains("wine"))
                return Math.round(13.0F - (float) (4 - itemStack.getOrCreateTag().getInt("wine")) * 13 / 4.0F);
            else return -1;
        }

        private static Item.Properties propertiesHelper(int hunger) {
            var properties = new Item.Properties();
            var builder = new FoodProperties.Builder();
            builder.nutrition(hunger);
            builder.saturationMod((float) ((Math.max(hunger - 2, 0)) / 10));
            builder.alwaysEat();
            properties.food(builder.build());
            properties.stacksTo(8);
            return properties;
        }


        @Override
        public void appendHoverText(ItemStack item, @Nullable Level world, List<Component> list, TooltipFlag flag) {
            var tag = item.getOrCreateTag();
            if (this.wineEnum == WineEnum.WINE)
                if (tag.contains("wine"))
                    list.add(Component.translatable("tsfWine.wine_num.message").append(Integer.toString(tag.getInt("wine"))));
        }
    }
}
