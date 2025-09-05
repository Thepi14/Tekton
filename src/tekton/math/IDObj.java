package tekton.math;

public class IDObj {
	public int id = 0;
	public String type;
	public Object value;
	
	public IDObj() {
		
	}
	
	public IDObj(int id, String type, Object value) {
		this();
		this.id = id;
		this.type = type;
		this.value = value;
	}
}
