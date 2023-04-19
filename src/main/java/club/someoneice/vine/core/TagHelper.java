package club.someoneice.vine.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagHelper {
    public static final TagKey<Item> Strawberry = bind("fruits/strawberry");
    public static final TagKey<Item> Peach =      bind("fruits/peach");
    public static final TagKey<Item> Pear =       bind("fruits/pear");
    public static final TagKey<Item> Coconut =    bind("fruits/coconut");
    public static final TagKey<Item> Lemon =      bind("fruits/lemon");
    public static final TagKey<Item> Cherry =     bind("fruits/cherry");
    public static final TagKey<Item> Pineapple =  bind("fruits/pineapple");
    public static final TagKey<Item> Milk =       bind("milk");
    public static final TagKey<Item> Corn =       bind("crops/corn");
    public static final TagKey<Item> Plum =       bind("fruits/plum");
    public static final TagKey<Item> Grape =       bind("fruits/grape");
    public static final TagKey<Item> Rice =       bind("crops/rice");

    public TagHelper() {};

    private static TagKey<Item> bind(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", name));
    }
}
