package softcat.kingvillager;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import softcat.kingvillager.Block.ThroneBlock;
import softcat.kingvillager.Profession.KingPOI;
import softcat.kingvillager.Profession.KingProfession;
import softcat.kingvillager.Profession.RandomTradeBuilder;

import java.util.stream.Collectors;

import static softcat.kingvillager.KingVillagerMod.MOD_ID;

@Mod(MOD_ID)
public class KingVillagerMod {

    public static final String MOD_ID = "kingvillager";

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Block throne = null;
    public static final PoiType kingpoi = null;
    public static final VillagerProfession king = null;

    public KingVillagerMod()
    {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        // LOGGER.info("HELLO FROM PREINIT");
        // LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        // InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        // LOGGER.info("Got IMC {}", event.getIMCStream().
                // map(m->m.messageSupplier().get()).
                // collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        // LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        // ***** BLOCKS *****
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // Register a new block here
            // LOGGER.info("HELLO from Register Block");
            blockRegistryEvent.getRegistry().registerAll(
                    new ThroneBlock(Block.Properties.of(Material.METAL).explosionResistance(4).sound(SoundType.METAL).noOcclusion()).setRegistryName(KingVillagerMod.MOD_ID, "throne")

            );
        }

        // ***** ITEM_BLOCKS *****
        @SubscribeEvent
        public static void onItemBlockRegistery(final RegistryEvent.Register<Item> itemBlockEvent)
        {

            itemBlockEvent.getRegistry().registerAll(
                    new BlockItem(throne, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)).setRegistryName(KingVillagerMod.MOD_ID, "throne")
            );
        }

        // ***** POINTS_OF_INTEREST *****
        @SubscribeEvent
        public static void registerPointOfInterest(RegistryEvent.Register<PoiType> POIEvent)
        {
            POIEvent.getRegistry().registerAll(new KingPOI().setRegistryName(KingVillagerMod.MOD_ID, "kingpoi"));
        }

        // ***** PROFESSIONS *****
        public static void registerProfession(RegistryEvent.Register<VillagerProfession> VillagerProfessionEvent) {
            VillagerProfessionEvent.getRegistry().registerAll(new KingProfession(kingpoi).setRegistryName(KingVillagerMod.MOD_ID, "king"));
            // injectWorkstation();
        }

    }

    @Mod.EventBusSubscriber(modid=KingVillagerMod.MOD_ID, value= Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientRegistryEvents
    {
        // ***** RENDERS *****
        @SubscribeEvent
        public static void onFMLClientSetup(FMLClientSetupEvent event) {

            ItemBlockRenderTypes.setRenderLayer(throne, RenderType.cutout());
        }
    }

    // FORGE BUS
    @Mod.EventBusSubscriber(modid = KingVillagerMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeBusRegistryEvents
    {
        // ***** TRADES *****
        @SubscribeEvent
        public static void registerTrades(VillagerTradesEvent TradesEvent){

            if(TradesEvent.getType() == king)
            {
                TradesEvent.getTrades().get(1).add((new RandomTradeBuilder(16, 10, 0.05F).setEmeraldPrice(2).setForSale(Items.IRON_INGOT, 1, 2).build()));
                TradesEvent.getTrades().get(1).add((new RandomTradeBuilder(16, 10, 0.05F).setPrice(Items.GOLD_NUGGET, 8,16).setForSale(Items.EMERALD, 1, 1).build()));
                TradesEvent.getTrades().get(2).add((new RandomTradeBuilder(8, 10, 0.05F).setEmeraldPrice(4).setForSale(Items.GOLD_INGOT, 1, 2).build()));
                TradesEvent.getTrades().get(3).add((new RandomTradeBuilder(8, 10, 0.05F).setEmeraldPrice(8).setForSale(Items.PRISMARINE_SHARD, 1, 2).build()));
                TradesEvent.getTrades().get(3).add((new RandomTradeBuilder(8, 10, 0.05F).setEmeraldPrice(8).setForSale(Items.PRISMARINE_CRYSTALS, 1, 2).build()));
                TradesEvent.getTrades().get(4).add((new RandomTradeBuilder(5, 10, 0.05F).setEmeraldPrice(20).setForSale(Items.GOLD_BLOCK, 1, 1).build()));
                TradesEvent.getTrades().get(4).add((new RandomTradeBuilder(5, 10, 0.05F).setEmeraldPrice(16).setForSale(Items.DIAMOND, 1, 1).build()));
                TradesEvent.getTrades().get(5).add((new RandomTradeBuilder(3, 12, 0.05F).setEmeraldPrice(36).setForSale(Items.TOTEM_OF_UNDYING, 1, 1).build()));
            }
        }
    }


    // Reflexion magic
    // Inject the throne into the workstations block in order to be notice by unemployed villagers
    /*
    private static Method injectWorkstation;
    static void injectWorkstation()
    {
        try
        {

            injectWorkstation = PointOfInterestType.class.getDeclaredMethod("registerBlockStates", PointOfInterestType.class);
            injectWorkstation.setAccessible(true);
            injectWorkstation.invoke(kingpoi, kingpoi);



        }
        catch (NoSuchMethodException | SecurityException | InvocationTargetException | IllegalAccessException e)
        {
            try
            {
                injectWorkstation = PointOfInterestType.class.getDeclaredMethod("func_221052_a", PointOfInterestType.class);
                injectWorkstation.setAccessible(true);
                injectWorkstation.invoke(kingpoi, kingpoi);
                e.printStackTrace();
            }
            catch (NoSuchMethodException | SecurityException | InvocationTargetException | IllegalAccessException er) {

                er.printStackTrace();

            }
        }
    }
    */

}
