/**
 * Created by Владимир on 07.02.2015.
 */
public class BattleShip {
    private int[] coordShip = new int[4];
    private int[][] aroundCoordShip;
    private int[] health;                          //0- death
    private int palub;
    private Boolean is_Hor;

    BattleShip(int coordX, int coordY, int palub, Boolean is_Hor){
        this.palub = palub;
        this.is_Hor = is_Hor;
        coordShip[0] = coordX;
        coordShip[1] = coordY;

        if (is_Hor){
            coordShip[2] = coordX + (30 * palub);
            coordShip[3] = coordY + 30;
        } else {
            coordShip[2] = coordX + 30;
            coordShip[3] = coordY + (30 * palub);
        }
        //System.out.println("coords: " + coordShip[0] + "," + coordShip[1] + "," + coordShip[2] + "," + coordShip[3]);

        int l = 0;
        switch (palub){
            case 1: l = 8; break;
            case 2: l = 10; break;
            case 3: l = 12; break;
            case 4: l = 14; break;
        }

        aroundCoordShip = new int[l][2];
        l = 0;
        for (int i = coordShip[0] - 30; i < coordShip[2] + 30; i += 30){
            for (int j = coordShip[1] - 30; j < coordShip[3] + 30; j += 30){
                if (is_Hor){
                    if (i >= coordShip[0] && i < coordShip[2] && j == coordShip[1])continue;
                } else if(i == coordShip[0] && j >= coordShip[1] && j < coordShip[3]) continue;
                aroundCoordShip[l][0] = i;
                aroundCoordShip[l][1] = j;
                l++;
            }
        }

        health = new int[palub];
        for (int i = 0; i < health.length; i++){
            health[i] = 1;
        }
    }

    int[] getCoords(){
        return coordShip;
    }

    Boolean getIs_Dead(){
        for (int i = 0; i < health.length; i++){
            if (health[i] == 1) return false;
        }
        return true;
    }

    int[][] getAroundCoordShip(){
        return aroundCoordShip;
    }

    void minusHealth(int x, int y){
        int i = -1;
        int tempX = coordShip[0] - 30;
        int tempY = coordShip[1] - 30;
        if (is_Hor){
            do {
                i++;
                tempX += 30;
            } while (x != tempX);
        } else {
            do {
                i++;
                tempY += 30;
            } while (y != tempY);
        }
        if (i != -1)health[i] = 0;
    }
}
