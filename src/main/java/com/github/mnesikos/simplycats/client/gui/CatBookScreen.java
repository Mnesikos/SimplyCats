package com.github.mnesikos.simplycats.client.gui;

import com.github.mnesikos.simplycats.SimplyCats;
import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CatBookScreen extends Screen {
    private static final int bookImageHeight = 182;
    private static final int bookImageWidth = 281;
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(SimplyCats.MOD_ID, "textures/gui/cat_book.png");

    private Level world;
    private int currPage;
    private final ListTag bookPages = new ListTag();
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;

    public SimplyCatEntity cat;
    protected int catHealth;
//    public static ItemStack book;

    public CatBookScreen(CompoundTag bookTag, Level world, int catInList) {
        this(bookTag, world);
        this.currPage = catInList;
    }

    public CatBookScreen(CompoundTag bookTag, Level world) {
        super(NarratorChatListener.NO_TITLE);
        if (bookTag != null && !bookTag.isEmpty()) {
            ListTag pages = bookTag.getList("pages", 8).copy();
            this.bookPages.addAll(pages);
            this.world = world;
        }
    }

    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        int centerX = (width - bookImageWidth) / 2;
        this.buttonNextPage = this.addRenderableWidget(new NextPageButton(centerX + 236, 157, true, (button) -> {
            this.pageForward();
        }));
        this.buttonPreviousPage = this.addRenderableWidget(new NextPageButton(centerX + 25, 157, false, (button) -> {
            this.pageBack();
        }));
        this.updateButtons();
    }

    private void updateButtons() {
        this.cat = (SimplyCatEntity) EntityType.loadEntityRecursive(bookPages.getCompound(this.currPage), world, entity1 -> entity1);
        this.buttonNextPage.visible = this.currPage < this.bookPages.size() - 1;
        this.buttonPreviousPage.visible = this.currPage > 0;
    }

    private void pageBack() {
        if (this.currPage > 0)
            --this.currPage;

        this.updateButtons();
    }

    private void pageForward() {
        if (this.currPage < this.bookPages.size() - 1)
            ++this.currPage;

        this.updateButtons();
    }

    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        int leftX = (width - bookImageWidth) / 2;
        int leftCenterX = leftX + (bookImageWidth / 4);

        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        blit(matrixStack, leftX, 2, 0, 0, bookImageWidth, bookImageHeight, 288, 256);

        if (bookPages.get(this.currPage) != null || !bookPages.getCompound(this.currPage).isEmpty() || cat != null) {
            InventoryScreen.renderEntityInInventory(leftX + 40, 74, 50, (leftX + 51) - mouseX, 50 - mouseY, cat);

            int nameWidth = this.font.width(cat.getName());
            this.font.draw(matrixStack, cat.getName(), leftCenterX - (nameWidth / 2), 14, 0);

            TextComponent sex = new TextComponent(new TranslatableComponent(cat.isFixed() ? "cat.fixed.name" : "cat.intact.name").getString()
                    + " "
                    + Genetics.Sex.getPrettyName(bookPages.getCompound(this.currPage).getString("Phaeomelanin")).getString());
            this.font.draw(matrixStack, sex, leftX + 66, 14 * 2, 0);

            this.renderCatHealth(matrixStack, leftX + 66, 14 * 3);

            //this.font.draw(matrixStack, "Purrsonality", leftX + 66, 14*4, 0); //todo

            String ownerName;
            if (cat.isTame()) {
                ownerName = cat.getOwnerName().getString();
                this.font.draw(matrixStack, new TranslatableComponent("tooltip.pet_carrier.owner", ownerName), leftX + 16, 14 * 6, 0);
            } else
                this.font.draw(matrixStack, new TranslatableComponent("entity.simplycats.cat.untamed"), leftX + 16, 14 * 6, 0);

            this.font.drawWordWrap(Genetics.getPhenotypeDescription(bookPages.getCompound(this.currPage), false), leftX + 16, 14 * 7, 120, 0);

            /*this.font.drawWordWrap("Vocal Level Bar Here",
                    leftX + 16, 14*9, 120, 0);
            this.font.drawWordWrap("Activity Level Bar Here",
                    leftX + 16, 14*9+9, 120, 0);*/ //todo

            //this.font.drawWordWrap("Pregnancy Data", leftX + 16, 14*11-5, 120, 0); //todo

            String eyeColor = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("EyeColor");
            String furLength = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("FurLength");
            String eumelanin = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("Eumelanin");
            String phaeomelanin = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("Phaeomelanin");
            String dilution = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("Dilution");
            String diluteMod = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("DiluteMod");
            String agouti = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("Agouti");
            String tabby = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("Tabby");
            String spotted = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("Spotted");
            String ticked = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("Ticked");
            String inhibitor = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("Inhibitor");
            String colorpoint = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("Colorpoint");
            String white = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("White");
            String bobtail = ChatFormatting.GRAY + bookPages.getCompound(this.currPage).getString("Bobtail");

            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.eye_color", eyeColor), leftX + 152, 24 - 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.fur_length", furLength), leftX + 152, 34 - 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.eumelanin", eumelanin), leftX + 152, 44 - 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.phaeomelanin", phaeomelanin), leftX + 152, 54 - 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.dilute", dilution), leftX + 152, 64 - 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.dilute_modifier", diluteMod), leftX + 152, 74 - 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.agouti", agouti), leftX + 152, 84 - 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.tabby", tabby), leftX + 152, 94 - 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.spotted", spotted), leftX + 152, 104 - 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.ticked", ticked), leftX + 152, 114 - 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.inhibitor", inhibitor), leftX + 152, 119, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.colorpoint", colorpoint), leftX + 152, 124 + 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.white", white), leftX + 152, 134 + 5, 0);
            this.font.draw(matrixStack, new TranslatableComponent("book.genetics.bobtail", bobtail), leftX + 152, 144 + 5, 0);

            //this.font.drawWordWrap("Heritage Data", leftX + 152, 14*11-5, 120, 0); //todo

        } /*else if (bookPages != null) {*/
//            this.font.drawWordWrap(new TranslationTextComponent("book.index_page.info"), leftX + 16, 60, 120, 0);
//        } else
//            this.font.drawWordWrap(new StringTextComponent("Error page, this should not happen, please report to github issue tracker, thanks."), leftX + 16, 60, 120, 0); //todo remove when done

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderCatHealth(PoseStack matrixStack, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
        this.catHealth = Mth.ceil(cat.getHealth());

        float maxHealth = (float) cat.getAttributeValue(Attributes.MAX_HEALTH);
        int l1 = Mth.ceil((maxHealth) / 2.0F / 10.0F);
        int i2 = Math.max(10 - (l1 - 2), 3);

        for (int wholeHearts = Mth.ceil((maxHealth) / 2.0F) - 1; wholeHearts >= 0; --wholeHearts) {
            int textureX = 16;
            int textureY = 0;

            int j4 = Mth.ceil((float) (wholeHearts + 1) / 10.0F) - 1;
            int guiX = x + wholeHearts % 10 * 8;
            int guiY = y - j4 * i2;

            this.blit(matrixStack, guiX, guiY, 16 + textureY * 9, 9 * textureY, 9, 9);

            if (wholeHearts * 2 + 1 < catHealth)
                this.blit(matrixStack, guiX, guiY, textureX + 36, 9 * textureY, 9, 9);

            if (wholeHearts * 2 + 1 == catHealth)
                this.blit(matrixStack, guiX, guiY, textureX + 45, 9 * textureY, 9, 9);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public class NextPageButton extends Button {
        private final boolean isNextButton;

        NextPageButton(int x, int y, boolean nextButton, OnPress pressable) {
            super(x, y, 20, 12, TextComponent.EMPTY, pressable);
            isNextButton = nextButton;
        }

        @Override
        public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            if (visible) {
                boolean isButtonPressed = (mouseX >= x && mouseY >= y
                        && mouseX < x + width && mouseY < y + height);

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1, 1, 1, 1);
                RenderSystem.setShaderTexture(0, BG_TEXTURE);
                int textureX = 2;
                int textureY = 193;

                if (isButtonPressed)
                    textureX += 23;

                if (!isNextButton)
                    textureY += 13;

                blit(matrixStack, x, y, textureX, textureY, width, height, 288, 256);
            }
        }

        @Override
        public void playDownSound(SoundManager soundHandler) {
            soundHandler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }
}
