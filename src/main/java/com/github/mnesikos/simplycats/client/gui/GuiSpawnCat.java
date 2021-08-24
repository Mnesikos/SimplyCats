//package com.github.mnesikos.simplycats.client.gui;
//
//import com.github.mnesikos.simplycats.Ref;
//import com.github.mnesikos.simplycats.entity.AbstractCat;
//import com.github.mnesikos.simplycats.entity.EntityCat;
//import com.github.mnesikos.simplycats.entity.core.Genetics;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiButton;
//import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.gui.inventory.GuiInventory;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import org.lwjgl.input.Keyboard;
//
//import java.io.IOException;
//
//@SideOnly(Side.CLIENT)
//public class GuiSpawnCat extends GuiScreen {
//    private static final int imageHeight = 181;
//    private static final int imageWidth = 229;
//    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Ref.MODID, "textures/gui/custom_spawn.png");
//
//    private GeneButton eumelanin1, eumelanin2, phaeomelanin1, phaeomelanin2, dilution1, dilution2, diluteMod1, diluteMod2, agouti1, agouti2, tabby1, tabby2, spotted1, spotted2, ticked1, ticked2, colorpoint1, colorpoint2, white1, white2, furLength1, furLength2, bobtail1, bobtail2;
//    private GeneButton[] geneButtons;
//
//    public EntityCat cat;
//    public EntityPlayer player;
//    public World world;
//    public BlockPos position;
//
//    public GuiSpawnCat(EntityPlayer player, int x, int y, int z) {
//        this.cat = new EntityCat(player.getEntityWorld());
//        this.player = player;
//        this.world = player.getEntityWorld();
//        this.position = new BlockPos(x + 0.5D, y, z + 0.5D);
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//    }
//
//    @Override
//    public void initGui() {
//        buttonList.clear();
//        super.initGui();
//        Keyboard.enableRepeatEvents(true);
//
//        int centerX = (width - imageWidth) / 2;
//        this.addButton(new RandomizeButton(0, centerX + 94, 11)); //randomize
//        this.addButton(new ConfirmButton(1, centerX + 8, 99, false)); //cancel
//        this.addButton(new ConfirmButton(2, centerX + 97, 99, true)); //confirm
//        this.addButton(new NextButton(3, centerX + 24, 116, false)); //eye color
//        this.addButton(new NextButton(4, centerX + 84, 116, true)); //eye color
//        this.addButton(new NextButton(29, centerX + 4, 130, false)); //face
//        this.addButton(new NextButton(30, centerX + 30, 130, true)); //face
//        this.addButton(new NextButton(31, centerX + 34, 130, false)); //body
//        this.addButton(new NextButton(32, centerX + 44, 130, true)); //body
//        this.addButton(new NextButton(33, centerX + 48, 130, false)); //tail
//        this.addButton(new NextButton(34, centerX + 58, 130, true)); //tail
//        this.geneButtons = new GeneButton[]{eumelanin1, eumelanin2, phaeomelanin1, phaeomelanin2, dilution1, dilution2, diluteMod1, diluteMod2, agouti1, agouti2, tabby1, tabby2, spotted1, spotted2, ticked1, ticked2, colorpoint1, colorpoint2, white1, white2, furLength1, furLength2, bobtail1, bobtail2};
//        for (int i = 0; i < 12; i++)
//            geneButtons[i + i] = this.addButton(new GeneButton(5 + i, centerX + 187, 9 + (14 * i)));
//        for (int i = 0; i < 12; i++)
//            geneButtons[i + i + 1] = this.addButton(new GeneButton(17 + i, centerX + 205, 9 + (14 * i)));
//
//        updateButtons();
//    }
//
//    @Override
//    public void onGuiClosed() {
//        Keyboard.enableRepeatEvents(false);
//    }
//
//    private void updateButtons() {
//        String[] eumelanin = cat.getGenotype(AbstractCat.EUMELANIN).split("-");
//        this.geneButtons[0].displayString = eumelanin[0];
//        this.geneButtons[1].displayString = eumelanin[1];
//        String[] phaeomelanin = cat.getGenotype(AbstractCat.PHAEOMELANIN).split("-");
//        this.geneButtons[2].displayString = phaeomelanin[0];
//        this.geneButtons[3].displayString = phaeomelanin[1];
//        String[] dilution = cat.getGenotype(AbstractCat.DILUTION).split("-");
//        this.geneButtons[4].displayString = dilution[0];
//        this.geneButtons[5].displayString = dilution[1];
//        String[] dilute_mod = cat.getGenotype(AbstractCat.DILUTE_MOD).split("-");
//        this.geneButtons[6].displayString = dilute_mod[0];
//        this.geneButtons[7].displayString = dilute_mod[1];
//        String[] agouti = cat.getGenotype(AbstractCat.AGOUTI).split("-");
//        this.geneButtons[8].displayString = agouti[0];
//        this.geneButtons[9].displayString = agouti[1];
//        String[] tabby = cat.getGenotype(AbstractCat.TABBY).split("-");
//        this.geneButtons[10].displayString = tabby[0];
//        this.geneButtons[11].displayString = tabby[1];
//        String[] spotted = cat.getGenotype(AbstractCat.SPOTTED).split("-");
//        this.geneButtons[12].displayString = spotted[0];
//        this.geneButtons[13].displayString = spotted[1];
//        String[] ticked = cat.getGenotype(AbstractCat.TICKED).split("-");
//        this.geneButtons[14].displayString = ticked[0];
//        this.geneButtons[15].displayString = ticked[1];
//        String[] colorpoint = cat.getGenotype(AbstractCat.COLORPOINT).split("-");
//        this.geneButtons[16].displayString = colorpoint[0];
//        this.geneButtons[17].displayString = colorpoint[1];
//        String[] white = cat.getGenotype(AbstractCat.WHITE).split("-");
//        this.geneButtons[18].displayString = white[0];
//        this.geneButtons[19].displayString = white[1];
//        String[] fur_length = cat.getGenotype(AbstractCat.FUR_LENGTH).split("-");
//        this.geneButtons[20].displayString = fur_length[0];
//        this.geneButtons[21].displayString = fur_length[1];
//        String[] bobtail = cat.getGenotype(AbstractCat.BOBTAIL).split("-");
//        this.geneButtons[22].displayString = bobtail[0];
//        this.geneButtons[23].displayString = bobtail[1];
//    }
//
//    @Override
//    protected void actionPerformed(GuiButton button) throws IOException {
//        if (button.enabled) {
//            if (button.id == 0) {
//                cat.setPhenotype();
//            }
//
//            if (button.id == 1) {
//                this.mc.displayGuiScreen(null);
//            } else if (button.id == 2) {
//                /*if (!world.isRemote) { // soon but not yet... fu packets
//                    cat.setLocationAndAngles(this.position.getX(), this.position.getY(), this.position.getZ(), MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
//                    cat.rotationYawHead = cat.rotationYaw;
//                    cat.renderYawOffset = cat.rotationYaw;
//                    world.spawnEntity(cat);
//                    cat.setTamed(true, player);
//                    cat.setHomePos(this.position);
//                    player.sendStatusMessage(new TextComponentTranslation("chat.info.set_home", cat.getName(), this.position.getX(), this.position.getY(), this.position.getZ()), true);
//                    cat.getNavigator().clearPath();
//                    cat.setOwnerId(player.getUniqueID());
//                    cat.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(cat)), null);
//                    float health = cat.getMaxHealth();
//                    cat.setHealth(health);
//                }*/
//                this.mc.displayGuiScreen(null);
//            }
//
//            if (button.id == 3) {
//                int eyeColor = Genetics.EyeColor.valueOf(cat.getPhenotype(AbstractCat.EYE_COLOR).toUpperCase()).ordinal();
//                cat.setGenotype(AbstractCat.EYE_COLOR, Genetics.EyeColor.init(eyeColor > 0 ? eyeColor - 1 : eyeColor));
//            } else if (button.id == 4) {
//                int eyeColor = Genetics.EyeColor.valueOf(cat.getPhenotype(AbstractCat.EYE_COLOR).toUpperCase()).ordinal();
//                cat.setGenotype(AbstractCat.EYE_COLOR, Genetics.EyeColor.init(eyeColor < 4 ? eyeColor + 1 : eyeColor));
//            }
//
//            if (button.id == 5 || button.id == 17) {
//                String[] eumelanin = cat.getGenotype(AbstractCat.EUMELANIN).split("-");
//                if (button.id == 5) {
//                    int gene = Genetics.Eumelanin.alleleToOrdinal(eumelanin[0]);
//                    eumelanin[0] = Genetics.Eumelanin.getByOrdinal(gene >= 0 && gene < 2 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.Eumelanin.alleleToOrdinal(eumelanin[1]);
//                    eumelanin[1] = Genetics.Eumelanin.getByOrdinal(gene >= 0 && gene < 2 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.EUMELANIN, eumelanin[0] + "-" + eumelanin[1]);
//            }
//
//            if (button.id == 6 || button.id == 18) {
//                String[] phaeomelanin = cat.getGenotype(AbstractCat.PHAEOMELANIN).split("-");
//                if (button.id == 6) {
//                    int gene = Genetics.Phaeomelanin.alleleToOrdinal(phaeomelanin[0]);
//                    phaeomelanin[0] = Genetics.Phaeomelanin.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.Phaeomelanin.alleleToOrdinal(phaeomelanin[1]);
//                    phaeomelanin[1] = Genetics.Phaeomelanin.getByOrdinal(gene >= 0 && gene < 2 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.PHAEOMELANIN, phaeomelanin[0] + "-" + phaeomelanin[1]);
//            }
//
//            if (button.id == 7 || button.id == 19) {
//                String[] dilution = cat.getGenotype(AbstractCat.DILUTION).split("-");
//                if (button.id == 7) {
//                    int gene = Genetics.Dilution.alleleToOrdinal(dilution[0]);
//                    dilution[0] = Genetics.Dilution.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.Dilution.alleleToOrdinal(dilution[1]);
//                    dilution[1] = Genetics.Dilution.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.DILUTION, dilution[0] + "-" + dilution[1]);
//            }
//
//            if (button.id == 8 || button.id == 20) {
//                String[] dilute_mod = cat.getGenotype(AbstractCat.DILUTE_MOD).split("-");
//                if (button.id == 8) {
//                    int gene = Genetics.DiluteMod.alleleToOrdinal(dilute_mod[0]);
//                    dilute_mod[0] = Genetics.DiluteMod.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.DiluteMod.alleleToOrdinal(dilute_mod[1]);
//                    dilute_mod[1] = Genetics.DiluteMod.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.DILUTE_MOD, dilute_mod[0] + "-" + dilute_mod[1]);
//            }
//
//            if (button.id == 9 || button.id == 21) {
//                String[] agouti = cat.getGenotype(AbstractCat.AGOUTI).split("-");
//                if (button.id == 9) {
//                    int gene = Genetics.Agouti.alleleToOrdinal(agouti[0]);
//                    agouti[0] = Genetics.Agouti.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.Agouti.alleleToOrdinal(agouti[1]);
//                    agouti[1] = Genetics.Agouti.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.AGOUTI, agouti[0] + "-" + agouti[1]);
//            }
//
//            if (button.id == 10 || button.id == 22) {
//                String[] tabby = cat.getGenotype(AbstractCat.TABBY).split("-");
//                if (button.id == 10) {
//                    int gene = Genetics.Tabby.alleleToOrdinal(tabby[0]);
//                    tabby[0] = Genetics.Tabby.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.Tabby.alleleToOrdinal(tabby[1]);
//                    tabby[1] = Genetics.Tabby.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.TABBY, tabby[0] + "-" + tabby[1]);
//            }
//
//            if (button.id == 11 || button.id == 23) {
//                String[] spotted = cat.getGenotype(AbstractCat.SPOTTED).split("-");
//                if (button.id == 11) {
//                    int gene = Genetics.Spotted.alleleToOrdinal(spotted[0]);
//                    spotted[0] = Genetics.Spotted.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.Spotted.alleleToOrdinal(spotted[1]);
//                    spotted[1] = Genetics.Spotted.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.SPOTTED, spotted[0] + "-" + spotted[1]);
//            }
//
//            if (button.id == 12 || button.id == 24) {
//                String[] ticked = cat.getGenotype(AbstractCat.TICKED).split("-");
//                if (button.id == 12) {
//                    int gene = Genetics.Ticked.alleleToOrdinal(ticked[0]);
//                    ticked[0] = Genetics.Ticked.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.Ticked.alleleToOrdinal(ticked[1]);
//                    ticked[1] = Genetics.Ticked.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.TICKED, ticked[0] + "-" + ticked[1]);
//            }
//
//            if (button.id == 13 || button.id == 25) {
//                String[] colorpoint = cat.getGenotype(AbstractCat.COLORPOINT).split("-");
//                if (button.id == 13) {
//                    int gene = Genetics.Colorpoint.alleleToOrdinal(colorpoint[0]);
//                    colorpoint[0] = Genetics.Colorpoint.getByOrdinal(gene >= 0 && gene < 2 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.Colorpoint.alleleToOrdinal(colorpoint[1]);
//                    colorpoint[1] = Genetics.Colorpoint.getByOrdinal(gene >= 0 && gene < 2 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.COLORPOINT, colorpoint[0] + "-" + colorpoint[1]);
//            }
//
//            if (button.id == 14 || button.id == 26) {
//                String[] white = cat.getGenotype(AbstractCat.WHITE).split("-");
//                if (button.id == 14) {
//                    int gene = Genetics.White.alleleToOrdinal(white[0]);
//                    white[0] = Genetics.White.getByOrdinal(gene >= 0 && gene < 2 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.White.alleleToOrdinal(white[1]);
//                    white[1] = Genetics.White.getByOrdinal(gene >= 0 && gene < 2 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.WHITE, white[0] + "-" + white[1]);
//                cat.selectWhiteMarkings();
//            }
//
//            if (button.id == 15 || button.id == 27) {
//                String[] fur_length = cat.getGenotype(AbstractCat.FUR_LENGTH).split("-");
//                if (button.id == 15) {
//                    int gene = Genetics.FurLength.alleleToOrdinal(fur_length[0]);
//                    fur_length[0] = Genetics.FurLength.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.FurLength.alleleToOrdinal(fur_length[1]);
//                    fur_length[1] = Genetics.FurLength.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.FUR_LENGTH, fur_length[0] + "-" + fur_length[1]);
//            }
//
//            if (button.id == 16 || button.id == 28) {
//                String[] bobtail = cat.getGenotype(AbstractCat.BOBTAIL).split("-");
//                if (button.id == 16) {
//                    int gene = Genetics.Bobtail.alleleToOrdinal(bobtail[0]);
//                    bobtail[0] = Genetics.Bobtail.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                } else {
//                    int gene = Genetics.Bobtail.alleleToOrdinal(bobtail[1]);
//                    bobtail[1] = Genetics.Bobtail.getByOrdinal(gene == 0 ? gene + 1 : 0);
//                }
//                cat.setGenotype(AbstractCat.BOBTAIL, bobtail[0] + "-" + bobtail[1]);
//            }
//
//            cat.resetTexturePrefix();
////            updateButtons(button);
//            initGui();
//        }
//    }
//
//    @Override
//    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        GlStateManager.color(1, 1, 1, 1);
//        int leftX = (width - imageWidth) / 2;
//        this.mc.getTextureManager().bindTexture(BG_TEXTURE);
//        drawModalRectWithCustomSizedTexture(leftX, 2, 0, 0, imageWidth, imageHeight, 256, 256);
//
//        if (player != null && world != null && position != null) {
//            GuiInventory.drawEntityOnScreen(leftX + 60, 100, 75, (leftX + 120) - mouseX, 50 - mouseY, cat);
//
//        }
//
//        super.drawScreen(mouseX, mouseY, partialTicks);
//    }
//
//    @SideOnly(Side.CLIENT)
//    static class NextButton extends GuiButton {
//        public final boolean isNextButton;
//
//        public NextButton(int buttonId, int x, int y, boolean isNextButton) {
//            this(buttonId, x, y, 10, 10, "", isNextButton);
//        }
//
//        public NextButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean isNextButton) {
//            super(buttonId, x, y, widthIn, heightIn, buttonText);
//            this.isNextButton = isNextButton;
//        }
//
//        @Override
//        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
//            if (visible) {
//                boolean isButtonPressed = (mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height);
//                GlStateManager.color(1, 1, 1, 1);
//                mc.getTextureManager().bindTexture(BG_TEXTURE);
//                int textureX = 0;
//                int textureY = 181;
//
//                if (isButtonPressed)
//                    textureY += 10;
//
//                if (!isNextButton)
//                    textureX += 10;
//
//                drawModalRectWithCustomSizedTexture(x, y, textureX, textureY, width, height, 256, 256);
//            }
//        }
//    }
//
//    @SideOnly(Side.CLIENT)
//    static class ConfirmButton extends NextButton {
//        public ConfirmButton(int buttonId, int x, int y, boolean isNextButton) {
//            super(buttonId, x, y, 15, 15, "", isNextButton);
//        }
//
//        @Override
//        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
//            if (visible) {
//                GlStateManager.color(1, 1, 1, 1);
//                mc.getTextureManager().bindTexture(BG_TEXTURE);
//                int textureX = 20;
//                int textureY = 181;
//
//                if (isNextButton)
//                    textureX += 16;
//
//                drawModalRectWithCustomSizedTexture(x, y, textureX, textureY, width, height, 256, 256);
//            }
//        }
//    }
//
//    @SideOnly(Side.CLIENT)
//    static class RandomizeButton extends GuiButton {
//        public RandomizeButton(int buttonId, int x, int y) {
//            super(buttonId, x, y, 16, 14, "");
//        }
//
//        @Override
//        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
//            if (visible) {
//                GlStateManager.color(1, 1, 1, 1);
//                mc.getTextureManager().bindTexture(BG_TEXTURE);
//                int textureX = 51;
//                int textureY = 181;
//
//                drawModalRectWithCustomSizedTexture(x, y, textureX, textureY, width, height, 256, 256);
//            }
//        }
//    }
//
//    @SideOnly(Side.CLIENT)
//    static class GeneButton extends GuiButton {
//        public GeneButton(int buttonId, int x, int y) {
//            super(buttonId, x, y, 18, 14, "Ta");
//        }
//
//        @Override
//        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
//            if (visible) {
//                GlStateManager.color(1, 1, 1, 1);
//                mc.getTextureManager().bindTexture(BG_TEXTURE);
//                int textureX = 21;
//                int textureY = 197;
//
//                drawModalRectWithCustomSizedTexture(x, y, textureX, textureY, width, height, 256, 256);
//                mc.fontRenderer.drawString(this.displayString, ((this.x + this.width / 2) - mc.fontRenderer.getStringWidth(this.displayString) / 2), this.y + (this.height - 8) / 2, 0x000000);
//            }
//        }
//    }
//}