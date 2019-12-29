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
package be.codecuisine.game.collision.impl;

import be.codecuisine.game.collision.Collidable;
import be.codecuisine.game.collision.Collider;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author kenne
 */
public class RectangleCollider extends Collider {

    private float width, height;

    public RectangleCollider(Collidable collidable, Vector2 position, float width, float height) {
        super(collidable, position);
        this.width = width;
        this.height = height;
        this.colliderType = ColliderType.RECTANGLE;
    }

    @Override
    public void DrawDebug(ShapeRenderer renderer) {
        renderer.rect(getPosition().x, getPosition().y, width, height);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    protected boolean evaluateCollision(Collider otherCollider) {
        switch (otherCollider.getColliderType()) {
            case CIRCLE:
                return evaluateCircleRectangle((CircleCollider) otherCollider, this);
            case RECTANGLE:
                return evaluateRectangleRectangle(this, (RectangleCollider) otherCollider);
            default:
                return false;
        }
    }

}
