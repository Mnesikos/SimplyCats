package com.github.mnesikos.simplycats.client.gui;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.entity.AbstractCat;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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

    public EntityCat cat;
    NBTTagCompound nbt;
    protected int catHealth;
    public static ItemStack book;
    protected final Random rand = new Random();

    public GuiCatBook(EntityCat cat) {
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
        int leftX = (width - bookImageWidth) / 2;
        int leftCenterX = (bookImageWidth / 2) + 8;

        this.mc.getTextureManager().bindTexture(BG_TEXTURE);
        drawModalRectWithCustomSizedTexture(leftX, 2, 0, 0, bookImageWidth, bookImageHeight, 288, 256);

        if (cat != null) {
            GuiInventory.drawEntityOnScreen(leftX + 40, 74, 50, (leftX + 51) - mouseX, 50 - mouseY, cat);

            int nameWidth = this.fontRenderer.getStringWidth(cat.getName());
            this.fontRenderer.drawString(cat.getName(), leftCenterX - (nameWidth / 2), 14, 0);

            String sex = (cat.isFixed() ? new TextComponentTranslation("cat.fixed.name").getUnformattedText() : new TextComponentTranslation("cat.intact.name").getUnformattedText())
                    + " " + Genetics.Sex.getPrettyName(cat.getSex().getName());
            this.fontRenderer.drawString(sex, leftX + 66, 14*2, 0);

            this.renderCatHealth(leftX + 66, 14*3);

            //this.fontRenderer.drawString("Purrsonality", leftX + 66, 14*4, 0); //todo

            String ownerName = "";
            if (cat.isTamed()) {
                ownerName = cat.getOwnerName().getFormattedText();
                this.fontRenderer.drawString(new TextComponentTranslation("tooltip.pet_carrier.owner").getUnformattedText() + " " + ownerName, leftX + 16, 14 * 6, 0);
            } else
                this.fontRenderer.drawString(new TextComponentTranslation("entity.simplycats.cat.untamed").getUnformattedText(), leftX + 16, 14 * 6, 0);

            this.fontRenderer.drawSplitString(Genetics.getPhenotypeDescription(nbt, false), leftX + 16, 14*7, 120, 0);

            /*this.fontRenderer.drawSplitString("Vocal Level Bar Here",
                    leftX + 16, 14*9, 120, 0);
            this.fontRenderer.drawSplitString("Activity Level Bar Here",
                    leftX + 16, 14*9+9, 120, 0);*/ //todo

            //this.fontRenderer.drawSplitString("Pregnancy Data", leftX + 16, 14*11-5, 120, 0); //todo

            String eyeColor = TextFormatting.GRAY + nbt.getString("EyeColor");
            String furLength = TextFormatting.GRAY + nbt.getString("FurLength");
            String eumelanin = TextFormatting.GRAY + nbt.getString("Eumelanin");
            String phaeomelanin = TextFormatting.GRAY + nbt.getString("Phaeomelanin");
            String dilution = TextFormatting.GRAY + nbt.getString("Dilution");
            String diluteMod = TextFormatting.GRAY + nbt.getString("DiluteMod");
            String agouti = TextFormatting.GRAY + nbt.getString("Agouti");
            String tabby = TextFormatting.GRAY + nbt.getString("Tabby");
            String spotted = TextFormatting.GRAY + nbt.getString("Spotted");
            String ticked = TextFormatting.GRAY + nbt.getString("Ticked");
            String colorpoint = TextFormatting.GRAY + nbt.getString("Colorpoint");
            String white = TextFormatting.GRAY + nbt.getString("White");

            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.eye_color").getUnformattedText() + ": " + eyeColor, leftX + 152, 24, 0);
            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.fur_length").getUnformattedText() + ": " + furLength, leftX + 152, 34, 0);
            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.eumelanin").getUnformattedText() + ": " + eumelanin, leftX + 152, 44, 0);
            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.phaeomelanin").getUnformattedText() + ": " + phaeomelanin, leftX + 152, 54, 0);
            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.dilute").getUnformattedText() + ": " + dilution, leftX + 152, 64, 0);
            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.dilute_modifier").getUnformattedText() + ": " + diluteMod, leftX + 152, 74, 0);
            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.agouti").getUnformattedText() + ": " + agouti, leftX + 152, 84, 0);
            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.tabby").getUnformattedText() + ": " + tabby, leftX + 152, 94, 0);
            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.spotted").getUnformattedText() + ": " + spotted, leftX + 152, 104, 0);
            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.ticked").getUnformattedText() + ": " + ticked, leftX + 152, 114, 0);
            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.colorpoint").getUnformattedText() + ": " + colorpoint, leftX + 152, 124, 0);
            this.fontRenderer.drawString(new TextComponentTranslation("book.genetics.white").getUnformattedText() + ": " + white, leftX + 152, 134, 0);

            //this.fontRenderer.drawSplitString("Heritage Data", leftX + 152, 14*11-5, 120, 0); //todo

        } else if (book != null) {
            this.fontRenderer.drawSplitString(new TextComponentTranslation("book.index_page.info").getUnformattedText(), leftX + 16, 60, 120, 0);
        } else
            this.fontRenderer.drawSplitString("Error page, this should not happen, please report to github issue tracker, thanks.", leftX + 16, 60, 120, 0); //todo remove when done

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void renderCatHealth(int x, int y) {
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

