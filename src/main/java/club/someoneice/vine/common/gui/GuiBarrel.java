package club.someoneice.vine.common.gui;

import club.someoneice.vine.core.TskimiSeiranVine;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiBarrel extends AbstractContainerScreen<ContainerBarrel> {
    public GuiBarrel(ContainerBarrel barrel, Inventory inventory, Component component) {
        super(barrel, inventory, component);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation(TskimiSeiranVine.MODID, "textures/gui/gui_barrel.png");

    @Override
    protected void renderBg(PoseStack ps, float tick, int mx, int my) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.blit(ps, x, y, 0, 0, this.imageWidth, this.imageHeight);

        this.blit(ps, x + 89, y + 54, 177, 17, this.menu.getScaledProgress(), 15);
    }

    @Override
    public void render(PoseStack ps, int mouseX, int mouseY, float delta) {
        this.renderBackground(ps);
        super.render(ps, mouseX, mouseY, delta);
        this.renderTooltip(ps, mouseX, mouseY);
    }
}
