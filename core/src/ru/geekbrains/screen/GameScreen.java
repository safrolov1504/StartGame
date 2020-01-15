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
import ru.geekbrains.Sprites.EnemyShip;
import ru.geekbrains.Sprites.MainShip;
import ru.geekbrains.Sprites.Star;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pull.BulletPool;
import ru.geekbrains.pull.EnemyPool;
import ru.geekbrains.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    //private Rect worldBounds;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;

    private Star[] stars;
    private MainShip mainShip;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    //private PoolEnemyShip_old poolEnemyShip;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;

    private float starAnimateInterval;
    private float starAnimateTimer;

    private Vector2 pos0;
    private Vector2 v0;

    private EnemyGenerator enemyGenerator;

    @Override
    public void show() {
        super.show();
        starAnimateInterval = 1f;
        bg = new Texture("background.jpg");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        stars = new Star[64];

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();

        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }

        bulletPool = new BulletPool();
        enemyPool = new EnemyPool(bulletPool,bulletSound,worldBounds);
        mainShip = new MainShip(atlas,bulletPool, laserSound);

        enemyGenerator = new EnemyGenerator(atlas,enemyPool,worldBounds);

        pos0 = new Vector2();
        v0 = new Vector2();



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
        mainShip.update(delta);
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);

        enemyGenerator.generate(delta);
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
        enemyPool.drawActiveSprites(batch);
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

    private void checkCollisions(){
        List<EnemyShip>  enemyShipList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        //вражеские карабли уничножаются, когда дотрагиваются до главного карабля
        for (EnemyShip enemyShip:enemyShipList) {
           if(!enemyShip.isOutside(mainShip)){
               enemyShip.destroyed();
           }
        }


        for (Bullet bullet:bulletList) {
            for (EnemyShip enemyShip : enemyShipList) {
                //hp главного карабля уменьшается, когда в него попадает пуля
                if((bullet.getOwner() == enemyShip) &&(!bullet.isOutside(mainShip))){
                    bullet.destroyed();
                    System.out.println(mainShip.getHp()+" "+bullet.getDamage());
                    mainShip.setHp(mainShip.getHp() - bullet.getDamage());
                    if(mainShip.getHp() == 0){
                        System.out.println("Game over");
                    }
                }

                //hp вражесткого карабля уменьшается, когда в него попадает пуля
                if ((bullet.getOwner() == mainShip) && (!bullet.isOutside(enemyShip))) {
                    bullet.destroyed();
                    enemyShip.setHp(enemyShip.getHp() - bullet.getDamage());
                    if (enemyShip.getHp() == 0) {
                        enemyShip.destroyed();
                    }
                    //System.out.println(enemyShip.getHp() + " " + bullet.getDamage());
                }
            }
        }
    }



    private void freeAllDestroyedActiveSprites(){
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
        //poolEnemyShip.freeAllDestroyedActiveSprites();
    }

    @Override
    public boolean keyDown(int keycode) {
        //System.out.println("keyDown "+keycode);
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        //System.out.println("keyUp "+keycode);
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

    @Override
    public void dispose() {
        atlas.dispose();
        bg.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        music.dispose();
        laserSound.dispose();
        bulletPool.dispose();
        super.dispose();
    }

}
