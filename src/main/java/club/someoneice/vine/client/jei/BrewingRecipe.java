package club.someoneice.vine.client.jei;

import club.someoneice.vine.core.Data;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class BrewingRecipe {
    Ingredient input;
    Ingredient output;

    public BrewingRecipe(Ingredient input, Ingredient output) {
        this.input = input;
        this.output = output;
    }

    public static List<BrewingRecipe> build() {
        final List<BrewingRecipe> recipes = Lists.newArrayList();
        Data.wineItemMap.forEach((key, value) -> recipes.add(new BrewingRecipe(Ingredient.of(key), Ingredient.of(new ItemStack(value.wineBottle.get(), 8)))));
        Data.wineTagList.forEach((key, value) -> recipes.add(new BrewingRecipe(Ingredient.of(key), Ingredient.of(new ItemStack(value.wineBottle.get(), 8)))));
        return recipes;
    }
}
