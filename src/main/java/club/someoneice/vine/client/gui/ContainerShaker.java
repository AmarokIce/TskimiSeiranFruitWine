package club.someoneice.vine.client.gui;

import club.someoneice.vine.common.item.ShakerItem;
import club.someoneice.vine.init.ContainerInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

public class ContainerShaker extends AbstractContainerMenu {
    ItemStack item;
    public ContainerShaker(int pContainerId, Inventory inv, FriendlyByteBuf data) {
        this(pContainerId, inv);
    }

    public ContainerShaker(int id, Inventory inventory) {
        super(ContainerInit.SHAKER_GUI.get(), id);

        this.item = inventory.player.getMainHandItem();
        if (!(item.getItem() instanceof ShakerItem)) return;
        addPlayerHotbar(inventory);
        addPlayerInventory(inventory);

        // F**k you, Capability.
        item.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(it -> {
            for (int i = 0; i < 3; i ++) for (int o = 0; o < 4; o ++)
                addSlot(new SlotInput(it, o + i * 4, 33 + o * 18, 12 + i * 18));
            addSlot(new SlotWine(it, 12, 133, 30));
        });
    }


    public static final int TE_INVENTORY_SLOT_COUNT = 12;
    public static final int HOTBAR_SLOT_COUNT = 9;
    public static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    public static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    public static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    public static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    public static final int VANILLA_FIRST_SLOT_INDEX = 0;
    public static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = this.slots.get(index);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!this.moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false))
                return ItemStack.EMPTY;
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!this.moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false))
                return ItemStack.EMPTY;
        } else return ItemStack.EMPTY;

        if (sourceStack.getCount() == 0) sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isDeadOrDying() && !player.hurtMarked && player.getMainHandItem() == this.item;
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
}
