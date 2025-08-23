package Dic;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppGui extends JFrame {
    private static final int HEIGHT = 400;
    private static final int WIDTH = 1250;

    public AppGui() {
        super("AES CMAC 16 Bytes");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        addGuiComponents();
    }

    private void addGuiComponents() {
        Font myFont = new Font("Dialog",Font.BOLD,30);
        JLabel InputSeedLabel = new JLabel("Seed (Hex)");
        InputSeedLabel.setFont(myFont);
        InputSeedLabel.setBounds(5,25,200,50);
        add(InputSeedLabel);

        JTextFieldLimit InputSeedField = new JTextFieldLimit(32,"[0-9A-Fa-f]+");
        InputSeedField.setFont(myFont);
        InputSeedField.setBounds(200,25,1000,50);
        add(InputSeedField);

        JLabel InputHashLabel = new JLabel("Hash (Hex)");
        InputHashLabel.setFont(myFont);
        InputHashLabel.setBounds(5,100,200,50);
        add(InputHashLabel);

        JTextFieldLimit InputHashField = new JTextFieldLimit(32,"[0-9A-Fa-f]+");
        InputHashField.setFont(myFont);
        InputHashField.setBounds(200,100,1000,50);
        add(InputHashField);

        JLabel OutputKeyLabel = new JLabel("Key (Hex)");
        OutputKeyLabel.setFont(myFont);
        OutputKeyLabel.setBounds(5,175,200,50);
        add(OutputKeyLabel);

        JTextField OutputKeyField = new JTextField();
        OutputKeyField.setFont(myFont);
        OutputKeyField.setBounds(200,175,1000,50);
        OutputKeyField.setEditable(false);
        add(OutputKeyField);

        JButton generateKeyButton = new JButton("Generate Key");
        generateKeyButton.setFont(myFont);
        generateKeyButton.setBounds((WIDTH - 500)/2, 250, 500, 50);
        generateKeyButton.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e){
                AES_CMAC aesCmac = new AES_CMAC();
                //String key = aesCmac.generateKey(InputSeedField.getText(), InputHashField.getText());
                String key= "";
                try {
                    key = aesCmac.generateKey(InputSeedField.getText(), InputHashField.getText());
                } catch (Exception e1) {
                    new PopUp(e1.getMessage());
                }
                OutputKeyField.setText(key);
            }
        });
        add(generateKeyButton);
    }
}
