package tekton.type.draw;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.Time;
import mindustry.gen.Building;
import mindustry.world.draw.DrawBlock;

public class DrawLinesToCenter extends DrawBlock {
    public Color lineColor = Color.valueOf("f58349"), particleColor = Color.valueOf("f2d585");
    public boolean drawCenter = true;
    public Blending blending = Blending.additive;
    public float alpha = 0.68f;
    public float lineAlpha = 0.9f;
    public int particles = 10;
    public float particleLife = 40f, particleRad = 7f, particleStroke = 1.1f, particleLen = 3f;
    public float flameRad = 1f, circleSpace = 2f, lineRadiusScl = 3f, lineRadiusMag = 0.3f, circleStroke = 1.5f;
    
    public int lines = 4;
    public float angleOffset = 45f;
    public float distanceCenter = 8f;
    public float lineStroke = 1.5f;
    public float particlesRand = 60f;
	
	@Override
    public void draw(Building build){
        if(build.warmup() > 0f && lineColor.a > 0.001f){
            float si = Mathf.absin(lineRadiusScl, lineRadiusMag);
            float a = alpha * build.warmup();
            float la = lineAlpha * build.warmup();

            float base = (Time.time / particleLife);
            rand.setSeed(build.id);
            
            for(int i = 0; i < lines; i++){
                float angle = ((float)i * (360f / (float)lines)) + angleOffset;
                float xPos = build.x + (Mathf.cosDeg(angle) * distanceCenter), yPos = build.y + (Mathf.sinDeg(angle) * distanceCenter);

                Draw.blend(blending);
                Draw.color(lineColor, la);
                Lines.stroke((lineStroke * build.warmup()) + si);
                Lines.line(build.x, build.y, xPos, yPos);

                Lines.stroke(particleStroke * build.warmup());
                for(int j = 0; j < particles; j++){
                    float fin = (rand.random(1f) + base) % 1f, fout = 1f - fin;
                    float angleRand = rand.random(-particlesRand, particlesRand) * 0.5f;
                    float angleParticle = angleRand + angle + 180f;
                    float len = particleRad * Interp.pow2Out.apply(fin);
                    Draw.color(particleColor, a);
                    Lines.lineAngle(xPos + Angles.trnsx(angleParticle, len), yPos + Angles.trnsy(angleParticle, len), angleParticle, particleLen * fout * build.warmup());
                }
            }
        }

        Draw.blend();
        Draw.reset();
    }
}
