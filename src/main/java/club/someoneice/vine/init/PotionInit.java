package club.someoneice.vine.init;

import club.someoneice.vine.TskimiSeiranVine;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionInit {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, TskimiSeiranVine.MODID);

    public static final RegistryObject<Potion> tsks_s_soup = POTIONS.register("tsks", () -> new Potion(
            new MobEffectInstance(MobEffects.WEAKNESS, 20 * 30, 0),
            new MobEffectInstance(MobEffects.CONFUSION, 20 * 30, 0),
            new MobEffectInstance(MobEffects.REGENERATION, 20 * 30, 0)
    ));
}
