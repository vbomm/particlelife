package particlesimulation.controller;

/**
 * Main class, starts the Thread of the main window.
 */
public class Runner {

    /**
     * New controller object gets created here to start the program.
     *
     * @param args
     */
    public static void main(String[] args) {
        String windowTitle = "ParticleLife";
        int particleDiameter = 4;
        int width = 1200;
        int height = 900;

        new Controller(windowTitle, width, height, particleDiameter);
    }
}
