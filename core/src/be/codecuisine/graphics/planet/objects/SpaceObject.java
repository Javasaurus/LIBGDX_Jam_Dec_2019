/* 
 * Copyright 2019 kenne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.codecuisine.graphics.planet.objects;

import be.codecuisine.graphics.rendering.Renderable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import java.util.Objects;

public abstract class SpaceObject extends Renderable implements Disposable {

    private Sprite sprite;
    private int zPos;
    private Vector2 position;
    private int size;
    private int color;
    private Color drawColor;

    protected SpaceObject() {

    }

    public SpaceObject(Sprite sprite) {
        this(sprite, Color.rgba8888(Color.WHITE));
        this.position = new Vector2(sprite.getOriginX() + size / 2, sprite.getOriginY() + size / 2);
    }

    public SpaceObject(Sprite sprite, int color) {
        this.sprite = sprite;
        this.color = color;

        drawColor = new Color(color);

        if (sprite != null) {
            this.size = (int) sprite.getWidth();
        }

        this.position = new Vector2(sprite.getOriginX() + size / 2, sprite.getOriginY() + size / 2);
    }

    public abstract void update(float delta);

    @Override
    public void drawSprite(Batch batch) {
        sprite.setColor(drawColor);
        sprite.draw(batch);
    }

    @Override
    public void drawShape(ShapeRenderer batch) {
        //
    }

    public void setPosition(Vector2 newPosition) {
        this.position = newPosition;
        sprite.setOrigin(newPosition.x - size / 2, newPosition.y - size / 2);
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void setZPos(int zPos) {
        this.zPos = zPos;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getSize() {
        return size;
    }

    public void setColor(int color) {
        this.color = color;
        this.drawColor = new Color(color);
    }

    public int getColor() {
        return color;
    }

    public Color getDrawColor() {
        return drawColor;
    }

    @Override
    public Integer getZ() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SpaceObject)) {
            return false;
        }
        SpaceObject other = (SpaceObject) o;
        return sprite.equals(other.sprite) && zPos == other.zPos;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.sprite);
        hash = 13 * hash + this.zPos;
        return hash;
    }

    @Override
    public void dispose() {

    }

}
