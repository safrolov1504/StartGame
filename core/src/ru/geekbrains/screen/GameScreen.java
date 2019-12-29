package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.Sprites.Background;
import ru.geekbrains.Sprites.Ship;
import ru.geekbrains.Sprites.Star;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;

public class GameScreen extends BaseScreen {

    private Rect worldBounds;

    private Texture bg;
    private Texture test;
    private Background background;
    private TextureAtlas atlas;

    private Star[] stars;
    private Ship ship;

    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        stars = new Star[64];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        test = new Texture("textures/bg.png");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        ship = new Ship(atlas);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        background.resize(worldBounds);
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }
        ship.resize(worldBounds);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        bg.dispose();
        test.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown "+keycode);
        ship.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("keyUp "+keycode);
        ship.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        ship.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        ship.touchUp( touch,  pointer, button);
        return false;
    }

    private void update(float delta){
        ship.update(delta);
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
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
        ship.draw(batch);
        batch.end();
    }
}
