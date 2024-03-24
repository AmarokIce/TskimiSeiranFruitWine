package club.someoneice.vine.client.jei;

import club.someoneice.vine.TskimiSeiranVine;
import club.someoneice.vine.init.BlockInit;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Brewing implements IRecipeCategory<BrewingRecipe> {
    public static final RecipeType<BrewingRecipe> TYPE = RecipeType.create(TskimiSeiranVine.MODID, "brewing", BrewingRecipe.class);
    private final IGuiHelper helper;

    public Brewing(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public RecipeType<BrewingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(String.format("%s.%s", TskimiSeiranVine.MODID, "brewing"));
    }

    @Override
    public IDrawable getBackground() {
        return helper.createDrawable(new ResourceLocation(TskimiSeiranVine.MODID, "textures/gui/jei/gui_barrel.png"), 0, 0, 144, 64);
    }

    @Override
    public IDrawable getIcon() {
        return helper.createDrawableItemStack(BlockInit.BrewingBarrelBlock.get().asItem().getDefaultInstance());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BrewingRecipe recipe, IFocusGroup group) {
        builder.addSlot(RecipeIngredientRole.INPUT, 39, 14).addIngredients(recipe.input);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 14).addIngredients(recipe.output);
    }
}
