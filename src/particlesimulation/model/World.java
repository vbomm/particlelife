package particlesimulation.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Represents the world, containing the particles. Creates update with threads.
 */
public class World {
    private ExecutorService executor;
    private ThreadPoolExecutor pool;
    private List<Rule> ruleThreads;
    private Rule[] rules;
    private int width;
    private int height;
    private Particle[][] worldArray;
    private ArrayList<ArrayList <Particle>> particles;
    private ParticleType[] particleTypes;

    /**
     * Initializes a new World.
     *
     * @param width  the width of the world
     * @param height the height of the world
     */
    public World(int width, int height) {
        executor = Executors.newCachedThreadPool();
        pool = (ThreadPoolExecutor) executor;

        this.width = width;
        this.height = height;
        worldArray = new Particle[height][width];

        createParticleLists();
        createRuleThreads();
    }


    /**
     * Creates threads that are used to generade world update.
     */
    private void createRuleThreads() {
        ruleThreads = new LinkedList<>();
        rules = new Rule[25];

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                rules[i * 5 + j] = new Rule(particles.get(i), particles.get(j), 0, width, height);
                ruleThreads.add(rules[i * 5 + j]);
            }
    }

    /**
     * Changes the strength of a rule.
     *
     * @param rule  the rule
     * @param value the new strength of g
     */
    public void changeRule(int rule, int value) {
        rules[rule].setG((float) value / 200);
    }

    /**
     * Checks if a particle wants to move.
     * If yes, moves it and return true, otherwise return false.
     *
     * @param p the particle
     * @return  true of particle moved, no otherwise
     */
    private boolean didMove(Particle p) {
        int currentX = (int) p.getX();
        int currentY = (int) p.getY();

        // used to find a bug
        // int tx = (int) p.getTendencyX();
        // int ty = (int) p.getTendencyY();

        int targetX = fitFloatToWorldWidth(p.getTendencyX());
        int targetY = fitFloatToWorldHeight(p.getTendencyY());

        // used to find a bug
        // if (targetX < 0 || targetX > width - 1) System.out.println("tx " + tx + " -> " + targetX);
        // if (targetY < 0 || targetY > height - 1) System.out.println("ty " + ty + " -> " + targetY);

        // move
        if (worldArray[targetY][targetX] == null) {
            worldArray[currentY][currentX] = null;
            worldArray[targetY][targetX] = p;
            p.setYX(targetX, targetY);

            return true;
        }

        // collision
        return false;
    }

    /**
     * Calls method to correct the x coordinate of a particle, so it doesn't move outside of the world.
     *
     * @param x the x coordinate
     * @return  the corrected x coordinate
     */
    private int fitFloatToWorldWidth(float x) {
        return fitFloatToWorld(x, width);
    }

    /**
     * Calls method to correct the y coordinate of a particle, so it doesn't move outside of the world.
     *
     * @param y
     * @return
     */
    private int fitFloatToWorldHeight(float y) {
        return fitFloatToWorld(y, height);
    }

    /**
     * Corrects the coordinate of a particle, so it doesn't move outside of the world.
     *
     * @param value    the coordinate
     * @param maxValue the biggest value possible of the coordinate
     * @return         the corrected coordinate
     */
    private int fitFloatToWorld(float value, int maxValue) {
        int i = (int) value;

        if (i < 0) i = maxValue + i % maxValue;
        if (i >= maxValue) i %= maxValue;

        return i;
    }

    /**
     * Creates a update of the world.
     * Starts the threads to update the world. Call method to move particles and create friction.
     */
    public void tick() {
        for (var thread : ruleThreads)
            executor.execute(thread);

        while (pool.getActiveCount() > 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("Current threads in pool: " + pool.getActiveCount());
        }

        try {
            for (int i = 0; i < particles.size(); i++)
                for (var p : particles.get(i)) {
                    didMove(p);
                    p.friction(0.9F);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Created the particle lists.
     */
    private void createParticleLists() {
        int n = 0;
        particles = new ArrayList<>(5);
        particleTypes = new ParticleType[5];

        for (int i = 0; i < 5; i++) {
            particles.add(new ArrayList<>());
            particleTypes[i] = new ParticleType(i + 1, 5, 0, 90);
            addParticles(i, n);
        }
    }

    /**
     * Removes a certain amount of a specific type of particle. If value exceeds the particles present, all particles get removed.
     *
     * @param index  the particle type
     * @param amount the amount to be removed
     */
    public void removeParticles(int index, int amount) {
        amount = Math.min(amount, particles.get(index).size());

        for (int i = 0; i < amount; i++)
            particles.get(index).remove(0);

        // clear worldarray
        for (int y= 0; y < worldArray.length; y++)
            for (int x = 0; x < worldArray[y].length; x++)
                worldArray[y][x] = null;

        // rebuild worldarray
        for (int i = 0; i < particles.size(); i++)
            for (var p : particles.get(i))
                if (worldArray[(int) p.getY()][(int) p.getY()] == null)
                    worldArray[(int) p.getY()][(int) p.getX()] = p;
    }

    /**
     * Adds a certain amount of particles of a specific type.
     *
     * @param index  type of particle
     * @param amount amount to be added
     */
    public void addParticles(int index, int amount) {
        for (int i = 0; i < amount; i++) {
            float x;
            float y;
            do {
                x = randomInt(width - 1);
                y = randomInt(height - 1);
            } while (worldArray[(int) y][(int) x] != null);

            //int type = index + 1;
            Particle p = new Particle(particleTypes[index], x, y);
            particles.get(index).add(p);
            worldArray[(int) y][(int) x] = p;
        }
    }

    /**
     * Creates method to create world update, returns the world as an two-dimensional int array.
     *
     * @return two-dimensional int array that represents the world
     */
    public int[][] compute() {
        tick();

        return particleArrayToIntArray();
    }

    /**
     * Converts the particle lists to an int array to make it usable for the Controller.
     *
     * @return two-dimensional int array that represents the world
     */
    private int[][] particleArrayToIntArray() {
        int[][] array = new int[height][width];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                if (worldArray[y][x] == null)
                    array[y][x] = 0;
                else
                    array[y][x] = worldArray[y][x].getType().getType();
            }

        return array;
    }

    /**
     * Creates a random value.
     *
     * @param max the maximum of the value
     * @return    a random value
     */
    private int randomInt(int max) {
        return (int) (Math.random() * (max + 1));
    }

    /**
     * Changes the velocity cap of a certain particle type.
     *
     * @param index the type of the particle
     * @param value the new velocity cap
     */
    public void changeVelocityCap(int index, int value) {
        particleTypes[index].setVelocityCap(value);
    }

    /**
     * Changes the range of a certain particle type.
     *
     * @param index the type of the particle
     * @param value the new range
     */
    public void changeRange(int index, int value) {
        particleTypes[index].setRangeMax(value);
    }
}
