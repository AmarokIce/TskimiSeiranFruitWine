package club.someoneice.vine.common.item.group;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public enum CreativeModeTabDef {
    TAB,
    WINE_TAB,
    COCKTAIL_TAB;

    private static Map<CreativeModeTabDef, Set<Supplier<ItemStack>>> itemsInTab = Maps.newHashMap();

    public void addIntoTab(Supplier<ItemStack> stack) {
        if (!itemsInTab.containsKey(this)) itemsInTab.put(this, Sets.newHashSet());
        itemsInTab.get(this).add(stack);
    }

    public static List<ItemStack> getItemsInTab(CreativeModeTabDef def) {
        return ImmutableSet.copyOf(Objects.requireNonNullElseGet(itemsInTab.get(def), ImmutableSet::of)).stream().map(Supplier::get).toList();
    }
}
