package tekton;

import ent.anno.Annotations.EntityDef;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;

public class EntityDefinitions{
    // This will generate `MyUnit` entity class that implements both `MyComp` and `Unit` in `mymod.gen.entities`.
    // The naming in the case of external `@EntityDef`s like in this case is reversed order of the components, without the `*c`:
    // `My` + `Unit` = `MyUnit`.
    // The `E myUnit` doesn't really matter; it's just there to hold the annotation so the processor recognizes it.
    public @EntityDef({Unitc.class, DistanceKillComp.class}) UnitType missileDistanceKillUnit;
    public @EntityDef({Unitc.class, DistanceMissileUnitEntity.class}) UnitType missileDistanceKillEntityUnit;
}
