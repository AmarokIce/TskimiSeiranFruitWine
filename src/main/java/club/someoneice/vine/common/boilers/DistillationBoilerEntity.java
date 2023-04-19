package club.someoneice.vine.common.boilers;

import club.someoneice.vine.common.item.Wine;
import club.someoneice.vine.core.Data;
import club.someoneice.vine.core.TagHelper;
import club.someoneice.vine.init.TileInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DistillationBoilerEntity extends BlockEntity {
    private Wine wine_type;
    public boolean hasWater;
    private int progress, time;
    public int wine;

    public DistillationBoilerEntity(BlockPos pos, BlockState state) {
        super(TileInit.DistillationBoiler.get(), pos, state);
        wine_type = null;
        this.hasWater = false;
        this.progress = 0;
        this.time = 0;
        this.wine = 0;
    }

    @Override
    public void load(CompoundTag tag) {
        wine_type   = Data.wineMap.get(tag.getString("wine_type"));
        hasWater    = tag.getBoolean("hasWater");
        progress    = tag.getInt("progress");
        time        = tag.getInt("time");
        wine        = tag.getInt("wine");
        super.load(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putString("wine_type", wine_type.name);
        tag.putBoolean("hasWater", hasWater);
        tag.putInt("progress", progress);
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

    public void setWater() {
        this.hasWater = true;
    }

    public boolean isStart() {
        return (wine_type != null && hasWater || this.wine > 0);
    }

    public int getWineProgress() {
        return this.progress;
    }

    public boolean setWine(Level world, BlockPos pos, ItemStack item) {
        if (!world.isClientSide) {
            var block = world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));
            if (block.is(Blocks.CAMPFIRE) || block.is(Blocks.SOUL_CAMPFIRE) || block.is(Blocks.FIRE) || block.is(Blocks.SOUL_CAMPFIRE) || block.is(Blocks.LEVER) || block.is(Blocks.MAGMA_BLOCK))
                return setWineWithDistillation(item);
            else return setWineWithFerment(item);
        }

        return false;
    }

    private boolean setWineWithFerment(ItemStack item) {
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

    private boolean setWineWithDistillation(ItemStack item) {
        if (Data.distillationItemMap.containsKey(item.getItem())) {
            this.wine_type = Data.distillationItemMap.get(item.getItem());
            return true;
        } else {
            for (var tag : Data.distillationTagList.keySet()) {
                if (item.is(tag)) {
                    this.wine_type = Data.distillationTagList.get(tag);
                    return true;
                }
            }
        }

        return false;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, DistillationBoilerEntity entity) {
        if (entity.isStart() && entity.wine == 0) {
            world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D + world.random.nextDouble() * 0.3D - 0.2D, pos.getY() + 0.6D, pos.getZ() + 0.5D + world.random.nextDouble() * 0.3D - 0.2D, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D + world.random.nextDouble() * 0.3D - 0.2D, pos.getY() + 0.6D, pos.getZ() + 0.5D + world.random.nextDouble() * 0.3D - 0.2D, 0.0D, 0.0D, 0.0D);
            ++entity.time;
            if (entity.time >= 20 * 15) {
                entity.time = 0;
                entity.progress++;

                if (entity.progress >= 100) {
                    entity.progress = 0;
                    entity.wine = 8;
                    entity.hasWater = false;
                }
            }
        }
    }
}
