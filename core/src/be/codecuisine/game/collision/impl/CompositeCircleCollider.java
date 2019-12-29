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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author kenne
 */
public class CompositeCircleCollider extends CircleCollider {

    private LinkedHashMap<Vector2, Float> positionsToCheckCollisions = new LinkedHashMap<>();

    public CompositeCircleCollider(Collidable collidable, Vector2 position, float radius) {
        super(collidable, position, radius);
        colliderType = ColliderType.CIRCLE;
    }

    public void setPositionsToCheck(LinkedHashMap<Vector2, Float> check) {
        Clear();
        positionsToCheckCollisions.putAll(check);
    }

    public void Clear() {
        positionsToCheckCollisions.clear();
    }

    @Override
    public void DrawDebug(ShapeRenderer renderer) {
        if (!positionsToCheckCollisions.isEmpty()) {
            for (Map.Entry<Vector2, Float> segmentPosition : positionsToCheckCollisions.entrySet()) {
                renderer.circle(segmentPosition.getKey().x, segmentPosition.getKey().y, segmentPosition.getValue());
            }
        }
    }

    @Override
    protected boolean evaluateCollision(Collider otherCollider) {

        switch (otherCollider.getColliderType()) {
            case CIRCLE:
                CircleCollider cTmp = (CircleCollider) otherCollider;
                for (Map.Entry<Vector2, Float> segmentPosition : positionsToCheckCollisions.entrySet()) {
                    boolean isHit = evaluateCircleCircle(segmentPosition.getKey(), segmentPosition.getValue(), cTmp.getPosition(), cTmp.getRadius());
                    if (isHit) {
                        return true;
                    }
                }
                break;
            case RECTANGLE:
                RectangleCollider rTmp = (RectangleCollider) otherCollider;
                for (Map.Entry<Vector2, Float> segmentPosition : positionsToCheckCollisions.entrySet()) {
                    boolean isHit = evaluateCircleRectangle(segmentPosition.getKey(), segmentPosition.getValue(), rTmp.getPosition(), rTmp.getWidth(), rTmp.getHeight());
                    if (isHit) {
                        return true;
                    }
                }
                break;
        }

        return false;
    }

}
