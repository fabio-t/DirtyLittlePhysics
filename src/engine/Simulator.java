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

import map.Cell;
import map.Map;
import utils.ImmutableVect3D;
import utils.Vect3D;
import collision.ICollider;
import collision.StaticObject;

/**
 * Entry Vect3D for the engine. Simulates the movement of all
 * added particles by taking into consideration the properties of the
 * cell they are into. It uses Verlet Integration for fast and accurate simulation
 * of even velocity-dependent forces (like drag).
 * 
 * <br />
 * <br />
 * 
 * <b>This class is NOT Thread-safe.</b>
 * 
 * @author Fabio Ticconi
 */
public class Simulator
{
    public static final boolean VERBOSE       = false;

    private Map                 world;

    private ICollider           collider;

    // initial maximum number of particles, used to
    // initialise the array
    public int                  MAX_PARTICLES = 1000;

    int                         NUM_OF_PARTICLES;
    Particle                    particles[];

    public Simulator(final Map world, final ICollider collider)
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

    public Simulator setMap(final Map map)
    {
        world = map;

        return this;
    }

    public Simulator setStaticCollider(final ICollider collider)
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
     * fluid dragCoefficient). Reference (particle physics paper):
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

        final ImmutableVect3D gravity = world.getGravity();
        final Vect3D netGravity = new Vect3D();
        double buoyancy;

        // halve the delta t, to save
        // a few divisions
        final double dt2 = dt / 2.0;

        List<StaticObject> collisions;

        Particle p;
        Vect3D acc;
        Vect3D force;
        Vect3D vel;
        Vect3D oldpos;
        Vect3D newpos;
        Cell cell;
        for (int i = 0; i < NUM_OF_PARTICLES; i++)
        {
            p = particles[i];
            // reset the acceleration vector
            acc = p.acc.set(ImmutableVect3D.zero);
            vel = p.vel;
            // oldpos will reference the current particle position,
            // it's not modified but used often in what follows
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
            world.correctPositions(oldpos, newpos);

            if (VERBOSE)
                System.out.println("pos corrected: " + p);

            collisions = collider.getCollisions(newpos);

            if (collisions.size() > 0)
            {
                System.out.println(p);
                System.out.println("colliding with " + collisions.size() + " objects");
                for (final StaticObject b : collisions)
                    System.out.println(b);
                System.exit(1);
            }

            // the world handler gives us a Cell
            // using the current player position
            cell = world.getCell(newpos);
            // gravity must be corrected by the buoyancy (if the cell has one).
            // Note: normally this would only make sense for the "z" dimension,
            // but who are we to limit your creativity?
            buoyancy = cell.getBuoyancy(p);
            netGravity.set(gravity).mul(buoyancy);
            // many kind of environmental force
            // could be applied to the particle,
            // for example fluid drag, friction,
            // impact forces
            force = cell.getForces(p, dt2);

            if (VERBOSE)
                System.out.println("force: " + force);

            // calculate acceleration from the accumulated
            // forces.
            // as the gravitational force is equal to m*g,
            // we can add it after the cumulative force has been
            // converted to acceleration - we store it
            // as "g", so we don't have to multiply by invmass here
            acc.x = (force.x * p.invmass) + netGravity.x;
            acc.y = (force.y * p.invmass) + netGravity.y;
            acc.z = (force.z * p.invmass) + netGravity.z;

            if (VERBOSE)
                System.out.println("acc: " + acc);

            newpos.x += dt * (vel.x + (dt2 * acc.x));
            newpos.y += dt * (vel.y + (dt2 * acc.y));
            newpos.z += dt * (vel.z + (dt2 * acc.z));

            if (VERBOSE)
                System.out.println("newpos: " + newpos);

            world.correctPositions(oldpos, newpos);

            if (VERBOSE)
                System.out.println("newpos corrected: " + newpos);

            collisions = collider.getCollisions(newpos);

            if (collisions.size() > 0)
            {
                System.out.println(p);
                System.out.println("colliding with " + collisions.size() + " objects");
                for (final StaticObject b : collisions)
                    System.out.println(b);
                System.exit(1);
            }

            vel.x += dt * acc.x;
            vel.y += dt * acc.y;
            vel.z += dt * acc.z;

            if (VERBOSE)
                System.out.println("vel: " + vel);

            cell = world.getCell(newpos);
            buoyancy = cell.getBuoyancy(p);
            netGravity.set(gravity).mul(buoyancy);
            force = cell.getForces(p, dt);

            acc.x = -acc.x + (force.x * p.invmass) + netGravity.x;
            acc.y = -acc.y + (force.y * p.invmass) + netGravity.y;
            acc.z = -acc.z + (force.z * p.invmass) + netGravity.z;

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
