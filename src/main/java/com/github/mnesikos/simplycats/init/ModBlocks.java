package com.github.mnesikos.simplycats.init;

import com.github.mnesikos.simplycats.block.BlockCropCatnip;
import com.github.mnesikos.simplycats.tileentity.TileEntityBowl;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.github.mnesikos.simplycats.Ref.MOD_ID;

public class ModBlocks {
    public static final DeferredRegister<Block> REGISTER = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);
    public static final RegistryObject<BlockCropCatnip> CROP_CATNIP = REGISTER.register("crop_catnip", BlockCropCatnip::new);

    /*public static final RegistryObject<BlockBowl> BOWL = REGISTER.register("cat_bowl", BlockBowl::new);

    public static final RegistryObject<BlockLitterBox> LITTER_BOX = REGISTER.register("litter_box", BlockLitterBox::new);

    public static final RegistryObject<BlockScratchingPost> SCRATCHING_POST = REGISTER.register("scratching_post", BlockScratchingPost::new);*/

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MOD_ID);
//    public static final RegistryObject<TileEntityType<TileEntityBowl>> BOWL_TYPE = TILE_ENTITY.register("cat_bowl", () -> TileEntityType.Builder.create(TileEntityBowl::new, BOWL.get()).build(null));
}
