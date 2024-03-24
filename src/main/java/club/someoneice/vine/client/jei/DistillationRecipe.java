package club.someoneice.vine.client.jei;

import club.someoneice.vine.core.Data;
import com.google.common.collect.Lists;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class DistillationRecipe {
    Ingredient input;
    Ingredient output;

    public DistillationRecipe(Ingredient input, Ingredient output) {
        this.input = input;
        this.output = output;
    }

    public static List<DistillationRecipe> build() {
        final List<DistillationRecipe> recipes = Lists.newArrayList();
        Data.distillationItemMap.forEach((key, value) -> recipes.add(new DistillationRecipe(Ingredient.of(key), Ingredient.of(new ItemStack(value.wineBottle.get(), 8)))));
        Data.distillationTagList.forEach((key, value) -> recipes.add(new DistillationRecipe(Ingredient.of(key), Ingredient.of(new ItemStack(value.wineBottle.get(), 8)))));
        return recipes;
    }
}
