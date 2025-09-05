package tekton;

import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.graphics.Trail;
import tekton.math.*;

public class TektonGambiarra {
	public static Seq<RSeq<Vec2F>> vec2F = new Seq<RSeq<Vec2F>>();
	//aberration
	public static Seq<IDObj> miscObjects = new Seq<IDObj>();
	
	public static RSeq<Vec2F> addVec2F(RSeq<Vec2F> rseq) {
		vec2F.add(rseq);
		return getVec2F(rseq.id);
	}
	
	public static RSeq<Vec2F> getVec2F(int id) {
		return vec2F.find(r -> r.id == id);
	}
	
	public static IDObj addMisc(IDObj trail) {
		miscObjects.add(trail);
		return getMisc(trail.id, trail.type);
	}
	
	public static IDObj getMisc(int id, String type) {
		return miscObjects.find(r -> { Log.info(miscObjects.size); return r.id == id && r.type.equals(type); });
	}
	
	public static void clearAllLists() {
		vec2F.clear();
		miscObjects.clear();
	}
}
