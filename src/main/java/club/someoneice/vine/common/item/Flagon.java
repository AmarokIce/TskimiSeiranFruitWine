package club.someoneice.vine.common.item;

import club.someoneice.vine.TskimiSeiranVine;
import club.someoneice.vine.util.Utilities;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Flagon extends Item {

    public Flagon() {
        super(new Properties().stacksTo(1).tab(TskimiSeiranVine.TAB));
    }

    @Override
    public int getUseDuration(ItemStack item) {
        return 16;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack item) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack item, Level world, LivingEntity entity) {
        if (!world.isClientSide && entity instanceof Player player) {
            world.gameEvent(player, GameEvent.EAT, player.eyeBlockPosition());
            world.playSound(player, player.getX(), player.getY(), player.getZ(), this.getDrinkingSound(), SoundSource.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
            var nbt = item.getOrCreateTag();
            player.getFoodData().eat(nbt.getInt("hunger"), nbt.getFloat("saturation"));
            nbt.putInt("wine", Math.max(nbt.getInt("wine") - 1, 0));
            if (nbt.getInt("wine") == 0) nbt.remove("wine_name");
        }
        return item;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (world.isClientSide) return InteractionResultHolder.success(itemstack);
        var ahand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        var nbt = itemstack.getOrCreateTag();
        if (nbt.getInt("wine") > 0 && !player.isShiftKeyDown()) {
            player.startUsingItem(hand);
        } else if (player.getItemInHand(ahand).getItem() instanceof Wine.WineItem wine && isSameWine(itemstack, wine)) {
            nbt.putInt("hunger", wine.getFoodProperties().getNutrition());
            nbt.putFloat("saturation", (float) (((wine.getFoodProperties().getNutrition()) - 2) / 10));
            nbt.putInt("wine", Math.min(nbt.getInt("wine") + 1, 4));

            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
            player.getItemInHand(ahand).shrink(1);
            Utilities.addItem2PlayerOrDrop(player, new ItemStack(wine.returnItem));
        }

        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        list.add(new TranslatableComponent("tsfWine.wine_num.message").append(Integer.toString(item.getOrCreateTag().getInt("wine"))));
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return Math.round(13.0F - (float) (4 - itemStack.getOrCreateTag().getInt("wine")) * 13 / 4.0F);
    }

    @Override
    public boolean isBarVisible(ItemStack item) {
        return true;
    }

    private boolean isSameWine(ItemStack item, Wine.WineItem wine) {
        return !item.getOrCreateTag().contains("wine_name") || (item.getOrCreateTag().getString("wine_name").equals(wine.name));
    }
}
