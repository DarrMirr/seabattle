import javax.swing.*;
import java.awt.*;

public class MainApp implements Observer {
    private WindowFrame windowFrame;
    private EndFrame endFrame;
    private Actions actions;
    private PaneMouseListener paneMouseListener = new PaneMouseListener();                            //слушатель мыши
    private PaneActionListener paneActionListener = new PaneActionListener();                         //слушатель действий
    private boolean isClick;                                                                          //было ли нажатие кнопки мыши
    private boolean isAuto;                                                                           //нажата ли кнопка Авторасстановка
    private boolean isCreateNew;                                                                      //начать сначала?
    private boolean isTurn;                                                                           //начался ли ход?
    private int x, y;
    private Object source;                                                                            //объект, вызвавший обработку событий
    private Thread threadRunning;
    private Runnable running = new Runnable() {
        @Override
        public void run() {
            while (!actions.getUserBattleShipList().isEmpty() && !actions.getEnemyBattleShipArList().isEmpty()){
                if (isCreateNew) break;
                if (isClick && source.equals(windowFrame.getBattlePaneList().get(1))) {
                    isTurn = true;                                                                    //ход начинается
                    isClick = false;                                                                  //обработка нажатия кнопки мыши завершена
                    actions.setMove(x, y);                                                            //выполнение хода
                    isTurn = false;                                                                   //ход заканчивается
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!isCreateNew){
                if (actions.getUserBattleShipList().isEmpty()) endFrame = new EndFrame("Вы проиграли", paneActionListener);
                else endFrame = new EndFrame("Вы выграли", paneActionListener);
                endFrame.setVisibleFrame();
            }
        }
    };

    MainApp(){
        paneMouseListener.registerObserver(this);                                                     //регистрация наблюдателей
        paneActionListener.registerObserver(this);
        windowFrame = new WindowFrame(paneMouseListener, paneActionListener);
        actions = new Actions(windowFrame);
        windowFrame.showFrame();
        createNewGame();
    }

    private void createNewGame(){
        actions.initNewActions(isCreateNew);
        isClick = isAuto = isCreateNew = isTurn = false;
    }

    private void createBattleship(){
        if (isAuto) actions.autoCreate();
        else actions.createToClick();
        isClick = isAuto = false;
        if (actions.getUserBattleShipList().size() == 10) {
            windowFrame.getContentPane().getComponent(0).getComponentAt(20, 20).setEnabled(false);
            windowFrame.getContentPane().getComponent(0).getComponentAt(120, 20).setEnabled(false);
            windowFrame.setDrawShipVisible();
            actions.autoCreate();                                                                         //авторасстановка для противника
            runningGame();
        }
    }

    private void runningGame(){
        threadRunning = new Thread(running);
        threadRunning.start();
    }

    @Override
    public void update(Object source, int getX, int getY, boolean isClick) {
        if (isClick){
            if (!isTurn){
                x = getX;
                y = getY;
                this.source = source;
                this.isClick = isClick;
                if (source.equals(windowFrame.getBattlePaneList().get(0)) && actions.getEnemyBattleShipArList().isEmpty()) createBattleship();
            }
        } else {
            actions.moveCursor(source, getX, getY);
        }
    }


    @Override
    public void update(String actionCommand) {
        if (actionCommand.equals("Новая игра")) {
            isCreateNew = true;
            if (!actions.getEnemyBattleShipArList().isEmpty()) {                                                                //запущен ли поток? (не явным способом)
                while (threadRunning.isAlive()){};                                                                              //ожидание завершение потока
            }
            if (actions.getUserBattleShipList().isEmpty() || actions.getEnemyBattleShipArList().isEmpty())endFrame.dispose();   //признак завершения игры - отсутствие кораблей  в коллекции
            createNewGame();
        }
        if (actionCommand.equals("Выход")) System.exit(0);
        if (actionCommand.equals("О программе")){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog((Component) windowFrame, "Написано на java Полукеевым В.С. \n" +
                            "01.05.2015", "О программе", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
        if (actionCommand.equals("Авторасстановка")) {
            windowFrame.setDrawShipVisible();
            isAuto = true;
            createBattleship();
        }
        if (actionCommand.equals("Повернуть")) windowFrame.setDrawShipRotate();
    }
}
