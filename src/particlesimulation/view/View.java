package particlesimulation.view;

import particlesimulation.controller.Controller;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View {
    private Controller controller;
    private String windowTitle;
    private JFrame frame;
    private ParticlePanel particlePanel;
    private JSpinner[] velocityCapSpinners;
    private JSpinner[] gravitySpinners;
    private JSpinner[] gravityRangeSpinners;
    private JPanel uiPanel;
    private int width;
    private int height;

    public View(Controller controller, String windowTitle, int width, int height, int particleDiameter) {
        this.controller = controller;
        this.windowTitle = windowTitle;
        this.width = width;
        this.height = height;

        frame = createFrame();
        uiPanel = createUIPanel();
        particlePanel = createParticlePanel(particleDiameter);
        frame.pack();
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        JFrame f = new JFrame();

        f.setLayout(new BorderLayout(0, 0));
        f.setTitle(windowTitle);
        f.setSize(width, height);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        return f;
    }

    private ParticlePanel createParticlePanel(int particleDiameter) {
        ParticlePanel p = new ParticlePanel(this, width, height, particleDiameter);

        p.setVisible(true);
        frame.add(p, BorderLayout.CENTER);

        return p;
    }

    private JPanel createUIPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel[] particleLabels = new JLabel[5];
        String[] particleNames = {"Yellow", "Red", "Green", "Gray", "Cyan"};

        velocityCapSpinners = new JSpinner[5];
        SpinnerNumberModel[] velocityCapModel = new SpinnerNumberModel[velocityCapSpinners.length];
        JLabel[] velocityCapLabels = new JLabel[velocityCapSpinners.length];

        gravityRangeSpinners = new JSpinner[5];
        SpinnerNumberModel[] rangeModel = new SpinnerNumberModel[gravityRangeSpinners.length];
        JLabel[] gravityRangeLabels = new JLabel[gravityRangeSpinners.length];

        gravitySpinners = new JSpinner[particleLabels.length * particleLabels.length];
        SpinnerNumberModel[] gravityModel = new SpinnerNumberModel[gravitySpinners.length];
        JLabel[] gravitySpinnerLabels = new JLabel[gravitySpinners.length];

        for (int l = 0; l < 5; l++) {
            particleLabels[l] = new JLabel(particleNames[l]);
            constraints.gridx = 0;
            constraints.gridwidth = 2;
            p.add(particleLabels[l], constraints);


            for (int s = 0; s < 5; s++) {
                int spinnerIndex = l * 5 + s;

                gravityModel[spinnerIndex] = new SpinnerNumberModel(0, -999, 999, 1);

                gravitySpinnerLabels[spinnerIndex] = new JLabel(particleNames[s]);
                constraints.gridx = 0;
                constraints.gridwidth = 1;
                p.add(gravitySpinnerLabels[spinnerIndex], constraints);

                gravitySpinners[spinnerIndex] = new JSpinner(gravityModel[spinnerIndex]);
                constraints.gridx = 1;
                constraints.gridwidth = 1;
                gravitySpinners[spinnerIndex].addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        spinnerChanged(e);
                    }
                });
                p.add(gravitySpinners[spinnerIndex], constraints);
            }

            constraints.gridx = 0;
            constraints.gridwidth = 1;
            velocityCapLabels[l] = new JLabel("Velocity Cap");
            p.add(velocityCapLabels[l], constraints);

            rangeModel[l] = new SpinnerNumberModel(5, -1, 999, 1);
            constraints.gridx = 1;
            constraints.gridwidth = 1;
            velocityCapSpinners[l] = new JSpinner(rangeModel[l]);
            velocityCapSpinners[l].addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    velocityChanged(e);
                }
            });
            p.add(velocityCapSpinners[l], constraints);

            constraints.gridx = 0;
            constraints.gridwidth = 1;
            gravitySpinnerLabels[l] = new JLabel("Gravity Range");
            p.add(gravitySpinnerLabels[l], constraints);

            rangeModel[l] = new SpinnerNumberModel(90, 0, 999, 1);
            constraints.gridx = 1;
            constraints.gridwidth = 1;
            gravityRangeSpinners[l] = new JSpinner(rangeModel[l]);
            gravityRangeSpinners[l].addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    rangeChanged(e);
                }
            });
            p.add(gravityRangeSpinners[l], constraints);
        }

        JButton removeParticles = new JButton("remove Particles");
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        removeParticles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.removeParticles();
            }
        });
        p.add(removeParticles, constraints);

        JButton addParticles = new JButton("add Particles");
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        addParticles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.addParticles();
            }
        });
        p.add(addParticles, constraints);

        p.setVisible(true);
        frame.add(p, BorderLayout.EAST);

        return p;
    }

    private void spinnerChanged(ChangeEvent e) {
        for (int i = 0; i < gravitySpinners.length; i++)
            if (gravitySpinners[i] == e.getSource()) {
                JTextField tf = ((JSpinner.DefaultEditor) gravitySpinners[i].getEditor()).getTextField();
                if ((int) gravitySpinners[i].getValue() < 0)
                    tf.setForeground(Color.RED);
                else
                    tf.setForeground(Color.BLACK);
                gravitySpinners[i].setForeground(Color.RED);

                controller.changeRule(i, (int) gravitySpinners[i].getValue());
                break;
            }
    }

    private void rangeChanged(ChangeEvent e) {
        for (int i = 0; i < gravityRangeSpinners.length; i++)
            if (gravityRangeSpinners[i] == e.getSource()) {
                controller.changeRange(i, (int) gravityRangeSpinners[i].getValue());
                break;
            }
    }

    private void velocityChanged(ChangeEvent e) {
        for (int i = 0; i < velocityCapSpinners.length; i++)
            if (velocityCapSpinners[i] == e.getSource()) {
                JTextField tf = ((JSpinner.DefaultEditor) velocityCapSpinners[i].getEditor()).getTextField();
                if ((int) velocityCapSpinners[i].getValue() < 0)
                    tf.setForeground(Color.RED);
                else
                    tf.setForeground(Color.BLACK);
                velocityCapSpinners[i].setForeground(Color.RED);

                controller.changeVelocity(i, (int) velocityCapSpinners[i].getValue());
            }
    }

    public void requestWorld() {
        controller.requestWorld();
    }

    public void updateWorld(int[][] world) {
        particlePanel.updateParticleList(world);
    }

}
