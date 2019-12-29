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
package be.codecuisine.game.enemies.waves.impl;

import be.codecuisine.game.enemies.grid.EnemyGrid;
import be.codecuisine.game.enemies.ships.BaseEnemy;
import be.codecuisine.game.enemies.ships.impl.BasicGridShip;
import be.codecuisine.game.enemies.waves.BaseEnemyWave;
import be.codecuisine.game.enemies.waves.GridWaveFactory;
import be.codecuisine.graphics.rendering.RenderingPool;
import be.codecuisine.game.math.Array2DUtil;
import be.codecuisine.game.math.VectorUtil;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.HashSet;

/**
 *
 * @author kenne
 */
public class GridStage extends BaseEnemyWave {

    private EnemyGrid grid;
    private HashSet<BaseEnemy> enemies = new HashSet<>();
    private int[][] lineup;
    private long waveStartTime;

    public GridStage(int waveNr) {
        super(waveNr);
        this.lineup = GridWaveFactory.GenerateLevel(waveNr);
        this.lineup = Array2DUtil.flipArrays(lineup);
    }

    @Override
    public void EndWave() {

    }

    @Override
    public void LaunchWave() {
        waveStartTime = TimeUtils.millis();
        enemies.clear();
        grid = new EnemyGrid(
                lineup.length,
                lineup[0].length,
                new Vector2(50, Gdx.graphics.getHeight() / 3),
                new Vector2(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 25));

        for (int i = 0; i < lineup.length; i++) {
            for (int j = 0; j < lineup[0].length; j++) {
                if (lineup[i][j] != 0) {
                    BaseEnemy enemy = getEnemy(lineup[i][j]);
                    if (enemy != null) {
                        enemy.health = Math.min(5, 1 + (waveNr / 5));
                        enemy.maxHealth = enemy.health;
                        RenderingPool.INSTANCE.add(enemy);
                        grid.SpawnEnemy(enemy, i, j);
                        enemies.add(enemy);
                    }
                }
            }
        }
    }

    private BaseEnemy getEnemy(int i) {
        BaseEnemy enemy = null;
        if (i > 0) {
            Vector2 spawnLocation = VectorUtil.RandomVectorScreenUP(2000);
            switch (i) {
                case 1:
                    enemy = new BasicGridShip(spawnLocation, 2f, 2f);
            }
            if (enemy != null) {
                enemy.maxHealth = 1 + (waveNr % 5);
            }
        }
        return enemy;
    }

    @Override
    public void update(float dt) {
        grid.update(dt);
    }

    @Override
    public boolean evaluateWave() {
        for (BaseEnemy enemy : enemies) {
            if (enemy != null && !enemy.isDestroyed) {
                return false;
            }
        }
        //here it means there are no more enemies known...
        return (TimeUtils.timeSinceMillis(waveStartTime) > 3000);

    }
}
