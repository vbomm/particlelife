package particlesimulation.controller;

import particlesimulation.model.Model;
import particlesimulation.view.View;

import javax.swing.*;

/**
 * Servers as intermediate point between model and view.
 */
public class Controller implements WorldUpdateListener {
    private Model model;
    private View view;
    private double lastTime;
    private boolean isWaitingForWorld;

    /**
     * Initializes a new Controller.
     *
     * @param windowTitle      title of the window
     * @param width            width of the panel where the particles will be displayed
     * @param height           height of the panel where the particles will be displayed
     * @param particleDiameter diameter used to display particles
     */
    public Controller(String windowTitle, int width, int height, int particleDiameter) {
        model = new Model(width, height);
        view = new View(this, windowTitle, width, height, particleDiameter);

        initController();
        initView();
    }

    /**
     * Sets the isWaitingForWorld to false so a new world computation will get requested and ads a listener that will be
     * used to inform when a new computation of the world is available.
     */
    private void initController() {
        isWaitingForWorld = false;
        model.addListener(this);
    }

    private void initView() {
        SwingUtilities.invokeLater(view::show);
    }

    /**
     * When not already waiting for the computation of a new world state, it will tell the model to start computing one.
     */
    public void requestWorld() {
        // int fps = (int) Math.round(1000000000 / -(lastTime - (lastTime = System.nanoTime())));
        // System.out.println(fps + " " + System.nanoTime());
        if (!isWaitingForWorld) {
            model.compute();
            isWaitingForWorld = true;
        }
    }


    public void changeRule(int rule, int value) {
        model.changeRule(rule, value);
    }

    /**
     * Changes the velocity of a certain particle type.
     *
     * @param index the particle type
     * @param value the velocity
     */
    public void changeVelocity(int index, int value) {
        model.changeVelocity(index, value);
    }

    /**
     * Changes the range of a certain particle type.
     *
     * @param index the particle type
     * @param value the range
     */
    public void changeRange(int index, int value) {
        model.changeRange(index, value);
    }

    /**
     * Removes a certain amount of particles to the world, index determines to which king of particles.
     *
     * @param index  the kind of particle
     * @param amount the amount of particles
     */
    public void removeParticles(int index, int amount) {
        model.removeParticles(index, amount);
    }


    /**
     * Removes a hard coded amount of particles to the world.
     */
    public void removeParticles() {
        int amount = 50;

        for (int i = 0; i < 5; i++)
            removeParticles(i, amount);
    }

    /**
     * Adds a certain amount of particles to the world, index determines to which king of particles.
     *
     * @param index  the kind of particle
     * @param amount the amount of particles
     */
    public void addParticles(int index, int amount) {
        model.addParticles(index, amount);
    }

    /**
     * Adds a hard coded amount of particles to the world.
     */
    public void addParticles() {
        int amount = 50;

        for (int i = 0; i < 5; i++)
            addParticles(i, amount);
    }

    /**
     * Gets called after receiving a world update from Model, then sends updated world to View.
     *
     * @param world the updated world
     */
    @Override
    public void worldUpdated(int[][] world) {
        view.updateWorld(world);
        isWaitingForWorld = false;
    }
}