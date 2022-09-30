package particlelife.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class World {
    private ExecutorService executor;
    private ThreadPoolExecutor pool;
    private List<Rules> ruleThreads;
    private Rules[] rules;
    private int width;
    private int height;
    private Particle[][] worldArray;
    private ArrayList<ArrayList <Particle>> particles;
    private ParticleType[] particleTypes;

    public World(int width, int height) {
        executor = Executors.newCachedThreadPool();
        pool = (ThreadPoolExecutor) executor;

        this.width = width;
        this.height = height;
        worldArray = new Particle[height][width];

        createParticleLists();
        createRuleThreads();
    }


    private void createRuleThreads() {
        ruleThreads = new LinkedList<>();
        rules = new Rules[25];

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                rules[i * 5 + j] = new Rules(particles.get(i), particles.get(j), 0, width, height);
                ruleThreads.add(rules[i * 5 + j]);
            }
    }

    public void changeRule(int rule, int value) {
        rules[rule].setG((float) value / 20);
    }

    /*
    private void addRandomizedRules(int index) {
        for (int i = 0; i < particles.length; i++) {

            ruleThreads.add();
        }
    }

    private float randomForceValue() {
        return (float) ((Math.random() * 0.2) - 0.1);
    }
    */

    private boolean didMove(Particle p) {
        int currentX = (int) p.getX();
        int currentY = (int) p.getY();

        int tx = (int) p.getTendencyX();
        int ty = (int) p.getTendencyY();

        int targetX = fitFloatToWorldWidth(p.getTendencyX());
        int targetY = fitFloatToWorldHeight(p.getTendencyY());

        if (targetX < 0 || targetX > width - 1) System.out.println("tx " + tx + " -> " + targetX);
        if (targetY < 0 || targetY > height - 1) System.out.println("ty " + ty + " -> " + targetY);


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

    private int fitFloatToWorldWidth(float x) {
        return fitFloatToWorld(x, width);
    }

    private int fitFloatToWorldHeight(float y) {
        return fitFloatToWorld(y, height);
    }

    private int fitFloatToWorld(float value, int maxValue) {
        int i = (int) value;

        if (i < 0) i = maxValue + i % maxValue;
        if (i >= maxValue) i %= maxValue;

        return i;
    }

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

    public void removeParticles(int index, int amount) {
        amount = Math.min(amount, particles.get(index).size());

        for (int i = 0; i < amount; i++)
            particles.get(index).remove(0);

        //clear worldarray
        for (int y= 0; y < worldArray.length; y++)
            for (int x = 0; x < worldArray[y].length; x++)
                worldArray[y][x] = null;

        //rebuild worldarray
        for (int i = 0; i < particles.size(); i++)
            for (var p : particles.get(i))
                if (worldArray[(int) p.getY()][(int) p.getY()] == null)
                    worldArray[(int) p.getY()][(int) p.getX()] = p;
    }

    public void addParticles(int index, int amount) {
        for (int i = 0; i < amount; i++) {
            float x;
            float y;
            do {
                x = randomInt(width - 1);
                y = randomInt(height - 1);
            } while (worldArray[(int) y][(int) x] != null);
            int type = index + 1;
            Particle p = new Particle(particleTypes[index], x, y);
            particles.get(index).add(p);
            worldArray[(int) y][(int) x] = p;
        }
    }

    public int[][] compute() {
        tick();

        return particleArrayToIntArray();
    }

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

    private int randomInt(int max) {
        return (int) (Math.random() * (max + 1));
    }

    private int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max + 1 - min));
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void changeVelocity(int index, int value) {
        particleTypes[index].setVelocityCap(value);
    }

    public void changeRange(int index, int value) {
        particleTypes[index].setRangeMax(value);
    }
}
