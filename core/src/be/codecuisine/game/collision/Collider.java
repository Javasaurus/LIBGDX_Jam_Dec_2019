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
package be.codecuisine.game.collision;

import be.codecuisine.game.collision.impl.CircleCollider;
import be.codecuisine.game.collision.impl.RectangleCollider;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 *
 * @author kenne
 */
public abstract class Collider {

    public enum ColliderType {
        CIRCLE, RECTANGLE;
    }

    protected ColliderType colliderType = ColliderType.CIRCLE;
    protected ArrayList<CollisionLayer> collisionLayers = new ArrayList<>();
    protected CollisionLayer myCollisionLayer = CollisionLayer.OTHER;
    protected Vector2 position;
    public boolean isEnabled = true;
    protected final Collidable collidable;

    public Collider(Collidable collidable, Vector2 position) {
        this.collidable = collidable;
        this.position = position;
        ColliderPool.INSTANCE.add(collidable);
    }

    public boolean isColliding(Collider otherCollider) {
        if (!otherCollider.isEnabled) {
            return false;
        }
        if (otherCollider != null && otherCollider != this) {
            if (collisionLayers.contains(otherCollider.myCollisionLayer)) {
                return evaluateCollision(otherCollider);
            }
        }
        return false;
    }

    public ColliderType getColliderType() {
        return colliderType;
    }

    protected abstract boolean evaluateCollision(Collider otherCollider);

    public boolean evaluateCircleCircle(Vector2 positionFirst, float radiusFirst, Vector2 positionSecond, float radiusSecond) {
        return positionSecond.dst(positionFirst) <= radiusSecond + radiusFirst;
    }

    public boolean evaluateCircleCircle(CircleCollider first, CircleCollider second) {
        return evaluateCircleCircle(first.getPosition(), first.getRadius(), second.getPosition(), second.getRadius());
    }

    public boolean evaluateCircleRectangle(Vector2 positionFirst, float radiusFirst, Vector2 positionSecond, float widthSecond, float heightSecond) {
        float cx = positionFirst.x;
        float cy = positionFirst.y;
        float cr = radiusFirst;

        float rx = positionSecond.x - widthSecond / 2;
        float ry = positionSecond.y - heightSecond / 2;

        float rw = widthSecond;
        float rh = heightSecond;

        float testX = cx;
        float testY = cy;
        if (cx < rx) {
            testX = rx;        // left edge
        } else if (cx > rx + rw) {
            testX = rx + rw;     // right edge
        }
        if (cy < ry) {
            testY = ry;        // top edge
        } else if (cy > ry + rh) {
            testY = ry + rh;
        }

        float distX = cx - testX;
        float distY = cy - testY;
        float distance = (float) Math.sqrt((distX * distX) + (distY * distY));

        return distance <= cr;
    }

    public boolean evaluateCircleRectangle(CircleCollider first, RectangleCollider second) {
        return evaluateCircleRectangle(first.getPosition(), first.getRadius(), second.getPosition(), second.getWidth(), second.getHeight());
    }

    public boolean evaluateRectangleRectangle(Vector2 positionFirst, float widthFirst, float heightFirst, Vector2 positionSecond, float widthSecond, float heightSecond) {
        float r1x = positionFirst.x - widthFirst / 2;
        float r1y = positionFirst.y - heightFirst / 2;
        float r1w = widthFirst;
        float r1h = heightFirst;

        float r2x = positionSecond.x - widthSecond / 2;
        float r2y = positionSecond.y - heightSecond / 2;
        float r2w = widthSecond;
        float r2h = heightSecond;

        if (r1x + r1w >= r2x
                && // r1 right edge past r2 left
                r1x <= r2x + r2w
                && // r1 left edge past r2 right
                r1y + r1h >= r2y
                && // r1 top edge past r2 bottom
                r1y <= r2y + r2h) {       // r1 bottom edge past r2 top
            return true;
        }
        return false;

    }

    public boolean evaluateRectangleRectangle(RectangleCollider first, RectangleCollider second) {
        return evaluateRectangleRectangle(first.getPosition(), first.getWidth(), first.getHeight(), second.getPosition(), second.getWidth(), second.getHeight());

    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public ArrayList<CollisionLayer> getCollisionLayers() {
        return collisionLayers;
    }

    public CollisionLayer getMyCollisionLayer() {
        return myCollisionLayer;
    }

    public void setMyCollisionLayer(CollisionLayer myCollisionLayer) {
        this.myCollisionLayer = myCollisionLayer;
    }

    public Collidable getCollidable() {
        return collidable;
    }

    public abstract void DrawDebug(ShapeRenderer renderer);

}
