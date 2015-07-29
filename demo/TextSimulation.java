import map.Map;
import maps.SimpleMap;
import utils.ImmutableVect3D;
import utils.Vect3D;
import colliders.ArrayGrid2D;
import collision.ICollider;
import engine.Particle;
import engine.Simulator;

/**
 * Copyright 2015 Fabio Ticconi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 * @author Fabio Ticconi
 */
public class TextSimulation
{
    static int x_min, x_max, y_min, y_max, z_min, z_max;

    public static void loop(final Simulator simulator, final long maxDurationInMillis, final int FPS)
    {
        final double frameDuration = 1000.0 / FPS;
        final double dt = frameDuration / 1000.0;

        System.out.println(String.format("FPS: %d, frameDuration: %f, dt: %f", FPS, frameDuration, dt));

        long previousTime = System.currentTimeMillis();
        long currentTime;

        double lag = 0.0;
        long elapsed;

        final long start = System.nanoTime();
        final long endCondition = start + maxDurationInMillis * 1000000l;
        while (endCondition > System.nanoTime())
        {
            currentTime = System.currentTimeMillis();
            elapsed = currentTime - previousTime;
            previousTime = currentTime;

            if (elapsed > 1000)
                System.out.println("falling behind");

            lag += elapsed;

            while (lag >= frameDuration)
            {
                simulator.update(dt);

                lag -= frameDuration;
            }
        }
        final long end = System.nanoTime();

        System.out.println(String.format("Simulated %d ms in %f ms. Lag: %f ms\n", maxDurationInMillis,
                                         (end - start) / 1000000.0, lag));
    }

    public static void addParticles(final Simulator simulator, final int particles)
    {
        for (int i = 0; i < particles; i++)
        {
            final Particle p = new Particle();
            p.setRadius(Math.random() / 2.0 + 0.1);
            p.setMass(Math.random() * 100.0 + 50.0);
            p.setCenter(new Vect3D(Math.random() * x_max * 2 - x_min, Math.random() * y_max * 2 - y_min, Math.random() *
                                                                                                         z_max *
                                                                                                         2 -
                                                                                                         z_min));
            p.setVelocity(new ImmutableVect3D(Math.random() * 50 - 25, 0.0, 0.0));
            simulator.addParticle(p);
        }

        System.out.println(String.format("%d particles added, total: %d\n", particles, simulator.getParticlesNumber()));
    }

    public static void main(final String[] args)
    {
        x_min = y_min = z_min = -1000;
        x_max = y_max = z_max = 1000;

        final Map map = new SimpleMap(x_min, x_max, y_min, y_max, z_min, z_max);
        final ICollider collider = new ArrayGrid2D((short) x_min, (short) x_max, (short) y_min, (short) y_max,
                                                   (short) 10);
        final Simulator simulator = new Simulator(map, collider);

        System.out.println("Creating world..");

        System.out.println("\n#####################\n");

        addParticles(simulator, 50000);

        final int FPS = 60;

        // 1 second
        loop(simulator, 1000, FPS);

        // 5 seconds
        loop(simulator, 5000, FPS);

        System.out.println("#####################\n");

        addParticles(simulator, 50000);

        // 1 second
        loop(simulator, 1000, FPS);

        // 5 seconds
        loop(simulator, 5000, FPS);

        System.out.println("#####################\n");

        addParticles(simulator, 50000);

        // 1 second
        loop(simulator, 1000, FPS);

        // 5 seconds
        loop(simulator, 5000, FPS);

        System.out.println("#####################\n");

        addParticles(simulator, 50000);

        // 1 second
        loop(simulator, 1000, FPS);

        // 5 seconds
        loop(simulator, 5000, FPS);

        System.out.println("#####################\n");

        addParticles(simulator, 50000);

        // 1 second
        loop(simulator, 1000, FPS);

        // 5 seconds
        loop(simulator, 5000, FPS);
    }
}
