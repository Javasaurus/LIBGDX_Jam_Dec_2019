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
public class CircleCollider extends Collider {

    private float radius;

    public CircleCollider(Collidable collidable, Vector2 position, float radius) {
        super(collidable, position);
        this.radius = radius;
        this.colliderType = ColliderType.CIRCLE;
    }

    @Override
    public void DrawDebug(ShapeRenderer renderer) {
        renderer.circle(getPosition().x, getPosition().y, radius);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    protected boolean evaluateCollision(Collider otherCollider) {
        switch (otherCollider.getColliderType()) {
            case CIRCLE:
                return evaluateCircleCircle((CircleCollider) this, (CircleCollider) otherCollider);
            case RECTANGLE:
                return evaluateCircleRectangle((CircleCollider) this, (RectangleCollider) otherCollider);
            default:
                return false;
        }
    }

}
