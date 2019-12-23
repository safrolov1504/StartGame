package ru.geekbrains.Sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Logo extends Sprite {

    private Vector2 position;
    private Vector2 speed;
    private Vector2 stopPos;
    private boolean keyPush;
    private int keyCode;

    public Logo(TextureRegion region) {
        super(region);
    }

    public void initLogo(float height, float posX,float posY) {
        speed = new Vector2();
        setHeightProportion(height);
        pos.set(moveCenter(new Vector2(posX, posY)));
        stopPos = moveCenter(new Vector2(posX, posY));
    }

    private void setPosition(Vector2 posIn) {
        pos.set(posIn);
        position = pos;
    }

    private Vector2 moveCenter(Vector2 vectorIn){
        return  vectorIn.set(vectorIn.x+this.getHalfWidth(),vectorIn.y+this.getHalfHeight());
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setPosition(position);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
       stopPos = moveCenter(new Vector2(touch.x,touch.y));
       speed = stopPos.cpy();
       speed.sub(pos.x,pos.y);
       speed.scl(0.01f);
       //System.out.println("speed "+speed.x+" "+speed.y + "stop "+ stopPos.x+" "+stopPos.y);
       //System.out.println("stop "+stopPos.x+" "+stopPos.y + "pos "+ pos.x+" "+pos.y);

       return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        keyPush = true;
        keyCode =keycode;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyPush = false;
        return false;
    }

    //движение по нажатию мышки
    public void moveByTouch(){
        if ((equalsMy(pos.x,stopPos.x))&&(equalsMy(pos.y,stopPos.y))){
            setPosition(pos.add(speed));
        }
    }

    //движение по клавишам
    public void moveByKey() {
        if(keyPush &&((keyCode==19)||(keyCode==20)||(keyCode==21)||(keyCode==22))){
            chooseSpeed(keyCode);
            pos.add(speed);
            stopPos = pos;
        }
    }

    //программа для выьора скорости в зависимости от нажатия стрелки
    private void chooseSpeed(int keycode) {
        if(keycode==19){
            speed.set(0,0.001f);
        } else if(keycode==20){
            speed.set(0,-0.001f);
        } else if(keycode==21){
            speed.set(-0.001f,0);
        } else if(keycode==22){
            speed.set(0.001f,0);
        }
    }

    private boolean equalsMy(float a, float b){
        if((a<b+0.001f) && (a>b-0.001f))
            return false;
        return true;
    }
}
