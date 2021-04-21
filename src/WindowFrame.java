import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Владимир on 08.02.2015.
 * комментарии: начало координат поля (30,30)
 */

public class WindowFrame extends JFrame {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu jMenu;
    private JPanel jPanel;
    private DrawCursor cursor;
    private DrawShip drawShip;
    private ArrayList<DrawBattlePane> battlePaneList = new ArrayList<DrawBattlePane>();
    private PaneMouseListener paneMouseListener;
    private PaneActionListener paneActionListener;

    WindowFrame(PaneMouseListener paneMouseListener, PaneActionListener paneActionListener){
        super("Морской бой");
        getContentPane().setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.paneMouseListener = paneMouseListener;
        this.paneActionListener = paneActionListener;
        //init menu
        createjMenu("Файл", KeyEvent.VK_F);
        createMenuItem("Новая игра", KeyEvent.VK_N);
        createMenuItem("Выход", KeyEvent.VK_X);
        createjMenu("Справка", KeyEvent.VK_H);
        createMenuItem("О программе", KeyEvent.VK_A);
        setJMenuBar(menuBar);
        //init mainFrame
        initFrame();
        pack();
        setResizable(false);
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
    }

    public void showFrame(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }

    private void createMenuItem(String name, int keyEvent){
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.setMnemonic(keyEvent);
        menuItem.setFont(new Font("Dialog", Font.PLAIN, 16));
        menuItem.addActionListener(paneActionListener);
        jMenu.add(menuItem);
    }

    private void createjMenu(String name, int keyEvent){
        jMenu = new JMenu(name);
        jMenu.setMnemonic(keyEvent);
        jMenu.setFont(new Font("Dialog", Font.PLAIN, 18));
        menuBar.add(jMenu);
    }

    private JTextArea createTextArea(String name){
        JTextArea textArea = new JTextArea(name);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        textArea.setFont(new Font("Dialog", Font.PLAIN, 20));
        return textArea;
    }

    private JLabel createJLabel(String name){
        JLabel jLabel = new JLabel(name);
        jLabel.setHorizontalAlignment(JLabel.CENTER);
        jLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        return jLabel;
    }

    private JPanel createBattlePane(int numberPane){
        if (numberPane == 0) drawShip  = new DrawShip(4, 30);
        else cursor = new DrawCursor();
        jPanel = new JPanel(new BorderLayout());
        battlePaneList.add(new DrawBattlePane());
        battlePaneList.get(numberPane).addMouseMotionListener(paneMouseListener);
        battlePaneList.get(numberPane).addMouseListener(paneMouseListener);
        jPanel.add(new DrawBattleChars(), BorderLayout.CENTER);
        jPanel.add(battlePaneList.get(numberPane), BorderLayout.CENTER);
        if (numberPane == 0) jPanel.add(drawShip, BorderLayout.CENTER);
        else jPanel.add(cursor, BorderLayout.CENTER);
        return jPanel;
    }

    private JButton createButton(String name){
        JButton jButton = new JButton(name);
        jButton.setFocusable(false);
        jButton.addActionListener(paneActionListener);
        return jButton;
    }

    private void initFrame(){
        jPanel = new JPanel();

        jPanel.add(createButton("Повернуть"), BorderLayout.CENTER);
        jPanel.add(createButton("Авторасстановка"), BorderLayout.CENTER);
        add(jPanel, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));

        add(createJLabel("МОЙ ФЛОТ"), new GridBagConstraints(1, 0, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 250, 0));
        add(createJLabel("ФЛОТ ПРОТИВНИКА"), new GridBagConstraints(4, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 170, 0));
        //отрисовка панели подсказки
        jPanel = new JPanel(new GridBagLayout());
        add(jPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
        jPanel.add(createTextArea("Разместите на поле \nваши корабли"), new GridBagConstraints(0, GridBagConstraints.RELATIVE, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5),0,0));
        for (int i = 1; i < 5; i++){
            jPanel.add(createJLabel(i + " x "), new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0));
            jPanel.add(new DrawShip(5 - i, 20), new GridBagConstraints(1, GridBagConstraints.RELATIVE, 1, 1, 0, 0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0));
        }
        jPanel.add(createTextArea("Подсказка: \nтак размещать \nкорабли на поле нельзя"), new GridBagConstraints(0, GridBagConstraints.RELATIVE, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0));
        jPanel.add(new DrawWrongShip(), new GridBagConstraints(0, GridBagConstraints.RELATIVE, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5),0, 50));
        //отрисовка поля битвы
        add(createBattlePane(0), new GridBagConstraints(2, 1, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
        add(createBattlePane(1), new GridBagConstraints(4, 1, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    }

    protected DrawShip getDrawShip(){
        return drawShip;
    }

    protected ArrayList<DrawBattlePane> getBattlePaneList(){
        return battlePaneList;
    }

    public void setCursor(int x, int y){
        cursor.setLocation(x, y);
    }

    public void setDrawShipRotate(){
        drawShip.rotateShip();
    }

    public void setDrawShipVisible(){
        drawShip.setVisible(false);
    }
}