package particlesimulation.model;

/**
 * Defines a Particle.
 */
public class Particle {
    private ParticleType particleType;
    private float x;
    private float y;
    private float tendencyX;
    private float tendencyY;
    private float velocityX;
    private float velocityY;
    private Object influenceVelo;

    /**
     * Initializes a new Particle.
     *
     * @param particleType the type of the particle
     * @param x            the x coordinate or the particle
     * @param y            the y coordinate of the particle
     */
    public Particle(ParticleType particleType, float x, float y) {
        this(particleType, x, y, 0, 0);
    }

    /**
     * Initializes a new Particle.
     *
     * @param particleType the type of the particle
     * @param x            the x coordinate of the particle
     * @param y            the y coordinate of the particle
     * @param velocityX    the x velocity of the particle
     * @param velocityY    thy y velocity of the particle
     */
    public Particle(ParticleType particleType, float x, float y, float velocityX, float velocityY) {
        this.particleType = particleType;
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        tendencyX = x;
        tendencyY = y;

        influenceVelo = new Object();
    }

    /**
     * Returns the type of the particle.
     *
     * @return type of the particle
     */
    public ParticleType getType() {
        return particleType;
    }

    /**
     * Returns the x coordinate of the particle.
     *
     * @return the x coordinate of the particle
     */
    public float getX() {
        return x;
    }


    /**
     * Returns the y coordinate of the particle.
     *
     * @return the y coordinate of the particle
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the x tendency of the particle.
     *
     * @return the x tenency of the particle
     */
    public float getTendencyX() {
        return tendencyX;
    }

    /**
     * Returns the y tendency of the particle.
     *
     * @return the y tendency of the particle
     */
    public float getTendencyY() {
        return tendencyY;
    }


    /**
     * Returns the x velocity of the particle.
     *
     * @return the x velocity of the particle
     */
    public float getVelocityX() {
        return velocityX;
    }

    /**
     * Returns the y velocity of the particle.
     *
     * @return the y velocity of the particle
     */
    public float getVelocityY() {
        return velocityY;
    }

    /**
     * Influences the velocity of the particle.
     *
     * @param fx influences velocity x
     * @param fy influences velocity y
     * @param g  how strong the influence is
     */
    public synchronized void influenceVelocity (float fx, float fy, float g) {
        velocityX = (velocityX + (fx * g)) / 2;
        velocityY = (velocityY + (fy * g)) / 2;

        velocityX = capVelocity(velocityX);
        velocityY = capVelocity(velocityY);

        tendencyX += velocityX;
        tendencyY += velocityY;
        //if (tendencyX <= -1200)
        //    System.out.println(fx + " " + velocityX + " " + tendencyX);
        //if (tendencyY <= -900)
        //    System.out.println(fy + " " + velocityY + " " + tendencyY);
    }

    /**
     * Caps the velocity.
     *
     * @param v the velocity
     * @return  the capped velocity
     */
    private float capVelocity(float v) {
        if (particleType.getVelocityCap() == -1)
            return v;

        if (v < -particleType.getVelocityCap()) v = -particleType.getVelocityCap();
        else if (v > particleType.getVelocityCap()) v = particleType.getVelocityCap();

        return v;
    }

    /**
     * Sets the x and y coordinate of the particle.
     *
     * @param x new x coordinate of the particle
     * @param y new y coordinate of the particle
     */
    public void setYX(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Reduces the velocity of the particle.
     *
     * @param t value < 1 to cause velocity to get reduced
     */
    public void friction(float t) {
        velocityX *= t;
        velocityY *= t;
    }
}
