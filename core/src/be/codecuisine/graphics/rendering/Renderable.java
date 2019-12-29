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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author kenne
 */
public abstract class Renderable implements Comparable<Renderable>{

    public abstract boolean isBackground();
    
    public  abstract void drawSprite(Batch batch);

    public abstract  void drawShape(ShapeRenderer shapeRenderer);

    public Integer getZ(){
        return 0;
    }
    
      @Override
  public int compareTo(Renderable u) {
    return getZ().compareTo(u.getZ());
  }
    
    
}
