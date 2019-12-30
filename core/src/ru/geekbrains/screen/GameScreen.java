package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.Sprites.Background;
import ru.geekbrains.Sprites.EnemyShip;
import ru.geekbrains.Sprites.MainShip;
import ru.geekbrains.Sprites.Star;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pull.BulletPool;
import ru.geekbrains.pull.PoolEnemyShip;

public class GameScreen extends BaseScreen {

    private Rect worldBounds;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;

    private Star[] stars;
    private MainShip mainShip;

    private BulletPool bulletPool;
    private PoolEnemyShip poolEnemyShip;

    private Music music;

    private float starAnimateInterval;
    private float starAnimateTimer;

    private Vector2 pos0;
    private Vector2 v0;

    @Override
    public void show() {
        super.show();
        starAnimateInterval = 1f;
        bg = new Texture("background.jpg");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        stars = new Star[64];
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        bulletPool = new BulletPool();
        mainShip = new MainShip(atlas,bulletPool);
        poolEnemyShip = new PoolEnemyShip();

        pos0 = new Vector2();
        v0 = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        freeAllDestroyedActiveSprites();
        draw();
    }

    private void update(float delta){
        mainShip.update(delta);
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
        bulletPool.updateActiveSprites(delta);
        poolEnemyShip.updateActiveSprites(delta);

        updateEnemyShip(delta);
    }

    private void updateEnemyShip(float delta){
        starAnimateTimer +=delta;

        if(starAnimateTimer>starAnimateInterval){
            starAnimateTimer =0;
            EnemyShip enemyShip = poolEnemyShip.obtain();
            pos0.set(Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight()),worldBounds.getTop());
            v0.set(0,Rnd.nextFloat(-0.1f,-0.5f));
            enemyShip.set(pos0, v0, 0.1f,worldBounds);
        }
    }

    private void draw(){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        //здесь будет рисоваться все
        background.draw(batch);
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }
        mainShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        poolEnemyShip.drawActiveSprites(batch);
        music.play();
        batch.end();



    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        background.resize(worldBounds);
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        bg.dispose();
        bulletPool.dispose();
        poolEnemyShip.dispose();
        music.dispose();
        super.dispose();
    }

    private void freeAllDestroyedActiveSprites(){
        bulletPool.freeAllDestroyedActiveSprites();
        poolEnemyShip.freeAllDestroyedActiveSprites();
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown "+keycode);
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("keyUp "+keycode);
        mainShip.keyUp(keycode);
        //ship.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShip.touchDown(touch,pointer,button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        mainShip.touchUp(touch,pointer,button);
        return false;
    }


}
