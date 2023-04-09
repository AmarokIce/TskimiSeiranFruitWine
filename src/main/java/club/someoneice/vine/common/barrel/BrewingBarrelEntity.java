package club.someoneice.vine.common.barrel;

import club.someoneice.vine.common.item.Wine;
import club.someoneice.vine.core.Data;
import club.someoneice.vine.core.TagHelper;
import club.someoneice.vine.init.TileInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BrewingBarrelEntity extends BlockEntity {
    private Wine wine_type;
    public boolean hasWater;
    private int level, progress, time;
    public int wine;

    public BrewingBarrelEntity(BlockPos pos, BlockState state) {
        super(TileInit.BrewingBarrel.get(), pos, state);
        wine_type = null;
        this.hasWater = false;
        this.level = 0;
        this.progress = 0;
        this.time = 0;
        this.wine = 0;
    }

    @Override
    public void load(CompoundTag tag) {
        wine_type   = Data.wineMap.get(tag.getString("wine_type"));
        hasWater    = tag.getBoolean("hasWater");
        progress    = tag.getInt("progress");
        level       = tag.getInt("level");
        time        = tag.getInt("time");
        wine        = tag.getInt("wine");
        super.load(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putString("wine_type", wine_type.name);
        tag.putBoolean("hasWater", hasWater);
        tag.putInt("progress", progress);
        tag.putInt("level", level);
        tag.putInt("time", time);
        tag.putInt("wine", wine);

        super.saveAdditional(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        tag.putString("wine_type", wine_type.name);
        tag.putBoolean("hasWater", hasWater);
        tag.putInt("progress", progress);
        tag.putInt("level", level);
        tag.putInt("time", time);
        tag.putInt("wine", wine);
        return tag;
    }

    public Wine getWine() {
        return this.wine_type;
    }

    public void removeWine() {
        this.wine_type = null;
    }

    public boolean setWine(ItemStack item) {
        if (Data.wineItemMap.containsKey(item.getItem())) {
            this.wine_type = Data.wineItemMap.get(item.getItem());
            return true;
        } else {
            for (TagKey<Item> tag : Data.wineTagList.keySet()) {
                if (item.is(tag)) {
                    this.wine_type = Data.wineTagList.get(tag);
                    if (tag == TagHelper.Milk) this.setWater();
                    return true;
                }
            }
        }
         return false;
    }

    public void setWater() {
        this.hasWater = true;
    }

    public boolean isStart() {
        return (wine_type != null && hasWater || this.wine > 0);
    }

    public int getWineLevel() {
        return this.level;
    }

    public int getWineProgress() {
        return this.progress;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, BrewingBarrelEntity entity) {
        if (entity.isStart() && entity.wine == 0) {
            ++entity.time;
            if (entity.time >= 20 * 10) {
                entity.time = 0;
                entity.progress++;

                if (entity.progress >= 100) {
                    entity.progress = 0;
                    entity.wine = 8;
                    entity.hasWater = false;

                    // entity.level++;
                    //if (entity.wine == 0) {
                    //    entity.wine = 8;
                    //    entity.hasWater = false;
                    //}
                }
            }
        }
    }
}
