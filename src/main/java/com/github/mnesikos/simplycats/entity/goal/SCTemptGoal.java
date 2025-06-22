package com.github.mnesikos.simplycats.entity.goal;

import com.github.mnesikos.simplycats.entity.SimplyCatEntity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class SCTemptGoal extends TemptGoal {
    @Nullable
    private Player selectedPlayer;
    private final SimplyCatEntity cat;

    public SCTemptGoal(SimplyCatEntity cat, double speed, Ingredient temptItems, boolean canScare) {
        super(cat, speed, temptItems, canScare);
        this.cat = cat;
    }

    @Override
    public void tick() {
        super.tick();
        if (selectedPlayer == null && mob.getRandom().nextInt(adjustedTickDelay(600)) == 0)
            selectedPlayer = player;
        else if (mob.getRandom().nextInt(adjustedTickDelay(500)) == 0)
            selectedPlayer = null;
    }

    @Override
    protected boolean canScare() {
        return !cat.isTame() && (selectedPlayer == null || !selectedPlayer.equals(player)) && super.canScare();
    }
}
