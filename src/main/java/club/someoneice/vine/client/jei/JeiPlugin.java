package club.someoneice.vine.client.jei;

import club.someoneice.vine.TskimiSeiranVine;
import club.someoneice.vine.init.BlockInit;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(TskimiSeiranVine.MODID, TskimiSeiranVine.MODID + "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var helper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new Brewing(helper));
        registration.addRecipeCategories(new Distillation(helper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(Brewing.TYPE, BrewingRecipe.build());
        registration.addRecipes(Distillation.TYPE, DistillationRecipe.build());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(BlockInit.BrewingBarrelBlock.get().asItem().getDefaultInstance(), Brewing.TYPE);
        registration.addRecipeCatalyst(BlockInit.DistillationBoilerBlock.get().asItem().getDefaultInstance(), Brewing.TYPE);

        registration.addRecipeCatalyst(BlockInit.DistillationBoilerBlock.get().asItem().getDefaultInstance(), Distillation.TYPE);
    }
}
