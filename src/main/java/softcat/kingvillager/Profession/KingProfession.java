package softcat.kingvillager.Profession;


import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import softcat.kingvillager.KingVillagerMod;

public class KingProfession extends VillagerProfession {

    public KingProfession(PoiType poiType) {
        super("king", poiType, ImmutableSet.of(Items.GOLD_INGOT), ImmutableSet.of(KingVillagerMod.throne.get()), SoundEvents.METAL_PLACE);
    }
}
