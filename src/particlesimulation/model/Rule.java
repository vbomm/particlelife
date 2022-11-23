package particlesimulation.model;

import java.util.ArrayList;

/**
 * Defines a rule that specifies how a particle type reacts to another particle type.
 */
public class Rule implements Runnable {
    private ArrayList<Particle> particlesA;
    private ArrayList<Particle> particlesB;
    private float g;
    private int width;
    private int height;

    /**
     * Initializes a new Rule.
     *
     * @param particlesA particle type that is affected
     * @param particlesB particle type that is the focus
     * @param g          strength of the rule
     * @param width      width of the world
     * @param height     height of the world
     */
    public Rule(ArrayList<Particle> particlesA, ArrayList<Particle> particlesB, float g, int width, int height) {
        this.particlesA = particlesA;
        this.particlesB = particlesB;
        this.width = width;
        this.height = height;
        this.g = g;
    }

    /**.
     * Iterate through particles and see if there are other particles in range.
     * If that is the case, cause force on the particle.
     */
    private void gravity() {

        for(int i = 0; i < particlesA.size(); i++) {
            float forceX = 0;
            float forceY = 0;
            Particle particle1 = particlesA.get(i);

            for (int j = 0; j < particlesB.size(); j++) {
                Particle particle2 = particlesB.get(j);

                float distanceX = computeDelta(particle1.getX(), particle2.getX(), width);
                float distanceY = computeDelta(particle1.getY(), particle2.getY(), height);

                float distance = (float) (Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2)));

                if (distance > particle2.getType().getRangeMin() && distance < particle2.getType().getRangeMax()) {
                    forceX += (distanceX / distance);
                    forceY += (distanceY / distance);
                }
            }
            particle1.influenceVelocity(forceX, forceY, g);
        }
    }

    /**
     * Calculates and corrects the new coordinate so a particle doesn't travel outside of the world.
     *
     * @param src     the source coordinate
     * @param dst     the distance to travel
     * @param mapSize the size of the map
     * @return        the corrected coordinate
     */
    private static float computeDelta(float src, float dst, int mapSize) {
        float increasing, decreasing;

        if (dst < src) {
            increasing = (mapSize + dst) - src;   // increasing direction is toroidal
            decreasing = src - dst;               // decreasing direction is direct
        } else {
            increasing = dst - src;               // increasing direction is direct
            decreasing = (mapSize + src) - dst;   // decreasing direction is toroidal
        }

        if (increasing <= decreasing)
            return  increasing;
        else
            return -decreasing;
    }

    /**
     * Sets the strength of the rule.
     *
     * @param value the new strength of the rule.
     */
    public void setG(float value) {
        g = value;
    }

    /**
     * Starts the thread. Calls method to affect particle.
     */
    @Override
    public void run() {
        gravity();
    }
}
