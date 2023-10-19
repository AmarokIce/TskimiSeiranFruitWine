package club.someoneice.vine.init;

import club.someoneice.vine.TskimiSeiranVine;
import club.someoneice.vine.common.RecipeShaker;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit {
    public static final DeferredRegister<RecipeSerializer<?>> RecipesList = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TskimiSeiranVine.MODID);

    public static final RegistryObject<RecipeSerializer<RecipeShaker>> SHAKER_RECIPE = RecipesList.register(RecipeShaker.NAME, () -> RecipeShaker.Serializer.INSTANCE);
}
