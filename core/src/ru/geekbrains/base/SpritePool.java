package ru.geekbrains.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public abstract class SpritePool <T extends Sprite> {
    private final List<T> activeObjects = new ArrayList<>();
    private final List<T> freeObjects = new ArrayList<>();

    public abstract T newObject();

    public T obtain(){ //вытаские из пула новый объект
        T object;
        if (freeObjects.isEmpty()){
            object = newObject();
        } else {
            object = freeObjects.remove(freeObjects.size()-1);
        }
        activeObjects.add(object);
        System.out.println("active/free: "+ activeObjects.size()+"/"+freeObjects.size());
        return object;
    }

    public void updateActiveSprites(float delta){
        for (T item:activeObjects) {
            if(!item.isDestroyed()){
                item.update(delta);
            }
        }
    }

    public void drawActiveSprites(SpriteBatch batch) {
        for (T item:activeObjects) {
            if(!item.isDestroyed()){
                item.draw(batch);
            }
        }
    }

    public void freeAllDestroyedActiveSprites(){
        for (int i = 0; i < activeObjects.size(); i++) {
            T item = activeObjects.get(i);
            if(item.isDestroyed()){
                free(item);
                i--;
                item.flushDestroyed();
            }
        }
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    public void dispose(){
        activeObjects.clear();
        freeObjects.clear();
    }

    private void free (T object){
        if (activeObjects.remove(object)){
            freeObjects.add(object);
        }
        System.out.println("active/free: "+ activeObjects.size()+"/"+freeObjects.size());
    }
}
