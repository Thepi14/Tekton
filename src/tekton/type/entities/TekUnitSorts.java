package tekton.type.entities;

import arc.math.Mathf;
import mindustry.entities.Units;
import mindustry.entities.Units.Sortf;
import tekton.type.biological.BiologicalUnit;

public class TekUnitSorts {
	public static Sortf 
	
	biological = (u, x, y) -> (u.type instanceof BiologicalUnit ? -10f : 10f) + Mathf.dst2(u.x, u.y, x, y) / 6400f,
	notBiological = (u, x, y) -> (u.type instanceof BiologicalUnit ? 10f : -10f) + Mathf.dst2(u.x, u.y, x, y) / 6400f,
	mostCrowded = (u, x, y) -> -(Units.count(u.x, u.y, u.hitSize * 8f, other -> (u.dst(other) < u.hitSize * 8f)) * 2f) + Mathf.dst2(u.x, u.y, x, y) / 6400f;
}
