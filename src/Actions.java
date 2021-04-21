import java.util.ArrayList;
import java.util.Random;

public class Actions {
    private ArrayList<DrawBattlePane> battlePaneList;
    private DrawShip drawShip;
    private WindowFrame windowFrame;

    private ArrayList<BattleShip> userBattleShipArList = new ArrayList<BattleShip>(10);
    private ArrayList<BattleShip> enemyBattleShipArList = new ArrayList<BattleShip>(10);
    //для генерации кораблей
    private int count;
    private int palub;
    //для стрельбы по соседним клеткам при попадании
    int direction;                                                                      //направление w - 0, n - 1, e - 2, s - 3
    private boolean isShooting;                                                         //сделал выстрел
    private int shootingCount;                                                          //колличество выстрелов
    private int[] lastTargetCoords = new  int[2];
    private Random random = new Random();
    private int[] currentSizeShip = new int[2];                                         //размеры до разворачивания
    private int[][] battlemapEnemy = new int[10][10];
    private int[][] battlemapUser = new int[10][10];

    Actions(WindowFrame windowFrame){
        this.windowFrame = windowFrame;
        this.drawShip = windowFrame.getDrawShip();
        this.battlePaneList = windowFrame.getBattlePaneList();
    }

    //инициализация actions при нажатии Новая игра
    public void initNewActions(boolean isCreateNew){
        count = 0;
        palub = 4;
        if (isCreateNew) {
            battlePaneList.get(0).drawClearPane();
            battlePaneList.get(1).drawClearPane();
            windowFrame.getContentPane().getComponent(0).getComponentAt(20, 20).setEnabled(true);
            windowFrame.getContentPane().getComponent(0).getComponentAt(120, 20).setEnabled(true);            drawShip.setEnabled(true);
            drawShip.setVisible(true);
            drawShip.nextShip(palub);
        }
        direction = random.nextInt(4);
        isShooting = false;
        shootingCount = 0;
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                battlemapEnemy[i][j] = -1;
                battlemapUser[i][j] = -1;
            }
            if (userBattleShipArList.size() != 0) userBattleShipArList.remove(userBattleShipArList.size()-1);
            if (enemyBattleShipArList.size() != 0) enemyBattleShipArList.remove(enemyBattleShipArList.size()-1);
        }
    }

    private void addShip(int x, int y, boolean is_Hor, ArrayList<BattleShip> addingShipList){
        addingShipList.add(new BattleShip(x, y, palub, is_Hor));
        if (addingShipList == userBattleShipArList) battlePaneList.get(0).drawShipOnPane(userBattleShipArList.get(userBattleShipArList.size()-1).getCoords());
        if (count == 0) {
            palub--;
            if (palub == 0) drawShip.setEnabled(false);
            count = 5 - palub;
            drawShip.nextShip(palub);
        }
        count--;
    }

    //авторазмещение
    public void autoCreate(){
        ArrayList<BattleShip> autoBattleShipCreate;
        if (userBattleShipArList.size() == 10){                                    //проверка комбинированого способа расстановки (ручной+авто)
            count = 0;
            palub = 4;
            autoBattleShipCreate = enemyBattleShipArList;
        } else autoBattleShipCreate = userBattleShipArList;
        int[] tempCoords;
        boolean is_Hor;

        while (autoBattleShipCreate.size() < 10){
            tempCoords = getAutoCoordsShip();
            is_Hor = random.nextBoolean();
            setCurrentShip(is_Hor);
            if ((tempCoords[0] + currentSizeShip[0] <= 300) && (tempCoords[1] + currentSizeShip[1] <= 300)){
                if (checkAround(tempCoords[0], tempCoords[1], currentSizeShip[0], currentSizeShip[1], autoBattleShipCreate)) addShip(tempCoords[0], tempCoords[1], is_Hor, autoBattleShipCreate);
            }
        }
    }

    //для расстановки
    private Boolean checkAround(int x, int y, int width, int height, ArrayList<BattleShip> checkBattleship){
        for (int i = x; i <= x + width; i += 30){
            for (int j = y; j <= y + height; j += 30){
                if (checkShip(i, j, checkBattleship, true)) return false;
            }
        }
        return true;
    }

    private void setCurrentShip(boolean is_Hor){
        if (is_Hor){
            currentSizeShip[0] = 30 * palub;
            currentSizeShip[1] = 30;
        } else {
            currentSizeShip[0] = 30;
            currentSizeShip[1] = 30 * palub;
        }
    }

    //генератор случайных координат
    private int[] getAutoCoordsShip(){
        int[] coords = new int[2];
        for (int i = 0; i < 2; i++){
            coords[i] = random.nextInt(300) / 30;
            coords[i] *= 30;
        }
        return coords;
    }

    //поведение компьютера
    private void UI(){
        int[] tempCoords;
        int x, y;
        isShooting = true;
        do {
            if (shootingCount == 0) {
                tempCoords = getAutoCoordsShip();
                y = tempCoords[1] / 30;
                x = tempCoords[0] / 30;
            }
            else {
                boolean isSearch = true;
                tempCoords = lastTargetCoords.clone();
                y = tempCoords[1] / 30;
                x = tempCoords[0] / 30;
                int l = x;
                int k = y;
                while (isSearch){
                    if (direction % 2 == 0){
                        if (l < 0 || l > 9 || battlemapUser[y][l] == 0) {
                            direction = random.nextInt(4);
                            l = x;
                        }
                        else if (battlemapUser[y][l] == 1) {
                            if (direction == 0) l--;
                            else l++;
                        } else {
                            isSearch = false;
                            x = l;
                            tempCoords[0] = x * 30;
                        }
                    } else {
                        if (k < 0 || k > 9 || battlemapUser[k][x] == 0) {
                            direction = random.nextInt(4);
                            k = y;
                        }
                        else if (battlemapUser[k][x] == 1){
                            if (direction == 1) k--;
                            else k++;
                        } else {
                            isSearch = false;
                            y = k;
                            tempCoords[1] = y * 30;
                        }
                    }
                }
            }
            if (battlemapUser[y][x] == -1){
                if (checkShip(tempCoords[0] , tempCoords[1], userBattleShipArList, false)){
                    battlePaneList.get(0).drawCrest(tempCoords[0], tempCoords[1]);
                    //есть попадание
                    lastTargetCoords = tempCoords;
                    shootingCount++;
                    //убит ли?
                    if (checkIsDead(tempCoords[0], tempCoords[1], userBattleShipArList)) shootingCount = 0;
                    writeBattlemap(tempCoords[0], tempCoords[1], false, 1);
                    //скорость хода противника
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    battlePaneList.get(0).drawDot(tempCoords[0] + 10, tempCoords[1] + 10);
                    isShooting = false;
                    writeBattlemap(tempCoords[0], tempCoords[1], false, 0);
                }
            }
        } while (isShooting);
    }

    //false - проверка координаты, true - проверка для checkAround
    private boolean checkShip(int x, int y, ArrayList<BattleShip> checkShipList, boolean trig){
        int[] tempCoords;
        for (int i = 0; i < checkShipList.size(); i++){
            tempCoords = checkShipList.get(i).getCoords();
            if (trig) {
                if (x >= tempCoords[0] && x <= tempCoords[2] && y >= tempCoords[1] && y <= tempCoords[3]) return true;
            } else if (x >= tempCoords[0] && x < tempCoords[2] && y >= tempCoords[1] && y < tempCoords[3]) return true;
        }
        return false;
    }

    private void writeBattlemap(int x, int y, boolean is_Comp, int num){
        if (is_Comp) battlemapEnemy[y / 30][x / 30] = num;
        else battlemapUser[y / 30][x / 30] = num;
    }

    boolean checkIsDead(int x, int y, ArrayList<BattleShip> checkShipList){
        int[] tempCoords;
        int[][] tempAroundCoords;
        int j = -1;
        int numPane = 0;
        boolean isComp = false;

        for (int i = 0; i < checkShipList.size(); i++){
            tempCoords = checkShipList.get(i).getCoords();
            if (x >= tempCoords[0] && x < tempCoords[2] && y >= tempCoords[1] && y < tempCoords[3]) j = i;
        }
        checkShipList.get(j).minusHealth(x, y);
        if (checkShipList.equals(enemyBattleShipArList)) { numPane = 1; isComp = true; }
        if (checkShipList.get(j).getIs_Dead()){
            tempAroundCoords = checkShipList.get(j).getAroundCoordShip();
            //отрисоква границы для кораблей компьютера
            battlePaneList.get(numPane).drawBoldRect(checkShipList.get(j).getCoords());
            //отрисовка по периметру
            for (int i = 0; i < tempAroundCoords.length; i++){
                if (tempAroundCoords[i][0] >= 0 && tempAroundCoords[i][0] < 300 && tempAroundCoords[i][1] >= 0 && tempAroundCoords[i][1] < 300) {
                    battlePaneList.get(numPane).drawDot(tempAroundCoords[i][0] + 10, tempAroundCoords[i][1] + 10);
                    writeBattlemap(tempAroundCoords[i][0], tempAroundCoords[i][1], isComp, 0);
                }
            }
            checkShipList.remove(j);
            return true;
        }
        return false;
    }

    public void setMove(int i, int j){
        i /= 30;
        j /= 30;
        if (battlemapEnemy[j][i] == -1){
            i *= 30;
            j *= 30;
            if (checkShip(i , j, enemyBattleShipArList, false)){
                battlePaneList.get(1).drawCrest(i, j);
                checkIsDead(i, j, enemyBattleShipArList);
                writeBattlemap(i, j, true, 1);
            } else {
                battlePaneList.get(1).drawDot(i + 10, j + 10);
                writeBattlemap(i, j, true, 0);
                UI();
            }
        }
    }

    public void moveCursor(Object source, int x, int y){
        int i;
        int j;
        if (source.equals(battlePaneList.get(0))){
            if (x > (300 - drawShip.getSizeShip().width)){                          //проверка выхода за пределы по х
                i = (300 - drawShip.getSizeShip().width) / 30 * 30;
            } else i = x / 30 * 30;
            if (y > (300 - drawShip.getSizeShip().height)){                         //проверка выхода за пределы по y
                j = (300 - drawShip.getSizeShip().height) / 30 * 30;
            } else j = y / 30 * 30;
            drawShip.setLocation(i, j);
        } else if (source.equals(battlePaneList.get(1))){
            if (x != 300) i = x / 30 * 30;
            else i = 270;
            if (y != 300) j = y / 30 * 30;
            else j = 270;
            windowFrame.setCursor(i, j);
        }
    }

    public void createToClick(){
        if (userBattleShipArList.size() < 10){
            if (checkAround(drawShip.getX(), drawShip.getY(), drawShip.getSizeShip().width, drawShip.getSizeShip().height, userBattleShipArList)) addShip(drawShip.getX(), drawShip.getY(), drawShip.is_Hor(), userBattleShipArList);
        }
    }

    public ArrayList<BattleShip> getUserBattleShipList(){
        return userBattleShipArList;
    }

    public ArrayList<BattleShip> getEnemyBattleShipArList(){
        return enemyBattleShipArList;
    }
}