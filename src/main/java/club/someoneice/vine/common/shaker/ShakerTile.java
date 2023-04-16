package club.someoneice.vine.common.shaker;

import club.someoneice.vine.core.DataMap;
import club.someoneice.vine.init.TileInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShakerTile extends BlockEntity {
    ItemStack cocktail;
    DataMap list;

    public ShakerTile(BlockPos p_155229_, BlockState p_155230_) {
        super(TileInit.SheckerTile.get(), p_155229_, p_155230_);
        cocktail = ItemStack.EMPTY;
        list = new DataMap();
    }

    @Override
    public void load(CompoundTag tag) {
        tag.put("cocktail", new ItemStack(cocktail.getItem()).save(new CompoundTag()));
        list.write(tag);
        super.load(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        cocktail = ItemStack.of(tag.getCompound("cocktail"));
        list.write(tag);
        super.saveAdditional(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        tag.put("cocktail", new ItemStack(cocktail.getItem()).save(new CompoundTag()));
        list.write(tag);
        return tag;
    }
}
