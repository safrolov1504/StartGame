package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;
import ru.geekbrains.Sprites.*;
import ru.geekbrains.base.*;
import ru.geekbrains.math.*;
import ru.geekbrains.pull.*;
import ru.geekbrains.utils.*;

public class GameScreen extends BaseScreen {

    private static final float FONT_PADDING = 0.01f;
    private static final float FONT_SIZE = 0.02f;
    private static final float FONT_SIZE_RL_GAMEOVER = 0.2f;
    private static final float FONT_SIZE_TOP_GAMEOVER = 0.57f;

    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    public enum State{PLYING, GAME_OVER}
    private State state;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private TextureAtlas atlasHelp;

    private TreckingStar[] stars;
    private MainShip mainShip;
    private GameOver gameOver;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private HelpPool helpPool;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explotionSound;

    private ButtonNewGame buttonNewGame;

    private EnemyGenerator enemyGenerator;
    private HelpGenerator helpGenerator;

    private int frags;
    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;

    @Override
    public void show() {
        super.show();

        bg = new Texture("background.jpg");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        atlasHelp = new TextureAtlas(Gdx.files.internal("textures/help.atlas"));
        stars = new TreckingStar[64];

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();

        gameOver = new GameOver(atlas);
        buttonNewGame = new ButtonNewGame(atlas,this);

        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explotionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        font = new Font("font/font.fnt", "font/font.png");

        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();

        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas,explotionSound);
        enemyPool = new EnemyPool(bulletPool,explosionPool, bulletSound, worldBounds);
        enemyGenerator = new EnemyGenerator(atlas,enemyPool,worldBounds);

        helpPool = new HelpPool(worldBounds);
        helpGenerator = new HelpGenerator(atlasHelp, helpPool,worldBounds);

        playNewGame();

        for (int i = 0; i < stars.length; i++) {
            stars[i] = new TreckingStar(atlas,mainShip.getV());
        }
    }

    public void playNewGame(){
        explosionPool = new ExplosionPool(atlas,explotionSound);
        mainShip = new MainShip(atlas,bulletPool, explosionPool, laserSound);

        state = State.PLYING;
        frags = 0;
        enemyGenerator.setLevel(1);
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
            enemyGenerator.generate(delta,frags);
            helpPool.updateActiveSprites(delta);
            helpGenerator.generate(delta);
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
            helpPool.drawActiveSprites(batch);
            printInfo(true,FONT_PADDING,FONT_PADDING);
        } else if(state == State.GAME_OVER){
            gameOver.draw(batch);
            buttonNewGame.draw(batch);
            printInfo(false, FONT_SIZE_RL_GAMEOVER,FONT_SIZE_TOP_GAMEOVER);
        }
        batch.end();
    }

    private void printInfo(boolean HPflag, float fontPaddingLeftRight, float fontPaddingTop){
        sbFrags.setLength(0);
        sbHp.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch,sbFrags.append(FRAGS).append(frags),
                worldBounds.getLeft()+fontPaddingLeftRight,worldBounds.getTop()-fontPaddingTop);
        font.draw(batch,sbLevel.append(LEVEL).append(enemyGenerator.getLevel()),
                worldBounds.getRight()-fontPaddingLeftRight,worldBounds.getTop()-fontPaddingTop,Align.right);

        if(HPflag){
            font.draw(batch,sbHp.append(HP).append(mainShip.getHp()),
                    worldBounds.pos.x,worldBounds.getTop()-FONT_PADDING, Align.center);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        background.resize(worldBounds);
        font.setSize(FONT_SIZE);
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
        List<Help> helpList = helpPool.getActiveObjects();

        //жизнь добавляется, если поймать бонус караблем
        for (Help help:helpList) {
            float minDist = help.getHalfWidth() + mainShip.getHalfWidth();
            if(help.pos.dst2(mainShip.pos) < minDist * minDist){
                help.destroyed();
                mainShip.addHp(help.getHp());
            }
        }

        //вражеские карабли уничножаются, когда дотрагиваются до главного карабля
        for (EnemyShip enemyShip:enemyShipList) {
            float minDist = enemyShip.getHalfWidth() + mainShip.getHalfWidth();
           if(enemyShip.pos.dst2(mainShip.pos) < minDist * minDist){
               enemyShip.destroyed();
               mainShip.damage(enemyShip.getDamage());
               frags++;
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
                        if(enemyShip.isDestroyed()){
                            frags++;
                        }
                    }
                }
            }
        }
        if(mainShip.isDestroyed()){
            bulletPool.dispose();
            enemyPool.dispose();
            state = State.GAME_OVER;
        }
    }



    private void freeAllDestroyedActiveSprites(){
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        helpPool.freeAllDestroyedActiveSprites();
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
        helpPool.dispose();
        super.dispose();
    }

}
