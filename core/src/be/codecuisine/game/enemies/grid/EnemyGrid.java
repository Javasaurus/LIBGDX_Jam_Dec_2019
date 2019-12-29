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
package be.codecuisine.game.enemies.grid;

import be.codecuisine.game.enemies.ships.BaseEnemy;
import be.codecuisine.graphics.rendering.RenderingPool;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author kenne
 */
public class EnemyGrid {

    public Vector2[][] positions;
    public BaseEnemy[][] enemies;

    public EnemyGrid(int rows, int columns, Vector2 bottomLeft, Vector2 topRight) {
        positions = new Vector2[rows][columns];
        enemies = new BaseEnemy[rows][columns];
        //should we use a fixed cellWidth?
        float cellWidth = (topRight.x - bottomLeft.x) / columns;
        float cellHeight = (topRight.y - bottomLeft.y) / rows;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {

                float cellX = (cellWidth / 2) + bottomLeft.x + cellWidth * column + 1;
                float cellY = bottomLeft.y + (cellHeight * row);

                positions[row][column] = new Vector2(cellX, cellY);
            }
        }
    }

    public void SpawnEnemy(BaseEnemy enemy, int i, int j) {
        //TODO figure this bug out
        if (enemy == null || enemies == null || positions == null) {
            return;
        }
        enemy.setGridPosition(new Vector2(positions[i][j]));
        enemy.setTargetPosition(new Vector2(enemy.getGridPosition()));
        enemies[i][j] = enemy;
        RenderingPool.INSTANCE.add(enemy);
    }

    public void update(float dt) {
        if (enemies != null) {
            for (int i = 0; i < enemies.length; i++) {
                for (int j = 0; j < enemies[0].length; j++) {
                    if (enemies[i][j] != null) {
                        enemies[i][j].update(dt);
                    }
                }
            }
        }
    }

}
