package club.someoneice.vine.common.container.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotInput extends SlotItemHandler {
    int index;
    public SlotInput(IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
        this.index = index;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !this.getItemHandler().extractItem(index, 1, true).isEmpty();
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }
}
