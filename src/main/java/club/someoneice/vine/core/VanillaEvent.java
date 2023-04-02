package club.someoneice.vine.core;

import club.someoneice.vine.init.ItemInit;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = TskimiSeiranVine.MODID)
public class VanillaEvent {
    @SubscribeEvent
    public void wandererTradesSell(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> trades = event.getGenericTrades();
        trades.add(new BasicItemListing(3, new ItemStack(ItemInit.Flagon.get(), 1), 3, 12, 0.05F));
        trades.add(new BasicItemListing(3, new ItemStack(ItemInit.Gourd.get(), 1), 3, 12, 0.05F));
    }
}
