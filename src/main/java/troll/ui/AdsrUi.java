package troll.ui;

import javax.swing.*;
import java.awt.*;

public class AdsrUi extends Component {
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 100, 100);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(300, 300);
        frame.getContentPane().add(new AdsrUi());
        frame.setVisible(true);
    }
}
