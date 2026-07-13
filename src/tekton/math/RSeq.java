package tekton.math;

import arc.struct.Seq;

public class RSeq<T> {
	public int id;
	public Seq<T> seq;

	public RSeq(){
		id = 0;
		seq = new Seq<>();
	}

	public RSeq(int id) {
		this();
		this.id = id;
	}

	public RSeq(int id, Seq<T> seq){
		this(id);
		this.seq = seq;
	}
}
