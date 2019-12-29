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
package be.codecuisine.game.enemies.waves;

import be.codecuisine.graphics.rendering.UILine;
import be.codecuisine.graphics.rendering.UIRendering;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kenne
 */
public abstract class BaseEnemyWave {

    protected final int waveNr;

    public BaseEnemyWave(int waveNr) {
        this.waveNr = waveNr;
    }

    public abstract void LaunchWave();

    public abstract void EndWave() ;

    public abstract boolean evaluateWave();

    public abstract void update(float dt);

}
