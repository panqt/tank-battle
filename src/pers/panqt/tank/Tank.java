package pers.panqt.tank;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 *  @time       2018年11月19日	3:10
 *	@since      V1.0
 *	@author     panqt
 *	@comment    坦克
 */
public class Tank {
    public int blood = 5;	//血
    public int life = 5;	//共有5条命
    public static int SPEED = 2;			//速度
    public static final int TankHight=35,TankWeight=35;		//坦克大小
    public boolean good;	//敌or我 坦克
    public boolean live = true;	//坦克活着
    private boolean BLeft=false,BRight=false,BUp=false,BDown=false;					//是否按键
    private Direction dir = Direction.STOP;				//当前方向
    private Direction frontDir = Direction.STOP;		//转向前方向
    Random random = new Random();
    public int Tank_X,Tank_Y;					//位置
    private int Tank_Old_X,Tank_Old_Y;
    MyFrame myframe;
    BloodBar bloodbar = new BloodBar();
    private int step = random.nextInt(50)+5;	// 移动步骤

    public Tank(int tank_X, int tank_Y,boolean good,MyFrame myframe,Direction dir) {		//构造
        Tank_X = tank_X;
        Tank_Y = tank_Y;
        this.myframe = myframe;
        this.good = good;
        this.dir = dir;
    }
    void draw(Graphics g){						//画图
        if(!this.live){return;}
        drawGunBarrel(g);
        move();
        if(good)bloodbar.draw(g);
    }

    void tankkeyPressed(KeyEvent e) {			//按下方向键
        int key = e.getKeyCode();
        //@SuppressWarnings("unused")
        switch(key){
            case KeyEvent.VK_ENTER:
                if(dir != Direction.STOP){
                    frontDir=dir;
                }
                dir=Direction.STOP;
                break;
            case KeyEvent.VK_F2:if(!this.live && this.life >0){
                Tank_X = MyFrame.WINDOW_WEIGHT/2;Tank_Y=MyFrame.WINDOW_HIGHT/2;
                this.live=true;this.blood=5;this.life-=1;myframe.shells.clear();
            }break;
            case KeyEvent.VK_Y: if(myframe.tanks.size()==0){
                Tank_X = MyFrame.WINDOW_WEIGHT/2;Tank_Y=MyFrame.WINDOW_HIGHT/2;
                MyFrame.Level+=1;
                myframe.shells.clear();
                myframe.wall.clear();
                myframe.bombs.clear();
                myframe.addWall();
                this.blood=5;
                myframe.addTank();
            }break;
            case KeyEvent.VK_SPACE:{
                if(MyFrame.start){
                    MyFrame.start=false;
                    //myframe.music.Close();
                }
            }break;
            case KeyEvent.VK_F5: MyFrame.cheat=!MyFrame.cheat;break;
            case KeyEvent.VK_UP: BUp=true;break;
            case KeyEvent.VK_DOWN:BDown=true;break;
            case KeyEvent.VK_LEFT: BLeft=true;break;
            case KeyEvent.VK_RIGHT: BRight=true;break;
        }
        locateDirection();
    }
    void tankkeyReleased(KeyEvent e){			//松开方向键
        int key = e.getKeyCode();
        switch(key){
            case KeyEvent.VK_D:fire();break;
            case KeyEvent.VK_A:superFire();break;
            case KeyEvent.VK_UP: BUp=false;break;
            case KeyEvent.VK_DOWN:BDown=false;break;
            case KeyEvent.VK_LEFT: BLeft=false;break;
            case KeyEvent.VK_RIGHT: BRight=false;break;
        }
    }
    void move(){								//移动
        switch(dir){
            case LEFT:
                this.Tank_Old_Y=this.Tank_Y;this.Tank_Old_X = this.Tank_X;Tank_X-=SPEED;break;
            case RIGHT:
                this.Tank_Old_Y=this.Tank_Y;this.Tank_Old_X = this.Tank_X;Tank_X+=SPEED;break;
            case DOWN:
                this.Tank_Old_Y=this.Tank_Y;this.Tank_Old_X = this.Tank_X;Tank_Y+=SPEED;break;
            case UP:
                this.Tank_Old_Y=this.Tank_Y;this.Tank_Old_X = this.Tank_X;Tank_Y-=SPEED;break;
            case STOP:
                this.Tank_Old_Y=this.Tank_Y;this.Tank_Old_X = this.Tank_X;break;
        }
        if(Tank_X>MyFrame.WINDOW_WEIGHT-30){//判断不许坦克出界
            frontDir = dir;
            dir = Direction.STOP;
            Tank_X=MyFrame.WINDOW_WEIGHT-30;
        }if(Tank_Y>MyFrame.WINDOW_HIGHT-30){
            frontDir = dir;
            dir = Direction.STOP;
            Tank_Y=MyFrame.WINDOW_HIGHT-30;
        }if(Tank_X<0 ){
            frontDir = dir;
            dir = Direction.STOP;
            Tank_X=0;
        }if(Tank_Y<23){
            frontDir = dir;
            dir = Direction.STOP;
            Tank_Y=23;
        }
        if(!good){
            Direction[]  dirs = Direction.values();
            if(step == 0){
                step = random.nextInt(50)+5;
                int r = random.nextInt(dirs.length-1);
                this.dir = dirs[r];
                if(random.nextInt(20)<MyFrame.Level*4){this.fire();}
            }
            step--;
        }

    }
    void locateDirection(){						//判断方向
        if(BLeft && !BRight && !BUp && !BDown) dir = Direction.LEFT;
        if(!BLeft && BRight && !BUp && !BDown) dir = Direction.RIGHT;
        if(!BLeft && !BRight && !BUp && BDown) dir = Direction.DOWN;
        if(!BLeft && !BRight && BUp && !BDown) dir = Direction.UP;
    }
    Shell fire(){			//开火
        if(!live)return null;
        Shell shell = new Shell(Tank_X,Tank_Y,dir,frontDir,good,myframe);
        myframe.shells.add(shell);
        return shell;
    }
    Shell fire(Direction dirs){
        if(!live)return null;
        Shell shell = new Shell(Tank_X,Tank_Y,dirs,frontDir,good,myframe);
        myframe.shells.add(shell);
        return shell;
    }
    void superFire(){
        Direction[] dirs = Direction.values();
        for(int i=0;i<4;i++){
            fire(dirs[i]);
        }
    }
    void drawGunBarrel(Graphics g){			//绘制炮筒
        Toolkit tool = Toolkit.getDefaultToolkit();
        Image image;//
        switch(dir){
            case LEFT:
                if(good)image = tool.getImage("./img/4.jpg");
                else image = tool.getImage("./img/8.jpg");
                g.drawImage(image,Tank_X,Tank_Y,TankWeight,TankHight, myframe);break;
            case RIGHT:
                if(good)image = tool.getImage("./img/3.jpg");
                else image = tool.getImage("./img/7.jpg");
                g.drawImage(image,Tank_X,Tank_Y,TankWeight,TankHight, myframe);break;
            case DOWN:
                if(good)image = tool.getImage("./img/2.jpg");
                else image = tool.getImage("./img/6.jpg");
                g.drawImage(image,Tank_X,Tank_Y,TankWeight,TankHight, myframe);break;
            case UP:
                if(good)image = tool.getImage("./img/1.jpg");
                else image = tool.getImage("./img/5.jpg");
                g.drawImage(image,Tank_X,Tank_Y,TankWeight,TankHight, myframe);break;
            case STOP:		//当方向为STOP时，安照STOP前的方向绘炮筒
                switch(frontDir){
                    case LEFT:
                        if(good)image = tool.getImage("./img/4.jpg");
                        else image = tool.getImage("./img/8.jpg");
                        g.drawImage(image,Tank_X,Tank_Y,TankWeight,TankHight, myframe);break;
                    case RIGHT:
                        if(good)image = tool.getImage("./img/3.jpg");
                        else image = tool.getImage("./img/7.jpg");
                        g.drawImage(image,Tank_X,Tank_Y,TankWeight,TankHight, myframe);break;
                    case DOWN:
                        if(good)image = tool.getImage("./img/2.jpg");
                        else image = tool.getImage("./img/6.jpg");
                        g.drawImage(image,Tank_X,Tank_Y,TankWeight,TankHight, myframe);break;
                    case UP:
                        if(good)image = tool.getImage("./img/1.jpg");
                        else image = tool.getImage("./img/5.jpg");
                        g.drawImage(image,Tank_X,Tank_Y,TankWeight,TankHight, myframe);break;
                    case STOP:
                        if(good)image = tool.getImage("./img/1.jpg");
                        else image = tool.getImage("./img/5.jpg");
                        g.drawImage(image,Tank_X,Tank_Y,TankWeight,TankHight, myframe);break;
                }
        }
    }
    Rectangle getRect(){
        return new Rectangle(Tank_X,Tank_Y,TankWeight,TankHight);
    }
    public void hitWall(Wall w){
        if(this.getRect().intersects(w.getRect())){
//			stop();
            if(Tank_X+TankWeight>w.x && 		//坦克撞墙
                    Tank_X<w.x+w.weight &&
                    Tank_Y+TankHight>w.y &&
                    Tank_Y<w.y+w.hight &&
                    w.x+w.weight-Tank_X<2*SPEED){	//左进
                Tank_X=w.x+w.weight;
            }
            if(Tank_X+TankWeight>w.x && 		//坦克撞墙
                    Tank_X<w.x+w.weight &&
                    Tank_Y+TankHight>w.y &&
                    Tank_Y<w.y+w.hight &&
                    Tank_X+TankWeight-w.x<2*SPEED){
                Tank_X=w.x-TankWeight;
            }
            if(Tank_X+TankWeight>w.x && 		//坦克撞墙
                    Tank_X<w.x+w.weight &&
                    Tank_Y+TankHight>w.y &&
                    Tank_Y<w.y+w.hight &&
                    w.y+w.hight-Tank_Y<2*SPEED){
                Tank_Y=w.y+w.hight;
            }
            if(Tank_X+TankWeight>w.x && 		//坦克撞墙
                    Tank_X<w.x+w.weight &&
                    Tank_Y+TankHight>w.y &&
                    Tank_Y<w.y+w.hight &&
                    Tank_Y+TankWeight-w.y<2*SPEED){
                Tank_Y=w.y-TankHight;
            }
        }
    }
    public void hitWithTank(ArrayList<Tank> tanks){
        for(int i=0;i<tanks.size();i++){
            Tank t = tanks.get(i);
            if(this !=t){
                if(this.live && t.live && this.getRect().intersects(t.getRect())){
                    this.frontDir = dir;
                    this.stop();
                    t.frontDir = dir;
                    t.stop();
                }
            }
        }
    }
    void stop(){
        this.dir = Direction.STOP;
        Tank_X = Tank_Old_X;
        Tank_Y = Tank_Old_Y;
    }
    int getBlood(){
        return this.blood;
    }

    private class BloodBar{
        public void draw(Graphics g){
            Color c = g.getColor();
            g.setColor(Color.BLACK);
            g.drawRect(Tank_X-1, Tank_Y-15, TankWeight+2, 6);
            g.setColor(Color.red);
            int weight = TankWeight/5;
            g.fillRect(Tank_X, Tank_Y-14, blood*weight, 4);
            g.setColor(c);
        }
    }
}
