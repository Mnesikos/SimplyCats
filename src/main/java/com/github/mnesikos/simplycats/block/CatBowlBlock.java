package com.github.mnesikos.simplycats.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CatBowlBlock extends Block {
    public static final EnumProperty<Fill> FILL = EnumProperty.create("fill", Fill.class);
    protected static final VoxelShape BOWL_AABB = box(5.5F, 0.0F, 5.5F, 10.5F, 2.0F, 10.5F);

    public CatBowlBlock() {
        super(Properties.of().noOcclusion().strength(0.2F));
        this.registerDefaultState(this.stateDefinition.any().setValue(FILL, Fill.EMPTY));
    }

    public void setFill(Level level, BlockPos pos, BlockState state, Fill fill) {
        level.setBlock(pos, state.setValue(FILL, fill), 2);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        ItemStack stack = player.getItemInHand(hand);
        Fill fill = state.getValue(FILL);

        if (fill.equals(Fill.WATER)) {
            if (stack.getItem() == Items.BUCKET) {
                if (!level.isClientSide) {
                    setFill(level, pos, state, Fill.EMPTY);
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);

            } else if (stack.getItem() == Items.GLASS_BOTTLE) {
                if (!level.isClientSide) {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    setFill(level, pos, state, Fill.EMPTY);
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);

            } else return InteractionResult.PASS;

        } else if (fill.equals(Fill.EMPTY)) {
            if (stack.getItem() == Items.WATER_BUCKET) {
                if (!level.isClientSide) {
                    setFill(level, pos, state, Fill.WATER);
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);

            } else if (stack.getItem() == Items.POTION && PotionUtils.getPotion(stack) == Potions.WATER) {
                if (!level.isClientSide) {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    setFill(level, pos, state, Fill.WATER);
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);

            } /*else if (stack == catfoodtag*//* && check if inventory has space*//*) { todo
                if (!level.isClientSide) {
                    if (!player.getAbilities().instabuild) stack.shrink(stack.getCount());
                    // add entire stack of contents to first open inventory slot
                    setFill(level, pos, state, Fill.FOOD);
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }*/
        } /*else if (fill.equals(Fill.FOOD)) { todo
            if (stack.isEmpty()) {
                if (!level.isClientSide) {
                    // remove first slot of items from inventory
                    // add to player's hand
                    // check if inventory is now empty & setFill(level, pos, state, Fill.EMPTY);
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }*/

        // open inventory todo

        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return BOWL_AABB;
    }

    public enum Fill implements StringRepresentable {
        EMPTY("empty"),
        FOOD("food"),
        WATER("water");

        private final String name;

        private Fill(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}
