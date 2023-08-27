package com.LockOriginalMods.Mellow.block.furnace;

import com.LockOriginalMods.Mellow.block.generator.GeneratorBlockEntity;
import com.LockOriginalMods.Mellow.block.generator.GeneratorContainer;
import com.LockOriginalMods.Mellow.utils.MagicHelpers;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;
import java.util.Arrays;

import static com.LockOriginalMods.Mellow.Mellow.MOD_ID;

public class ElectricFurnaceScreen extends AbstractContainerScreen<ElectricFurnaceMenu> {

    private static final ResourceLocation GUI = new ResourceLocation(MOD_ID, "texture/gui/electric_furnace_gui.png");

    public ElectricFurnaceScreen(ElectricFurnaceMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(GUI, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(graphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isSmelting())
            guiGraphics.blit(GUI, x + 105, y + 33, 176, 0, 8, menu.getScaledProgress());
    }



    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, delta);
        renderTooltip(graphics, mouseX, mouseY);
    }
}

