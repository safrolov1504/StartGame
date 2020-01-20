package ru.geekbrains.pull;

import com.badlogic.gdx.audio.Sound;

import ru.geekbrains.Sprites.EnemyShip;
import ru.geekbrains.base.SpritePool;
import ru.geekbrains.math.Rect;

public class EnemyPool extends SpritePool<EnemyShip> {

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private Sound sound;
    private Rect worldBounds;

    public EnemyPool(BulletPool bulletPool, ExplosionPool explosionPool, Sound sound, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.sound = sound;
        this.worldBounds = worldBounds;
    }

    @Override
    public EnemyShip newObject() {
        return new EnemyShip(bulletPool,explosionPool, sound,worldBounds);
    }
}
