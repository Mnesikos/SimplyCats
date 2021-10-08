package com.github.mnesikos.simplycats;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.*;

public class SCReference {

    private static final Map<UUID, String> CUSTOM_CATS = Maps.newHashMap();

    private static List<Item> EDIBLE = new ArrayList<>();

    public static void registerCatFoods() { //todo
        // add vanilla raw meats
        EDIBLE.add(Items.FISH);
        EDIBLE.add(Items.RABBIT);
        EDIBLE.add(Items.MUTTON);
        EDIBLE.add(Items.CHICKEN);
        EDIBLE.add(Items.PORKCHOP);
        EDIBLE.add(Items.BEEF);
        // add vanilla cooked meats
        EDIBLE.add(Items.COOKED_FISH);
        EDIBLE.add(Items.COOKED_RABBIT);
        EDIBLE.add(Items.COOKED_MUTTON);
        EDIBLE.add(Items.COOKED_CHICKEN);
        EDIBLE.add(Items.COOKED_PORKCHOP);
        EDIBLE.add(Items.COOKED_BEEF);

        /*NonNullList<ItemStack> meatrawDictionary = OreDictionary.getOres("listAllmeatraw");
        for (ItemStack stack : meatrawDictionary)
            EDIBLE.add(stack.getItem());
        NonNullList<ItemStack> meatcookedDictionary = OreDictionary.getOres("listAllmeatcooked");
        for (ItemStack stack : meatcookedDictionary)
            EDIBLE.add(stack.getItem());
        NonNullList<ItemStack> fishrawDictionary = OreDictionary.getOres("listAllfishraw");
        for (ItemStack stack : fishrawDictionary)
            EDIBLE.add(stack.getItem());
        NonNullList<ItemStack> fishcookedDictionary = OreDictionary.getOres("listAllfishcooked");
        for (ItemStack stack : fishcookedDictionary)
            EDIBLE.add(stack.getItem());

        NonNullList<ItemStack> tofuDictionary = OreDictionary.getOres("listAlltofu");
        for (ItemStack stack : tofuDictionary)
            EDIBLE.remove(stack.getItem());*/
    }

    public static boolean catFoodItems(ItemStack stack) {
        Iterator foods = EDIBLE.iterator();
        Item i;
        do {
            if (!foods.hasNext()) {
                return false;
            }

            i = (Item) foods.next();
        } while (stack.getItem() != i);

        return true;
    }

    public static void drawNameplateScaled(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, float scale) { //todo
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        if (!isSneaking)
            GlStateManager.disableDepth();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) (-i - 1), (double) (-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (-i - 1), (double) (8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (i + 1), (double) (8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (i + 1), (double) (-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        if (!isSneaking) {
            fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, 553648127);
            GlStateManager.enableDepth();
        }

        GlStateManager.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 553648127 : -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static Map<UUID, String> getCustomCats() {
        return CUSTOM_CATS;
    }

    static {
        CUSTOM_CATS.put(UUID.fromString("9b1ef261-ebc0-42ad-aacb-621b50fb8269"), "penny");
        CUSTOM_CATS.put(UUID.fromString("966ebb69-a63d-4bb2-ac90-ed39d8c64b80"), "spinny");
    }
}
