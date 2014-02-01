package net.jurka.inspect.gui;

import org.powerbot.script.wrappers.Tile;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends JFrame {

    private DefaultTableModel model = new DefaultTableModel();
    private final JTextArea logText = new JTextArea(4, 20);
    private final JTable table = new JTable(model);
    private final JButton addCurrentTile = new JButton("Add current tile");
    private final JButton closeFrame = new JButton("Close frame");
    private final JButton freezeBtn = new JButton("Freeze");
    private final JButton cleaLogsBtn = new JButton("Clear logs");
    private final JLabel animationLbl = new JLabel("");
    private final JLabel stanceLbl = new JLabel("");

    private AtomicBoolean running = new AtomicBoolean(true);
    private AtomicInteger animation = new AtomicInteger();
    private AtomicInteger stance = new AtomicInteger();
    private volatile Tile currentTile = null;


    public Main() {

        initUI();
    }

    private void initUI() {

        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        model.addColumn("Name");
        model.addColumn("Id");
        model.addColumn("Location");

        animationLbl.setForeground(Color.BLUE);
        stanceLbl.setForeground(Color.GREEN);

        addCurrentTile.setBounds(100, 60, 100, 30);
        addCurrentTile.setToolTipText("Get current user tile");
        addCurrentTile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentTile != null) {
                    logText.append(currentTile.toString() + "\n");
                }
            }
        });

        closeFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        freezeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running.get()) {
                    running.set(false);
                    freezeBtn.setText("Freeze");
                } else {
                    running.set(true);
                    freezeBtn.setText("Running");
                }
            }
        });

        cleaLogsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logText.setText("");
            }
        });

        // Add components
        buttonPanel.add(addCurrentTile);
        buttonPanel.add(freezeBtn);
        buttonPanel.add(cleaLogsBtn);
        buttonPanel.add(closeFrame);

        buttonPanel.add(stanceLbl);
        buttonPanel.add(animationLbl);

        panel.add(new JScrollPane(table));
        panel.add(BorderLayout.NORTH, buttonPanel);
        panel.add(BorderLayout.SOUTH, logText);

        getContentPane().add(panel);

        setTitle("Tooltip");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            if (!running.get()) return;;

                            stanceLbl.setText("Stance: " + stance.get());
                            animationLbl.setText("Animation: " + animation.get());
                        }
                    });

                    try {
                        Thread.sleep(400);

                    } catch (InterruptedException e) {

                    }
                }

            }
        });
        thread.start();
    }

    public void setCurrentTile(Tile current) {
        this.currentTile = current;
    }

    public void setAnimation(int animation) {
        this.animation.set(animation);
    }

    public void setStance(int stance) {
        this.stance.set(stance);
    }

    public DefaultTableModel getModel() {

        return model;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main ex = new Main();
                ex.setVisible(true);
            }
        });
    }

}
