package tekton.type.defense;

public interface LightningAbsorber {
	public void absorbLightning();
	public default float lightningProtectionRadius() { return 0f; };
}
