package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.geekbrains.Sprites.Background;
import ru.geekbrains.Sprites.Bullet;
import ru.geekbrains.Sprites.ButtonNewGame;
import ru.geekbrains.Sprites.EnemyShip;
import ru.geekbrains.Sprites.GameOver;
import ru.geekbrains.Sprites.MainShip;
import ru.geekbrains.Sprites.Star;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pull.BulletPool;
import ru.geekbrains.pull.EnemyPool;
import ru.geekbrains.pull.ExplosionPool;
import ru.geekbrains.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    public enum State{PLYING, GAME_OVER}
    private State state;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;

    private Star[] stars;
    private MainShip mainShip;
    private GameOver gameOver;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explotionSound;

    private ButtonNewGame buttonNewGame;

    private EnemyGenerator enemyGenerator;



    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        stars = new Star[64];

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();

        gameOver = new GameOver(atlas);
        buttonNewGame = new ButtonNewGame(atlas,this);

        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explotionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }

//        bulletPool = new BulletPool();
//        explosionPool = new ExplosionPool(atlas,explotionSound);
//
//        enemyPool = new EnemyPool(bulletPool,explosionPool, bulletSound, worldBounds);
//
//        mainShip = new MainShip(atlas,bulletPool, explosionPool, laserSound);
//        enemyGenerator = new EnemyGenerator(atlas,enemyPool,worldBounds);
        playNewGame();
    }

    public void playNewGame(){
        state = State.PLYING;

        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas,explotionSound);
        enemyPool = new EnemyPool(bulletPool,explosionPool, bulletSound, worldBounds);
        mainShip = new MainShip(atlas,bulletPool, explosionPool, laserSound);

        mainShip.resize(worldBounds);

        enemyGenerator = new EnemyGenerator(atlas,enemyPool,worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        freeAllDestroyedActiveSprites();
        draw();
    }

    private void update(float delta){
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
        explosionPool.updateActiveSprites(delta);

        if(state == State.PLYING){
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyGenerator.generate(delta);
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

        explosionPool.drawActiveSprites(batch);
        if(state == State.PLYING) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        } else if(state == State.GAME_OVER){
            gameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        //System.out.println(state);
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
        gameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    private void checkCollisions(){
        if(state != State.PLYING) return;

        List<EnemyShip>  enemyShipList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        //вражеские карабли уничножаются, когда дотрагиваются до главного карабля
        for (EnemyShip enemyShip:enemyShipList) {
            float minDist = enemyShip.getHalfWidth() + mainShip.getHalfWidth();
           if(enemyShip.pos.dst2(mainShip.pos) < minDist * minDist){
               enemyShip.destroyed();
               mainShip.damage(enemyShip.getDamage());
           }
        }

        for (Bullet bullet:bulletList){
            if(bullet.isDestroyed()) continue;
            if(bullet.getOwner() != mainShip) {
                if(mainShip.isBulletCollision(bullet)){
                    mainShip.damage(bullet.getDamage());
                    bullet.destroyed();
                }
            } else {
                for (EnemyShip enemyShip : enemyShipList) {
                    if (enemyShip.isBulletCollision(bullet)) {
                        enemyShip.damage(bullet.getDamage());
                        bullet.destroyed();
                    }
                }
            }
        }
        if(mainShip.isDestroyed()){
            state = State.GAME_OVER;
        }
    }



    private void freeAllDestroyedActiveSprites(){
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        //poolEnemyShip.freeAllDestroyedActiveSprites();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(state == State.PLYING) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(state == State.PLYING) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if(state == State.PLYING){
            mainShip.touchDown(touch,pointer,button);
        } else if(state == State.GAME_OVER){
            buttonNewGame.touchDown(touch,pointer,button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if(state == State.PLYING) {
            mainShip.touchUp(touch, pointer, button);
        } else if (state == State.GAME_OVER){
            System.out.println(state);
            buttonNewGame.touchUp(touch,pointer,button);
            System.out.println(state);
        }
        return false;
    }

    @Override
    public void dispose() {
        atlas.dispose();
        bg.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        music.dispose();
        laserSound.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        explotionSound.dispose();
        super.dispose();
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
