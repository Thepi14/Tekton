package tekton.content;

import arc.struct.Seq;
import tekton.math.IDObj;
import tekton.math.RSeq;
import tekton.math.Vec2F;

public class TektonVars {
	public static final int 
	visualMaxGravity = 16,
	gravityMul = 2
			;

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
		return miscObjects.find(r -> { return r.id == id && r.type.equals(type); });
	}
	
	public static void clearAllLists() {
		vec2F.clear();
		miscObjects.clear();
	}
}
