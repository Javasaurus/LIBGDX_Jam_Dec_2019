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
package be.codecuisine.game.enemies.ships.impl.dev;

import be.codecuisine.game.ai.impl.WaypointPatroller;
import be.codecuisine.game.enemies.ships.BaseEnemy;
import be.codecuisine.graphics.rendering.RenderingPool;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kenne
 */
public class SimpleWaypointEnemy extends BaseEnemy {

    private static final Texture spriteSheet = new Texture(Gdx.files.internal("sprites/ships/enemy_02.png"));
    private static final TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 4, spriteSheet.getHeight());
    private final Animation<TextureRegion> animation;

    private float playBackSpeed = .6f;
    private final WaypointPatroller waypointPatroller;

    public SimpleWaypointEnemy(Vector2 position, float sizeX, float sizeY, int shape) {
        super(sizeX, sizeY);
        this.shipPosition = position;
        RenderingPool.INSTANCE.add(this);
        TextureRegion[] frames = new TextureRegion[tmp.length * tmp[0].length];
        int index = 0;
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        animation = new Animation<TextureRegion>(0.025f, frames);
        waypointPatroller = new WaypointPatroller(this);

        //generate points in a circle ...
        switch (shape) {
            case 0:
                //CIRCLE
                waypointPatroller.setWayPoints(CreateCircle());
                break;
            case 1:
                //TRIANGLE_TOP
                waypointPatroller.setWayPoints(CreateTopTriangle());
                break;
            case 2:
                //TRIANGLE_BOTTOM
                waypointPatroller.setWayPoints(CreateBottomTriangle());
                break;
            default:
                waypointPatroller.setWayPoints(CreateCircle());
                break;
        }

        this.ai.add(waypointPatroller);

    }

    private List<Vector2> CreateCircle() {
        List<Vector2> wayPoints = new ArrayList<Vector2>();
        int points = 36;
        double angle = (360 / points) * MathUtils.degreesToRadians;
        for (int i = 0; i <= points; i++) {

            wayPoints.add(new Vector2(
                    (float) ((Gdx.graphics.getWidth() / 2) + (MathUtils.cos((float) (i * angle)) * 400)),
                    (float) ((Gdx.graphics.getHeight() / 2) + (MathUtils.sin((float) (i * angle)) * 400))
            )
            );
        }
        return wayPoints;
    }

    private List<Vector2> CreateTopTriangle() {
        List<Vector2> wayPoints = new ArrayList<Vector2>();
        //Top
        wayPoints.add(new Vector2(
                Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight())
        );

        wayPoints.add(new Vector2(
                0, 0)
        );

        wayPoints.add(new Vector2(
                Gdx.graphics.getWidth(), 0)
        );

        return wayPoints;
    }

    private List<Vector2> CreateBottomTriangle() {
        List<Vector2> wayPoints = new ArrayList<Vector2>();
        //Top
        wayPoints.add(new Vector2(
                Gdx.graphics.getWidth() / 2, 0)
        );

        wayPoints.add(new Vector2(
                0, Gdx.graphics.getHeight())
        );

        wayPoints.add(new Vector2(
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        );

        return wayPoints;
    }

    @Override
    public TextureRegion getTextureToDraw() {

        if (isDestroyed) {
            return null;
        }
        return (TextureRegion) animation.getKeyFrame(totalTimeAlive * playBackSpeed, true);
    }

    @Override
    public void drawShape(ShapeRenderer renderer) {
        for (Vector2 waypoint : waypointPatroller.getWayPoints()) {
            renderer.circle(waypoint.x, waypoint.y, 3f);
        }
    }

    @Override
    public int GetPoints() {
        return 100;
    }

}
