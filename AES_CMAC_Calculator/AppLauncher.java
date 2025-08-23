import javax.swing.SwingUtilities;

import Dic.AppGui;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                new AppGui().setVisible(true);
            }
        });
    }

}
