package particlelife.controller;

import particlelife.model.Model;
import particlelife.view.View;

import javax.swing.*;

public class Controller implements WorldUpdateListener {
    private Model model;
    private View view;
    private double lastTime;
    private boolean isWaitingForWorld;

    public Controller(String windowTitle, int width, int height, int particleDiameter) {
        model = new Model(width, height);
        view = new View(this, windowTitle, width, height, particleDiameter);

        initController();
        initView();
    }

    private void initController() {
        isWaitingForWorld = false;
        model.addListener(this);
    }

    private void initView() {
        SwingUtilities.invokeLater(view::show);
    }

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

    public void changeVelocity(int index, int value) {
        model.changeVelocity(index, value);
    }

    public void changeRange(int index, int value) {
        model.changeRange(index, value);
    }

    public void removeParticles(int index, int amount) {
        model.removeParticles(index, amount);
    }

    public void removeParticles() {
        int amount = 50;

        for (int i = 0; i < 5; i++)
            removeParticles(i, amount);
    }

    public void addParticles(int index, int amount) {
        model.addParticles(index, amount);
    }


    public void addParticles() {
        int amount = 50;

        for (int i = 0; i < 5; i++)
            addParticles(i, amount);
    }

    @Override
    public void worldUpdated(int[][] world) {
        view.updateWorld(world);
        isWaitingForWorld = false;
    }
}