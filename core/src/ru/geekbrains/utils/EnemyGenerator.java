package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.Sprites.EnemyShip;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pull.EnemyPool;

public class EnemyGenerator {
    private static float ENEMY_SMALL_HEIGHT = 0.1f;
    private static float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static float ENEMY_SMALL_BULLET_VY = - 0.3f;
    private static int ENEMY_SMALL_DAMAGE = 1;
    private static float ENEMY_SMALL_RELOAD_INTERVAL = 3f;
    private static int ENEMY_SMALL_HP = 1;

    private static float ENEMY_MEDIUM_HEIGHT = 0.15f;
    private static float ENEMY_MEDIUM_BULLET_HEIGHT = 0.02f;
    private static float ENEMY_MEDIUM_BULLET_VY = - 0.25f;
    private static int ENEMY_MEDIUM_DAMAGE = 5;
    private static float ENEMY_MEDIUM_RELOAD_INTERVAL = 4f;
    private static int ENEMY_MEDIUM_HP = 5;

    private static float ENEMY_BIG_HEIGHT = 0.2f;
    private static float ENEMY_BIG_BULLET_HEIGHT = 0.04f;
    private static float ENEMY_BIG_BULLET_VY = - 0.3f;
    private static int ENEMY_BIG_DAMAGE = 10;
    private static float ENEMY_BIG_RELOAD_INTERVAL = 1f;
    private static int ENEMY_BIG_HP = 10;

    private final TextureRegion [] enemySmallRegions;
    private final TextureRegion [] enemyMediumRegions;
    private final TextureRegion [] enemyBigRegions;

    private final TextureRegion bulletRegion;

    private final Vector2 enemySmallV = new Vector2(0,-0.2f);
    private final Vector2 enemyMediumV = new Vector2(0,-0.03f);
    private final Vector2 enemyBigV = new Vector2(0,-0.005f);

    private float generateInterval = 4f;
    private float generateTimer = generateInterval;

    private EnemyPool enemyPool;

    private Rect worldBounds;

    private int level = 1;

    public EnemyGenerator(TextureAtlas textureAtlas,EnemyPool enemyPool,Rect worldBounds) {
        TextureRegion enemy0 = textureAtlas.findRegion("enemy0");
        TextureRegion enemy1 = textureAtlas.findRegion("enemy1");
        TextureRegion enemy2 = textureAtlas.findRegion("enemy2");
        this.enemySmallRegions = Regions.split(enemy0,1,2,2);
        this.enemyMediumRegions = Regions.split(enemy1,1,2,2);
        this.enemyBigRegions = Regions.split(enemy2,1,2,2);

        this.bulletRegion = textureAtlas.findRegion("bulletEnemy");

        this.enemyPool = enemyPool;
        this.worldBounds = worldBounds;
    }

    public void generate(float delta, int frags){
        generateTimer+=delta;
        level =frags / 4 + 1;
        if(generateTimer > generateInterval){
            generateTimer = 0f;
            EnemyShip enemyShip = enemyPool.obtain();
            float type = (float) Math.random();
            if(type < 0.5f){
            enemyShip.set(
                    enemySmallRegions,
                    enemySmallV,
                    bulletRegion,
                    ENEMY_SMALL_BULLET_HEIGHT,
                    ENEMY_SMALL_BULLET_VY,
                    ENEMY_SMALL_DAMAGE * level,
                    ENEMY_SMALL_RELOAD_INTERVAL,
                    ENEMY_SMALL_HP,
                    ENEMY_SMALL_HEIGHT
            );
            }
            else if(type < 0.8f){
                enemyShip.set(
                        enemyMediumRegions,
                        enemyMediumV,
                        bulletRegion,
                        ENEMY_MEDIUM_BULLET_HEIGHT,
                        ENEMY_MEDIUM_BULLET_VY,
                        ENEMY_MEDIUM_DAMAGE * level,
                        ENEMY_MEDIUM_RELOAD_INTERVAL,
                        ENEMY_MEDIUM_HP,
                        ENEMY_MEDIUM_HEIGHT
                );
            } else
            {
                enemyShip.set(
                        enemyBigRegions,
                        enemyBigV,
                        bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        ENEMY_BIG_BULLET_VY,
                        ENEMY_BIG_DAMAGE * level,
                        ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HP,
                        ENEMY_BIG_HEIGHT
                );
            }

            enemyShip.pos.x = Rnd.nextFloat(
                    worldBounds.getLeft()+enemyShip.getHalfWidth(),
                    worldBounds.getRight()-enemyShip.getHalfWidth());

            enemyShip.setBottom(worldBounds.getTop());
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
