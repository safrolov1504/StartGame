package ru.geekbrains.Sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Star extends Sprite {

    protected Vector2 v;
    protected Rect worldBounds;
    protected float starAnimateInterval;
    protected float starAnimateTimer;

    protected float height;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        //звезды будут падать вниз с рандомной скоростью
        float vx = Rnd.nextFloat(-0.005f, 0.005f);
        float vy = Rnd.nextFloat(-0.2f,-0.03f);
        v = new Vector2(vx,vy);
        starAnimateInterval=1f;
        starAnimateTimer = Rnd.nextFloat(0,1f);
        height =0.01f;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(),worldBounds.getTop());
        pos.set(posX,posY);
        setHeightProportion(height);
    }


    @Override
    public void update(float delta) {
        pos.mulAdd(v,delta);
        checkBounds();
        height+=0.0001f;
        setHeightProportion(height);
        starAnimateTimer +=delta;
        if(starAnimateTimer >starAnimateInterval){
            starAnimateTimer = 0f;
            height=0.01f;
        }
    }

    protected void checkBounds(){
        if(getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        if(getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if(getTop() < worldBounds.getBottom()) setBottom(worldBounds.getTop());
        if(getBottom() > worldBounds.getTop()) setTop(worldBounds.getBottom());

    }
}
