package particlesimulation.controller;

/**
 * Interface for listener that gets called when a new world update is available.
 */
public interface WorldUpdateListener {
    void worldUpdated(int[][] world);
}
