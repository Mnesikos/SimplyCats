package com.github.mnesikos.simplycats.client.gui;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.entity.AbstractCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class GuiCatBook extends GuiScreen {
    private static final int bookImageHeight = 182;
    private static final int bookImageWidth = 281;
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Ref.MODID, "textures/gui/cat_book.png");

    private int bookTotalPages = 1;
    private int currPage;
    private NBTTagList bookPages;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private HomePageButton buttonHome;
    private AddBookmarkButton buttonAddBookmark;
    private BookmarkButton buttonBookmark;

    public AbstractCat cat;
    NBTTagCompound nbt;
    protected int catHealth;
    public static ItemStack book;
    protected final Random rand = new Random();

    public GuiCatBook(AbstractCat cat) {
        this();
        this.cat = cat;
        nbt = new NBTTagCompound();
        cat.writeToNBT(nbt);
    }

    public GuiCatBook() {
        if (book != null) {
            if (book.hasTagCompound()) {
                NBTTagCompound nbttagcompound = book.getTagCompound();
                this.bookPages = nbttagcompound.getTagList("pages", Constants.NBT.TAG_COMPOUND).copy();
                this.bookTotalPages = this.bookPages.tagCount();

                if (this.bookTotalPages < 1) {
                    this.bookPages.appendTag(new NBTTagString(""));
                    this.bookTotalPages = 1;
                }
            }

            if (this.bookPages == null) {
                this.bookPages = new NBTTagList();
                this.bookPages.appendTag(new NBTTagString(""));
                this.bookTotalPages = 1;
            }
        }
    }

    @Override
    public void initGui() {
        buttonList.clear();
        super.initGui();
        Keyboard.enableRepeatEvents(true);

        int centerX = (width - bookImageWidth) / 2;
        this.buttonNextPage = this.addButton(new NextPageButton(1, centerX + 236, 157, true));
        this.buttonPreviousPage = this.addButton(new NextPageButton(2, centerX + 25, 157, false));
        this.buttonHome = this.addButton(new HomePageButton(3, centerX + -3, 18));
        this.buttonAddBookmark = this.addButton(new AddBookmarkButton(4, centerX + 116, 153));
        this.buttonBookmark = this.addButton(new BookmarkButton(5, centerX + 268, 18));
        this.updateButtons();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    private void updateButtons() {
        this.buttonNextPage.visible = this.currPage < this.bookTotalPages - 1;
        this.buttonPreviousPage.visible = this.currPage > 0;
        this.buttonHome.isActive = this.currPage == 0;
        this.buttonAddBookmark.visible = this.currPage > 0;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 3) { // home button
                if (this.currPage > 0)
                    this.currPage = 0;

            } else if (button.id == 1) { // next button
                if (this.currPage < this.bookTotalPages - 1)
                    ++this.currPage;

            } else if (button.id == 2) { // previous button
                if (this.currPage > 0)
                    --this.currPage;

            } else if (button.id == 4) { // add bookmark button
                
            }

            this.updateButtons();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1, 1, 1, 1);
        int centerX = (width - bookImageWidth) / 2;

        this.mc.getTextureManager().bindTexture(BG_TEXTURE);
        drawModalRectWithCustomSizedTexture(centerX, 2, 0, 0, bookImageWidth, bookImageHeight, 288, 256);

        if (cat != null) {
            GuiInventory.drawEntityOnScreen(centerX + 40, 74, 50, (centerX + 51) - mouseX, 50 - mouseY, cat);

            String ownerName = "";
            if (cat.isTamed())
                ownerName = cat.getOwnerName().getFormattedText();

            this.renderCatHealth(centerX + 66, 24);
            this.fontRenderer.drawSplitString(cat.getName(), centerX + 66, 40, 68, 0);
            this.fontRenderer.drawString("Purrsonality", centerX + 66, 64, 0);

            this.fontRenderer.drawSplitString(Genetics.getPhenotypeDescription(nbt), centerX + 16, 90, 120, 0);

            this.fontRenderer.drawSplitString(ownerName, centerX + 16, 122, 120, 0);

            this.fontRenderer.drawSplitString("Some vocal level shit",
                    centerX + 16, 136, 120, 0);
            this.fontRenderer.drawSplitString("Some activity level shit",
                    centerX + 16, 148, 120, 0);

            this.fontRenderer.drawString(nbt.getString("EyeColor"), centerX + 230, 12 + 12, 0);
            this.fontRenderer.drawString(nbt.getString("FurLength"), centerX + 230, 22 + 12, 0);
            this.fontRenderer.drawString(nbt.getString("Eumelanin"), centerX + 230, 32 + 12, 0);
            this.fontRenderer.drawString(nbt.getString("Phaeomelanin"), centerX + 230, 42 + 12, 0);
            this.fontRenderer.drawString(nbt.getString("Dilution"), centerX + 230, 52 + 12, 0);
            this.fontRenderer.drawString(nbt.getString("DiluteMod"), centerX + 230, 62 + 12, 0);
            this.fontRenderer.drawString(nbt.getString("Agouti"), centerX + 230, 72 + 12, 0);
            this.fontRenderer.drawString(nbt.getString("Tabby"), centerX + 230, 82 + 12, 0);
            this.fontRenderer.drawString(nbt.getString("Spotted"), centerX + 230, 92 + 12, 0);
            this.fontRenderer.drawString(nbt.getString("Ticked"), centerX + 230, 102 + 12, 0);
            this.fontRenderer.drawString(nbt.getString("Colorpoint"), centerX + 230, 112 + 12, 0);
            this.fontRenderer.drawString(nbt.getString("White"), centerX + 230, 122 + 12, 0);

            this.fontRenderer.drawString("Eye color", centerX + 152, 24, 0);
            this.fontRenderer.drawString("Fur length", centerX + 152, 34, 0);
            this.fontRenderer.drawString("Eumelanin", centerX + 152, 44, 0);
            this.fontRenderer.drawString("Phaeomelanin", centerX + 152, 54, 0);
            this.fontRenderer.drawString("Dilute", centerX + 152, 64, 0);
            this.fontRenderer.drawString("Dilute modifier", centerX + 152, 74, 0);
            this.fontRenderer.drawString("Agouti", centerX + 152, 84, 0);
            this.fontRenderer.drawString("Tabby", centerX + 152, 94, 0);
            this.fontRenderer.drawString("Spotted", centerX + 152, 104, 0);
            this.fontRenderer.drawString("Ticked", centerX + 152, 114, 0);
            this.fontRenderer.drawString("Colorpoint", centerX + 152, 124, 0);
            this.fontRenderer.drawString("White", centerX + 152, 134, 0);

            this.fontRenderer.drawSplitString("Pregnancy/Heat Data", centerX + 150, 148, 120, 0);

        } else if (book != null) {
            this.fontRenderer.drawString("Index page?", centerX + 40, 80, 0);
        } else
            this.fontRenderer.drawString("error page this should not happen", centerX + 40, 80, 0); //todo remove when done

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void renderCatHealth(int x, int y) {
        GlStateManager.color(1, 1, 1, 1);
        this.mc.getTextureManager().bindTexture(ICONS);
        this.catHealth = MathHelper.ceil(cat.getHealth());
        IAttributeInstance iattributeinstance = cat.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

        float maxHealth = (float)iattributeinstance.getAttributeValue();
        int l1 = MathHelper.ceil((maxHealth) / 2.0F / 10.0F);
        int i2 = Math.max(10 - (l1 - 2), 3);

        for (int wholeHearts = MathHelper.ceil((maxHealth) / 2.0F) - 1; wholeHearts >= 0; --wholeHearts) {
            int textureX = 16;
            int textureY = 0;

            int j4 = MathHelper.ceil((float) (wholeHearts + 1) / 10.0F) - 1;
            int guiX = x + wholeHearts % 10 * 8;
            int guiY = y - j4 * i2;

            this.drawTexturedModalRect(guiX, guiY, 16 + textureY * 9, 9 * textureY, 9, 9);

            if (wholeHearts * 2 + 1 < catHealth)
                this.drawTexturedModalRect(guiX, guiY, textureX + 36, 9 * textureY, 9, 9);

            if (wholeHearts * 2 + 1 == catHealth)
                this.drawTexturedModalRect(guiX, guiY, textureX + 45, 9 * textureY, 9, 9);
        }
    }

    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, AbstractCat cat)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = cat.renderYawOffset;
        float f1 = cat.rotationYaw;
        float f2 = cat.rotationPitch;
        float f3 = cat.prevRotationYawHead;
        float f4 = cat.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        cat.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
        cat.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
        cat.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        cat.rotationYawHead = cat.rotationYaw;
        cat.prevRotationYawHead = cat.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(cat, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        cat.renderYawOffset = f;
        cat.rotationYaw = f1;
        cat.rotationPitch = f2;
        cat.prevRotationYawHead = f3;
        cat.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton {
        private final boolean isNextButton;

        NextPageButton(int buttonId, int x, int y, boolean nextButton) {
            super(buttonId, x, y, 20, 12, "");
            isNextButton = nextButton;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (visible) {
                boolean isButtonPressed = (mouseX >= x && mouseY >= y
                        && mouseX < x + width && mouseY < y + height);

                GlStateManager.color(1, 1, 1, 1);
                mc.getTextureManager().bindTexture(BG_TEXTURE);
                int textureX = 2;
                int textureY = 193;

                if (isButtonPressed)
                    textureX += 23;

                if (!isNextButton)
                    textureY += 13;

                drawModalRectWithCustomSizedTexture(x, y, textureX, textureY, width, height, 288, 256);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    static class HomePageButton extends GuiButton {
        boolean isActive;

        HomePageButton(int buttonId, int x, int y) {
            super(buttonId, x, y, 16, 16, "");
            this.isActive = false;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (visible) {
                GlStateManager.color(1, 1, 1, 1);
                mc.getTextureManager().bindTexture(BG_TEXTURE);
                int textureX = 3;
                int textureY = 239;

                if (isActive)
                    textureX += 21;

                drawModalRectWithCustomSizedTexture(x, y, textureX, textureY, width, height, 288, 256);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    static class AddBookmarkButton extends GuiButton {
        AddBookmarkButton(int buttonId, int x, int y) {
            super(buttonId, x, y, 15, 15, "");
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (visible) {
                boolean isButtonPressed = (mouseX >= x && mouseY >= y
                        && mouseX < x + width && mouseY < y + height);

                GlStateManager.color(1, 1, 1, 1);
                mc.getTextureManager().bindTexture(BG_TEXTURE);
                int textureX = 42;
                int textureY = 220;

                if (isButtonPressed)
                    textureX += 14;

                drawModalRectWithCustomSizedTexture(x, y, textureX, textureY, width, height, 288, 256);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    static class BookmarkButton extends GuiButton {
        boolean isActive;

        BookmarkButton(int buttonId, int x, int y) {
            super(buttonId, x, y, 16, 16, "");
            this.isActive = false;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (visible) {
                GlStateManager.color(1, 1, 1, 1);
                mc.getTextureManager().bindTexture(BG_TEXTURE);
                int textureX = 3;
                int textureY = 220;

                if (isActive)
                    textureX += 21;

                drawModalRectWithCustomSizedTexture(x, y, textureX, textureY, width, height, 288, 256);
            }
        }
    }
}

