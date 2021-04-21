import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EndFrame extends JFrame {
    private PaneActionListener paneActionListener;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private JLabel jLabel;
    private JButton jButton;

    EndFrame(String nameFrame, PaneActionListener paneActionListener){
        super(nameFrame);
        this.paneActionListener = paneActionListener;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 1));
        jLabel = new JLabel(nameFrame);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(jLabel);
        jButton = new JButton("Новая игра");
        jButton.setFocusable(false);
        jButton.addActionListener(paneActionListener);
        add(jButton);
        jButton = new JButton("Выход");
        jButton.setFocusable(false);
        jButton.addActionListener(paneActionListener);
        add(jButton);
        pack();
        setResizable(false);
        setLocation((screenSize.width - getWidth()) /2, (screenSize.height - getHeight()) / 2);
    }

    public void setVisibleFrame(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }
}
