package ru.geekbrains.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class ScaledButton extends Sprite{

    private static final float PRESS_SCALE = 0.9f;
    //переменная хранит состояние кнопки
    private boolean pressed = false;
    private int pointer;

    public ScaledButton(TextureRegion region) {
        super(region);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if(pressed || !isMe(touch)){
            return false;
        }
        this.pointer=pointer;
        this.scale=PRESS_SCALE;
        this.pressed = true;
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if(this.pointer!=pointer || !pressed){
            return false;
        }
        if(isMe(touch)){

            action();
        }
        pressed =false;
        this.scale = 1f;
        return false;
    }

    //метод который будет реализовываться в подклассе
    public abstract void action();
}
