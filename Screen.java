import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class Screen {
    public static void draw() {
        String initialText = "Chess";
        final JTextArea area = new JTextArea(initialText, 8, 50);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton(new AbstractAction("To Lower Case") {
            public void actionPerformed(ActionEvent e) {
                area.setText(area.getText().toLowerCase());
            }
        }));
        buttonPanel.add(new JButton(new AbstractAction("To Upper Case") {
            public void actionPerformed(ActionEvent e) {
                area.setText(area.getText().toUpperCase());
            }
        }));

        JFrame frame = new JFrame("Capitalizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(area), BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }	
}
