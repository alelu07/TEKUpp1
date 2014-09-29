package game1;

import java.util.Random;

public class Mobs {
    Random r = new Random();
    int x=0, y=1;
    int mobnum = 1;
    int size = 256;
    int[][] iMove=new int[2][mobnum];
    int[][] turn=new int[2][mobnum];
    float[][] pos=new float[2][mobnum];
    int movewait=0;
    void startpos(int width, int height, int blocksize) {
        for (int i=0; i<mobnum; i++) {
            pos[x][i]=r.nextInt(width/blocksize)*blocksize;
            pos[y][i]=r.nextInt(height/blocksize)*blocksize;
        }
    }
    void move(int blocksize, double speed) {
        movewait++;
        if (movewait>=blocksize/speed) {
            for (int i=0; i<mobnum; i++) {
                iMove[x][i] = r.nextInt(3)-1;
                iMove[y][i] = r.nextInt(3)-1;
                turn[x][i]=-iMove[x][i];
                if (turn[x][i]==0) turn[x][i]=1;
            }
            movewait=0;
        }
        for (int i=0; i<mobnum; i++) {
            pos[x][i] += (float)iMove[x][i]*speed;
            pos[y][i] += (float)iMove[y][i]*speed;
        }
    }
    boolean attacking = false;
    int attackchance=50;
    int attackminimum=5;
    int attackmaximum=10;
    int iattack=0;
    int attack() {
        attacking = false;
        if (r.nextInt(100)<attackchance)
            return r.nextInt(attackmaximum-attackminimum)+attackminimum;
        return 0;
    }
    int health=50000;
    boolean alive() {return health>0;}
    void hurt(int hurt) {health-=hurt;}
}
