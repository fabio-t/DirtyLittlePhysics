/**
 * Copyright 2014 Fabio Ticconi
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

package engine;

import java.util.Arrays;
import java.util.List;

import shapes.Box;
import shapes.Shape;
import utils.ImmutableVect3D;
import utils.Maths;
import utils.Vect3D;
import collision.BroadPhase;
import collision.Collider;
import environment.World;

/**
 * Entry Vect3D for the engine. Simulates the movement of all
 * added particles by taking into consideration the properties of the
 * cell they are into. It uses Verlet Integration for fast and accurate simulation
 * of even velocity-dependent forces (like drag).
 * 
 * @author Fabio Ticconi
 */
public class Simulator
{
    public static final boolean VERBOSE       = false;

    private World               world;

    private BroadPhase          collider;

    // initial maximum number of particles, used to
    // initialise the array
    public int                  MAX_PARTICLES = 1000;

    int                         NUM_OF_PARTICLES;
    Particle                    particles[];

    public Simulator(final World world, final BroadPhase collider)
    {
        if (world == null)
            throw new IllegalArgumentException("A world must be defined!");

        if (collider == null)
            throw new IllegalArgumentException("A collider must be defined!");

        this.world = world;
        this.collider = collider;

        particles = new Particle[MAX_PARTICLES];
        NUM_OF_PARTICLES = 0;
    }

    public Simulator setWorld(final World world)
    {
        this.world = world;

        return this;
    }

    public Simulator setCollider(final BroadPhase collider)
    {
        this.collider = collider;

        return this;
    }

    /**
     * Returns the number of currently active
     * particles.
     * 
     * @return
     */
    public int getParticlesNumber()
    {
        return NUM_OF_PARTICLES;
    }

    /**
     * Adds a new particle to the simulator.<br />
     * O(1)
     * 
     * @param p
     *            {@link Particle} to be added
     */
    public void addParticle(final Particle p)
    {
        if (p == null)
            return;

        if (NUM_OF_PARTICLES >= MAX_PARTICLES)
        {
            MAX_PARTICLES *= 2;

            // TODO: check heap exception
            particles = Arrays.copyOf(particles, MAX_PARTICLES);
        }

        particles[NUM_OF_PARTICLES++] = p;
    }

    /**
     * Removes a particle from the simulator,
     * if it was there. <br />
     * O(N)
     * 
     * @param p
     *            {@link Particle} to be removed
     */
    public void removeParticle(final Particle p)
    {
        if (p == null)
            return;

        for (int i = 0; i < NUM_OF_PARTICLES; i++)
            if (particles[i] == p)
            {
                particles[i] = particles[--NUM_OF_PARTICLES];
                break;
            }
    }

    /**
     * Removes all particles from the simulator. <br />
     * O(1)
     */
    public void clearParticles()
    {
        NUM_OF_PARTICLES = 0;
    }

    /**
     * <p>
     * Verlet Velocity integration method, slightly modified to take into account forces dependent on velocity (eg,
     * fluid drag). Reference (particle physics paper):
     * http://pages.csam.montclair.edu/~yecko/ferro/papers/FerroDPD/GrootWarren_ReviewDPD_97.pdf
     * </p>
     * 
     * <p>
     * However, as also described here: http://gamedev.stackexchange.com/a/41917/51181 we used a lambda of 1 and
     * re-arranged a bit to reduce the number of divisions and multiplications. Semantically is the same as the above
     * paper.
     * </p>
     * 
     * @param dt
     *            how much to advance the simulation of
     */
    public void update(final double dt)
    {
        // TODO: if particles will begin interacting with each other (attractors for example), this will need some
        // modifications (updating all positions before recalculating new accelerations?)

        // halve the delta t, to save
        // a few divisions
        final double dt2 = dt / 2.0;

        List<Shape> collisions;
        final Vect3D isec = new Vect3D();
        final Vect3D normal = new Vect3D();
        final Vect3D direction = new Vect3D();

        Particle p;
        Vect3D acc;
        Vect3D force;
        Vect3D vel;
        Vect3D oldpos;
        Vect3D newpos;
        for (int i = 0; i < NUM_OF_PARTICLES; i++)
        {
            p = particles[i];

            // reset the acceleration vector
            acc = p.acc.set(ImmutableVect3D.zero);
            // reset the force vector
            force = p.force.set(ImmutableVect3D.zero);

            vel = p.vel;
            // we save the current position
            oldpos = p.oldCenter.set(p.getCenter());
            // newpos will be updated in the following code,
            // and thus update the actual particle position
            newpos = p.getCenter();

            if (VERBOSE)
                System.out.println("\n#simulator#\npre: " + p);

            // applies space-dependent correction of position.
            // for example, if the world is non-toroidal it clamps
            // newpos to be just at the border, if it was over it.
            // Conversely, if toroidal it moves the particle to the
            // right side
            world.process(p, dt2);

            if (VERBOSE)
                System.out.println("processed: " + p);

            collisions = collider.getCollisions(newpos);

            if (collisions.size() > 0)
            {
                System.out.println(p);
                System.out.println("colliding with " + collisions.size() + " objects");
                for (final Shape obj : collisions)
                {
                    direction.set(vel).normalise();

                    Collider.intersectRayBox(oldpos, direction, (Box) obj, isec, normal);

                    // generate a sort of "impulse vector", but a force, not a velocity
                    force.add(Maths.contactForce(vel, normal, p.bounciness, p.mass).div(dt2));

                    newpos.set(isec.add(normal.mul(0.01)));

                    force.add(new Vect3D(1.0, 1.0, 0.0).mul(vel).invert().mul(0.4 * p.mass / dt2));
                }
            }

            // calculate acceleration from the accumulated forces
            acc.x = (force.x * p.invmass);
            acc.y = (force.y * p.invmass);
            acc.z = (force.z * p.invmass);

            if (VERBOSE)
                System.out.println("acc: " + acc);

            newpos.x += dt * (vel.x + (dt2 * acc.x));
            newpos.y += dt * (vel.y + (dt2 * acc.y));
            newpos.z += dt * (vel.z + (dt2 * acc.z));

            if (VERBOSE)
                System.out.println("newpos: " + newpos);

            force.set(ImmutableVect3D.zero);

            world.process(p, dt);

            collisions = collider.getCollisions(newpos);

            if (collisions.size() > 0)
            {
                System.out.println(p);
                System.out.println("colliding with " + collisions.size() + " objects");
                for (final Shape obj : collisions)
                {
                    direction.set(vel).normalise();

                    Collider.intersectRayBox(oldpos, direction, (Box) obj, isec, normal);

                    // generate a sort of "impulse vector", but a force, not a velocity
                    force.add(Maths.contactForce(vel, normal, p.bounciness, p.mass).div(dt));

                    newpos.set(isec.add(normal.mul(0.01)));

                    force.add(new Vect3D(1.0, 1.0, 0.0).mul(vel).invert().mul(0.4 * p.mass / dt));
                }
            }

            if (VERBOSE)
                System.out.println("processed: " + newpos);

            vel.x += dt * acc.x;
            vel.y += dt * acc.y;
            vel.z += dt * acc.z;

            if (VERBOSE)
                System.out.println("vel: " + vel);

            acc.x = -acc.x + (force.x * p.invmass);
            acc.y = -acc.y + (force.y * p.invmass);
            acc.z = -acc.z + (force.z * p.invmass);

            if (VERBOSE)
                System.out.println("acc: " + acc);

            vel.x += dt2 * acc.x;
            vel.y += dt2 * acc.y;
            vel.z += dt2 * acc.z;

            if (VERBOSE)
                System.out.println("post: " + p);
        }
    }
}
