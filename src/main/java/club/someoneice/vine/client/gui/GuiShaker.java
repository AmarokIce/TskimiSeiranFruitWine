package club.someoneice.vine.client.gui;

import club.someoneice.vine.TskimiSeiranVine;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiShaker extends AbstractContainerScreen<ContainerShaker> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TskimiSeiranVine.MODID, "textures/gui/gui_shaker.png");

    public GuiShaker(ContainerShaker shaker, Inventory inventory, Component cup) {
        super(shaker, inventory, cup);
    }

    @Override
    public void renderBg(PoseStack ps, float tick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.blit(ps, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(PoseStack ps, int mouseX, int mouseY, float delta) {
        this.renderBackground(ps);
        super.render(ps, mouseX, mouseY, delta);
        this.renderTooltip(ps, mouseX, mouseY);
    }
}
