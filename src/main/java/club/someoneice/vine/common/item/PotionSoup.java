package club.someoneice.vine.common.item;

import club.someoneice.vine.init.PotionInit;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class PotionSoup extends LingeringPotionItem {

    public PotionSoup() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), PotionInit.tsks_s_soup.get());
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable("message.rebbitsoup.potion"));
    }

    //@Override
    //public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
    //    if (this.allowedIn(tab)) {
    //        list.add(PotionUtils.setPotion(new ItemStack(this), PotionInit.tsks_s_soup.get()));
    //    }
    //}
}
