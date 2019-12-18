package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Texture background;
    private Vector2 pos;
    private Vector2 stopPos;
    private Vector2 speed;

    private boolean keyPush;
    private int keyCode;
    @Override
    public void show() {
        super.show();

        img = new Texture("badlogic.jpg");
        background = new Texture("background.jpg");
        pos = new Vector2(0,0);
        stopPos = pos;
        keyPush = false;
        speed = new Vector2(0f,0f);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background,0,0);
        batch.draw(img, pos.x, pos.y);

        moveByTouch();
        moveByKey();

        batch.end();
    }

    private void moveByTouch() {
        if ((equalsMy(pos.x,stopPos.x))&&(equalsMy(pos.y,stopPos.y))){
            pos.add(speed);
        }
    }

    private void moveByKey() {
        if(keyPush &&((keyCode==19)||(keyCode==20)||(keyCode==21)||(keyCode==22))){
            chooseSpeed(keyCode);
            pos.add(speed);
            stopPos = pos;
        }
    }

    private void chooseSpeed(int keycode) {
        if(keycode==19){
            speed.set(0,1);
        } else if(keycode==20){
            speed.set(0,-1);
        } else if(keycode==21){
            speed.set(-1,0);
        } else if(keycode==22){
            speed.set(1,0);
        }
    }

    @Override
    public void dispose() {
        img.dispose();
        background.dispose();
        super.dispose();
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        stopPos = new Vector2(screenX,Gdx.graphics.getHeight()-screenY);
        speed = stopPos.cpy();
        speed.sub(pos.x,pos.y);
        speed.scl(0.01f);
        return false;
    }


    private boolean equalsMy(float a, float b){
        if((a<b+1) && (a>b-1))
            return false;
        return true;
    }

}
