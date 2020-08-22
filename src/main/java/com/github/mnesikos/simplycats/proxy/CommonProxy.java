package com.github.mnesikos.simplycats.proxy;

/*@Mod.EventBusSubscriber
public class CommonProxy implements IGuiHandler {
    public static final int FIXER_VERSION = 2;

    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new SimplyCatsEvents());

        int ENTITY_ID = 0;
        EntityRegistry.registerModEntity(new ResourceLocation(Ref.MODID + ":cat"), EntityCat.class, "Cat", ENTITY_ID++, SimplyCats.instance, 80, 1, true);
    }

    public void init(FMLInitializationEvent e) {
        ModItems.registerOres();
        ModRecipes.init();

        ModFixs fixer = FMLCommonHandler.instance().getDataFixer().init(Ref.MODID, FIXER_VERSION);
        fixer.registerFix(FixTypes.ENTITY, new CatDataFixer());

        NetworkRegistry.INSTANCE.registerGuiHandler(SimplyCats.instance, SimplyCats.PROXY);

        ModProfessions.associateCareersAndTrades();
        VillagerRegistry.instance().registerVillageCreationHandler(new VillagePetShelterHandler());
        MapGenStructureIO.registerStructureComponent(ComponentPetShelter.class, Ref.MODID + ":PetShelterStructure");
    }

    public void postInit(FMLPostInitializationEvent e) {
        Ref.registerCatFoods();
    }

    public void registerItemRenderer(Item item, int meta, String id) {
    }

    @Nullable
    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case BlockBowl.GUI_ID:
                return new ContainerBowl(player.inventory, (TileEntityBowl)world.getTileEntity(new BlockPos(x, y, z)));
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}*/
