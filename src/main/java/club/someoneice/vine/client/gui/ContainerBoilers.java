package club.someoneice.vine.client.gui;

import club.someoneice.vine.common.block.boilers.DistillationBoilerEntity;
import club.someoneice.vine.init.BlockInit;
import club.someoneice.vine.init.ContainerInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;

public class ContainerBoilers extends AbstractContainerMenu {
    Level world;
    DistillationBoilerEntity entity;
    Inventory inventory;
    ContainerData data;

    public ContainerBoilers(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, (DistillationBoilerEntity) inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public ContainerBoilers(int id, Inventory inv, DistillationBoilerEntity entity, ContainerData data) {
        super(ContainerInit.BOILER_GUI.get(), id);
        this.world = entity.getLevel();
        this.entity = entity;
        this.inventory = inv;
        this.data = data;
        this.addDataSlots(this.data);

        this.addPlayerHotbar(inv);
        this.addPlayerInventory(inv);
        entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(it -> {
            addSlot(new SlotWine(it, 0, 55, 27));
            addSlot(new SlotWine(it, 1, 108, 27));
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.create(this.world, this.entity.getBlockPos()), player, BlockInit.DistillationBoilerBlock.get());
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int i = 0; i < 3; ++i) for (int l = 0; l < 9; ++l)
            this.addSlot(new Slot(inventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    public boolean isDistillation() {
        return this.data.get(1) == 1;
    }

    public int getScaledProgress() {
        int time = this.data.get(0);
        return time != 0 ? (int) Math.floor(time * (38 / 100.0D)) : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = this.slots.get(index);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < 36) {
            if (!this.moveItemStackTo(sourceStack, 36, 36, false)) {
                return ItemStack.EMPTY;
            }
        } else return ItemStack.EMPTY;

        if (sourceStack.getCount() == 0)
            sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }
}
