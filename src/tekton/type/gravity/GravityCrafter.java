package tekton.type.gravity;

import mindustry.world.blocks.production.GenericCrafter;

public class GravityCrafter extends GenericCrafter {
	
	public GravityCrafter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public class GravityCrafterBuild extends GenericCrafterBuild implements GravityConsumer {
		public float gravity;
		public float minGravity = 2;
        public float[] sideGravity = new float[4];

        @Override
        public float[] sideGravity(){
            return sideGravity;
        }

        @Override
        public float gravityRequirement(){
            return minGravity;
        }

		@Override
		public float gravity() {
			return gravity;
		}
	}
}
