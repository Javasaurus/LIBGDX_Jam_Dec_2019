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
package be.codecuisine.game.math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author kenne
 */
public class VectorUtil {

    public static Vector2 MoveTo(Vector2 start, Vector2 end, float alpha) {
        return start.add(new Vector2(end).sub(start).nor().scl(alpha));
    }

    public static Vector2 RandomVectorScreen(float radius) {
        Vector2 center = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        return RandomVector(center, radius);
    }

    public static Vector2 RandomVector(Vector2 center, float radius) {
        double angle = Math.random() * Math.PI * 2;
        return new Vector2(center.x + (float) Math.cos(angle) * radius, center.y + (float) Math.sin(angle) * radius);
    }

    public static Vector2 RandomVectorScreenUP(float radius) {
        Vector2 center = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        return RandomVectorUP(center, radius);
    }

    public static Vector2 RandomVectorUP(Vector2 center, float radius) {
        double angle = Math.random() * Math.PI * 2;
        return new Vector2(center.x + (float) Math.cos(angle) * radius, center.y + (float) Math.abs(Math.sin(angle) * radius));
    }

}
