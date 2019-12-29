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
package be.codecuisine.game.enemies.ships.impl.dev;

import be.codecuisine.game.GameBehavior;
import be.codecuisine.game.collision.ColliderPool;
import be.codecuisine.game.enemies.ships.BaseEnemy;
import be.codecuisine.graphics.effects.Explosion;
import be.codecuisine.graphics.rendering.RenderingPool;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author kenne
 */
public class ChainChaseEnemy extends SimpleWaypointEnemy {

    public BaseEnemy chain[];
    private final int chainLength;

    public ChainChaseEnemy(Vector2 position, GameBehavior target, int chainLength, float sizeX, float sizeY) {
        super(position, sizeX, sizeY, 1);
        this.chain = new BaseEnemy[chainLength];
        GameBehavior tmp = this;
        for (int i = 0; i < chainLength; i++) {
            chain[i] = new SimpleChaseEnemy(new Vector2(position), tmp, sizeX, sizeY);
            tmp = chain[i];
            RenderingPool.INSTANCE.add(chain[i]);
        }
        this.chainLength = chainLength;
    }

    @Override
    public void update(float t) {
        super.update(t);
        if (!isDestroyed) {
            isDestroyed = true;
            for (int i = 0; i < chain.length; i++) {
                if (chain[i] != null && !chain[i].isDestroyed) {
                    isDestroyed = false;
                }
                chain[i].update(t);
            }
        }
    }

    @Override
    public void Die() {
        ColliderPool.INSTANCE.remove(this);
        RenderingPool.INSTANCE.remove(this);
        //do die effects
        Explosion explosion = new Explosion(shipPosition, false);
        explosion.setAutoDestroy(true);
        explosion.setScale(3);
        explosion.setPlayBackSpeed(1f);
        // DO NOT DESTROY
    }

    @Override
    public int GetPoints() {
        return chainLength * 200;
    }

}
