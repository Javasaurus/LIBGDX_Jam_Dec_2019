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
package be.codecuisine.graphics.planet.objects;

import be.codecuisine.game.collision.Collidable;
import be.codecuisine.game.collision.Collider;
import be.codecuisine.game.collision.ColliderPool;
import be.codecuisine.game.collision.Collision;
import be.codecuisine.game.collision.CollisionLayer;
import be.codecuisine.game.collision.impl.CircleCollider;
import be.codecuisine.graphics.Shaders;
import be.codecuisine.graphics.rendering.RenderingPool;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class SpaceBody extends SpaceObject implements Disposable, Collidable {

    private ShaderProgram planetShader;
    private float time;
    private float rotationSpeed;
    private float radius;
    private float direction;
    private String texture;
    private Collider collider;

    private float health = 100;
    private float currentHealth = 100;
    public boolean isDestroyed;

    private SpaceBody() {
        this(new Sprite(), 0);
    }

    public SpaceBody(Sprite sprite, int direction) {
        super(sprite);

        this.direction = direction;

        rotationSpeed = 1 / 50f;
        radius = sprite.getWidth() / 2;

        planetShader = Shaders.PLANET_SHADER;

    }

    public void enableCollider() {
        if (collider != null) {
            collider.isEnabled = true;
        }
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    public void setCollider(Vector2 position, float radius) {
        Collider tmp = new CircleCollider(this, position, radius);
        this.collider = tmp;
    }

    public void hit(float dmg) {
        //isBeingSlurped...
        currentHealth -= dmg;
        if (currentHealth <= 0) {
            isDestroyed = true;
        }
    }

    @Override
    public void update(float delta) {
        time += rotationSpeed * delta;

        getSprite().setPosition(getPosition().x, getPosition().y);
        // If the code below this comment is moved to update, it causes graphic issues with orbiting objects
        planetShader.begin();
        planetShader.setUniformf("time", direction * time);
        //planetShader.setUniformf("flash", flashingTimer > 0 ? 1 : 0);
        planetShader.setUniformf("grayness", 1 - (float) (currentHealth / health));
        planetShader.end();

        getCollider().getPosition().x = getPosition().x + getRadius();
        getCollider().getPosition().y = getPosition().y + getRadius();
        enableCollider();
    }

    public float getWidthAtY(float y) {
        return (float) Math.sqrt(radius * radius - y * y);
    }

    public float getMinimumCloudRadiusAtY(float y) {
        return getWidthAtY(y) + 6;
    }

    public String getTextureString() {
        return texture;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void dispose() {
        super.dispose();
        ColliderPool.INSTANCE.remove(this);
        RenderingPool.INSTANCE.remove(this);
    }

    @Override
    public void onCollisionEnter(Collision collision) {
        if (collision.getOther(this).getCollider().getMyCollisionLayer() == CollisionLayer.PLAYER) {
            //don't really need to do anything?
        }
    }

    @Override
    public void drawSprite(Batch batch) {
        batch.setShader(planetShader);
        super.drawSprite(batch);
        batch.setShader(null);
    }

    @Override
    public void drawShape(ShapeRenderer shapeRenderer) {
        //       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isBackground() {
        return true;
    }

}
