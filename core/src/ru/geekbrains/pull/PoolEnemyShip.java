package ru.geekbrains.pull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.Sprites.EnemyShip;
import ru.geekbrains.base.SpritePool;

public class PoolEnemyShip extends SpritePool<EnemyShip> {

    private TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));

    @Override
    public EnemyShip newObject() {
        return new EnemyShip(textureAtlas);
    }
}
