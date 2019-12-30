package ru.geekbrains.pull;

import ru.geekbrains.Sprites.Bullet;
import ru.geekbrains.base.SpritePool;

public class BulletPool extends SpritePool<Bullet> {
    @Override
    public Bullet newObject() {
        return new Bullet();

    }
}
