package com.github.mnesikos.simplycats.client.render.entity;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Credit for this goes to sekelsta and the horse-colors code, thank you!
 */
@OnlyIn(Dist.CLIENT)
public class LayeredTexture extends AbstractTexture {
    public final String[] texturePaths;

    public LayeredTexture(String[] texturePaths) {
        this.texturePaths = texturePaths;
        if (this.texturePaths.length <= 0)
            throw new IllegalStateException("No textures provided.");
    }

    @Override
    public void load(ResourceManager manager) throws IOException {
        NativeImage image = getLayer(manager, texturePaths);

        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> {
                this.loadImage(image);
            });
        } else
            this.loadImage(image);
    }

    public NativeImage getLayer(ResourceManager manager, String[] texturePaths) {
        List<String> layers = Arrays.asList(texturePaths);
        Iterator<String> iterator = layers.iterator();
        String baseLayer = iterator.next();
        NativeImage baseImage = tryLayer(manager, baseLayer);
        if (baseImage == null)
            return null;

        while (iterator.hasNext()) {
            String layer = iterator.next();
            if (layer == null)
                continue;
            NativeImage image = tryLayer(manager, layer);
            if (image != null)
                blendLayer(baseImage, image);
        }

        return baseImage;
    }

    public NativeImage tryLayer(ResourceManager manager, String layer) {
        if (layer == null)
            return null;

        try {
            Resource resource = manager.getResource(new ResourceLocation(layer)).orElseThrow();
            return NativeImage.read(resource.open());
        } catch (IOException exception) {
            throw new IllegalStateException("Couldn't load texture layers.", exception);
        }
    }

    public void blendLayer(NativeImage base, NativeImage image) {
        for (int i = 0; i < image.getHeight(); ++i) {
            for (int j = 0; j < image.getWidth(); ++j) {
                int color = image.getPixelRGBA(j, i);
                blendPixel(base, j, i, FastColor.ABGR32.color(FastColor.ABGR32.alpha(color), FastColor.ABGR32.blue(color), FastColor.ABGR32.green(color), FastColor.ABGR32.red(color)));
            }
        }
    }

    private void loadImage(NativeImage image) {
        TextureUtil.prepareImage(this.getId(), image.getWidth(), image.getHeight());
        image.upload(0, 0, 0, true);
    }

    public void blendPixel(NativeImage image, int x, int y, int color) {
        int baseColor = image.getPixelRGBA(x, y);
        float a = (float) FastColor.ABGR32.alpha(color) / 255.0F;
        float blue = (float) FastColor.ABGR32.blue(color);
        float green = (float) FastColor.ABGR32.green(color);
        float red = (float) FastColor.ABGR32.red(color);
        float baseAlpha = (float) FastColor.ABGR32.alpha(baseColor) / 255.0F;
        float baseBlue = (float) FastColor.ABGR32.blue(baseColor);
        float baseGreen = (float) FastColor.ABGR32.green(baseColor);
        float baseRed = (float) FastColor.ABGR32.red(baseColor);
        float alph = a * a + baseAlpha * (1 - a);
        int finalAlpha = (int) (alph * 255.0F);
        int finalBlue = (int) (blue * a + baseBlue * (1 - a));
        int finalGreen = (int) (green * a + baseGreen * (1 - a));
        int finalRed = (int) (red * a + baseRed * (1 - a));
        if (finalAlpha > 255) {
            finalAlpha = 255;
        }

        if (finalBlue > 255) {
            finalBlue = 255;
        }

        if (finalGreen > 255) {
            finalGreen = 255;
        }

        if (finalRed > 255) {
            finalRed = 255;
        }

        image.setPixelRGBA(x, y, FastColor.ABGR32.color(finalAlpha, finalBlue, finalGreen, finalRed));
    }
}
