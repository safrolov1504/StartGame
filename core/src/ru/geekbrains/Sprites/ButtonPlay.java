package ru.geekbrains.Sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;

public class ButtonPlay extends ScaledButton {

    private Game game;

    public ButtonPlay(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("btPlay"));
        this.game=game;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.19f);
        setLeft(worldBounds.getLeft()+0.05f);
        setBottom(worldBounds.getBottom()+0.05f);

    }

    @Override
    public void action() {
        game.setScreen(new GameScreen());
    }
}
