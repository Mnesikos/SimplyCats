package com.github.mnesikos.simplycats.client.gui;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.entity.AbstractCat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

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
    public static ItemStack book;
    NBTTagCompound nbt;

    public GuiCatBook(AbstractCat cat) {
        this();
        this.cat = cat;
        nbt = cat.getEntityData();
        cat.writeToNBT(nbt);
    }

    public GuiCatBook() {
        if (this.book != null) {
            if (this.book.hasTagCompound()) {
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
        /*

        if (button.enabled)
        {
            if (button.id == 0)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
                this.sendBookToServer(false);
            }
            else if (button.id == 3 && this.bookIsUnsigned)
            {
                this.bookGettingSigned = true;
            }
            else if (button.id == 1)
            {
                if (this.currPage < this.bookTotalPages - 1)
                {
                    ++this.currPage;
                }
                else if (this.bookIsUnsigned)
                {
                    this.addNewPage();

                    if (this.currPage < this.bookTotalPages - 1)
                    {
                        ++this.currPage;
                    }
                }
            }
            else if (button.id == 2)
            {
                if (this.currPage > 0)
                {
                    --this.currPage;
                }
            }
            else if (button.id == 5 && this.bookGettingSigned)
            {
                this.sendBookToServer(true);
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            else if (button.id == 4 && this.bookGettingSigned)
            {
                this.bookGettingSigned = false;
            }

            this.updateButtons();
        }
        * */
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1, 1, 1, 1);
        int centerX = (width - bookImageWidth) / 2;

        this.mc.getTextureManager().bindTexture(BG_TEXTURE);
        drawModalRectWithCustomSizedTexture(centerX, 2, 0, 0, bookImageWidth, bookImageHeight, 288, 256);

        if (cat != null) {
            GuiInventory.drawEntityOnScreen(centerX + 40, 80, 50, (centerX + 51) - mouseX, 50 - mouseY, cat);

            this.fontRenderer.drawString(cat.getName(), centerX + 66, 24, 0);
            this.fontRenderer.drawString(cat.getSex(), centerX + 66, 36, 0);
            this.fontRenderer.drawString("Purrsonality", centerX + 66, 48, 0);
            this.fontRenderer.drawString("Owner", centerX + 66, 60, 0);
            this.fontRenderer.drawString("Health", centerX + 66, 72, 0);

            this.fontRenderer.drawSplitString("Description will go here and sort of here ish :)",
                    centerX + 20, 90, 114, 0);
            this.fontRenderer.drawSplitString("Some vocal level shit",
                    centerX + 20, 124, 114, 0);
            this.fontRenderer.drawSplitString("Some activity level shit",
                    centerX + 20, 136, 114, 0);

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

        } else if (book != null) {
            this.fontRenderer.drawString("Index page?", centerX + 40, 80, 0);
        } else
            this.fontRenderer.drawString("error page this should not happen", centerX + 40, 80, 0); //todo remove when done

        super.drawScreen(mouseX, mouseY, partialTicks);
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

