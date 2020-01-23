package ru.geekbrains.pull;

import com.badlogic.gdx.audio.Sound;

import ru.geekbrains.Sprites.Help;
import ru.geekbrains.base.SpritePool;
import ru.geekbrains.math.Rect;

public class HelpPool extends SpritePool<Help> {
    private Rect worldBounds;

    public HelpPool(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    public Help newObject() {
        return new Help(worldBounds);
    }
}
