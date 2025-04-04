package tekton.type.gravity;

import mindustry.world.blocks.production.GenericCrafter;

public class GravityCrafter extends GenericCrafter {
	
	public GravityCrafter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public class GravityCrafterBuild extends GenericCrafterBuild implements GravityConsumer {
		public int gravity;

		@Override
		public float[] sideGravity() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public float gravityRequirement() {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}
