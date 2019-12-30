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
package be.codecuisine.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 *
 * @author kenne
 */
public class Shaders {

    private static final Shaders INSTANCE = new Shaders();
    
    public static ShaderProgram PLANET_SHADER;

    public static ShaderProgram SHIP_SHADER;

    
    
    private Shaders() {
        PLANET_SHADER = new ShaderProgram(Gdx.files.internal("shaders/planet.vsh"), Gdx.files.internal("shaders/planet.fsh"));
        isCompiled(PLANET_SHADER);
        SHIP_SHADER = new ShaderProgram(Gdx.files.internal("shaders/ship.vsh"), Gdx.files.internal("shaders/ship.fsh"));
        isCompiled(SHIP_SHADER);
    }

    private boolean isCompiled(ShaderProgram shader) {
        if (!shader.isCompiled()) {
            Gdx.app.error("Planet Shader Error", "\n" + shader.getLog());
           throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        }
        return true;
    }

}
