package pers.panqt.tank;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 *  @time       2018年11月19日	3:09
 *	@since      V1.0
 *	@author     panqt
 *	@comment    用户界面
 */
public class MyFrame extends Frame{
    public static int Level = 1; // 游戏难度等级

    public static boolean cheat = true; // 作弊
    public static boolean start = true; // 游戏开始图片
    private static final long serialVersionUID = 1L;
    static final int WINDOW_HIGHT = 500, WINDOW_WEIGHT = 800; // 游戏窗口大小
    ArrayList<Shell> shells = new ArrayList<Shell>();
    ArrayList<Tank> tanks = new ArrayList<Tank>();
    ArrayList<Wall> wall = new ArrayList<Wall>();
    ArrayList<Bomb> bombs = new ArrayList<Bomb>();
    Image ibuffer = null;
    Music music;

    Tank myTank = new Tank(WINDOW_WEIGHT / 2, WINDOW_HIGHT / 2, true, this,
            Direction.STOP);

    void launchFrame() { // Frame
        music = new Music("./music/backgroundmusic.wav", false);
        this.setBounds(300, 50, WINDOW_WEIGHT, WINDOW_HIGHT);
        this.setVisible(true);
        this.setResizable(false);
        this.setBackground(new Color(199, 237, 204));
        this.setTitle("坦克大战");
        new Thread(new PaintThread()).start();

        addTank();
        addWall();
        this.addKeyListener(new KeyMonitor());
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

        });
    }

    void addTank() {
        for (int i = 1; i <= 8; i++) {
            tanks.add(new Tank(100 * i, 50, false, this, Direction.DOWN));
            tanks.add(new Tank(100 * i, 550, false, this, Direction.UP));
        }
    }

    void addWall() {
        if (Level < 6) {
            if (Level < 5) {
                wall.add(new Wall(355, 150, 110, 20, this)); // 上
                if (Level < 4) {
                    wall.add(new Wall(300, 150, 20, 200, this)); // 左
                    if (Level < 3) {
                        wall.add(new Wall(500, 150, 20, 200, this)); // 右
                        if (Level < 2) {
                            wall.add(new Wall(300, 350, 220, 20, this)); // 下
                        }
                    }
                }
            }
        }
    }

    public void update(Graphics g) {
        if (ibuffer == null) {
            ibuffer = createImage(this.getSize().width, this.getSize().height);
        }
        Graphics gbuffer = ibuffer.getGraphics();
        gbuffer.setColor(getBackground());
        gbuffer.fillRect(0, 0, WINDOW_WEIGHT, WINDOW_HIGHT);
        paint(gbuffer);
        g.drawImage(ibuffer, 0, 0, null);
    }

    public void paint(Graphics g) { // 绘制坦克
        if (start) {
            Toolkit tool = Toolkit.getDefaultToolkit();
            Image image = tool.getImage("./img/start.jpg");
            g.drawImage(image, 0, 0, WINDOW_WEIGHT, WINDOW_HIGHT, null);
        } else {
            if (myTank.live) {

                if (tanks.size() != 0) {

                    Color c = g.getColor();
                    g.setColor(Color.BLACK);
                    g.drawString("生命：" + myTank.life, 10, 40);
                    g.drawString("等级：" + MyFrame.Level, 10, 60);
                    g.setColor(c);

                    myTank.draw(g);
                    myTank.hitWithTank(tanks);
                    Tank t = null;
                    Shell s = null;
                    Wall w = null;

                    for (int i = 0; i < wall.size(); i++) { // MyTank与每一堵墙碰撞检测
                        w = wall.get(i);
                        myTank.hitWall(w);
                    }
                    for (int i = 0; i < tanks.size(); i++) {
                        t = tanks.get(i);
                        t.draw(g);

                        for (int j = 0; j < wall.size(); j++) {
                            w = wall.get(j);
                            t.hitWall(w);
                        }
                        t.hitWithTank(tanks);
                    }
                    for (int i = 0; i < wall.size(); i++) {
                        w = wall.get(i);
                        w.draw(g);
                    }
                    for (int i = 0; i < shells.size(); i++) {
                        s = shells.get(i);
                        s.hitTank(tanks);
                        if (cheat)
                            s.hitTank(myTank);
                        for (int j = 0; j < wall.size(); j++) {
                            w = wall.get(j);
                            s.hitWall(w);
                        }
                        if (!s.isLive())
                            shells.remove(i);
                        else
                            s.draw(g);
                    }

                    for (int i = 0; i < bombs.size(); i++) {
                        Bomb b = bombs.get(i);
                        b.draw(g);
                    }

                } else {
                    Toolkit tool = Toolkit.getDefaultToolkit();
                    Image image = tool.getImage("./img/win.jpg");
                    g.drawImage(image, 0, 0, WINDOW_WEIGHT, WINDOW_HIGHT, this);
                }
            } else if (!myTank.live && myTank.life > 0) {
                Toolkit tool = Toolkit.getDefaultToolkit();
                Image image = tool.getImage("./img/die.jpg");
                g.drawImage(image, 0, 0, WINDOW_WEIGHT, WINDOW_HIGHT, this);
            } else {
                Toolkit tool = Toolkit.getDefaultToolkit();
                Image image = tool.getImage("./img/gameover.jpg");
                g.drawImage(image, 0, 0, WINDOW_WEIGHT, WINDOW_HIGHT, this);
            }
        }
    }

    private class PaintThread extends Thread { // 重画线程
        public void run() {
            while (true) {
                repaint();
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class KeyMonitor extends KeyAdapter { // 键盘监听(方向)
        public void keyReleased(KeyEvent e) {
            myTank.tankkeyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            myTank.tankkeyPressed(e);
        }
    }
}
