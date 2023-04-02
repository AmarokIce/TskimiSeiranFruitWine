package club.someoneice.vine.common.shaker;

import club.someoneice.vine.common.item.Wine;
import club.someoneice.vine.core.DataList;
import club.someoneice.vine.core.TskimiSeiranVine;
import club.someoneice.vine.init.ItemInit;
import com.epherical.croptopia.register.helpers.Juice;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class Shaker extends BaseEntityBlock {
    public Shaker() {
        super(Properties.of(Material.BAMBOO).noOcclusion());
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide && world.getBlockEntity(pos) instanceof ShakerTile tile) {
            if (player.isShiftKeyDown()) {
                ItemStack item = new ItemStack(ItemInit.Shaker.get());
                CompoundTag tag = new CompoundTag();
                tile.list.write(tag);
                tag.put("cocktail", new ItemStack(tile.cocktail.getItem()).save(new CompoundTag()));
                item.getOrCreateTag().put("shaker", tag);
                player.addItem(item);
                world.removeBlock(pos, false);
                world.removeBlockEntity(pos);
                world.gameEvent(GameEvent.BLOCK_DESTROY, pos);
            } else {
                ItemStack item = player.getItemInHand(hand);
                if (item.getItem() instanceof Wine.WineItem) {
                    tile.list.put(item.copy().getItem());
                    item.shrink(1);
                } else if (TskimiSeiranVine.isCroptopiaInstall) {
                    for (Juice juice : Juice.copy()) {
                        if (juice.m_5456_() == item.getItem()) tile.list.put(item.copy().getItem());
                        item.shrink(1);
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack item) {
        if (item.getOrCreateTag().contains("shaker")) {
            if (world.getBlockEntity(pos) instanceof ShakerTile tile) {
                assert item.getTag() != null;
                DataList list = new DataList();
                list.read(item.getTag());
                tile.list = list;
                tile.cocktail = ItemStack.of(item.getTag().getCompound("cocktail"));
            }
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
        if (world.getBlockEntity(pos) instanceof ShakerTile tile) {
            ItemStack item = new ItemStack(ItemInit.Shaker.get());
            CompoundTag tag = new CompoundTag();
            tile.list.write(tag);
            tag.put("cocktail", new ItemStack(tile.cocktail.getItem()).save(new CompoundTag()));
            item.getOrCreateTag().put("shaker", tag);
            player.addItem(item);
        }
        return true;
    }

    @Nullable @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShakerTile(pos, state);
    }
}
