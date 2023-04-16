package club.someoneice.vine.common.boilers;

import club.someoneice.vine.init.ItemInit;
import club.someoneice.vine.init.TileInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DistillationBoiler extends BaseEntityBlock {
    private static final IntegerProperty AMOUNT = IntegerProperty.create("amount", 1, 3);
    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public DistillationBoiler() {
        super(Properties.copy(Blocks.IRON_BLOCK).noOcclusion());
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) return InteractionResult.SUCCESS;
        if (world.getBlockEntity(pos) instanceof DistillationBoilerEntity entity) {
            var item = player.getItemInHand(hand);
            if (!entity.isStart()) {
                if (item.getItem() == Items.WATER_BUCKET && !entity.hasWater) {
                    entity.setWater();
                    item.shrink(1);
                    player.addItem(new ItemStack(Items.BUCKET));
                    world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_EMPTY, SoundSource.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
                } else if ((item.getCount() >= 16 || item.getMaxStackSize() == 1) && entity.getWine() == null && entity.setWine(world, pos, item)) {
                    if (item.getMaxStackSize() == 1) item.shrink(1);
                    else item.shrink(16);
                    if (item.hasContainerItem()) player.addItem(item.getContainerItem());
                    player.sendMessage(new TranslatableComponent("tsfWine.puttsfWine.message").append(new TranslatableComponent(entity.getWine().name)), UUID.randomUUID());
                }
            } else if (entity.isStart()) {
                if (item != ItemStack.EMPTY && entity.wine > 0) {
                    if (item.getItem() == Items.BUCKET && entity.wine >= 4) {
                        item.shrink(1);
                        player.addItem(new ItemStack(entity.getWine().bucket.get()));
                        entity.wine -= 4;
                    } else if (item.getItem() == Items.GLASS_BOTTLE) {
                        item.shrink(1);
                        player.addItem(new ItemStack(entity.getWine().bottle.get()));
                        entity.wine -= 1;
                    } else if (item.getItem() == ItemInit.Cup.get()) {
                        item.shrink(1);
                        player.addItem(new ItemStack(entity.getWine().cup.get()));
                        entity.wine -= 1;
                    } else if (item.getItem() == ItemInit.WineBottle.get() && entity.wine >= 4) {
                        item.shrink(1);
                        player.addItem(new ItemStack(entity.getWine().wineBottle.get()));
                        entity.wine -= 4;
                    } else if (item.getItem() == ItemInit.GlassBottle.get()) {
                        item.shrink(1);
                        player.addItem(new ItemStack(entity.getWine().glass.get()));
                        entity.wine -= 1;
                    }

                    if (entity.wine == 0) {
                        entity.removeWine();
                        entity.hasWater = false;
                    }
                } else {
                    Component name = new TranslatableComponent(entity.getWine().name);
                    int progress = entity.getWineProgress();

                    player.sendMessage(new TranslatableComponent("tsfWine.get_wine_name.message").append(name), UUID.randomUUID());
                    if (entity.wine != 0 && entity.wine != 8)
                        player.sendMessage(new TranslatableComponent("tsfWine.has_wine.message").append(Integer.toString(entity.wine)), UUID.randomUUID());
                    else
                        player.sendMessage(new TranslatableComponent("tsfWine.get_win_progress.message").append(Integer.toString(progress)), UUID.randomUUID());
                }
            }
        }

        return InteractionResult.SUCCESS;
    }


    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder loot) {
        var itemList = new ArrayList<ItemStack>();
        itemList.add(new ItemStack(this.asItem()));
        return itemList;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DistillationBoilerEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> tileentity) {
        return createTickerHelper(tileentity, TileInit.DistillationBoiler.get(), DistillationBoilerEntity::tick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AMOUNT, FACING);
    }

    @javax.annotation.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
