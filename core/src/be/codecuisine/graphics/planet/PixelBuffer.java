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

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class PixelBuffer extends FrameBuffer {

    private TextureRegion pixelBufferRegion;

    public PixelBuffer(int width, int height) {
        super(Pixmap.Format.RGBA8888, width, height, false);
        getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        pixelBufferRegion = new TextureRegion(getColorBufferTexture(), 0, 0, width, height);
        pixelBufferRegion.flip(false, true);
    }

    public void render(SpriteBatch[] batches, OrthographicCamera screenCamera) {
        for (SpriteBatch batch : batches) {
            batch.setShader(null);
            batch.setProjectionMatrix(screenCamera.combined);
            batch.begin();
            batch.draw(pixelBufferRegion, 0, 0, screenCamera.viewportWidth, screenCamera.viewportHeight);
            batch.end();
        }
    }

    public void clear() {
        pixelBufferRegion.setRegion(new TextureRegion(getColorBufferTexture()), 0, 0, getWidth(), getHeight());
    }


}
