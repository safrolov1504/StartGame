package ru.geekbrains.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Ship extends Sprite {

    private Vector2 speed;
    private int keyCode;
    private Vector2 touch;

    private boolean buttonPushed=false;
    private boolean touchPushed = false;
    private boolean achieveRightBoard =false;
    private boolean achieveLeftBoard =false;

    private Rect worldBounds;

    public Ship(TextureAtlas atlas) {
        TextureRegion region = new TextureRegion(atlas.findRegion("main_ship"),0,0,
                (atlas.findRegion("main_ship")).getRegionWidth()/2,
                (atlas.findRegion("main_ship")).getRegionHeight());
        regions = new TextureRegion[1];
        regions[0]=region;
        speed = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        setHeightProportion(0.1f);
        setLeft(worldBounds.getLeft()+0.3f);
        setBottom(worldBounds.getBottom()+0.05f);
    }

    @Override
    public void update(float delta) {
        checkBounds();
        pos.add(speed);
        movingByKey();
    }


    @Override
    public boolean keyDown(int keycode) {
        //22  - право
        //21 - лево
        buttonPushed =true;
        keyCode = keycode;
        return false;
    }

    private void checkBounds(){
        if(getRight() < worldBounds.getLeft()+halfWidth+0.05f) achieveRightBoard =true;
            else achieveRightBoard = false;

        if(getLeft() > worldBounds.getRight()-halfHeight-0.05f) achieveLeftBoard = true;
            else achieveLeftBoard = false;

    }

    private void movingByKey(){
            if(buttonPushed || touchPushed){
                if (keyCode ==21 || touch.x<0){
                    if(achieveRightBoard)
                        speed.set(0,0);
                    else
                        speed.set(-0.003f,0);
                }

                if (keyCode ==22 || touch.x>0) {
                    if(achieveLeftBoard)
                        speed.set(0,0);
                    else
                        speed.set(0.003f,0);
                }
            } else speed.set(0,0);
    }

    @Override
    public boolean keyUp(int keycode) {
        buttonPushed =false;
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        touchPushed = true;
        this.touch = touch;

        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        touchPushed = false;
        this.touch.set(0,0);
        return false;
    }
}
