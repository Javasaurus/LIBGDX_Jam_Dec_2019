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
package be.codecuisine.game.glutto;

import be.codecuisine.game.collision.Collider;
import be.codecuisine.Constants;
import be.codecuisine.game.AudioManager;
import be.codecuisine.game.GameBehavior;
import be.codecuisine.game.GameLogic;
import be.codecuisine.game.Input;
import be.codecuisine.game.collision.Collidable;
import be.codecuisine.game.collision.Collision;
import be.codecuisine.game.collision.CollisionLayer;
import be.codecuisine.game.collision.impl.CompositeCircleCollider;
import be.codecuisine.game.enemies.projectile.BaseProjectile;
import be.codecuisine.graphics.planet.objects.SpaceBody;
import be.codecuisine.graphics.rendering.Renderable;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *
 * @author kenne
 */
public class GluttoHead extends Renderable implements Collidable, GameBehavior {

    public static GluttoHead INSTANCE;

    private float radius = 10;
    private int retractSpeed = 6;
    public Vector2 position;
    public Vector2 velocity;
    public float launchingSpeed = 4500;

    public boolean isDocked;

    //######################SLURPING
    private SpaceBody slurpedBody;
    public boolean isSlurping;
    private int slurpIndex;
    private float slurpTimer;
    private float slurpDelay = 5f;

    //#######################REVERTING
    public boolean isReverting;

    private CompositeCircleCollider collider;
    private final GluttoBody paddle;

    private LinkedList<Vector2> waypoints = new LinkedList<>();

    public GluttoHead(GluttoBody paddle) {
        INSTANCE = this;
        this.paddle = paddle;
        position = new Vector2();
        position.x += radius;
        position.y += radius;
        velocity = new Vector2();
        collider = new CompositeCircleCollider(this, position, radius);
        collider.setMyCollisionLayer(CollisionLayer.PLAYER);
        collider.getCollisionLayers().add(CollisionLayer.OTHER);
        collider.getCollisionLayers().add(CollisionLayer.ENEMY);
        collider.getCollisionLayers().add(CollisionLayer.PLANET);
        collider.getCollisionLayers().add(CollisionLayer.PROJECTILE);
        collider.isEnabled = true;
        isDocked = true;
    }

    public void update(float t) {
        if (isDocked) {
            MoveDocked(t);
        } else {
            UpdateSegmentPositions();
            if (isSlurping) {
                if (!Input.IsMouseButtonDown(0) && !Input.IsKeyDown(Keys.SPACE)) {
                    isSlurping = false;
                    isReverting = true;
                    MoveBackwards(t);
                } else {
                    MoveSlurping(t);
                }
            } else {
                if (!isReverting) {
                    MoveForward(t);
                } else {
                    MoveBackwards(t);
                }
                float tmpY = position.y;
                Vector2 tmp = new Vector2(position).lerp(paddle.getCenterPosition(0), t);
                position.x = tmp.x;
                position.y = tmpY;
            }

        }

        collider.getPosition().x = position.x;
        collider.getPosition().y = position.y;

    }

    private void UpdateSegmentPositions() {
        collider.Clear();
        waypoints.clear();
        //get the paddle's center position
        Vector2 dock = paddle.getCenterPosition(radius * 2.5f);
        //calculate a random mid position 
        Vector2 midPosition = new Vector2(this.position).add(dock).scl(.5f);
        midPosition.x += (dock.x > this.position.x ? +radius : -radius) * (Math.abs(dock.x - this.position.x) / 5);
        Bezier<Vector2> bezier = new Bezier<Vector2>(dock, midPosition, this.position);

        LinkedHashMap<Vector2, Float> segments = new LinkedHashMap<>();

        int count = (int) (bezier.approxLength(100) / radius);

        //   waypoints.add(dock);
        for (int i = 0; i < count; i++) {
            float ratio = (float) i / count;
            Vector2 tmp = bezier.valueAt(new Vector2(), ratio);
            waypoints.add(tmp);
            segments.put(tmp, ratio * radius);
        }
        //    waypoints.add(position);
        if (!isSlurping) {
            collider.setPositionsToCheck(segments);
        }
    }

    private void MoveDocked(float t) {
        position = paddle.getCenterPosition((float) (2.5f * radius));
        if (Input.IsKeyDown(Keys.SPACE) || Input.IsMouseButtonDown(0)) {
            isDocked = false;
            isReverting = false;
            velocity.y = launchingSpeed;
        }
    }

    private void MoveSlurping(float t) {

        if (slurpedBody != null && !slurpedBody.isDestroyed) {
            slurpedBody.hit(t * 50);
            GluttoStats.DMG_GLUTTO(-1);
            AudioManager.PlaySound(AudioManager.SUCK);
        } else {
            slurpedBody = null;
            isSlurping = false;
            isReverting = true;
        }
    }

    private void MoveForward(float t) {

        //collisions with the borders
        if (position.x - radius <= 0 || position.x + radius >= Constants.WIDTH) {
            velocity.x *= -1;
        }

        if (position.y + radius >= Constants.HEIGHT) {
            revert();
            velocity.y *= -1;
        }

        position.x += velocity.x * t;
        position.y += velocity.y * t;

    }

    private void MoveBackwards(float t) {
        //move backwards through the positions recorded...
        if (waypoints.isEmpty() || new Vector2(position).dst(paddle.getCenterPosition(2.5f * radius)) < radius) {
            velocity.x = 0;
            velocity.y = 0;
            isReverting = false;
            isDocked = true;
            isSlurping = false;
        } else {
            //probably want to skip some to increase the speed
            Vector2 lastPosition = waypoints.getFirst();
            for (int i = 1; i < retractSpeed; i++) {
                if (waypoints.peek() == null) {
                    break;
                }
                lastPosition = waypoints.pollLast();
            }
            position.x = lastPosition.x;
            position.y = lastPosition.y;
        }

    }

    public void revert() {
        isReverting = true;
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public void onCollisionEnter(Collision collision) {
        if (!isDocked) {
            velocity.x = 0;
            velocity.y = 0;
            Collider collider = collision.getOther(this).getCollider();
            switch (collider.getMyCollisionLayer()) {
                case PLAYER:
                    if (collider.getCollidable() instanceof GluttoBody) {
                        isDocked = true;
                    }
                    break;
                case ENEMY:
                    revert();
                    break;
                case PROJECTILE:
                    revert();
                    BaseProjectile proj = (BaseProjectile) collision.getOther(this);
                    proj.die(true);
                    if (isDocked) {
                        GluttoStats.DMG_GLUTTO(proj.getDamage());
                    }
                    break;
                case PLANET:
                    //ACTIVATE SUCTION EVENT
                    if (!isSlurping) {
                        isSlurping = true;
                        slurpedBody = (SpaceBody) collision.getOther(this);
                    }
                    break;
            }
        }
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void drawShape(ShapeRenderer r) {
        if (GluttoStats.GLUTTO_HEALTH() <= 0) {
            return;
        }
        r.set(ShapeRenderer.ShapeType.Filled);
        //   r.setColor(Color.BLACK);
        // r.circle(position.x, position.y, 1.2f * radius);
        r.setColor(Color.LIME);
        r.circle(position.x, position.y, 1.1f * radius);

        int i = 0;
        slurpIndex = isSlurping ? slurpIndex - 1 : 0;
        if (slurpIndex < 0) {
            slurpIndex = waypoints.size();
        }
        for (Vector2 waypoint : waypoints) {
            i++;
            float tmp = radius * 1.27f * (1 - (float) i / waypoints.size());
            if (isSlurping) {
                for (int j = 0; j < 10; j++) {
                    if (i == slurpIndex + j) {
                        tmp *= Math.abs(5 - j / 2);
                        break;
                    }
                }
            }
            //   r.setColor(Color.BLACK);
            // r.circle(waypoint.x, waypoint.y, 1.2f * tmp);
            // r.setColor(Color.LIME);
            r.circle(waypoint.x, waypoint.y, 1.1f * tmp);
        }

    }

    @Override
    public void drawSprite(Batch batch) {
        //
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getSpeed() {
        return launchingSpeed;
    }

    @Override
    public float getHP() {
        return -1;
    }

    @Override
    public boolean isBackground() {
        return false;
    }

    @Override
    public Integer getZ() {
        return 198;
    }

}
