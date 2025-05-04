package tekton.content;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.Fill;
import arc.math.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.StatusEffect;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;

import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;
import static mindustry.Vars.*;


public class TektonStatusEffects {
	public static StatusEffect tarredInMethane, wetInAcid, acidified, shortCircuit, incineration, radioactiveContamination, radiationAbsorption, cobwebbed, neurosporaSlowed, foggerStatus;
	
	public static void load(){
		tarredInMethane = new StatusEffect("status-tarred-in-methane") {{
			show = true;
			hideDetails = false;
			outline = false;
			color = applyColor = Color.valueOf("57592b");
			speedMultiplier = 0.9f;
			transitionDamage = 5f;
			dragMultiplier = 1.1f;
			effect = new Effect(42f, e -> {
		        color(TektonColor.liquidMethane);

		        randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
		            Fill.circle(e.x + x, e.y + y, e.fout());
		        });
		    });
			init(() -> {
                affinity(StatusEffects.melting, (unit, result, time) -> result.set(StatusEffects.melting, result.time + time));
                affinity(StatusEffects.burning, (unit, result, time) -> result.set(StatusEffects.burning, result.time + time));
                affinity(StatusEffects.blasted, (unit, result, time) -> result.set(StatusEffects.blasted, result.time + time));
            });
		}};
	    
		neurosporaSlowed = new StatusEffect("status-neurospora-slowed") {{
			show = true;
			hideDetails = false;
			outline = false;
	    	color = applyColor = TektonColor.neurospora;
            speedMultiplier = 0.9f;
			dragMultiplier = 1.1f;
            effect = TektonFx.neurosporaContaminationSapped;
            effectChance = 0.04f;
	    }};
		
		wetInAcid = new StatusEffect("status-wet-in-acid") {{
			show = true;
			hideDetails = false;
			outline = false;
	        color = Color.valueOf("82d629");
			applyColor = Color.green.cpy();
	        speedMultiplier = 0.95f;
	        reloadMultiplier = 0.95f;
	        damage = 5f / 60f;
			transitionDamage = 20f;
	        init(() -> {
                affinity(acidified, (unit, result, time) -> result.set(acidified, result.time + time));
            });
	        effect = new Effect(40f, e -> {
	            color(Color.valueOf("82d629"), Pal.berylShot, e.fout() / 5f + Mathf.randomSeedRange(e.id, 0.12f));
	
	            randLenVectors(e.id, 2, 1f + e.fin() * 3f, (x, y) -> {
	                Fill.circle(e.x + x, e.y + y, .2f + e.fout() * 1.2f);
	            });
	        });
	    }};
		
		acidified = new StatusEffect("status-acidified") {{
			show = true;
			hideDetails = false;
			outline = false;
	        color = Color.valueOf("82d629");
			applyColor = Color.green.cpy();
	        speedMultiplier = 0.8f;
	        reloadMultiplier = 0.8f;
	        healthMultiplier = 0.9f;
	        damage = 20f / 60f;
	        effect = new Effect(40f, e -> {
	            color(Color.valueOf("82d629"), Pal.berylShot, e.fout() / 5f + Mathf.randomSeedRange(e.id, 0.12f));
	
	            randLenVectors(e.id, 2, 1f + e.fin() * 3f, (x, y) -> {
	                Fill.circle(e.x + x, e.y + y, .2f + e.fout() * 1.2f);
	            });
	        });
	    }};
	    
	    shortCircuit = new StatusEffect("status-short-circuit") {{
			show = true;
			hideDetails = false;
			outline = false;
	        color = Color.valueOf("82d629");
			applyColor = Color.yellow.cpy();
	        speedMultiplier = 0.8f;
	        reloadMultiplier = 0.7f;
	        damage = 3f / 60f;
	        effect = new Effect(40f, e -> {
	            color(Color.valueOf("ffe14a"));

	            randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
	                Fill.square(e.x + x, e.y + y, e.fslope() * 1.1f, 45f);
	            });
	        });
	    }};
		
		incineration = new StatusEffect("status-incineration") {{
			show = true;
			hideDetails = false;
			outline = false;
	        color = Pal.redSpark.cpy();
			applyColor = Pal.redSpark.cpy();
	        speedMultiplier = 0.4f;
	        healthMultiplier = 0.8f;
	        damage = 4f;
	        effect = new Effect(40f, e -> {
	            color(Pal.redSpark, Liquids.slag.color, e.fout() / 5f + Mathf.randomSeedRange(e.id, 0.12f));
	
	            randLenVectors(e.id, 2, 1f + e.fin() * 3f, (x, y) -> {
	                Fill.circle(e.x + x, e.y + y, .2f + e.fout() * 1.2f);
	            });
	        });
	        init(() -> {
	        	opposite(StatusEffects.freezing);
	        	opposite(StatusEffects.wet);
        	}); 
	    }};
	    
	    float rspeedMultiplier = 0.5f,
    		rbuildSpeedMultiplier = 0.6f,
    		rdamageMultiplier = 0.8f,
    		rdragMultiplier = 1.4f;
	    
	    radioactiveContamination = new StatusEffect("status-radioactive-contamination") {{
			show = true;
			hideDetails = false;
			outline = false;
			permanent = false;
	        color = Color.valueOf("bbd658");
			applyColor = Color.valueOf("bbd658");
	        speedMultiplier = rspeedMultiplier;
	        buildSpeedMultiplier = rbuildSpeedMultiplier;
	        damageMultiplier = rdamageMultiplier;
	        dragMultiplier = rdragMultiplier;
	        damage = 10f / 60f;
	        effect = new Effect(40f, e -> {
	            color(Color.valueOf("bbd658"));

	            randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
	                Fill.square(e.x + x, e.y + y, e.fslope() * 1.1f, 45f);
	            });
	        });
	    }};
	    
	    radiationAbsorption = new StatusEffect("status-radiation-absorption") {{
			show = true;
			hideDetails = false;
			outline = false;
			permanent = false;
	        color = Color.valueOf("bbd658");
			applyColor = Color.valueOf("bbd658");
	        speedMultiplier = 2f - rspeedMultiplier;
	        buildSpeedMultiplier = 2f - rbuildSpeedMultiplier;
	        damageMultiplier = 2f - rdamageMultiplier;
	        healthMultiplier = 1.3f;
	        //dragMultiplier = -rdragMultiplier;
	        damage = -40f / 60f;
	        effect = Fx.none;
	        init(() -> {
	        	opposite(radioactiveContamination);
	        });
	    }};
	    
	    cobwebbed = new StatusEffect("status-cobwebbed") {{
			show = true;
			hideDetails = false;
			outline = false;
	        color = Color.valueOf("d6d6d6");
			applyColor = Color.valueOf("d6d6d6");
	        speedMultiplier = 0.1f;
	        dragMultiplier = 1.2f;
	        effect = TektonFx.cobwebbed;
	    }};
	    
	    foggerStatus = new StatusEffect("status-fogger") {{
	    	show = false;
			hideDetails = true;
			outline = false;
			permanent = true;
	        effect = Fx.none;
	    }};
	}
}