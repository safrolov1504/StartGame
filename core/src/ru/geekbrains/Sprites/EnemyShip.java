package ru.geekbrains.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.utils.Regions;

public class EnemyShip extends Sprite {
    private Vector2 v;
    private Rect worldBounds;

    protected TextureRegion[] regionsIn;

    public EnemyShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"),1,2,2);
        regionsIn = Regions.split(atlas.findRegion("main_ship"),1,2,2);
        v = new Vector2();
    }


    public void set(Vector2 pos0, Vector2 v0, float height,Rect worldBounds){
        this.regions[0] = regionsIn[frame];
        this.pos.set(pos0);
        this.v.set(v0);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
    }


    @Override
    public void update(float delta) {
        pos.mulAdd(v,delta);
        if(isOutside(worldBounds)){
            destroyed();
        }
    }
}
