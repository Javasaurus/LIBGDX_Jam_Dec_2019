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
package be.codecuisine.graphics.rendering;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author kenne
 */
public class UILine {

    private final String line;
    private final float x;
    private final float y;

    public UILine(String line, float x, float y) {
        this.line = line;
        this.x = x;
        this.y = y;
    }

    public UILine(String line, Vector2 position) {
        this(line, position.x, position.y);
    }

    public String getLine() {
        return line;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}