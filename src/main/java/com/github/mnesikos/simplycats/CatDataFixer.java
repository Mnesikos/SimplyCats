package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import com.github.mnesikos.simplycats.entity.core.Genetics;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class CatDataFixer {
    @SubscribeEvent
    public static void fixCatData(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof SimplyCatEntity) {
            SimplyCatEntity cat = (SimplyCatEntity) event.getEntity();
            if (!cat.getPersistentData().contains("Inhibitor")) {
                cat.getPersistentData().putString("Inhibitor", Genetics.Inhibitor.NORMAL.getAllele() + "-" + Genetics.Inhibitor.init(new Random()));
            }
        }
    }
}
