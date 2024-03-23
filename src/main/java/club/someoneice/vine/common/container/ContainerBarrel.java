package club.someoneice.vine.common.container;

import club.someoneice.vine.common.block.barrel.BrewingBarrelEntity;
import club.someoneice.vine.common.container.slot.SlotWine;
import club.someoneice.vine.init.BlockInit;
import club.someoneice.vine.init.ContainerInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class ContainerBarrel extends AbstractContainerMenu {
    Level world;
    BrewingBarrelEntity entity;
    Inventory inventory;
    final ContainerData data;

    public ContainerBarrel(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, (BrewingBarrelEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public ContainerBarrel(int id, Inventory inv, BrewingBarrelEntity entity, ContainerData data) {
        super(ContainerInit.BARREL_GUI.get(), id);
        this.world = entity.getLevel();
        this.entity = entity;
        this.inventory = inv;
        this.data = data;
        this.addDataSlots(this.data);

        this.addPlayerHotbar(inv);
        this.addPlayerInventory(inv);
        entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(it -> {
            addSlot(new SlotWine(it, 0, 55, 27));
            addSlot(new SlotWine(it, 1, 108, 27));
        });
    }

    public int getScaledProgress() {
        int time = this.data.get(0);
        return time != 0 ? (int) Math.floor(time * (38 / 100.0D)) : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.create(this.world, this.entity.getBlockPos()), player, BlockInit.BrewingBarrelBlock.get());
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int i = 0; i < 3; ++i) for (int l = 0; l < 9; ++l)
            this.addSlot(new Slot(inventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int i = 0; i < 9; ++i) this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = this.slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < 36) {
            if (!this.moveItemStackTo(sourceStack, 36, 36, false)) return ItemStack.EMPTY;
        } else return ItemStack.EMPTY;

        if (sourceStack.getCount() == 0)
            sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }
}
