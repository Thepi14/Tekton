package tekton.content;

import static arc.graphics.g2d.Draw.color;

import arc.graphics.Color;
import static arc.graphics.g2d.Draw.*;
import arc.graphics.g2d.*;
import arc.math.*;
import static tekton.content.TektonFx.*;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.entities.abilities.LiquidExplodeAbility;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.ammo.*;
import mindustry.world.meta.Env;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

public class TektonBioUnit extends UnitType {
	public TektonBioUnit(String name) {
		super(name);
        //drawCell = false;
        researchCostMultiplier = 0f;
        outlineColor = TektonColor.tektonOutlineColor;
        envDisabled = Env.space | Env.scorching;
        abilities.add(new LiquidExplodeAbility() {{ liquid = TektonLiquids.acid; }});
        immunities.addAll(TektonStatusEffects.radioactiveContamination, TektonStatusEffects.wetInAcid, TektonStatusEffects.shortCircuit, TektonStatusEffects.acidified, /*TektonStatusEffects.tarredInMethane,*/ StatusEffects.freezing);
        ammoType = new PowerAmmoType(0f);
        engineSize = 0f;
        itemCapacity = 0;
        fallEffect = new Effect(110, e -> {
            color(TektonLiquids.acid.color.cpy(), TektonLiquids.methane.color.cpy(), e.rotation);
            Fill.circle(e.x, e.y, e.fout() * 3.5f);
        });
        deathExplosionEffect = new Effect(30, 500f, b -> {
            float intensity = b.rotation;
            float baseLifetime = 26f + intensity * 15f;
            b.lifetime = 43f + intensity * 35f;

            color(Color.gray);
            //TODO awful borders with linear filtering here
            alpha(0.9f);
            for(int i = 0; i < 4; i++){
                rand.setSeed(b.id*2 + i);
                float lenScl = rand.random(0.4f, 1f);
                int fi = i;
                b.scaled(b.lifetime * lenScl, e -> {
                    randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(3f * intensity), 14f * intensity, (x, y, in, out) -> {
                        float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                        Fill.circle(e.x + x, e.y + y, fout * ((2f + intensity) * 1.8f));
                    });
                });
            }

            b.scaled(baseLifetime, e -> {
                e.scaled(5 + intensity * 2.5f, i -> {
                    stroke((3.1f + intensity/5f) * i.fout());
                    Lines.circle(e.x, e.y, (3f + i.fin() * 14f) * intensity);
                    Drawf.light(e.x, e.y, i.fin() * 14f * 2f * intensity, Color.white, 0.9f * e.fout());
                });

                color(TektonLiquids.acid.color.cpy(), TektonLiquids.methane.color.cpy(), Color.gray, e.fin());
                stroke((1.7f * e.fout()) * (1f + (intensity - 1f) / 2f));

                Draw.z(Layer.effect + 0.001f);
                randLenVectors(e.id + 1, e.finpow() + 0.001f, (int)(9 * intensity), 40f * intensity, (x, y, in, out) -> {
                    lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + out * 4 * (3f + intensity));
                    Drawf.light(e.x + x, e.y + y, (out * 4 * (3f + intensity)) * 3.5f, Draw.getColor(), 0.8f);
                });
            });
        });
	}

    @Override
    public void init() {
        super.init();
        float maxWeaponRange = 0;
        for (Weapon weapon : weapons) {
            if (weapon.range() > maxWeaponRange) {
                maxWeaponRange = weapon.range();
            }
        }
        fogRadius = maxWeaponRange / 4;
    }
}
