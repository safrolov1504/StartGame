package ru.geekbrains.pull;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.Sprites.Explosion;
import ru.geekbrains.base.SpritePool;

public class ExplosionPool extends SpritePool<Explosion> {

    private TextureAtlas area;
    private Sound explotionSound;

    public ExplosionPool(TextureAtlas area, Sound explotionSound) {
        this.area = area;
        this.explotionSound = explotionSound;
    }

    @Override
    public Explosion newObject() {
        return new Explosion(area,explotionSound);
    }
}
