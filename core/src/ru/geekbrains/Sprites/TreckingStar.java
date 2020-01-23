package ru.geekbrains.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rnd;

public class TreckingStar extends Star {
    private Vector2 trackingV;
    private Vector2 sumV;

    public TreckingStar(TextureAtlas atlas, Vector2 trackingV) {
        super(atlas);
        this.trackingV = trackingV;
        this.sumV = new Vector2();
    }

    @Override
    public void update(float delta) {
        sumV.setZero().mulAdd(trackingV, 0.2f).rotate(180).add(v);
        pos.mulAdd(sumV,delta);
        checkBounds();
    }
}
