package tekton.type.abilities;

import static mindustry.Vars.state;

import arc.Events;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.util.Time;
import mindustry.Vars;
import mindustry.ai.types.MissileAI;
import mindustry.entities.Units;
import mindustry.entities.abilities.UnitSpawnAbility;
import mindustry.game.EventType.UnitCreateEvent;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.type.UnitType;
import tekton.type.ai.MinionAI;

public class MinionSpawnAbility extends UnitSpawnAbility  {
	public boolean alwaysSpawn = true;

	public MinionSpawnAbility(UnitType unit, float spawnTime, float spawnX, float spawnY){
        this.unit = unit;
        this.spawnTime = spawnTime;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    public MinionSpawnAbility(){
    }

	@Override
    public void update(Unit unit){
        timer += Time.delta * state.rules.unitBuildSpeed(unit.team);

        if(timer >= spawnTime && (Units.canCreate(unit.team, this.unit) || alwaysSpawn) && !unit.disarmed && unit.isShooting){
            float x = unit.x + Angles.trnsx(unit.rotation, spawnY, -spawnX), y = unit.y + Angles.trnsy(unit.rotation, spawnY, -spawnX);
            spawnEffect.at(x, y, 0f, parentizeEffects ? unit : null);
            Unit u = this.unit.create(unit.team);
            u.set(x, y);
            u.rotation = unit.rotation;
            if(u.controller() instanceof MinionAI ai) {
            	ai.shooter = unit;
            }
            else if (u.controller() instanceof MissileAI ai) {
            	ai.shooter = unit;
            }
            Events.fire(new UnitCreateEvent(u, null, unit));
            if(!Vars.net.client()){
                u.add();
                //Units.notifyUnitSpawn(u);
            }

            timer = 0f;
        }
    }

	@Override
    public void draw(Unit unit){
        if((Units.canCreate(unit.team, this.unit) || alwaysSpawn) && !unit.disarmed && unit.isShooting){
            Draw.draw(Draw.z(), () -> {
                float x = unit.x + Angles.trnsx(unit.rotation, spawnY, -spawnX), y = unit.y + Angles.trnsy(unit.rotation, spawnY, -spawnX);
                Drawf.construct(x, y, this.unit.fullIcon, unit.rotation - 90, timer / spawnTime, 1f, timer);
            });
        }
    }
}
