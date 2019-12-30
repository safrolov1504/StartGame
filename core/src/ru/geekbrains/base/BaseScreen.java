package ru.geekbrains.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.MatrixUtils;
import ru.geekbrains.math.Rect;

//общие для все экранов
public class BaseScreen implements Screen, InputProcessor {
    protected SpriteBatch batch;

    private Rect screenBounds;//описывает экран с пиксеньной системой координат
    private Rect worldBounds;//описывает мировую сисему координат
    private Rect glBounds;//описывает систему координат, которую использует openGL\

    //матрицы используются для преобразования из одной системы координат
    //в другую систему координат
    //матрица для вырисовки объектов
    private Matrix4 worldToGl;

    //матрица для описания событий
    private Matrix3 screenToWorld;

    private Vector2 touch;

    @Override
    public void show() {
        System.out.println("show");
        batch = new SpriteBatch();
        screenBounds = new Rect();
        worldBounds= new Rect();
        glBounds = new Rect(0,0,1f,1f);
        worldToGl = new Matrix4();

        screenToWorld = new Matrix3();
        touch = new Vector2();
        Gdx.input.setInputProcessor(this);
    }

    public Matrix3 getScreenToWorld() {
        return screenToWorld;
    }

    public Vector2 recalVector(float posX, float posY){
        Vector2 vectorOut = new Vector2();
        vectorOut.set(posX,posY).mul(screenToWorld);
        //touch.set(screenX,Gdx.graphics.getHeight()-screenY).mul(screenToWorld);
        //System.out.println(screenToWorld.toString());
        return vectorOut;
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize "+ width+" "+height);
        //передаем в наш прямоуголник размеры экрана
        screenBounds.setSize(width,height);
        //пишем левую нижнюю точку
        screenBounds.setLeft(0);
        //и дно
        screenBounds.setBottom(0);

        //считаем соотнощение строн
        float aspect = width/(float)height;

        //задаем мировую сетку
        worldBounds.setHeight(1f);
        worldBounds.setWidth(1f*aspect);

        MatrixUtils.calcTransitionMatrix(worldToGl,worldBounds,glBounds);

        MatrixUtils.calcTransitionMatrix(screenToWorld,screenBounds,worldBounds);

        batch.setProjectionMatrix(worldToGl);
        resize(worldBounds);
    }

    public void resize(Rect worldBounds) {
        System.out.println("resize worldBounds width = "+worldBounds.getWidth() + " height = "+ worldBounds.getHeight());
    }

    @Override
    public void pause() {
        System.out.println("pause");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
        System.out.println("hide");
        dispose();
    }

    @Override
    public void dispose() {
        System.out.println("dispose");
        batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown "+keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("keyUp "+keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        System.out.println("keyTyped "+character);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchDown "+screenX+" "+screenY);
        touch.set(screenX,Gdx.graphics.getHeight()-screenY).mul(screenToWorld);
        touchDown(touch,pointer,button);
        return false;
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        System.out.println("touchDown touchX "+touch.x+" touchY "+touch.y);
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //System.out.println("touchUp "+screenX+" "+screenY);
        touch.set(screenX,Gdx.graphics.getHeight()-screenY).mul(screenToWorld);
        //System.out.println(screenToWorld.toString());
        touchUp(touch,pointer,button);
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        //System.out.println("touchUp touchX "+touch.x+" touchY "+touch.y);
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.println("touchDragged "+screenX+" "+screenY);
        touch.set(screenX,Gdx.graphics.getHeight()-screenY).mul(screenToWorld);

        touchDragged(touch, pointer);
        return false;
    }

    public boolean touchDragged(Vector2 touch, int pointer) {
        System.out.println("touchDragged touchX "+touch.x+" touchY "+touch.y);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        System.out.println("scrolled "+amount);
        return false;
    }

    public Vector2 getTouch() {
        return touch;
    }
}
