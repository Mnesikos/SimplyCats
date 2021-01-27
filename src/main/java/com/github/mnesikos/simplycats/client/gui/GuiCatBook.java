package com.github.mnesikos.simplycats.client.gui;

import com.github.mnesikos.simplycats.Ref;
import com.github.mnesikos.simplycats.entity.EntityCat;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

public class GuiCatBook extends Screen {
    private static final int bookImageHeight = 182;
    private static final int bookImageWidth = 281;
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Ref.MOD_ID, "textures/gui/cat_book.png");

    private int bookTotalPages = 1;
    private int currPage;
    private ListNBT bookPages;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private HomePageButton buttonHome;
    private AddBookmarkButton buttonAddBookmark;
    private BookmarkButton buttonBookmark;

    public EntityCat cat;
    CompoundNBT nbt;
    protected int catHealth;
    public static ItemStack book;

    public GuiCatBook(EntityCat cat) {
        this();
        this.cat = cat;
        nbt = new CompoundNBT();
        cat.writeAdditional(nbt);
    }

    public GuiCatBook() {
        super(new TranslationTextComponent("item.cat_book.name"));
        if (book != null) {
            if (book.hasTag() && book.getTag() != null) {
                CompoundNBT nbttagcompound = book.getTag();
                this.bookPages = nbttagcompound.getList("pages", Constants.NBT.TAG_COMPOUND).copy();
                this.bookTotalPages = this.bookPages.size();

                if (this.bookTotalPages < 1) {
                    this.bookPages.add(StringNBT.valueOf(""));
                    this.bookTotalPages = 1;
                }
            }

            if (this.bookPages == null) {
                this.bookPages = new ListNBT();
                this.bookPages.add(StringNBT.valueOf(""));
                this.bookTotalPages = 1;
            }
        }
    }

    @Override
    protected void init() {
        buttons.clear();
        super.init();
        this.minecraft.keyboardListener.enableRepeatEvents(true);

        int centerX = (width - bookImageWidth) / 2;
        this.buttonNextPage = this.addButton(new NextPageButton(centerX + 236, 157, true, (iPressable) -> {
            if (this.currPage < this.bookTotalPages - 1) ++this.currPage;
            this.updateButtons();
        }));
        this.buttonPreviousPage = this.addButton(new NextPageButton(centerX + 25, 157, false, (iPressable) -> {
            if (this.currPage > 0) --this.currPage;
            this.updateButtons();
        }));
        this.buttonHome = this.addButton(new HomePageButton(centerX + -3, 18, (iPressable) -> {
            if (this.currPage != 0) this.currPage = 0;
            this.updateButtons();
        }));
        this.buttonAddBookmark = this.addButton(new AddBookmarkButton(centerX + 116, 153, (iPressable) -> {
            this.updateButtons();
        }));
        this.buttonBookmark = this.addButton(new BookmarkButton(centerX + 268, 18, (iPressable) -> {
            this.updateButtons();
        }));
        this.updateButtons();
    }

    @Override
    public void onClose() {
        super.onClose();
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    private void updateButtons() {
        this.buttonNextPage.visible = this.currPage < this.bookTotalPages - 1;
        this.buttonPreviousPage.visible = this.currPage > 0;
        this.buttonHome.isActive = this.currPage == 0;
        this.buttonAddBookmark.visible = this.currPage > 0;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1, 1, 1, 1);
        int leftX = (width - bookImageWidth) / 2;
        int leftCenterX = leftX + (bookImageWidth / 4);

        Minecraft.getInstance().getTextureManager().bindTexture(BG_TEXTURE);
        blit(leftX, 2, 0, 0, bookImageWidth, bookImageHeight, 288, 256);

        if (cat != null) {
            InventoryScreen.drawEntityOnScreen(leftX + 40, 74, 50, (leftX + 51) - mouseX, 50 - mouseY, cat);

            float nameWidth = this.font.getStringWidth(cat.getName().getFormattedText());
            this.font.drawString(cat.getName().getFormattedText(), leftCenterX - (nameWidth / 2), 14, 0);

            String sex = (cat.isFixed() ? I18n.format("cat.fixed.name") : I18n.format("cat.intact.name"))
                    + " " + Genetics.Sex.getPrettyName(cat.getSex().getName());
            this.font.drawString(sex, leftX + 66, 14 * 2, 0);

            this.renderCatHealth(leftX + 66, 14 * 3);

            //this.font.drawString("Purrsonality", leftX + 66, 14*4, 0); //todo

            String ownerName = "";
            if (cat.isTamed()) {
                ownerName = cat.getOwnerName().getFormattedText();
                this.font.drawString(I18n.format("tooltip.pet_carrier.owner", ownerName), leftX + 16, 14 * 6, 0);
            } else
                this.font.drawString(I18n.format("entity.simplycats.cat.untamed"), leftX + 16, 14 * 6, 0);

            this.font.drawSplitString(Genetics.getPhenotypeDescription(nbt, false), leftX + 16, 14 * 7, 120, 0);

            /*this.font.drawSplitString("Vocal Level Bar Here",
                    leftX + 16, 14*9, 120, 0);
            this.font.drawSplitString("Activity Level Bar Here",
                    leftX + 16, 14*9+9, 120, 0);*/ //todo

            //this.font.drawSplitString("Pregnancy Data", leftX + 16, 14*11-5, 120, 0); //todo

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
            String bobtail = TextFormatting.GRAY + nbt.getString("Bobtail");

            this.font.drawString(I18n.format("book.genetics.eye_color", eyeColor), leftX + 152, 24, 0);
            this.font.drawString(I18n.format("book.genetics.fur_length", furLength), leftX + 152, 34, 0);
            this.font.drawString(I18n.format("book.genetics.eumelanin", eumelanin), leftX + 152, 44, 0);
            this.font.drawString(I18n.format("book.genetics.phaeomelanin", phaeomelanin), leftX + 152, 54, 0);
            this.font.drawString(I18n.format("book.genetics.dilute", dilution), leftX + 152, 64, 0);
            this.font.drawString(I18n.format("book.genetics.dilute_modifier", diluteMod), leftX + 152, 74, 0);
            this.font.drawString(I18n.format("book.genetics.agouti", agouti), leftX + 152, 84, 0);
            this.font.drawString(I18n.format("book.genetics.tabby", tabby), leftX + 152, 94, 0);
            this.font.drawString(I18n.format("book.genetics.spotted", spotted), leftX + 152, 104, 0);
            this.font.drawString(I18n.format("book.genetics.ticked", ticked), leftX + 152, 114, 0);
            this.font.drawString(I18n.format("book.genetics.colorpoint", colorpoint), leftX + 152, 124, 0);
            this.font.drawString(I18n.format("book.genetics.white", white), leftX + 152, 134, 0);
            this.font.drawString(I18n.format("book.genetics.bobtail", bobtail), leftX + 152, 144, 0);

            //this.font.drawSplitString("Heritage Data", leftX + 152, 14*11-5, 120, 0); //todo

        } else if (book != null) {
            this.font.drawSplitString(I18n.format("book.index_page.info"), leftX + 16, 60, 120, 0);
        } else
            this.font.drawSplitString("Error page, this should not happen, please report to github issue tracker, thanks.", leftX + 16, 60, 120, 0); //todo remove when done

        super.render(mouseX, mouseY, partialTicks);
    }

    private void renderCatHealth(int x, int y) {
        RenderSystem.color4f(1, 1, 1, 1);
        Minecraft.getInstance().getTextureManager().bindTexture(GUI_ICONS_LOCATION);
        this.catHealth = MathHelper.ceil(cat.getHealth());
        IAttributeInstance iattributeinstance = cat.getAttribute(SharedMonsterAttributes.MAX_HEALTH);

        float maxHealth = (float) iattributeinstance.getValue();
        int l1 = MathHelper.ceil((maxHealth) / 2.0F / 10.0F);
        int i2 = Math.max(10 - (l1 - 2), 3);

        for (int wholeHearts = MathHelper.ceil((maxHealth) / 2.0F) - 1; wholeHearts >= 0; --wholeHearts) {
            int textureX = 16;
            int textureY = 0;

            int j4 = MathHelper.ceil((float) (wholeHearts + 1) / 10.0F) - 1;
            int guiX = x + wholeHearts % 10 * 8;
            int guiY = y - j4 * i2;

            this.blit(guiX, guiY, 16 + textureY * 9, 9 * textureY, 9, 9);

            if (wholeHearts * 2 + 1 < catHealth)
                this.blit(guiX, guiY, textureX + 36, 9 * textureY, 9, 9);

            if (wholeHearts * 2 + 1 == catHealth)
                this.blit(guiX, guiY, textureX + 45, 9 * textureY, 9, 9);
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class NextPageButton extends Button {
        private final boolean isForward;

        NextPageButton(int x, int y, boolean nextButton, Button.IPressable iPressable) {
            super(x, y, 20, 12, "", iPressable);
            isForward = nextButton;
        }

        @Override
        public void renderButton(int mouseX, int mouseY, float partialTicks) {
            if (visible) {
                boolean isButtonPressed = (mouseX >= x && mouseY >= y
                        && mouseX < x + width && mouseY < y + height);

                RenderSystem.color4f(1, 1, 1, 1);
                Minecraft.getInstance().getTextureManager().bindTexture(BG_TEXTURE);
                int textureX = 2;
                int textureY = 193;

                if (isButtonPressed)
                    textureX += 23;

                if (!isForward)
                    textureY += 13;

                blit(x, y, textureX, textureY, width, height, 288, 256);
            }
        }

        @Override
        public void playDownSound(SoundHandler handler) {
            handler.play(SimpleSound.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class HomePageButton extends Button {
        boolean isActive;

        HomePageButton(int x, int y, Button.IPressable iPressable) {
            super(x, y, 16, 16, "", iPressable);
            this.isActive = false;
        }

        @Override
        public void renderButton(int mouseX, int mouseY, float partialTicks) {
            if (visible) {
                RenderSystem.color4f(1, 1, 1, 1);
                Minecraft.getInstance().getTextureManager().bindTexture(BG_TEXTURE);
                int textureX = 3;
                int textureY = 239;

                if (isActive)
                    textureX += 21;

                blit(x, y, textureX, textureY, width, height, 288, 256);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class AddBookmarkButton extends Button {
        AddBookmarkButton(int x, int y, Button.IPressable iPressable) {
            super(x, y, 15, 15, "", iPressable);
        }

        @Override
        public void renderButton(int mouseX, int mouseY, float partialTicks) {
            if (visible) {
                boolean isButtonPressed = (mouseX >= x && mouseY >= y
                        && mouseX < x + width && mouseY < y + height);

                RenderSystem.color4f(1, 1, 1, 1);
                Minecraft.getInstance().getTextureManager().bindTexture(BG_TEXTURE);
                int textureX = 42;
                int textureY = 220;

                if (isButtonPressed)
                    textureX += 14;

                blit(x, y, textureX, textureY, width, height, 288, 256);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class BookmarkButton extends Button {
        boolean isActive;

        BookmarkButton(int x, int y, Button.IPressable iPressable) {
            super(x, y, 16, 16, "", iPressable);
            this.isActive = false;
        }

        @Override
        public void renderButton(int mouseX, int mouseY, float partialTicks) {
            if (visible) {
                RenderSystem.color4f(1, 1, 1, 1);
                Minecraft.getInstance().getTextureManager().bindTexture(BG_TEXTURE);
                int textureX = 3;
                int textureY = 220;

                if (isActive)
                    textureX += 21;

                blit(x, y, textureX, textureY, width, height, 288, 256);
            }
        }
    }
}
