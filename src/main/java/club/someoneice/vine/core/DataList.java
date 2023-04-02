package club.someoneice.vine.core;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DataList {
    private List<Item> list = new ArrayList<>();

    public DataList(Item ... itemList) {
        if (itemList.length > 12) list.addAll(Arrays.asList(itemList).subList(0, 12));
        else list.addAll(List.of(itemList));
    }

    public Item get(int slot) {
        return list.get(slot);
    }

    public int getSize() {
        return this.list.size();
    }

    public boolean put(Item item) {
        if (this.list.size() <= 12) {
            list.add(item);
            return true;
        } else return false;
    }

    public void write(CompoundTag tag) {
        CompoundTag listTag = new CompoundTag();
        int i = 0;
        for (Item item : list) {
            listTag.put(Integer.toString(i), new ItemStack(item).save(new CompoundTag()));
            i++;
        }
        tag.put("itemList", listTag);
    }

    public void read(CompoundTag tag) {
        if (tag.contains("itemList")) {
            CompoundTag listTag = tag.getCompound("itemList");
            for (int i = 0; i <= 12; ++i)
                if (listTag.contains(Integer.toString(i))) this.list.add(ItemStack.of(listTag.getCompound(Integer.toString(i))).getItem());
        }
    }

    public boolean isSame(List<Item> items) {
        return new HashSet<>(this.list).containsAll(items);
    }

    public boolean isSame(DataList list) {
        return this.isSame(list.getList());
    }

    private List<Item> getList() {
        return this.list;
    }
}
