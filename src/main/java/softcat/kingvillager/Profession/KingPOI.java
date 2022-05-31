package softcat.kingvillager.Profession;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import softcat.kingvillager.KingVillagerMod;

public class KingPOI extends PoiType {

    public KingPOI() {
        super("king", getBlockStates(KingVillagerMod.throne.get()), 1, 1);
    }
}