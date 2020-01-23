package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.Sprites.EnemyShip;
import ru.geekbrains.Sprites.Help;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pull.EnemyPool;
import ru.geekbrains.pull.HelpPool;

public class HelpGenerator {
    private static float HELP_HEIGHT = 0.05f;
    private static int HELP_HP_1 = 1;
    private static int HELP_HP_2 = 5;
    private static int HELP_HP_3 = 7;


    private final TextureRegion[] helpRegions_1;
    private final TextureRegion [] helpRegions_2;
    private final TextureRegion [] helpRegions_3;


    private final Vector2 helpV_1 = new Vector2(0,-0.2f);
    private final Vector2 helpV_2 = new Vector2(0,-0.2f);
    private final Vector2 helpV_3 = new Vector2(0,-0.2f);

    private float generateInterval = 20f;
    private float generateTimer = generateInterval;

    private HelpPool helpPool;

    private Rect worldBounds;

    public HelpGenerator(TextureAtlas textureAtlas, HelpPool helpPool, Rect worldBounds) {
        TextureRegion helpRegions_1 = textureAtlas.findRegion("help");
        TextureRegion helpRegions_2 = textureAtlas.findRegion("keks");
        TextureRegion helpRegions_3 = textureAtlas.findRegion("pizza");
        this.helpRegions_1 = Regions.split(helpRegions_1,1,1,1);
        this.helpRegions_2 = Regions.split(helpRegions_2,1,1,1);
        this.helpRegions_3 = Regions.split(helpRegions_3,1,1,1);

        this.helpPool = helpPool;
        this.worldBounds = worldBounds;
    }

    public void generate(float delta){
        generateTimer+=delta;
        if(generateTimer > generateInterval){
            generateTimer = 0f;
            Help help = helpPool.obtain();
            float type = (float) Math.random();
            if(type < 0.5f){
                help.set(
                        helpRegions_1,
                        helpV_1,
                        HELP_HP_1,
                        HELP_HEIGHT
                );
            }
            else if(type < 0.8f){
                help.set(
                        helpRegions_2,
                        helpV_2,
                        HELP_HP_2,
                        HELP_HEIGHT
                );
            } else
            {
                help.set(
                        helpRegions_3,
                        helpV_3,
                        HELP_HP_3,
                        HELP_HEIGHT
                );
            }

            help.pos.x = Rnd.nextFloat(
                    worldBounds.getLeft()+help.getHalfWidth(),
                    worldBounds.getRight()-help.getHalfWidth());

            help.setBottom(worldBounds.getTop());
        }
    }
}
