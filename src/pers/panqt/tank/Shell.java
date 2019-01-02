package pers.panqt.tank;

import java.awt.*;
import java.util.ArrayList;

/**
 *  @time       2018年11月19日	3:09
 *	@since      V1.0
 *	@author     panqt
 *	@comment    子弹
 */
public class Shell {
    private final int SPEED = MyFrame.Level+3;
    private final int ShellHight = 8,ShellWeight=8;
    private int Shell_X,Shell_Y;
    private boolean live = true;
    private boolean shellgood = true;
    MyFrame myframe;
    Direction dir;

    Shell(int Tank_X,int Tank_Y,Direction Tankdir,Direction frontDir,boolean good,MyFrame myframe){
        this.myframe = myframe;
        this.dir = Tankdir;
        this.shellgood = good;
        if(Tankdir == Direction.STOP){
            if(frontDir==Direction.STOP)
                this.dir = Direction.UP;
            else this.dir=frontDir;
            if(frontDir == Direction.STOP){
                this.Shell_X = Tank_X+(Tank.TankWeight-ShellWeight)/2;
                this.Shell_Y = Tank_Y;
            }
            if(frontDir == Direction.UP){
                this.Shell_X = Tank_X+(Tank.TankWeight-ShellWeight)/2;
                this.Shell_Y = Tank_Y;
            }
            if(frontDir == Direction.DOWN){
                this.Shell_X = Tank_X+(Tank.TankWeight-ShellWeight)/2;
                this.Shell_Y = Tank_Y+(Tank.TankHight-ShellHight);
            }
            if(frontDir == Direction.LEFT){
                this.Shell_X = Tank_X;
                this.Shell_Y = Tank_Y+(Tank.TankHight-ShellHight)/2;
            }
            if(frontDir == Direction.RIGHT){
                this.Shell_X = Tank_X+(Tank.TankWeight-ShellWeight);
                this.Shell_Y = Tank_Y+(Tank.TankHight-ShellHight)/2;
            }
        }
        if(Tankdir == Direction.UP){
            this.Shell_X = Tank_X+(Tank.TankWeight-ShellWeight)/2;
            this.Shell_Y = Tank_Y;
        }
        if(Tankdir == Direction.DOWN){
            this.Shell_X = Tank_X+(Tank.TankWeight-ShellWeight)/2;
            this.Shell_Y = Tank_Y+(Tank.TankHight-ShellHight);
        }
        if(Tankdir == Direction.LEFT){
            this.Shell_X = Tank_X;
            this.Shell_Y = Tank_Y+(Tank.TankHight-ShellHight)/2;
        }
        if(Tankdir == Direction.RIGHT){
            this.Shell_X = Tank_X+(Tank.TankWeight-ShellWeight);
            this.Shell_Y = Tank_Y+(Tank.TankHight-ShellHight)/2;
        }
    }
    void draw(Graphics g){
        Color c = g.getColor();
        if(shellgood)g.setColor(Color.red);
        else g.setColor(Color.BLACK);
        g.fillOval(Shell_X, Shell_Y, ShellHight, ShellWeight);
        move();
        g.setColor(c);
    }
    void move(){								//移动
        switch(dir){
            case LEFT:
                Shell_X-=SPEED;break;
            case RIGHT:
                Shell_X+=SPEED;break;
            case DOWN:
                Shell_Y+=SPEED;break;
            case UP:
                Shell_Y-=SPEED;break;
            default:break;
        }
        if(Shell_X<0 || Shell_Y<0 || Shell_X>MyFrame.WINDOW_WEIGHT ||Shell_Y>MyFrame.WINDOW_HIGHT){	//子弹出界判断子弹不存在了
            live = false;
        }
    }
    boolean isLive(){
        return live;
    }
    public boolean hitTank(Tank t){
        if(this.getRect().intersects(t.getRect()) && t.live==true && shellgood != t.good){
            if(t.good){
                t.blood-=1;
                if(t.blood<=0){
                    t.live = false;
                    new Music("./music/bomb.WAV",true);
                }
            }
            else {t.live = false;new Music("./music/bomb.WAV",true);}
            this.live = false;
            return true;
        }return false;
    }
    public void hitTank(ArrayList<Tank> tank){
        for(int i=0;i<tank.size();i++){
            if(shellgood != tank.get(i).good){
                Tank t = tank.get(i);
                hitTank(t);
                if(!t.live){
                    tank.remove(i);
                    Bomb b = new Bomb(t.Tank_X, t.Tank_Y, myframe);
                    myframe.bombs.add(b);

                }
            }
        }
    }
    Rectangle getRect(){
        return new Rectangle(Shell_X,Shell_Y,ShellWeight,ShellHight);
    }

    public boolean hitWall(Wall w){
        if(this.live && this.getRect().intersects(w.getRect())){
            this.live=false;
        }return false;
    }
}
