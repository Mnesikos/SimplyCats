package com.github.mnesikos.simplycats.entity.ai;

import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.GroundPathNavigator;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class CatTemptGoal extends Goal {
    private static final EntityPredicate ENTITY_PREDICATE = (new EntityPredicate()).setDistance(10.0D).allowInvulnerable().allowFriendlyFire().setSkipAttackChecks().setLineOfSiteRequired();
    protected final CreatureEntity creature;
    private final double speed;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double pitch;
    private double yaw;
    protected PlayerEntity closestPlayer;
    private int delayTemptCounter;
    private boolean isRunning;
    private final Item temptItem;
    private final boolean scaredByPlayerMovement;

    public CatTemptGoal(EntityCat cat, double speedIn, Item temptItemsIn, boolean scaredByMovement) {
        this.creature = cat;
        this.speed = speedIn;
        this.temptItem = temptItemsIn;
        this.scaredByPlayerMovement = scaredByMovement;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(cat.getNavigator() instanceof GroundPathNavigator))
            throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
    }
    public boolean shouldExecute() {
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        } else {
            this.closestPlayer = this.creature.world.getClosestPlayer(ENTITY_PREDICATE, this.creature);
            if (this.closestPlayer == null)
                return false;
            else
                return this.isTempting(this.closestPlayer.getHeldItemMainhand()) || this.isTempting(this.closestPlayer.getHeldItemOffhand());
        }
    }

    private boolean isTempting(ItemStack stack) {
        return stack.getItem() == temptItem;
    }

    public boolean shouldContinueExecuting() {
        if (this.isScaredByPlayerMovement()) {
            if (this.creature.getDistanceSq(this.closestPlayer) < 36.0D) {
                if (this.closestPlayer.getDistanceSq(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D)
                    return false;

                if (Math.abs((double)this.closestPlayer.rotationPitch - this.pitch) > 5.0D || Math.abs((double)this.closestPlayer.rotationYaw - this.yaw) > 5.0D)
                    return false;
            } else {
                this.targetX = this.closestPlayer.posX;
                this.targetY = this.closestPlayer.posY;
                this.targetZ = this.closestPlayer.posZ;
            }

            this.pitch = (double)this.closestPlayer.rotationPitch;
            this.yaw = (double)this.closestPlayer.rotationYaw;
        }

        return this.shouldExecute();
    }

    protected boolean isScaredByPlayerMovement() {
        return this.scaredByPlayerMovement;
    }

    public void startExecuting() {
        this.targetX = this.closestPlayer.posX;
        this.targetY = this.closestPlayer.posY;
        this.targetZ = this.closestPlayer.posZ;
        this.isRunning = true;
    }

    public void resetTask() {
        this.closestPlayer = null;
        this.creature.getNavigator().clearPath();
        this.delayTemptCounter = 100;
        this.isRunning = false;
    }

    public void tick() {
        this.creature.getLookController().setLookPositionWithEntity(this.closestPlayer, (float)(this.creature.getHorizontalFaceSpeed() + 20), (float)this.creature.getVerticalFaceSpeed());
        if (this.creature.getDistanceSq(this.closestPlayer) < 6.25D)
            this.creature.getNavigator().clearPath();
        else
            this.creature.getNavigator().tryMoveToEntityLiving(this.closestPlayer, this.speed);

    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
