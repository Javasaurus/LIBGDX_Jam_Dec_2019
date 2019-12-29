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
package be.codecuisine.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import java.util.HashMap;

/**
 *
 * @author kenne
 */
public class Input implements InputProcessor {

    private static HashMap<Integer, Boolean> INPUT = new HashMap<>();
    private static Vector2 MOUSE_POSITION = new Vector2();
    public static Vector2 STORED_MOUSE_POSITION;
    private static HashMap<Integer, Boolean> MOUSE_INPUT = new HashMap<>();

    public Input() {

    }

    private void RegisterKeyPress(int keycode, boolean state) {
        INPUT.put(keycode, state);
    }

    private void RegisterMousePress(int button, boolean state) {
        MOUSE_INPUT.put(button, state);
    }

    public static boolean IsKeyDown(int keyCode) {
        return INPUT.getOrDefault(keyCode, false);
    }

    public static boolean IsMouseButtonDown(int button) {
        return MOUSE_INPUT.getOrDefault(button, false);
    }

    public static Vector2 GetMousePosition() {
        return MOUSE_POSITION;
    }

    @Override
    public boolean keyDown(int keycode) {
        RegisterKeyPress(keycode, true);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        RegisterKeyPress(keycode, false);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        RegisterMousePress(button, true);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        RegisterMousePress(button, false);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        MOUSE_POSITION.x = screenX;
        MOUSE_POSITION.y = Gdx.graphics.getHeight() - screenY;
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public static void StoreMousePosition() {
        STORED_MOUSE_POSITION = new Vector2(Gdx.input.getX(), Gdx.input.getY());
    }

    public static void ReStoreMousePosition() {
        Gdx.input.setCursorPosition((int) STORED_MOUSE_POSITION.x, (int) STORED_MOUSE_POSITION.y);
    }

}
