package tekton.type.abilities;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class EffectStatusEffectAbility extends Ability { //horrible class name
    public StatusEffect status = StatusEffects.none;
    public float delay = 0f;
    
	public float interval = 3f, chance = 0f;
    public int amount = 1;
    public float x, y, rotation, rangeX, rangeY, rangeLengthMin, rangeLengthMax;
    public boolean rotateEffect = false;
    public float effectParam = 3f;
    public boolean teamColor = false;
    public boolean parentizeEffects;
    public Color color = Color.white;
    public Effect effect = Fx.missileTrail;

    protected float counter, delayCounter;

    public EffectStatusEffectAbility(float x, float y, Color color, Effect effect, float interval, StatusEffect status){
        this.x = x;
        this.y = y;
        this.color = color;
        this.effect = effect;
        this.interval = interval;
        this.status = status;
        display = false;
    }
    
    public EffectStatusEffectAbility(float x, float y, Color color, Effect effect, float interval, StatusEffect status, float delay){
        this.x = x;
        this.y = y;
        this.color = color;
        this.effect = effect;
        this.interval = interval;
        this.status = status;
        this.delay = delay;
        display = false;
    }

    public EffectStatusEffectAbility(){
        display = false;
    }

    @Override
    public void update(Unit unit){
        if(Vars.headless) return;

        counter += Time.delta;
        if (unit.hasEffect(status)) {
        	delayCounter += Time.delta;
            
        	if(delayCounter >= delay && ((counter >= interval || (chance > 0 && Mathf.chanceDelta(chance)))) && !unit.inFogTo(Vars.player.team())){
                if(rangeLengthMax > 0){
                    Tmp.v1.trns(unit.rotation - 90f, x, y).add(Tmp.v2.rnd(Mathf.random(rangeLengthMin, rangeLengthMax)));
                }else{
                    Tmp.v1.trns(unit.rotation - 90f, x + Mathf.range(rangeX), y + Mathf.range(rangeY));
                }

                counter %= interval;
                for(int i = 0; i < amount; i++){
                    effect.at(Tmp.v1.x + unit.x, Tmp.v1.y + unit.y, (rotateEffect ? unit.rotation : effectParam) + rotation, teamColor ? unit.team.color : color, parentizeEffects ? unit : null);
                }
            }
        }
        else {
        	delayCounter = 0;
        }
    }
}
