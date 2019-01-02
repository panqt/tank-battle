package pers.panqt.tank;

import java.awt.*;

/**
 *  @time       2018年11月19日	3:09
 *	@since      V1.0
 *	@author     panqt
 *	@comment    爆炸
 */
public class Bomb {
    int x, y;
    private boolean live = true;

    private MyFrame myframe ;

    int[] diameter = {4, 7, 12, 18, 26, 32, 49, 30, 14, 6};
    int step = 0;

    public Bomb(int x, int y, MyFrame myframe) {
        this.x = x;
        this.y = y;
        this.myframe = myframe;
    }

    public void draw(Graphics g) {
        if(!live) {
            myframe.bombs.remove(this);
            return;
        }

        if(step == diameter.length) {
            live = false;
            step = 0;
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.red);
        g.fillOval(x, y, diameter[step], diameter[step]);
        g.setColor(c);

        step ++;
    }
}
