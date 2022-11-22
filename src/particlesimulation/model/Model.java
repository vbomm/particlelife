package particlesimulation.model;

import particlesimulation.controller.WorldUpdateListener;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The Model that communicates to World and gives feedback to the Controller.
 */
public class Model {
    private final List<WorldUpdateListener> listeners;
    private final World world;

    /**
     * Initialzes a new Model.
     *
     * @param width  the width of the world
     * @param height the height of the world
     */
    public Model(int width, int height) {
        listeners = new LinkedList<>();
        world = new World(width, height);
    }

    /**
     * Gets called to create a world update. Calls the listeners when the world update is finished.
     */
    public void compute() {
        CompletableFuture<int[][]> future = CompletableFuture.supplyAsync(world::compute);
        future.thenAcceptAsync(result -> {
            for (var listener : listeners)
                listener.worldUpdated(result);
        });
    }

    /**
     * Adds a listener that gets called when a world update is finished.
     *
     * @param listener
     */
    public void addListener(WorldUpdateListener listener) {
        listeners.add(listener);
    }

    /**
     * Changes the strength of a rule.
     *
     * @param rule  the rule
     * @param value the new strength
     */
    public void changeRule(int rule, int value) {
        world.changeRule(rule, value);
    }

    /**
     * calls World to change the velocity cap of a certain partice type.
     *
     * @param index the particle type
     * @param value the new velocity cap
     */
    public void changeVelocityCap(int index, int value) {
        world.changeVelocityCap(index, value);
    }

    /**
     * Changes the range of a certain particle type.
     *
     * @param index the particle type
     * @param value the new range
     */
    public void changeRange(int index, int value) {
        world.changeRange(index, value);
    }

    /**
     * Removes a specific amount of a certain particle type.
     *
     * @param index  the particle type
     * @param amount the amount to be removed
     */
    public void removeParticles(int index, int amount) {
        world.removeParticles(index, amount);
    }

    /**
     * Adds a specific amount of a certain particle type.
     *
     * @param index  the particle type
     * @param amount the amount to be added
     */
    public void addParticles(int index, int amount) {
        world.addParticles(index, amount);
    }
}

