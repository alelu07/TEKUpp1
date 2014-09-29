package game1;

import java.util.Random;

public class Hero {
    
    Random r = new Random();
    
    float[] pos = new float[2];
    int time;
    int turn;
    int movewait;
    void move() {
        movewait++;
        if (movewait>=128) {
            if (time<3) time++; else time=0;
            movewait=0;
        } 
    }
    boolean attacking = false;
    int attackchance=75;
    int attackminimum=50;
    int attackmaximum=100;
    int iattack=0;
    int attack() {
        attacking = false;
        if (r.nextInt(100)<attackchance)
            return r.nextInt(attackmaximum-attackminimum)+attackminimum;
        return 0;
    }
    int health = 1000;
    boolean alive() {return health>0;}
    void hurt(int hurt) {health-=hurt;}
}
