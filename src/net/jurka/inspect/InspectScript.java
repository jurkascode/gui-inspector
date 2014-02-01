package net.jurka.inspect;

import net.jurka.inspect.gui.Main;
import org.powerbot.event.MessageEvent;
import org.powerbot.event.MessageListener;
import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@Manifest(
        name = "Inspector script",
        authors = "Jurka",
        description = "Inspect the elements around you")
public class InspectScript extends PollingScript implements MessageListener, PaintListener {

    private Main frame;

    @Override
    public void start() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new Main();
                frame.setVisible(true);
            }
        });
    }

    @Override
    public int poll() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Player current = ctx.players.local();
                frame.setCurrentTile(current.getLocation());

                frame.setAnimation(current.getAnimation());
                frame.setStance(current.getStance());

                DefaultTableModel model = frame.getModel();
                model.setRowCount(0);

                for (GameObject object : ctx.objects.select().nearest().limit(7)) {
                    model.addRow(new Object[] {
                            object.getName(),
                            object.getId(),
                            object.getLocation().toString()
                    });
                }
            }
        });

        return 1000;
    }

    @Override
    public void messaged(MessageEvent e) {
    }

    @Override
    public void repaint(Graphics g) {

    }
}