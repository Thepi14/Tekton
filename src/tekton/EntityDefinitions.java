package tekton;

import ent.anno.Annotations.EntityDef;
import mindustry.gen.*;
import mindustry.type.UnitType;
import tekton.type.weathers.ObstaclesWeather.*;

@SuppressWarnings("unused")
public class EntityDefinitions<E>{
    // This will generate `MyUnit` entity class that implements both `MyComp` and `Unit` in `mymod.gen.entities`.
    // The naming in the case of external `@EntityDef`s like in this case is reversed order of the components, without the `*c`:
    // `My` + `Unit` = `MyUnit`.
    // The `E myUnit` doesn't really matter; it's just there to hold the annotation so the processor recognizes it.
    //public @EntityDef({Unitc.class, DistanceKillc.class}) E missileDistanceKillUnit;
    //public @EntityDef({Unitc.class, SkyObstaclec.class}) E skyObstacleCompUnit;
}
