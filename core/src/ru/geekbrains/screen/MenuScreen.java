package ru.geekbrains.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.Sprites.Background;
import ru.geekbrains.Sprites.ButtonExit;
import ru.geekbrains.Sprites.ButtonPlay;
import ru.geekbrains.Sprites.Star;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;

public class MenuScreen extends BaseScreen {

    private Game game;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;

    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;

    private Star[] stars;

    private Music music;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        buttonExit = new ButtonExit(atlas);
        buttonPlay = new ButtonPlay(atlas,game);
        stars = new Star[256];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
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
        background.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }

    }

    private void update(float delta){
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        buttonExit.touchDown(touch,pointer,button);
        buttonPlay.touchDown(touch,pointer,button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonExit.touchUp(touch,pointer,button);
        buttonPlay.touchUp(touch,pointer,button);
        return false;
    }

    @Override
    public void dispose() {
        atlas.dispose();
        bg.dispose();
        music.dispose();
        super.dispose();
    }

    public void draw(){
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        //здесь будет рисоваться все
        background.draw(batch);
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        music.play();
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }
        batch.end();
    }

}
