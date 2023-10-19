package club.someoneice.vine.common.item.cocktail;

import club.someoneice.vine.init.ItemInit;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class CocktailBase extends Item {
    protected MobEffect[] effects;

    public CocktailBase(Properties properties, MobEffect... effects) {
        super(properties.stacksTo(1));
        this.effects = effects;
    }

    public UseAnim getUseAnimation(ItemStack item) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack item, Level world, LivingEntity entity) {
        super.finishUsingItem(item, world, entity);
        for (MobEffect effect : effects) entity.addEffect(new MobEffectInstance(effect, 20 * 60 * 2, 0));
        return ItemInit.Goblet.get().getDefaultInstance();
    }
}
