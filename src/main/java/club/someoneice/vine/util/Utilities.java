package club.someoneice.vine.util;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Utilities {
    public static void addItem2PlayerOrDrop(Player player, ItemStack stack) {
        if (player.addItem(stack)) return;
        final Level level = player.level();
        if (level.isClientSide) return;
        final ItemEntity e = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), stack);
        level.addFreshEntity(e);
    }
}
