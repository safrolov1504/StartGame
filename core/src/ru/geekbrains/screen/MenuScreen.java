package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.Sprites.Background;
import ru.geekbrains.Sprites.Logo;
import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Texture bg;
    private Background background;
    private Logo logo;

    private boolean flagInitialImg=true;
    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        img = new Texture("badlogic.jpg");
        background = new Background(new TextureRegion(bg));
        //logo = new Logo(new TextureRegion(img));
        logo = new Logo(new TextureRegion(img));

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        //при первой инициализации нужно, не смог обойти эти костыли(
        if(flagInitialImg){
            Vector2 startPos = (new Vector2(0,0)).mul(this.getScreenToWorld());
            logo.initLogo(0.15f,startPos.x,startPos.y);
            flagInitialImg=false;
        }

        //здесь будет рисоваться сам фон
        background.draw(batch);
        logo.draw(batch);

        logo.moveByTouch();
        logo.moveByKey();
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        //logo.resize(worldBounds);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);

        logo.touchDown(getTouch(),pointer,button);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        logo.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        logo.keyUp(keycode);
        return false;
    }

    @Override
    public void dispose() {
        img.dispose();
        bg.dispose();
        super.dispose();
    }

}
