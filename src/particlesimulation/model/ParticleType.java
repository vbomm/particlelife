package particlesimulation.model;

/**
 * Defines a particle type.
 */
public class ParticleType {
    private int type;
    private float velocityCap;
    private int rangeMin;
    private int rangeMax;

    /**
     * Initializes a new ParticleType
     *
     * @param type        the type
     * @param velocityCap the velocity cap
     * @param rangeMin    the minimum range
     * @param rangeMax    the maximum range
     */
    public ParticleType(int type, float velocityCap, int rangeMin, int rangeMax) {
        this.type = type;
        this.velocityCap = velocityCap;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    /**
     * Returns the type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }


    /**
     * Returns the velocity cap.
     *
     * @return the velocity cap
     */
    public float getVelocityCap() {
        return velocityCap;
    }

    /**
     * Sets the velocity cap.
     *
     * @param velocityCap the velocity cap
     */
    public void setVelocityCap(float velocityCap) {
        this.velocityCap = velocityCap;
    }

    /**
     * Returns the minimum range.
     *
     * @return the minimum range
     */
    public int getRangeMin() {
        return rangeMin;
    }

    /**
     * Sets the minimum range.
     *
     * @param rangeMin the minimum range
     */
    public void setRangeMin(int rangeMin) {
        this.rangeMin = rangeMin;
    }

    /**
     * Returns the maximum range.
     *
     * @return the maximum range
     */
    public int getRangeMax() {
        return rangeMax;
    }

    /**
     * Sets the maximum range.
     *
     * @param rangeMax the maximum range
     */
    public void setRangeMax(int rangeMax) {
        this.rangeMax = rangeMax;
    }
}
