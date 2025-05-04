package tekton.type.defense;

public interface LightningAbsorber {
	public void absorbLightning();
	public default float getRadius() { return 0f; };
}
