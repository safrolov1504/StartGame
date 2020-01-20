package ru.geekbrains.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class GameOver extends Sprite {
    Rect worldBounds;

    public GameOver(TextureAtlas atlas) {
        super(atlas.findRegion("message_game_over"), 1, 1, 2);

    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        setHeightProportion(0.07f);
        pos.set(0,0);
        //setBottom(worldBounds.getHalfHeight());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setHeightProportion(0.3f);
        setBottom(worldBounds.getHalfHeight());
    }
}
