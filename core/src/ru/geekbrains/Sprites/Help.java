package ru.geekbrains.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;

public class Help extends Ship {
    private enum State {DESCENT, FLY}

    private State state;
    private Vector2 descentV = new Vector2(0,-0.15f);

    public Help(Rect worldBound) {
        this.worldBounds = worldBound;
        this.v0 = new Vector2();
        this.v = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        switch (state) {
            case DESCENT:
                if(getTop() <=worldBounds.getTop()){
                    v.set(v0);
                    state = Help.State.FLY;
                }
                break;
            case FLY:
                if(getTop() < worldBounds.getBottom()){
                    destroyed();
                }
                break;
        }

        if(isOutside(worldBounds)){
            destroyed();
        }
    }

    public void set(
            TextureRegion [] regions,
            Vector2 v0,
            int hp,
            float height
    ){
        this.regions = regions;
        this.v0.set(v0);
        this.hp = hp;
        setHeightProportion(height);
        this.v.set(descentV);
        state = Help.State.DESCENT;
    }

    @Override
    public void destroyed() {
        setDestroyed(true);
    }
}
