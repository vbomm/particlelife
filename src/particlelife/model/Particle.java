package particlelife.model;

public class Particle {
    private ParticleType particleType;
    private float x;
    private float y;
    private float tendencyX;
    private float tendencyY;
    private float velocityX;
    private float velocityY;
    private Object influenceVelo;

    public Particle(ParticleType particleType, float x, float y) {
        this(particleType, x, y, 0, 0);
    }

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

    public ParticleType getType() {
        return particleType;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getTendencyX() {
        return tendencyX;
    }

    public float getTendencyY() {
        return tendencyY;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    // synchronized?
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

    private float capVelocity(float v) {
        if (particleType.getVelocityCap() == -1)
            return v;

        if (v < -particleType.getVelocityCap()) v = -particleType.getVelocityCap();
        else if (v > particleType.getVelocityCap()) v = particleType.getVelocityCap();

        return v;
    }

    public void setYX(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void friction(float t) {
        velocityX *= t;
        velocityY *= t;
    }
}
