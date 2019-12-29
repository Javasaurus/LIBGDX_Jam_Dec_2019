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
package be.codecuisine.graphics.planet;

import com.badlogic.gdx.utils.Array;

public class ColorGroup {
    private Array<Integer> colors;

    public ColorGroup() {
        colors = new Array<Integer>();
    }

    public int at(int index) {
        return colors.get(index);
    }

    public void set(int index, int newColor) {
        colors.set(index, newColor);
    }

    public ColorGroup add(int color) {
        colors.add(color);
        return this;
    }

    public int random() {
        return colors.random();
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < colors.size; i++) {
            str += "0x" + Integer.toHexString(colors.get(i));
            if(i != colors.size - 1) {
                str += ", ";
            }
        }
        return str;
    }
}