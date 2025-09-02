package tekton.type.ai;

import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.entities.Units;
import mindustry.entities.units.AIController;
import mindustry.gen.BuildingTetherc;
import tekton.type.defense.BuilderUnitFactory.BuilderUnitSourceBuild;

import static mindustry.Vars.*;

public class RebuilderAI extends AIController {
	public @Nullable Vec2 targetPos;
	public float distance = 0f;
	public boolean keepDistance = false, arrival = false;

    public RebuilderAI() {
    }
    
    @Override
    public void init() {
        unit.updateBuilding = true;
    }
	
	@Override
    public void updateMovement(){
        unit.updateBuilding = true;
        if (targetPos == null)
        	targetPos = new Vec2(unit.x, unit.y);
        
        if(!(unit instanceof BuildingTetherc tether) || tether.building() == null) return;
        if (!(tether.building() instanceof BuilderUnitSourceBuild build)) return;
        
        moveTo(targetPos, distance, 0.5f, keepDistance, new Vec2(), arrival);
	}
    
    public float prefSpeed(){
        return unit.speed();
    }
	
	public void moveTo(Vec2 target, float circleLength, float smooth, boolean keepDistance, @Nullable Vec2 offset, boolean arrive){
        if(target == null) return;
        
        float speed = prefSpeed();

        vec.set(target).sub(unit);

        float length = circleLength <= 0.001f ? 1f : Mathf.clamp((unit.dst(target) - circleLength) / smooth, -1f, 1f);

        vec.setLength(speed * length);

        if(arrive){
            Tmp.v3.set(-unit.vel.x / unit.type.accel * 2f, -unit.vel.y / unit.type.accel * 2f).add((target.getX() - unit.x), (target.getY() - unit.y));
            vec.add(Tmp.v3).limit(speed * length);
        }

        if(length < -0.5f){
            if(keepDistance){
                vec.rotate(180f);
            }else{
                vec.setZero();
            }
        }else if(length < 0){
            vec.setZero();
        }

        if(offset != null){
            vec.add(offset);
            vec.setLength(speed * length);
        }

        //ignore invalid movement values
        if(vec.isNaN() || vec.isInfinite() || vec.isZero()) return;

        if(!unit.type.omniMovement && unit.type.rotateMoveFirst){
            float angle = vec.angle();
            unit.lookAt(angle);
            if(Angles.within(unit.rotation, angle, 3f)){
                unit.movePref(vec);
            }
        }else{
            unit.movePref(vec);
        }
    }

    @Override
    public boolean shouldShoot(){
        return !unit.isBuilding() && unit.type.canAttack;
    }
}
