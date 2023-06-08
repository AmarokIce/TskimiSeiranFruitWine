package club.someoneice.vine.common.item;

import club.someoneice.vine.common.RecipeShaker;
import club.someoneice.vine.common.gui.ContainerShaker;
import club.someoneice.vine.core.Data;
import club.someoneice.vine.core.TskimiSeiranVine;
import club.someoneice.vine.init.BlockInit;
import club.someoneice.vine.init.PotionInit;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class ShakerItem extends Item {
    public ShakerItem() {
        super(new Properties().tab(TskimiSeiranVine.TAB).stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        super.use(world, player, hand);
        var item = player.getItemInHand(hand);
        var tag = item.getOrCreateTag();
        if (player.isShiftKeyDown()) {
            if (!world.isClientSide) {
                checkRecipes(item, world);
                NetworkHooks.openGui((ServerPlayer) player, new SimpleMenuProvider((id, inv, user) -> new ContainerShaker(id, inv), new TextComponent("")));
            }
            return InteractionResultHolder.sidedSuccess(item, world.isClientSide);
        }

        world.playSound(null, player, SoundEvents.HONEY_DRINK, SoundSource.PLAYERS, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
        tag.putInt("shake_count", tag.getInt("shake_count") + 1);
        world.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.HONEY_DRINK, SoundSource.PLAYERS, 0.5F, 2.6F + world.random.nextFloat() * 0.8F);

        return InteractionResultHolder.success(item);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        super.useOn(context);
        if (context.getLevel().isClientSide) return InteractionResult.SUCCESS;

        if (context.getLevel().getBlockState(context.getClickedPos()).is(BlockInit.GobletBlock.get())) {
            checkRecipes(context.getItemInHand(), context.getLevel());
            context.getItemInHand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(it -> {
                if (!it.getStackInSlot(12).isEmpty()) {
                    context.getPlayer().addItem(it.getStackInSlot(12).copy());
                    it.getStackInSlot(12).setCount(0);
                } else context.getLevel().setBlock(context.getClickedPos(), BlockInit.NoneCocktail.get().defaultBlockState(), 3);
                removeItem(context.getItemInHand());
            });
        }

        context.getItemInHand().getOrCreateTag().putInt("shake_count", 0);

        return InteractionResult.SUCCESS;
    }

    private void checkRecipes(ItemStack item, Level world) {
        var tag = item.getOrCreateTag();

        SimpleContainer container = new SimpleContainer(8);
        item.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(it -> {
            for (int i = 0; i < 12; i ++) {
                var itm = it.getStackInSlot(i).copy();
                if (itm.getItem() instanceof Wine.WineItem wine)
                    container.addItem(Data.wineMap.get(wine.name).bottle.get().getDefaultInstance());
                else if (itm.getItem() instanceof PotionSoup) {
                    container.addItem(PotionUtils.setPotion(new ItemStack(this), PotionInit.tsks_s_soup.get()));
                } else container.addItem(itm);
            }
            Optional<RecipeShaker> match = world.getRecipeManager().getRecipeFor(RecipeShaker.Type.INSTANCE, container, world);
            match.ifPresent(recipe -> {
                if (tag.getInt("shake_count") > recipe.getShaking())
                    it.insertItem(12, recipe.getResultItem(), false);
            });
        });
    }

    private void removeItem(ItemStack item) {
        item.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(it -> {
            for (int i = 0; i < 12; i ++) {
                var itm = it.getStackInSlot(i);
                if (itm.getItem() instanceof Wine.WineItem wine) {
                    if (wine.wineEnum.equals(Wine.WineEnum.BUCKET) || wine.wineEnum.equals(Wine.WineEnum.WINE)) {
                        var wine_tag = itm.getOrCreateTag();
                        if (wine_tag.contains("wine")) {
                            wine_tag.putInt("wine", wine_tag.getInt("wine"));
                        } else wine_tag.putInt("wine", 3);

                        if (wine_tag.getInt("wine") < 1) {
                            it.getStackInSlot(i).shrink(1);
                            it.insertItem(i, wine.returnItem.getDefaultInstance(), false);
                        }
                    } else {
                        it.getStackInSlot(i).shrink(1);
                        it.insertItem(i, wine.returnItem.getDefaultInstance(), false);
                    }
                } else {
                    it.getStackInSlot(i).shrink(1);
                    if (itm.hasContainerItem()) {
                        it.insertItem(i, itm.getContainerItem(), false);
                    }
                }
            }
        });
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack item, CompoundTag nbt) {
        super.initCapabilities(item, nbt);
        return new CapabilityHandle();
    }

    @Override
    public CompoundTag getShareTag(ItemStack item) {
        var result = Objects.requireNonNullElse(super.getShareTag(item), new CompoundTag());
        item.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(it -> result.put("items", ((ItemStackHandler) it).serializeNBT()));
        return result;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null)
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(it -> ((ItemStackHandler) it).deserializeNBT(nbt.getCompound("items")));
    }

    public static class CapabilityHandle implements ICapabilitySerializable<CompoundTag> {
        private final LazyOptional<ItemStackHandler> handler;

        public CapabilityHandle() {
            handler = LazyOptional.of(() -> new ItemStackHandler(13) {
                @Override
                public int getSlotLimit(int slot) {
                    return 1;
                }
            });
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
            return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? handler.cast() : LazyOptional.empty();
        }

        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
            return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? handler.cast() : LazyOptional.empty();
        }

        private ItemStackHandler getItemHandler() {
            return handler.orElseThrow(RuntimeException::new);
        }

        @Override
        public CompoundTag serializeNBT() {
            return getItemHandler().serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            getItemHandler().deserializeNBT(nbt);
        }

    }
}
