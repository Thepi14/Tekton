package tekton.content;

import arc.struct.ObjectSet;
import mindustry.type.StatusEffect;
import mindustry.content.StatusEffects;
import mindustry.type.unit.MissileUnitType;

public class TektonMissileUnitType extends MissileUnitType {
	
	public static ObjectSet<StatusEffect> defaultImmunities = immunityLister();

	public TektonMissileUnitType(String name) {
		super(name);
		immunities.addAll(defaultImmunities);
	}
	
	private static ObjectSet<StatusEffect> immunityLister() {
		var a = new ObjectSet<StatusEffect>();
		a.addAll(
				StatusEffects.freezing, 
				TektonStatusEffects.cobwebbed, 
				TektonStatusEffects.wetInAcid, 
				TektonStatusEffects.shortCircuit, 
				TektonStatusEffects.tarredInMethane, 
				TektonStatusEffects.neurosporaSlowed, 
				TektonStatusEffects.radiationAbsorption,
				TektonStatusEffects.weaponLock
				);
		return a;
	}
}
