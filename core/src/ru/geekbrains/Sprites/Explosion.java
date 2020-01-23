package ru.geekbrains.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;

public class Explosion extends Sprite {

    private float animateInterval = 0.017f;
    private float animateTimer;

    private Sound explotionSound;

    public Explosion(TextureAtlas atlas, Sound explotionSound) {
        super(atlas.findRegion("explosion"), 9, 9, 74);
        this.explotionSound = explotionSound;
    }

    public void set(float height, Vector2 pos){
        this.pos.set(pos);
        setHeightProportion(height);
        explotionSound.play(0.07f);
    }

    @Override
    public void update(float delta) {
        animateTimer+=delta;
        if(animateTimer >= animateInterval){
            animateTimer = 0f;
            //проверяет, если у нас еще fram или уже анимация закончилась
            if(++frame == regions.length){
                destroyed();
            }
        }
    }

    @Override
    public void destroyed() {
        super.destroyed();
        frame = 0;
    }
}
