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

import static be.codecuisine.Constants.HEIGHT;
import static be.codecuisine.Constants.WIDTH;
import be.codecuisine.graphics.planet.PixelBuffer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author kenne
 */
public class RenderingPool implements Disposable {

    private PixelBuffer pixelBuffer;
    private final SpriteBatch spriteRenderer;
    private final ShapeRenderer shapeRenderer;

    private HashSet<Renderable> toRender = new HashSet<>();
    private HashSet<Renderable> toAdd = new HashSet<>();
    private HashSet<Renderable> toRemove = new HashSet<>();

    public static final RenderingPool INSTANCE = new RenderingPool();

    private RenderingPool() {
        this.pixelBuffer = new PixelBuffer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.spriteRenderer = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
    }

    public void add(Renderable renderable) {
        toAdd.add(renderable);
    }

    public void remove(Renderable renderable) {
        toRemove.add(renderable);
    }

    public void render(OrthographicCamera screenCamera) {

        toRender.removeAll(toRemove);
        toRender.addAll(toAdd);

        toRemove.clear();
        toAdd.clear();

        ResetBuffer();
        renderLines();
        renderSprites(screenCamera);

    }

    private void renderSprites(OrthographicCamera screenCamera) {

        pixelBuffer.begin();
        spriteRenderer.begin();

        List<Renderable> list = new ArrayList<Renderable>(toRender);
        Collections.sort(list);

        for (Renderable renderable : list) {
            if (renderable == null) {
                continue;
            }
            renderable.drawSprite(spriteRenderer);
        }
        spriteRenderer.end();
        pixelBuffer.end();

        pixelBuffer.render(new SpriteBatch[]{spriteRenderer}, screenCamera);

    }

    private void renderLines() {
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        for (Renderable renderable : toRender) {
            if (renderable == null) {
                continue;
            }
            renderable.drawShape(shapeRenderer);
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        //    spriteRenderer.dispose();
        //  shapeRenderer.dispose();
        ResetBuffer();
    }

    public void ResetBuffer() {

        if (pixelBuffer != null) {
            pixelBuffer.dispose();
        }
        pixelBuffer = new PixelBuffer(WIDTH, HEIGHT);

        // THESE ARE NEW
        pixelBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        pixelBuffer.end();
        // END NEW

    }

    public void clear() {
        ResetBuffer();
        toRemove.clear();
        toAdd.clear();
        toRender.clear();
    }

}
