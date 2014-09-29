package game1;

import java.util.Random;

public class Weapon {
    Random r = new Random();
    int min=0, max=1; 
    int[] attack = {50, 100};
    int attack() {return r.nextInt(attack[max]-attack[min])+attack[min];}
    int size=30;
    float x, y;
    int xp=0;
    int t=0, tmax=1000;
    int xdir = 0;
    void xdir(int dir) {xdir=dir;}
    void happening(int blocksize) {
        if (t >= tmax) t=0;
        x=(float)Math.cos((float)t*Math.PI/tmax*4)*blocksize*2
                + (float)Math.cos((float)xp*Math.PI/tmax)*blocksize*4;
        y=(float)Math.sin((float)t*Math.PI/tmax*4)*blocksize*2
                + (float)Math.sin((float)xp*Math.PI/tmax)*blocksize*4;
        t++;
    }
}
