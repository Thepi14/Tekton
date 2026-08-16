package tekton.type.biological;

import mindustry.content.StatusEffects;
import mindustry.type.StatusEffect;
import tekton.content.TektonStatusEffects;

//mainly used as a tag for easier detection by other scripts and for some functions.
public interface BiologicalUnit {
	public static StatusEffect[] getDefaultImmunities() {
		return new StatusEffect[] { TektonStatusEffects.radioactiveContamination,
        		TektonStatusEffects.wetInAcid,
        		TektonStatusEffects.acidified,
        		TektonStatusEffects.weaponLock,
        		TektonStatusEffects.shortCircuit,
        		//TektonStatusEffects.tarredInMethane,
        		TektonStatusEffects.neurosporaSlowed,
        		//StatusEffects.freezing
        		};
	}
}
