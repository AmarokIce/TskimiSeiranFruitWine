package club.someoneice.vine.core;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class DataMap {
    private final Map<Item, Integer> map;
    public DataMap() {
        this.map = Maps.newHashMap();
    }

    public Map<Item, Integer> getMap() {
        return this.map;
    }

    public CompoundTag write(CompoundTag nbt) {
        var tag = new CompoundTag();
        int i = 0;
        for (var item : map.keySet()) {
            tag.put("item" + i, item.getDefaultInstance().save(new CompoundTag()));
            tag.putInt("count" + i, map.get(i));
        }
        nbt.put("multiMap", tag);
        return nbt;
    }

    public void read(CompoundTag nbt) {
        if (!nbt.contains("multiMap")) return;
        var tag = nbt.getCompound("multiMap");
        for (int i = 0; i < 12; i ++) {
            if (!tag.contains("item" + i)) return;
            var item = ItemStack.of(tag.getCompound("item" + i)).getItem();
            var count = tag.getInt("count" + i);
            map.put(item, count);
        }
    }

    public void put(ItemStack item) {
        map.put(item.getItem(), item.getCount());
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }
}
