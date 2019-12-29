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

/**
 *
 * @author kenne
 */
public class SimplePatroller implements AIBehavior {

    private final BaseEnemy enemy;
    private Vector2 positionDirective;
    private Vector2 directionDirective;
    private boolean isGridLocked = true;
    private boolean isAtGrid;

    private float wiggleRoom = 50;

    public SimplePatroller(BaseEnemy enemy) {
        this.enemy = enemy;
        this.positionDirective = new Vector2();
        this.directionDirective = new Vector2();
        this.wiggleRoom = (float) (wiggleRoom / 2 + (Math.random() * (wiggleRoom / 2)));
    }

    @Override
    public void update(float dt) {
        Vector2 targetPosition;
        if (isAtGrid) {
            targetPosition = new Vector2(enemy.getPosition());
            targetPosition.x = enemy.getGridPosition().x + (float) (Math.sin(enemy.getTotalTimeAlive()) * wiggleRoom);
            positionDirective = targetPosition;
            directionDirective = directionDirective.lerp(new Vector2(0, -1), dt * enemy.getSpeed());
        } else {
            //move towards the targetPosition
            targetPosition = new Vector2(enemy.isLockedToGrid() ? enemy.getGridPosition() : enemy.getTargetPosition());
            Vector2 currentPosition = new Vector2(enemy.getShipPosition());

            float dst = currentPosition.dst(targetPosition);
            if (dst < 1f) {
                currentPosition = targetPosition;
                isAtGrid = true;
            } else {
                currentPosition = VectorUtil.MoveTo(currentPosition, targetPosition, dt * enemy.getSpeed() * 5);
                //           isAtGrid = false;
            }

            if (isGridLocked && isAtGrid) {
                directionDirective = directionDirective.lerp(new Vector2(0, -1), dt * enemy.getSpeed() / 100);
            } else {
                positionDirective = currentPosition;
                directionDirective = directionDirective.lerp(new Vector2(currentPosition).sub(enemy.getShipPosition()), dt * enemy.getSpeed());
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
        //      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
