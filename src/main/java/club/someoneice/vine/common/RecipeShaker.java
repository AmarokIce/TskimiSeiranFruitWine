package club.someoneice.vine.common;

import club.someoneice.vine.TskimiSeiranVine;
import club.someoneice.vine.init.RecipeInit;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class RecipeShaker implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final int shaking;
    public static final String NAME = "shaker";

    public RecipeShaker(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, int shaking) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.shaking = shaking;
    }

    @Override
    public boolean matches(SimpleContainer container, Level world) {
        List<ItemStack> itemList = Lists.newArrayList();
        for (int o = 0; o < 12; o ++)
            itemList.add(container.getItem(o).copy());

        for (int i = 0; i < recipeItems.size(); i ++) {
            boolean pass = false;
            for (int o = 0; o < 12; o ++) {
                var item = itemList.get(o);
                if (this.recipeItems.get(i).test(item)) {
                    itemList.get(o).shrink(1);
                    pass = true;
                    break;
                }
            }

            if (!pass) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer container) {
        List<ItemStack> itemList = Lists.newArrayList();
        for (int o = 0; o < 12; o ++)
            itemList.add(container.getItem(o));

        for (int i = 0; i < recipeItems.size(); i ++) {
            boolean pass = false;
            for (int o = 0; o < 12; o ++) {
                var item = itemList.get(o);
                if (this.recipeItems.get(i).test(item)) {
                    itemList.get(o).shrink(1);
                    pass = true;
                    break;
                }
            }

            if (!pass) return ItemStack.EMPTY;
        }
        return this.output;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.SHAKER_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public int getShaking() {
        return this.shaking;
    }

    public static class Type implements RecipeType<RecipeShaker> {
        private Type() {}
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<RecipeShaker> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(TskimiSeiranVine.MODID, RecipeShaker.NAME);

        @Override
        public RecipeShaker fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            int shaking = GsonHelper.getAsInt(json, "shake_count");

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) inputs.set(i, Ingredient.fromJson(ingredients.get(i)));

            return new RecipeShaker(id, output, inputs, shaking);
        }

        @Override
        public RecipeShaker fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            inputs.replaceAll(it -> Ingredient.fromNetwork(buf));
            ItemStack output = buf.readItem();

            return new RecipeShaker(id, output, inputs, 8);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RecipeShaker recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) ing.toNetwork(buf);

            buf.writeItemStack(recipe.getResultItem(), false);
        }

        @SuppressWarnings("unchecked")
        private static <G> Class<G> castClass() {
            return (Class<G>) RecipeSerializer.class;
        }
    }
}