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

public class Distillation implements IRecipeCategory<DistillationRecipe> {
    public static final RecipeType<DistillationRecipe> TYPE = RecipeType.create(TskimiSeiranVine.MODID, "distillation", DistillationRecipe.class);
    private final IGuiHelper helper;

    public Distillation(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public RecipeType<DistillationRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(String.format("%s.%s", TskimiSeiranVine.MODID, "distillation"));
    }

    @Override
    public IDrawable getBackground() {
        return helper.createDrawable(new ResourceLocation(TskimiSeiranVine.MODID, "textures/gui/jei/gui_boiler.png"), 0, 0, 144, 64);
    }

    @Override
    public IDrawable getIcon() {
        return helper.createDrawableItemStack(BlockInit.DistillationBoilerBlock.get().asItem().getDefaultInstance());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DistillationRecipe recipe, IFocusGroup group) {
        builder.addSlot(RecipeIngredientRole.INPUT, 39, 14).addIngredients(recipe.input);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 14).addIngredients(recipe.output);
    }
}
