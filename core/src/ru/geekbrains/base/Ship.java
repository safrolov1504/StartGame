package ru.geekbrains.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.Sprites.Bullet;
import ru.geekbrains.Sprites.Explosion;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pull.BulletPool;
import ru.geekbrains.pull.ExplosionPool;

public class Ship extends Sprite {

    protected Rect worldBounds;

    protected BulletPool bulletPool;
    protected ExplosionPool explosionPool;
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

    protected float damageAnimateInterval = 0.1f;
    protected float damageAnimateTimer = damageAnimateInterval;

    public Ship() {
        super();
    }

    public Ship(TextureRegion region, int row, int cols, int frames) {
        super(region, row, cols, frames);
    }

    protected void shoot(){
        Bullet bullet = bulletPool.obtain();
        shootSound.play(0.07f);
        bullet.set(this,bulletRegion, pos, bulletV,bulletHeight,worldBounds,damage);
        //System.out.println(bulletPool.getClass() + " "  + bullet.getOwner().getClass());
    }

    protected void boom(){
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(),this.pos);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v,delta);
        damageAnimateTimer+=delta;
        if(damageAnimateTimer>=damageAnimateInterval){
            frame =0;
        }
    }

    public int getDamage() {
        return damage;
    }

    public Vector2 getV() {
        return v;
    }

    public void damage(int damage){
        hp-=damage;
        if(hp<=0){
            destroyed();
            hp = 0;
        }
        //после попадания создается эфект мигания, переключаются карабли
        frame =1;
        damageAnimateTimer = 0f;
    }

    @Override
    public void destroyed() {
        super.destroyed();
        boom();
    }

    public int getHp() {
        return hp;
    }
}
