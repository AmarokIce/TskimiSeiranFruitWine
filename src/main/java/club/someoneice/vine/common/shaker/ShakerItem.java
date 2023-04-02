package club.someoneice.vine.common.shaker;

import club.someoneice.vine.core.Data;
import club.someoneice.vine.core.DataList;
import club.someoneice.vine.core.TskimiSeiranVine;
import club.someoneice.vine.init.BlockInit;
import club.someoneice.vine.init.ItemInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.UUID;

public class ShakerItem extends BlockItem {
    public ShakerItem() {
        super(BlockInit.ShakerBlock.get(), new Item.Properties().tab(TskimiSeiranVine.TAB));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var nbt = context.getItemInHand().getOrCreateTag();
        if (context.getPlayer().isShiftKeyDown()) {
            if (!nbt.contains("shaker")) return super.useOn(context);
            context.getLevel().playSound(null, context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), SoundEvents.HONEY_DRINK, SoundSource.NEUTRAL, 1.0F, 1.0F);
            nbt.putInt("shake_time", nbt.getInt("shake_time") + 1);
            if (nbt.getInt("shake_time") == 8) {
                context.getLevel().playSound(null, context.getPlayer().getX(), context.getPlayer().getY(), context.getPlayer().getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1.0F, 1.0F);
                var tag = nbt.getCompound("shaker");
                DataList data = new DataList();
                data.read(tag);
                boolean hasCocktail = false;
                for (DataList list : Data.cocktailMap.keySet()) if (data.isSame(list)) {
                    tag.put("cocktail", new ItemStack(Data.cocktailMap.get(list)).save(new CompoundTag()));
                    hasCocktail = true;
                    break;
                }
                if (!hasCocktail)
                    tag.put("cocktail", new ItemStack(ItemInit.NoneCocktail.get()).save(new CompoundTag()));

                nbt.put("shaker", tag);
            }

            return InteractionResult.SUCCESS;
        }

        if (context.getLevel().getBlockState(context.getClickedPos()).is(BlockInit.GobletBlock.get())) {
            if (!nbt.contains("shaker") || ItemStack.of(nbt.getCompound("shaker").getCompound("cocktail")).sameItem(ItemStack.EMPTY))
                return InteractionResult.FAIL;

            var block = ((Cocktail.CocktailItem) ItemStack.of(nbt.getCompound("shaker").getCompound("cocktail")).getItem()).getBlock();
            context.getLevel().setBlock(context.getClickedPos(), block.defaultBlockState(), 0);
            context.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, context.getClickedPos());
            nbt.remove("shaker");
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        var item = player.getItemInHand(hand);
        var nbt = item.getOrCreateTag();
        if (player.isShiftKeyDown() && nbt.contains("shaker")) {
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.HONEY_DRINK, SoundSource.NEUTRAL, 1.0F, 1.0F);
            nbt.putInt("shake_time", nbt.getInt("shake_time") + 1);
            if (nbt.getInt("shake_time") == 8) {
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1.0F, 1.0F);
                var tag = nbt.getCompound("shaker");
                DataList data = new DataList();
                data.read(tag);
                boolean hasCocktail = false;
                for (DataList list : Data.cocktailMap.keySet()) if (data.isSame(list)) {
                    tag.put("cocktail", new ItemStack(Data.cocktailMap.get(list)).save(new CompoundTag()));
                    hasCocktail = true;
                    break;
                }
                if (!hasCocktail)
                    tag.put("cocktail", new ItemStack(ItemInit.NoneCocktail.get()).save(new CompoundTag()));
            }
        }
        return InteractionResultHolder.success(item);
    }
}
