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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 *
 * @author kenne
 */
public class Constants {

    public static final int KEY_LEFT = Keys.LEFT;
    public static final int KEY_RIGHT = Keys.RIGHT;
    public static final int KEY_UP = Keys.UP;
    public static final int KEY_DOWN = Keys.DOWN;
    public static final int KEY_ESC = Keys.ESCAPE;

    public static final int WIDTH = Gdx.graphics.getWidth();
    public static final int HEIGHT = Gdx.graphics.getHeight();
    public static final int CENTER_X = WIDTH / 2;
    public static final int CENTER_Y = HEIGHT / 2;

    public static String formatScore(long score) {
        String scoreString = ("" + score);
        return ("00000000000" + scoreString).substring((scoreString).length());
    }

}
