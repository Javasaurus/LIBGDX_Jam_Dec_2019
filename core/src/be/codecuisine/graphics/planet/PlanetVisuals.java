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

import static be.codecuisine.Constants.CENTER_X;
import static be.codecuisine.Constants.CENTER_Y;
import be.codecuisine.graphics.planet.generators.NoiseGenerator;
import be.codecuisine.graphics.planet.generators.ObjectGenerator;
import be.codecuisine.graphics.planet.objects.SpaceBody;
import be.codecuisine.graphics.planet.objects.SpaceObject;
import be.codecuisine.graphics.planet.objects.Star;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;
import be.codecuisine.game.collision.CollisionLayer;
import be.codecuisine.graphics.rendering.Renderable;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class PlanetVisuals extends Renderable implements Disposable {

    public static final Texture pixelTexture = new Texture(Gdx.files.internal("pixel.png"));
    public static final PlanetVisuals INSTANCE = new PlanetVisuals();
    public static Pixmap pixmap;

    private ObjectGenerator objectGenerator;
    private Array<SpaceObject> spaceObjects;
    private Array<Star> stars;

    private PlanetVisuals() {

        spaceObjects = new Array<>();
        stars = new Array<>();

        ShaderProgram.pedantic = false;
        Init();
    }

    private void Init() {
        this.removeObjects(this.spaceObjects);
        this.removeObjects(stars);

        int zDir = MathUtils.randomSign();
        int velDir = MathUtils.randomSign();
        objectGenerator = new ObjectGenerator(this, velDir, zDir);
        this.stars = getObjectGenerator().createStars(100);
        //create a random moon
        /*     SpaceBody moon = createBody(velDir, MathUtils.random(50, 100), CollisionLayer.PLAYER, new CollisionLayer[]{CollisionLayer.PLAYER, CollisionLayer.OTHER});
        moon.setPosition(new Vector2(this.planet.getPosition()).sub(new Vector2(this.planet.getRadius() * 3f, this.planet.getRadius() * 2.5f)));
        spaceObjects.add(moon);*/
    }

    public SpaceBody CreatePlanet() {
        Init();
        return createBody(MathUtils.randomSign(), MathUtils.random(500, 800), CollisionLayer.PLANET, new CollisionLayer[]{CollisionLayer.PLAYER, CollisionLayer.OTHER});
    }

    public void update(float delta) {
        updateObjects(delta);
    }

    private void updateObjects(float delta) {
        for (Star spaceObject : stars) {
            spaceObject.update(delta);
        }
        for (SpaceObject spaceObject : spaceObjects) {
            spaceObject.update(delta);
        }
        spaceObjects.sort();
    }

    @Override
    public void dispose() {

    }

    private SpaceBody createBody(int velDir, int size, CollisionLayer myLayer, CollisionLayer[] collisionLayers) {
        Pixmap pixmap = generatePlanetPixmap(1024);
        Sprite planet = new Sprite(new Texture(pixmap));
        planet.setSize(size, size);
        Vector2 position = new Vector2(CENTER_X - size / 2, (CENTER_Y + size / 2));
        position.y = Gdx.graphics.getHeight() + 150;
        planet.setPosition(position.x, position.y);
        SpaceBody body = new SpaceBody(planet, velDir);
        body.setPosition(new Vector2(position));
        body.setCollider(position, size / 2);
        body.getCollider().setMyCollisionLayer(myLayer);
        for (CollisionLayer layer : collisionLayers) {
            body.getCollider().getCollisionLayers().add(layer);
        }
        body.getCollider().isEnabled = true;
        spaceObjects.add(body);
        return body;
    }

    public static Pixmap generatePlanetPixmap(int size) {
        if (pixmap == null) {
            float[][] generated = NoiseGenerator.GenerateWhiteNoise(size, size);
            generated = NoiseGenerator.GeneratePerlinNoise(generated, 8);
            pixmap = new Pixmap(generated.length, generated.length, Pixmap.Format.RGBA8888);
            for (int x = 0; x < generated.length; x++) {
                for (int y = 0; y < generated.length; y++) {
                    double value = generated[x][y];

                    if (value < 0.40f) {
                        // Deep ocean
                        pixmap.drawPixel(x, y, Color.rgba8888(47f / 255f, 86f / 255f, 118f / 255f, 1f));
                    } else if (value < 0.55f) {
                        // Ocean
                        pixmap.drawPixel(x, y, Color.rgba8888(62f / 255f, 120f / 255f, 160f / 255f, 1f));
                    } else {
                        // Land
                        pixmap.drawPixel(x, y, Color.rgba8888(146f / 255f, 209f / 255f, 135f / 255f, 1f));
                    }
                }
            }
        }
        return pixmap;
    }

    public void clear() {
        for (SpaceObject object : spaceObjects) {
            object.getSprite().getTexture().dispose();
        }
        spaceObjects.clear();
        stars.clear();
    }

    public ObjectGenerator getObjectGenerator() {
        return objectGenerator;
    }

    public void removeObjects(Array<? extends SpaceObject> objects) {
        spaceObjects.removeAll(objects, false);
    }

    public void removeObject(SpaceObject object) {
        spaceObjects.removeValue(object, false);
    }

    public Array<Star> getStars() {
        return stars;
    }

    public void addStar(Star star) {
        stars.add(star);
    }

    public void removeStar(Star star) {
        stars.removeValue(star, false);
    }

    @Override
    public void drawSprite(Batch batch) {
        for (Star star : getStars()) {
            star.drawSprite(batch);
        }
        for (int i = 0; i < spaceObjects.size; i++) {
            spaceObjects.get(i).drawSprite(batch);
        }
    }

    @Override
    public void drawShape(ShapeRenderer shapeRenderer) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isBackground() {
        return true;
    }

}
