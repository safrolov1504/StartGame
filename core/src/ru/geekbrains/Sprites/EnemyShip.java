package ru.geekbrains.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pull.BulletPool;
import ru.geekbrains.pull.ExplosionPool;

public class EnemyShip extends Ship {

    private enum State {DESCENT, FIGHT}
    private State state;
    private Vector2 descentV = new Vector2(0,-0.15f);

    public EnemyShip(BulletPool bulletPool, ExplosionPool explosionPool, Sound shootSound, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.shootSound=shootSound;
        this.explosionPool = explosionPool;
        this.v = new Vector2();
        this.v0 = new Vector2();
        this.bulletV = new Vector2();
        this.worldBounds = worldBounds;
        this.reloadTimer = reloadInterval;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        switch (state) {
            case DESCENT:
                if(getTop() <=worldBounds.getTop()){
                    v.set(v0);
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                reloadTimer +=delta;
                if(reloadTimer>reloadInterval){
                    reloadTimer =0;
                    shoot();
                }
                if(getBottom() < worldBounds.getBottom()){
                    destroyed();
                }
                break;
        }
        if(isOutside(worldBounds)){
            destroyed();
        }
    }

    public void set(
            TextureRegion [] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            int hp,
            float height
            ){

        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0,bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.hp = hp;
        setHeightProportion(height);
        this.v.set(descentV);
        state = State.DESCENT;
    }

    public boolean isBulletCollision(Rect bullet){
        return ! (
                bullet.getRight() <getLeft()
                || bullet.getLeft()  >getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y

        );
    }
}
