package club.someoneice.vine.client.gui;

import club.someoneice.vine.TskimiSeiranVine;
import club.someoneice.vine.common.container.ContainerBoilers;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiBoiler extends AbstractContainerScreen<ContainerBoilers> {
    public GuiBoiler(ContainerBoilers barrel, Inventory inventory, Component component) {
        super(barrel, inventory, component);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation(TskimiSeiranVine.MODID, "textures/gui/gui_boiler.png");

    @Override
    protected void renderBg(GuiGraphics gg, float tick, int mx, int my) {
        gg.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        gg.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        gg.blit(TEXTURE, x + 89, y + 54, 177, 17, this.menu.getScaledProgress(), 15);

        if (this.menu.isDistillation())
            gg.blit(TEXTURE, x + 56, y + 53, 177, 0, 16, 16);
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float delta) {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, delta);
        this.renderTooltip(gg, mouseX, mouseY);
    }
}
