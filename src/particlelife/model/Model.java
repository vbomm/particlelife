package particlelife.model;

import particlelife.controller.WorldUpdateListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Model {
    private final List<WorldUpdateListener> listeners;
    private final World world;

    public Model(int width, int height) {
        listeners = new LinkedList<>();
        world = new World(width, height);
    }

    public void compute() {
        CompletableFuture<int[][]> future = CompletableFuture.supplyAsync(world::compute);
        future.thenAcceptAsync(result -> {
            for (var listener : listeners)
                listener.worldUpdated(result);
        });
    }

    public void addListener(WorldUpdateListener listener) {
        listeners.add(listener);
    }

    public void changeRule(int rule, int value) {
        world.changeRule(rule, value);
    }

    public void changeVelocity(int index, int value) {
        world.changeVelocity(index, value);
    }

    public void changeRange(int index, int value) {
        world.changeRange(index, value);
    }

    public void removeParticles(int index, int amount) {
        world.removeParticles(index, amount);
    }

    public void addParticles(int index, int amount) {
        world.addParticles(index, amount);
    }
}

