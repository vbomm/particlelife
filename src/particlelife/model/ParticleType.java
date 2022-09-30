package particlelife.model;

public class ParticleType {
    private int type;
    private float velocityCap;
    private int rangeMin;
    private int rangeMax;

    public ParticleType(int type, float velocityCap, int rangeMin, int rangeMax) {
        this.type = type;
        this.velocityCap = velocityCap;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getVelocityCap() {
        return velocityCap;
    }

    public void setVelocityCap(float velocityCap) {
        this.velocityCap = velocityCap;
    }

    public int getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(int rangeMin) {
        this.rangeMin = rangeMin;
    }

    public int getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(int rangeMax) {
        this.rangeMax = rangeMax;
    }
}
