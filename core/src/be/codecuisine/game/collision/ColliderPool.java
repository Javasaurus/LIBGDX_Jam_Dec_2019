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

import be.codecuisine.game.enemies.ships.BaseEnemy;
import com.badlogic.gdx.utils.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author kenne
 */
public class ColliderPool {

    public static ColliderPool INSTANCE = new ColliderPool();
    private HashSet<Collidable> colliders = new HashSet<Collidable>();
    private HashSet<Collidable> toAdd = new HashSet<Collidable>();
    private HashSet<Collidable> toRemove = new HashSet<Collidable>();
    private HashSet<Collision> collisionCache = new HashSet<>();

    private ColliderPool() {

    }

    public void add(Collidable collidable) {
        toAdd.add(collidable);
    }

    public void remove(Collidable collidable) {
        toRemove.add(collidable);
    }

    public void addAll(Collection<Collidable> collidables) {
        toAdd.addAll(collidables);
    }

    public void removeAll(Collection<Collidable> collidables) {
        toRemove.addAll(collidables);
    }

    public void addAll(Array<Collidable> collidables) {
        toAdd.addAll(Arrays.asList(collidables.items));
    }

    public void removeAll(Array<Collidable> collidables) {
        toRemove.addAll(Arrays.asList(collidables.items));
    }

    public void updateCollisions() {

        colliders.addAll(toAdd);
        colliders.removeAll(toRemove);

        toAdd.clear();
        toRemove.clear();
        collisionCache.clear();

        for (Collidable collidable : colliders) {
            HashSet<Collision> collisions = ColliderPool.INSTANCE.getCollisions(collidable);
            for (Collision collision : collisions) {
                if (!collisionCache.contains(collision)) {
                    collision.getFirst().onCollisionEnter(collision);
                    collision.getSecond().onCollisionEnter(collision);
                    collisionCache.add(collision);
                }
            }
        }
    }

    private HashSet<Collision> getCollisions(Collidable collidable) {

        HashSet<Collision> collisions = new HashSet<>();
        if (!collidable.getCollider().isEnabled) {
            return collisions;
        }
        for (Collidable otherCollidable : colliders) {
            if (otherCollidable.equals(collidable)) {
                continue;
            }
            if (otherCollidable.getCollider().isEnabled) {
                if (collidable.getCollider().isColliding(otherCollidable.getCollider())) {
                    Collision collision = new Collision(collidable, otherCollidable);
                    collisions.add(collision);
                }
            }
        }
        return collisions;
    }

    public boolean contains(BaseEnemy enemy) {
        return colliders.contains(enemy);
    }

    public void clear() {
        colliders.clear();
        toAdd.clear();
        toRemove.clear();
    }

}
