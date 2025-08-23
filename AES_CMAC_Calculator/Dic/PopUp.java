package Dic;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PopUp extends JFrame {
    public PopUp(String arg){
        super("Warning");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        JButton okButton = new JButton("OK");
        okButton.setBounds(100, 75, 100, 30);
        okButton.addActionListener(e -> dispose());
        add(okButton);

        JLabel messageLabel = new JLabel(arg);
        messageLabel.setBounds(50, 25, 200, 50);
        add(messageLabel);
        setVisible(true);
    }
}
