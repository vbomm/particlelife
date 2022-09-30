package particlelife.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParticlePanel extends JPanel {
    private View view;
    private final int width;
    private final int height;
    private final int particleDiameter;
    private Timer timer;
    private int framesPerSecond;
    private int[][] world;

    public ParticlePanel(View view, int width, int height, int particleDiameter) {
        this.width = width;
        this.height = height;
        this.particleDiameter = particleDiameter;
        framesPerSecond = 24;

        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setBackground(Color.BLACK);
        timer = new Timer(1000 / framesPerSecond, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                view.requestWorld();
            }
        });
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (world == null) return;

        Graphics2D g2D = (Graphics2D) g;

        for (int y = 0; y < world.length; y++)
            for (int x = 0; x < world[y].length; x++) {
                if (world[y][x] == 0)
                    continue;

                Color color = switch (world[y][x]) {
                    case 1 -> Color.YELLOW;
                    case 2 -> Color.RED;
                    case 3 -> Color.GREEN;
                    case 4 -> Color.LIGHT_GRAY;
                    case 5 -> Color.CYAN;
                    default -> Color.BLACK;
                };
                g2D.setColor(color);
                g2D.fillOval(x, y, particleDiameter, particleDiameter);
            }
    }

    public void updateParticleList(int[][] world) {
        this.world = world;
    }
}
