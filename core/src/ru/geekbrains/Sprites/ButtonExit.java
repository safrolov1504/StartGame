package ru.geekbrains.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.math.Rect;

public class ButtonExit extends ScaledButton {
    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("btExit"));
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.15f);
        //0.05f - смещение от дня и правой рамки
        setRight(worldBounds.getRight() - 0.05f);
        setBottom(worldBounds.getBottom()+0.05f);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
