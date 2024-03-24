package club.someoneice.vine.common.block.barrel;

import club.someoneice.vine.common.item.Wine;
import club.someoneice.vine.init.ItemInit;
import club.someoneice.vine.init.TileInit;
import club.someoneice.vine.util.Utilities;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("all")
public class BrewingBarrel extends BaseEntityBlock {
    private static final IntegerProperty AMOUNT = IntegerProperty.create("amount", 1, 3);
    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BrewingBarrel() {
        super(Properties.copy(Blocks.BARREL).noOcclusion());
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack item) {
        var tag = item.getOrCreateTag();
        if (!tag.contains("contents")) return;
        BlockEntity blockentity = world.getBlockEntity(pos);
        if (!(blockentity instanceof BrewingBarrelEntity bb)) return;
        bb.loadItemListFromTag((ListTag) tag.get("contents"));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof BrewingBarrelEntity entity)) return InteractionResult.FAIL;
        if (player.isShiftKeyDown() && !world.isClientSide) NetworkHooks.openScreen(((ServerPlayer) player), entity, pos);

        var item = player.getMainHandItem();
        if (!entity.hasWater) {
            handleWater(player, entity);
            return InteractionResult.SUCCESS;
        }
        if (!entity.hasWine && entity.setWine(player, item)) return InteractionResult.SUCCESS;
        if (entity.isFinish) {
            entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(it -> handleWine(player, entity, it));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    private void handleWater(final Player player, final BrewingBarrelEntity entity) {
        final var item = player.getMainHandItem();
        final var world = player.level();
        if (!item.is(Items.WATER_BUCKET)) {
            if (player.level().isClientSide) player.displayClientMessage(Component.translatable("tsfWine.shouldWater.message"), true);
            return;
        }
        entity.setWater();
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
        item.shrink(1);
        Utilities.addItem2PlayerOrDrop(player, Items.BUCKET.getDefaultInstance());
    }

    private void handleWine(final Player player, final BrewingBarrelEntity entity, final IItemHandler it) {
        final var item = player.getMainHandItem();
        final var wine = ((Wine.WineItem) it.getStackInSlot(1).getItem()).getWineType();
        final var wineItem = wine.getItemByContainerOnUse(item);
        if (wineItem == null) return;
        int flag =  ((Wine.WineItem) wineItem.getItem()).wineEnum == Wine.WineEnum.WINE ? 4 : 1;
        if (it.getStackInSlot(1).getCount() < flag) return;

        Utilities.addItem2PlayerOrDrop(player, wineItem);
        item.shrink(1);

        it.extractItem(1, flag, false);
        if (it.getStackInSlot(1) == ItemStack.EMPTY) entity.init();
    }

    @Nullable @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BrewingBarrelEntity(pos, state);
    }

    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> tileentity) {
        return createTickerHelper(tileentity, TileInit.BrewingBarrel.get(), BrewingBarrelEntity::tick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AMOUNT, FACING);
    }

    @Nullable @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        var ret = ItemInit.BrewingBarrelItem.get().getDefaultInstance();
        BlockEntity blockentity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (!(blockentity instanceof BrewingBarrelEntity bb) || bb.isItemListEmpty()) return Lists.newArrayList(ret);
        var tag = ret.getOrCreateTag();
        tag.put("contents", bb.createItemListTag());
        return Lists.newArrayList(ret);
    }
}
