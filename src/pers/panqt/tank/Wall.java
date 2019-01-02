package pers.panqt.tank;

import java.awt.*;

/**
 *  @time       2018年11月19日	3:10
 *	@since      V1.0
 *	@author     panqt
 *	@comment    墙
 */
public class Wall {
    int x;
    int y;
    int weight;
    int hight;
    MyFrame myframe;

    public Wall(int x, int y, int weight, int hight,MyFrame myframe) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.hight = hight;
        this.myframe = myframe;
    }
    public void draw(Graphics g){
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(x, y, weight, hight);
        g.setColor(c);
    }
    public Rectangle getRect(){
        return new Rectangle(x,y,weight,hight);
    }
}
