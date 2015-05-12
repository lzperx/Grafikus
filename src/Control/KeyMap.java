package Control;

/*
 A robothoz rendelt gombok �rt�k�nek t�rol�s��rt felel�s oszt�ly
 Ezeket k�rdezz�k le a KeyListener haszn�lat�n�l
 */

public class KeyMap {

    private int leftKey;
    private int upKey;
    private int rightKey;
    private int downKey;
    private int oilKey;
    private int glueKey;
    private  int gunKey;

    public boolean left;
    public boolean up;
    public boolean right;
    public boolean down;


    public KeyMap(int leftKey, int upKey, int rightKey, int downKey, int oilKey, int glueKey, int gunKey) {
        this.leftKey = leftKey;
        this.upKey = upKey;
        this.rightKey = rightKey;
        this.downKey = downKey;
        this.oilKey = oilKey;
        this.glueKey = glueKey;
        this.gunKey = gunKey;
    }

    public int getLeftKey() {
        return leftKey;
    }

    public int getUpKey() {
        return upKey;
    }

    public int getRightKey() {
        return rightKey;
    }

    public int getDownKey() {
        return downKey;
    }

    public int getOilKey() {
        return oilKey;
    }

    public int getGlueKey() {
        return glueKey;
    }

    public int getGunKey() {
        return gunKey;
    }
}
