package softcat.kingvillager.Profession;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import softcat.kingvillager.KingVillagerMod;

public class KingPOI extends PoiType {

    public KingPOI() {
        super("king", getBlockStates(KingVillagerMod.throne), 1, 1);
    }
}