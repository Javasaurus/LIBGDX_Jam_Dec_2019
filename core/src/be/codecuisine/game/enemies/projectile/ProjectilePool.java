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
package be.codecuisine.game.enemies.projectile;

import be.codecuisine.game.collision.*;
import be.codecuisine.game.enemies.ships.BaseEnemy;
import com.badlogic.gdx.utils.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author kenne
 */
public class ProjectilePool {

    public static ProjectilePool INSTANCE = new ProjectilePool();
    private HashSet<BaseProjectile> projectiles = new HashSet<BaseProjectile>();
    private HashSet<BaseProjectile> toAdd = new HashSet<BaseProjectile>();
    private HashSet<BaseProjectile> toRemove = new HashSet<BaseProjectile>();

    private ProjectilePool() {

    }

    public void add(BaseProjectile projectile) {
        toAdd.add(projectile);
    }

    public void remove(BaseProjectile projectile) {
        toRemove.add(projectile);
    }

    public void addAll(Collection<BaseProjectile> projectiles) {
        toAdd.addAll(projectiles);
    }

    public void removeAll(Collection<BaseProjectile> projectiles) {
        toRemove.addAll(projectiles);
    }

    public void addAll(Array<BaseProjectile> projectiles) {
        toAdd.addAll(Arrays.asList(projectiles.items));
    }

    public void removeAll(Array<BaseProjectile> projectiles) {
        toRemove.addAll(Arrays.asList(projectiles.items));
    }

    public void updateProjectiles(float t) {

        projectiles.addAll(toAdd);
        projectiles.removeAll(toRemove);

        toAdd.clear();
        toRemove.clear();

        for (BaseProjectile projectile : projectiles) {
            projectile.update(t);

        }
    }

    public void clear() {
        toRemove.clear();
        toAdd.clear();
        projectiles.clear();
    }

}
