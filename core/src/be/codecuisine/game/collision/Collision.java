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
package be.codecuisine.game.collision;

import java.util.Objects;

/**
 *
 * @author kenne
 */
public class Collision {

    private final Collidable first;
    private final Collidable second;

    public Collision(Collidable first, Collidable second) {
        this.first = first;
        this.second = second;
    }

    public Collidable getOther(Collidable me) {
        return first.equals(me) ? second : first;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.first);
        hash = 19 * hash + Objects.hashCode(this.second);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Collision other = (Collision) obj;

        if (first.equals(other.first) && second.equals(other.second)) {
            return true;
        }
        if (first.equals(other.second) && second.equals(other.first)) {
            return true;
        }

        return false;
    }

    public Collidable getFirst() {
        return first;
    }

    public Collidable getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return first + " collided with " + second;
    }

}
