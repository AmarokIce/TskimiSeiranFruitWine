package club.someoneice.vine.init;

import club.someoneice.vine.TskimiSeiranVine;
import club.someoneice.vine.common.item.group.CreativeModeTabDef;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabInit {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TskimiSeiranVine.MODID);

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register(TskimiSeiranVine.MODID, () -> CreativeModeTab.builder()
            .icon(() -> ItemInit.Flagon.get().getDefaultInstance())
            .title(Component.translatable("itemGroup." + TskimiSeiranVine.MODID))
            .displayItems((p, l) -> l.acceptAll(CreativeModeTabDef.getItemsInTab(CreativeModeTabDef.TAB)))
            .build());

    public static final RegistryObject<CreativeModeTab> WINE_TAB = TABS.register(TskimiSeiranVine.MODID + ".wine", () -> CreativeModeTab.builder()
            .icon(() -> ItemInit.Cider.cup.get().getDefaultInstance())
            .title(Component.translatable("itemGroup." + TskimiSeiranVine.MODID + ".wine"))
            .displayItems((p, l) -> l.acceptAll(CreativeModeTabDef.getItemsInTab(CreativeModeTabDef.WINE_TAB)))
            .build());

    public static final RegistryObject<CreativeModeTab> COCKTAIL_TAB = TABS.register(TskimiSeiranVine.MODID + ".cocktail", () -> CreativeModeTab.builder()
            .icon(() -> ItemInit.NoneCocktail.get().getDefaultInstance())
            .title(Component.translatable("itemGroup." + TskimiSeiranVine.MODID + ".cocktail"))
            .displayItems((p, l) -> {
                l.acceptAll(CreativeModeTabDef.getItemsInTab(CreativeModeTabDef.COCKTAIL_TAB));
                l.accept(PotionUtils.setPotion(new ItemStack(ItemInit.TskimiSeiran_SOUP.get()), PotionInit.tsks_s_soup.get()));
            })
            .build());
}
