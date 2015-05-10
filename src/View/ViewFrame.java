package View;
import Model.GameMapContainer;

import javax.swing.*;
import java.awt.*;

public class ViewFrame extends JFrame {

    public ViewPanel viewPanel;

    public ViewFrame(GameMapContainer gameMapContainer, Dimension size) {

        viewPanel = new ViewPanel(gameMapContainer);
        add(viewPanel);
        setTitle("LZ/X :: Phoebe Project");
        setSize(size.width, size.height);
        setLocationRelativeTo(null);
        setResizable(false);
        viewPanel.repaint();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}