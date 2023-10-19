package club.someoneice.vine.init;

import club.someoneice.vine.TskimiSeiranVine;
import club.someoneice.vine.common.RecipeShaker;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TskimiSeiranVine.MODID);

    public static final RegistryObject<RecipeSerializer<RecipeShaker>> SHAKER_RECIPE = RECIPES.register(RecipeShaker.NAME, () -> RecipeShaker.Serializer.INSTANCE);
}
