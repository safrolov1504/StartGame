package ru.geekbrains.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.Sprites.Bullet;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pull.BulletPool;

public class Ship extends Sprite {

    protected Rect worldBounds;

    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected float bulletHeight;
    protected Vector2 bulletV;
    protected int damage;
    protected int hp;

    protected Sound shootSound;

    protected Vector2 v;
    protected Vector2 v0;

    protected float reloadTimer;
    protected float reloadInterval;

    public Ship() {
        super();
    }

    public Ship(TextureRegion region, int row, int cols, int frames) {
        super(region, row, cols, frames);
    }

    protected void shoot(){
        Bullet bullet = bulletPool.obtain();
        shootSound.play(0.07f);
        bullet.set(this,bulletRegion,pos,bulletV,bulletHeight,worldBounds,damage);
        //System.out.println(bulletPool.getClass() + " "  + bullet.getOwner().getClass());
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v,delta);

    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
