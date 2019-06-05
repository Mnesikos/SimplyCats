package com.github.mnesikos.simplycats.client.render.entity;

import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@SideOnly(Side.CLIENT)
public class TextureHelper {

    private static class LayerTexture extends AbstractTexture {
        protected final ResourceLocation BASE_TEX;
        protected final ResourceLocation TABBY_TEX;
        protected final ResourceLocation TORTIE_TEX;
        protected final ResourceLocation WHITE_TEX;
        protected final ResourceLocation EYES_TEX;

        public LayerTexture(EntityCat cat, ResourceLocation base, ResourceLocation eyes) {
            this.BASE_TEX = base;
            this.TABBY_TEX = getOverlay("tabby", cat.getMarkingNum("tabby"));
            this.TORTIE_TEX = getOverlay("tortie", cat.getMarkingNum("tortie"));
            this.WHITE_TEX = getOverlay("white", cat.getMarkingNum("white"));
            this.EYES_TEX = eyes;
        }

        @Override
        public void loadTexture(IResourceManager resManager) throws IOException {
            this.deleteGlTexture();
            InputStream inputstreamBase = null;
            InputStream inputstreamTabby = null;
            InputStream inputStreamTortie = null;
            InputStream inputstreamWhite = null;
            InputStream inputstreamEyes = null;

            try {
                IResource iresource_base = resManager.getResource(this.BASE_TEX);
                inputstreamBase = iresource_base.getInputStream();
                BufferedImage base = ImageIO.read(inputstreamBase);

                int w = base.getWidth();
                int h = base.getHeight();

                IResource iresource_eyes = resManager.getResource(this.EYES_TEX);
                inputstreamEyes = iresource_eyes.getInputStream();
                Image eyes = ImageIO.read(inputstreamEyes);

                try {
                    if (this.TABBY_TEX == null && this.WHITE_TEX == null && this.TORTIE_TEX == null) {
                        BufferedImage selfCombined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                        Graphics g = selfCombined.getGraphics();
                        g.drawImage(base, 0, 0, null);
                        g.drawImage(eyes, 0, 0, null);
                        base = selfCombined;
                    } else {
                        try {
                            if (this.TABBY_TEX != null) {
                                IResource iresource_tabby = resManager.getResource(this.TABBY_TEX);
                                inputstreamTabby = iresource_tabby.getInputStream();
                                Image tabby = ImageIO.read(inputstreamTabby);

                                BufferedImage tabbyAdded = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                                Graphics g2 = tabbyAdded.getGraphics();
                                g2.drawImage(base, 0, 0, null);
                                g2.drawImage(tabby, 0, 0, null);
                                base = tabbyAdded;
                            }
                        } catch (Exception e) {
                            System.out.println(e + ": Failed combining tabby overlay: " + TABBY_TEX);
                        }
                        try {
                            if (this.TORTIE_TEX != null) {
                                IResource iresource_tortie = resManager.getResource(this.TORTIE_TEX);
                                inputStreamTortie = iresource_tortie.getInputStream();
                                Image tortie = ImageIO.read(inputStreamTortie);

                                BufferedImage tortieAdded = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                                Graphics g2 = tortieAdded.getGraphics();
                                g2.drawImage(base, 0, 0, null);
                                g2.drawImage(tortie, 0, 0, null);
                                base = tortieAdded;
                            }
                        } catch (Exception e) {
                            System.out.println(e + ": Failed combining tortie overlay: " + TORTIE_TEX);
                        }
                        try {
                            if (this.WHITE_TEX != null) {
                                IResource iresource_white = resManager.getResource(this.WHITE_TEX);
                                inputstreamWhite = iresource_white.getInputStream();
                                Image white = ImageIO.read(inputstreamWhite);

                                BufferedImage whiteAdded = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                                Graphics g2 = whiteAdded.getGraphics();
                                g2.drawImage(base, 0, 0, null);
                                g2.drawImage(white, 0, 0, null);
                                base = whiteAdded;
                            }
                        } catch (Exception e) {
                            System.out.println(e + ": Failed combining white overlay: " + WHITE_TEX);
                        }
                        try {
                            BufferedImage eyesCombined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                            Graphics g3 = eyesCombined.getGraphics();
                            g3.drawImage(base, 0, 0, null);
                            g3.drawImage(eyes, 0, 0, null);
                            base = eyesCombined;
                        } catch (Exception e) {
                            System.out.println(e + ": Failed combining eye overlay: " + EYES_TEX);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e + ":Failed combining any images, base texture: " + BASE_TEX);
                }

                boolean flag = false;
                boolean flag1 = false;

                if (iresource_base != null && iresource_base.hasMetadata()) {
                    try {
                        TextureMetadataSection texturemetadatasection = (TextureMetadataSection) iresource_base.getMetadata("texture");

                        if (texturemetadatasection != null) {
                            flag = texturemetadatasection.getTextureBlur();
                            flag1 = texturemetadatasection.getTextureClamp();
                        }
                    } catch (RuntimeException runtimeexception) {
                        System.out.println("Failed reading metadata of: " + this.BASE_TEX + ": " + runtimeexception);
                    }
                }

                TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), base, flag, flag1);

            } finally {
                if (inputstreamBase != null) {
                    inputstreamBase.close();
                }
                if (inputstreamTabby != null) {
                    inputstreamTabby.close();
                }
                if (inputStreamTortie != null) {
                    inputStreamTortie.close();
                }
                if (inputstreamWhite != null) {
                    inputstreamWhite.close();
                }
                if (inputstreamEyes != null) {
                    inputstreamEyes.close();
                }
            }
        }
    }

    public static void createTexture(EntityCat cat, ResourceLocation base, ResourceLocation newLoc) {
        TextureManager manager = Minecraft.getMinecraft().getTextureManager();
        if (manager.getTexture(newLoc) == null) {
            ResourceLocation eyes = RenderCat.EYES.get(cat.getMarkingNum("eyes"));
            ITextureObject texture;
            try {
                texture = new LayerTexture(cat, base, eyes);
            } catch (Exception e1) {
                System.out.println("Failed to create overlayed texture object: " + e1);
                texture = manager.getTexture(base);
            }

            manager.loadTexture(newLoc, texture);
        }
    }

    private static ResourceLocation getOverlay(String type, int i) {
        ResourceLocation loc = null;
        if (i == 0) {
            return null;
        }
        else if (i > 0 && type.equals("tabby")) {
            loc = RenderCat.TABBY.get((i - 1));
            if (loc == null) { return getOverlay("tabby", i); }
        }
        else if (i > 0 && type.equals("tortie")) {
            loc = RenderCat.TORTIE.get((i - 1));
            if (loc == null) {
                return getOverlay("tortie", i);
            }
        }
        else if (i > 0 && type.equals("white")) {
            loc = RenderCat.WHITE.get((i - 1));
            if (loc == null) { return getOverlay("white", i); }
        }
        return loc;
    }
}
