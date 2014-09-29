//Adventure Alpha 0.1
/**Aurthors:
**Lundgren Alexander
**/
/*Changelog:
*
*/
package game1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main extends JFrame implements Runnable {
    
    public int width = 1280, height = 720;
    Random r = new Random();
    Font font = new Font("Arial", Font.BOLD, 32);
    
    Mobs mobs = new Mobs();
    Hero hero = new Hero();
    Weapon weapon = new Weapon();
    
    
    int x=0, y=1,
        xdir, ydir,
        world[] = new int[2],
        rp;
    
    
    private BufferedImage heroImg;
    private BufferedImage sheepImg;
    private BufferedImage fireImg;
    
    public boolean drawterrain = true;
    private int blocksize = 32;
    static int blocksizesource = 32;
    private int blockmax = 64;
    private int[][] blocktype = new int[blockmax][blockmax];
    private BufferedImage terrain;
    
    private Image dbImage;
    private Graphics dbg;
    
    @Override
    public void run() {
        try {
            while(hero.alive() && mobs.alive()) {
                move();
                Thread.sleep(1);
            }
        } catch(Exception e) {System.out.println(e);}
    }
    public void move() {
        if(drawterrain) mobs.startpos(width, height, blocksize);
        mobs.move(blocksize*5, .5);
        hero.move();
        
        hero.pos[x]+=(float)xdir*.5;
        hero.pos[y]+=(float)ydir*.5;
        world[x] = -(int)hero.pos[x];
        world[y] = -(int)hero.pos[y];   
        hero.turn=xdir;
        if (hero.turn==0) hero.turn=1;
        
        weapon.xp+=weapon.xdir;
        weapon.happening(blocksize);
        
        if (mobs.attacking) {mobs.iattack=mobs.attack(); hero.hurt(mobs.iattack);}
        if (hero.attacking) {hero.iattack=hero.attack(); mobs.hurt(hero.iattack);}
    }
    public void Xdir(int Xdir) {
        xdir = Xdir;
    }
    public void Ydir(int Ydir) {
        ydir = Ydir;
    }
    public Main() {
        try {
            heroImg = ImageIO.read(new File("C:\\Adventure\\hero.png"));
            terrain = ImageIO.read(new File("C:\\Adventure\\terrain.png"));
            sheepImg = ImageIO.read(new File("C:\\Adventure\\sheep.png"));
           fireImg = ImageIO.read(new File("C:\\Adventure\\fireball.png"));
        } catch (IOException e) {
            System.out.println("couldn't load Image: " + e);
        }
    addKeyListener(new Key());
    setTitle("HEJ 0.0.1");
    setSize(width, height);
    setResizable(false);
    setBackground(Color.BLACK);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    @Override
    public void paint(Graphics g) {
        dbImage = createImage(getWidth(), getHeight());
        dbg = dbImage.getGraphics();
        paintWorld(dbg);
        paintComponent(dbg);
        g.drawImage(dbImage, 0, 0, this);
    }
    
    public void paintWorld(Graphics g) {
        for (int i=0; i<blockmax; i++) {
            for (int j=0; j<blockmax; j++) {
                if (drawterrain) {rp=r.nextInt(100); if (i>=blockmax-1 && j>=blockmax-1) drawterrain=false;
                if (rp<50) blocktype[i][j]=0; else blocktype[i][j]=1;}
                g.drawImage(terrain, world[x]+blocksize*i, world[y]+blocksize*j, world[x]+blocksize*i+blocksize, world[y]+blocksize*j+blocksize, blocksizesource*blocktype[i][j], 0, blocksizesource*blocktype[i][j]+blocksizesource, blocksizesource, null);
            }
        }
        //(repaint();
    }
    public void paintComponent(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(font);
        Rectangle sheeprect = new Rectangle(0, 0, 0, 0);
        for (int i=0; i<mobs.mobnum; i++) {
            sheeprect = new Rectangle ((int)mobs.pos[x][i]+world[x], (int)mobs.pos[y][i]+world[y], mobs.size, mobs.size);
            g.drawImage(sheepImg, sheeprect.x-sheeprect.width/2*mobs.turn[x][i]+sheeprect.width/2, sheeprect.y, sheeprect.width*mobs.turn[x][i], sheeprect.height, null);
            //g.drawRect(sheeprect.x, sheeprect.y, Math.abs(sheeprect.width), sheeprect.height);
        }
        
        Rectangle weaponrect = new Rectangle((int)weapon.x+width/2, (int)weapon.y+height/2, weapon.size, weapon.size);
        g.drawImage(fireImg, weaponrect.x, weaponrect.y, weaponrect.x+weaponrect.width, weaponrect.y+weaponrect.height, 0, 0, 32, 32, null);
        
        Rectangle herorect = new Rectangle(width/2, height/2, blocksize, blocksize);
        g.drawImage(heroImg, herorect.x-herorect.width/2*hero.turn+herorect.width/2, herorect.y, herorect.x+herorect.width/2*hero.turn+herorect.width/2, herorect.y+herorect.height,blocksizesource*hero.time,0,blocksizesource*(hero.time+1),blocksizesource, null);
        //g.drawRect(herorect.x, herorect.y, herorect.width, herorect.height);
        
        if (sheeprect.intersects(herorect)) mobs.attacking=true;
        if (weaponrect.intersects(sheeprect)) hero.attacking=true;
        g.drawString("HP: "+hero.health, 20, 50);
        g.drawString("Enemy Health: "+mobs.health, width-400, 50);
        if (!hero.alive()) g.drawString("You DIED! (please restart.)", width/2, height/2);
        else if (!mobs.alive()) g.drawString("You won!", width/2, height/2);
        if (mobs.attacking)
            if (mobs.iattack>0) g.drawString(""+mobs.iattack, herorect.x, herorect.y);
        if (hero.attacking)
            if (hero.iattack>0) g.drawString(""+hero.iattack, sheeprect.x, sheeprect.y);
        repaint();
    }
    public static void main(String[] args) {
        Main m = new Main();
        Thread t1 = new Thread(m);
        t1.start();
    }
    public class Key extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == e.VK_W) Ydir(-1);
            if (key == e.VK_A) Xdir(-1);
            if (key == e.VK_S) Ydir(1);
            if (key == e.VK_D) Xdir(1);
            if (key == e.VK_LEFT) weapon.xdir(-1);
            if (key == e.VK_RIGHT) weapon.xdir(1);
        }
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == e.VK_W) Ydir(0);
            if (key == e.VK_A) Xdir(0);
            if (key == e.VK_S) Ydir(0);
            if (key == e.VK_D) Xdir(0);
            if (key == e.VK_LEFT) weapon.xdir(0);
            if (key == e.VK_RIGHT) weapon.xdir(0);
        }
    }
}
