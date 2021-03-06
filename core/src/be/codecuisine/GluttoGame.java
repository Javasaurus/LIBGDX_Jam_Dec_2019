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
package be.codecuisine;

import be.codecuisine.graphics.planet.PlanetVisuals;
import be.codecuisine.graphics.rendering.RenderingPool;
import be.codecuisine.screens.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class GluttoGame extends Game {

    GameStateManager gsm = new GameStateManager(this);

    @Override
    public void create() {
        Gdx.input.setCursorCatched(false);
        this.setScreen(new MainMenuScreen(this));
        PlanetVisuals.generatePlanetPixmap(500);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        RenderingPool.INSTANCE.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

}
