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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import java.util.List;

/**
 *
 * @author kenne
 */
public class UIRendering implements Disposable {

    public static final UIRendering INSTANCE = new UIRendering();
    public SpriteBatch batch;
    public ShapeRenderer renderer;
    public BitmapFont font;

    private UIRendering() {
        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        renderer = new ShapeRenderer();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        renderer.dispose();
    }

    public static void renderLines(List<UILine> lines, float scale) {
        INSTANCE.font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        INSTANCE.font.getData().setScale(scale);

        INSTANCE.batch.begin();
        for (UILine line : lines) {
            INSTANCE.font.draw(UIRendering.INSTANCE.batch, line.getLine(), line.getX(), line.getY());
        }
        INSTANCE.batch.end();
    }

    public static void renderRectangle(boolean filled, Color color, int x, int y, int width, int height) {
        INSTANCE.renderer.setAutoShapeType(true);
        INSTANCE.renderer.begin();
        INSTANCE.renderer.set(filled ? ShapeRenderer.ShapeType.Filled : ShapeRenderer.ShapeType.Line);
        INSTANCE.renderer.setColor(color);
        INSTANCE.renderer.rect(x, y, width, height);
        INSTANCE.renderer.end();
        INSTANCE.renderer.setColor(Color.WHITE);
    }

}
