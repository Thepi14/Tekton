package tekton.type.part;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.entities.part.DrawPart;

public class LinePart extends DrawPart{
    public float stroke = 1f, strokeTo = -1f;
    public float x1, y1, x2, y2;
    public float x1To, y1To, x2To, y2To;
    public float moveX, moveY, moveRot;
    public Color color = Color.white;
    public @Nullable Color colorTo;
    public boolean mirror = false;
    public boolean clampProgress = true;
    public PartProgress progress = PartProgress.warmup;
    public float layer = -1f, layerOffset = 0f;

	@Override
	public void draw(PartParams params) {
		float z = Draw.z();
        if(layer > 0) Draw.z(layer);
        if(under && turretShading) Draw.z(z - 0.0001f);

        Draw.z(Draw.z() + layerOffset);

        float prog = progress.getClamp(params, clampProgress),
        str = strokeTo < 0 ? stroke : Mathf.lerp(stroke, strokeTo, prog);

        for(int sign : Mathf.signs){
        	
            if(color != null && colorTo != null){
                Draw.color(color, colorTo, prog);
            }else if(color != null){
                Draw.color(color);
            }
            
            if(str > 0.0001f){
                Lines.stroke(str);
                
                Lines.line(
                		params.x + Angles.trnsx((moveRot * prog) + params.rotation - 90, (Mathf.lerp(x1, x1To, prog) + (moveX * prog)) * sign, Mathf.lerp(y1, y1To, prog) + (moveY * prog)), 
                		params.y + Angles.trnsy((moveRot * prog) + params.rotation - 90, (Mathf.lerp(x1, x1To, prog) + (moveX * prog)) * sign, Mathf.lerp(y1, y1To, prog) + (moveY * prog)), 
                		params.x + Angles.trnsx((moveRot * prog) + params.rotation - 90, (Mathf.lerp(x2, x2To, prog) + (moveX * prog)) * sign, Mathf.lerp(y2, y2To, prog) + (moveY * prog)), 
                		params.y + Angles.trnsy((moveRot * prog) + params.rotation - 90, (Mathf.lerp(x2, x2To, prog) + (moveX * prog)) * sign, Mathf.lerp(y2, y2To, prog) + (moveY * prog))
            		);
                
                Lines.stroke(1f);
            }
            if(color != null) Draw.color();
        }

        Draw.z(z);
	}
	
    @Override
    public void load(String name){

    }
}
