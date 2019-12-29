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
package be.codecuisine.game.ai.impl;

import be.codecuisine.game.ai.AIBehavior;
import be.codecuisine.game.enemies.ships.BaseEnemy;
import be.codecuisine.game.math.VectorUtil;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kenne
 */
public class WaypointPatroller implements AIBehavior {

    private final BaseEnemy enemy;
    private Vector2 positionDirective;
    private Vector2 directionDirective;
    private float arrivalRange = 50f;

    private List<Vector2> wayPoints = new ArrayList<>();
    private int currentIndex = 0;

    public WaypointPatroller(BaseEnemy enemy) {
        this.enemy = enemy;
        this.positionDirective = new Vector2();
        this.directionDirective = new Vector2();
    }

    @Override
    public void update(float dt) {
        Vector2 target = new Vector2(wayPoints.get(currentIndex));
        Vector2 currentPosition = new Vector2(enemy.getShipPosition());

        directionDirective = directionDirective.lerp(new Vector2(target).sub(enemy.getShipPosition()), dt * enemy.getSpeed());

        //for now just do simple chase
        float dst = currentPosition.dst(target);
        if (dst > arrivalRange) {
            positionDirective = VectorUtil.MoveTo(currentPosition, target, dt * enemy.getSpeed() * 5);
        } else {
            currentIndex++;
            if (currentIndex >= wayPoints.size()) {
                currentIndex = 0;
            }
        }

    }

    @Override
    public Vector2 getPositionDirective() {
        return positionDirective;
    }

    @Override
    public Vector2 getDirectionDirective() {
        return directionDirective;
    }

    @Override
    public void executeAction() {
        //
    }

    public List<Vector2> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<Vector2> wayPoints) {
        this.wayPoints = wayPoints;
    }

}
