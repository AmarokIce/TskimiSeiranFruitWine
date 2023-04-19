package club.someoneice.vine.core;

import club.someoneice.vine.common.item.Wine;
import club.someoneice.vine.init.ItemInit;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class Data {
    // 酒普
    public static final Map<String, Wine> wineMap = new HashMap<>();

    // 酿造食谱
    public static final Map<Item, Wine> wineItemMap = new HashMap<>();
    public static final Map<TagKey<Item>, Wine> wineTagList = new HashMap<>();

    // 蒸馏食谱
    public static final Map<Item, Wine> distillationItemMap = new HashMap<>();
    public static final Map<TagKey<Item>, Wine> distillationTagList = new HashMap<>();

    // CockTail
    public static final Map<DataMap, Item> cocktailMap = new HashMap<>();

    public static void init() {
        wineItemMap.put(Items.HONEY_BOTTLE, ItemInit.HoneyWine);
        wineItemMap.put(Items.WHEAT, ItemInit.Beer);
        wineItemMap.put(Items.SWEET_BERRIES, ItemInit.BerriesWine);
        wineItemMap.put(Items.GLOW_BERRIES, ItemInit.GlowBerriesWine);
        wineItemMap.put(Items.APPLE, ItemInit.Cider);
        wineItemMap.put(Items.BREAD, ItemInit.Kwas);

        wineTagList.put(TagHelper.Milk, ItemInit.MilkWine);
        wineTagList.put(TagHelper.Cherry, ItemInit.Cherry);
        wineTagList.put(TagHelper.Pineapple, ItemInit.Pineapple);
        wineTagList.put(TagHelper.Lemon, ItemInit.Lemon);
        wineTagList.put(TagHelper.Pear, ItemInit.Pear);
        wineTagList.put(TagHelper.Peach, ItemInit.Peach);
        wineTagList.put(TagHelper.Strawberry, ItemInit.Strawberry);
        wineTagList.put(TagHelper.Coconut, ItemInit.Coconut);
        wineTagList.put(TagHelper.Plum, ItemInit.Plum);
        wineTagList.put(TagHelper.Grape, ItemInit.Grape);

        /*
        wineItemMap.put(Content.CHERRY.m_5456_(), ItemInit.Cherry);
        wineItemMap.put(Content.PINEAPPLE.m_5456_(), ItemInit.Pineapple);
        wineItemMap.put(Content.LEMON.m_5456_(), ItemInit.Lemon);
        wineItemMap.put(Content.PEAR.m_5456_(), ItemInit.Pear);
        wineItemMap.put(Content.PEACH.m_5456_(), ItemInit.Peach);
        wineItemMap.put(Content.STRAWBERRY.m_5456_(), ItemInit.Strawberry);
        wineItemMap.put(Content.COCONUT.m_5456_(), ItemInit.Coconut);
        */

        distillationItemMap.put(Items.SUGAR_CANE, ItemInit.Rum);
        distillationItemMap.put(Items.POTATO, ItemInit.Vodka);
        distillationItemMap.put(Items.WHEAT, ItemInit.Whiskey);

        distillationTagList.put(TagHelper.Rice, ItemInit.Sake);
        distillationTagList.put(TagHelper.Corn, ItemInit.Spirits);
        }
}
