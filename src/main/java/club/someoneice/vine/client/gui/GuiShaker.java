package club.someoneice.vine.client.gui;

import club.someoneice.vine.TskimiSeiranVine;
import club.someoneice.vine.common.container.ContainerShaker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiShaker extends AbstractContainerScreen<ContainerShaker> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TskimiSeiranVine.MODID, "textures/gui/gui_shaker.png");

    public GuiShaker(ContainerShaker shaker, Inventory inventory, Component cup) {
        super(shaker, inventory, cup);
    }

    @Override
    public void renderBg(GuiGraphics gg, float tick, int mouseX, int mouseY) {
        gg.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        gg.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float delta) {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, delta);
        this.renderTooltip(gg, mouseX, mouseY);
    }
}
