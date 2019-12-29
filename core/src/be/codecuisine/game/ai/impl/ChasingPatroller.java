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

import be.codecuisine.game.GameBehavior;
import be.codecuisine.game.ai.AIBehavior;
import be.codecuisine.game.collision.Collidable;
import be.codecuisine.game.enemies.projectile.BaseProjectile;
import be.codecuisine.game.enemies.ships.BaseEnemy;
import be.codecuisine.game.glutto.GluttoHead;
import be.codecuisine.game.math.VectorUtil;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author kenne
 */
public class ChasingPatroller implements AIBehavior {

    private final BaseEnemy enemy;
    private GameBehavior target;
    private Vector2 positionDirective;
    private Vector2 directionDirective;
    private boolean shoot = false;
    private float arrivalRange = 5f;

    public ChasingPatroller(BaseEnemy enemy) {
        this.enemy = enemy;
        this.positionDirective = new Vector2();
        this.directionDirective = new Vector2();
        this.target = GluttoHead.INSTANCE;
    }

    public ChasingPatroller(BaseEnemy enemy, GameBehavior target, float arrivalRange) {
        this.enemy = enemy;
        this.positionDirective = new Vector2();
        this.directionDirective = new Vector2();
        this.target = target;
        this.arrivalRange = arrivalRange;
    }

    @Override
    public void update(float dt) {
        Vector2 currentPosition = new Vector2(enemy.getShipPosition());

        directionDirective = directionDirective.lerp(new Vector2(target.getPosition()).sub(enemy.getShipPosition()), dt * enemy.getSpeed());

        //for now just do simple chase
        float dst = currentPosition.dst(target.getPosition());
        if (dst > arrivalRange) {
            positionDirective = VectorUtil.MoveTo(currentPosition, target.getPosition(), dt * enemy.getSpeed() * 5);
        } else {
         //   new BaseProjectile(currentPosition, directionDirective, 25, 3, 7);
        }

    }

    public GameBehavior getTarget() {
        return target;
    }

    public void setTarget(GameBehavior target) {
        this.target = target;
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

    }

}
