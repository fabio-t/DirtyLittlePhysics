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

import map.Cell;
import map.Map;
import utils.Vect3D;

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

    private final Map           world;

    // initial maximum number of particles, used to
    // initialise the array
    public int                  MAX_PARTICLES = 1000;

    private Vect3D              gravity;

    int                         NUM_OF_PARTICLES;
    Particle                    particles[];

    public Simulator(final Map world)
    {
        if (world == null)
            throw new IllegalArgumentException("A world must be defined!");

        this.world = world;

        particles = new Particle[MAX_PARTICLES];
        NUM_OF_PARTICLES = 0;

        setGravity(new Vect3D(0d, 0d, -9.81d));
    }

    /**
     * Sets the gravity as an <b>acceleration</b>
     * vector.<br />
     * Example: for Earth, the vector would be (0, 0, -9.81).
     * Incidentally, this is also the default value is
     * this method is not called.
     * 
     * @param gravity
     */
    public void setGravity(final Vect3D gravity)
    {
        this.gravity = gravity;
    }

    /**
     * Returns a copy of the gravity vector.
     * 
     * @return
     */
    public Vect3D getGravity()
    {
        return new Vect3D(gravity);
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

        final Vect3D netGravity = new Vect3D();
        final Vect3D acc = new Vect3D();
        double buoyancy;

        // halve the delta t, to save
        // a few divisions
        final double dt2 = dt / 2.0d;

        final Vect3D newpos = new Vect3D();

        Particle p;
        Vect3D force;
        Vect3D vel;
        Vect3D oldpos;
        Cell cell;
        for (int i = 0; i < NUM_OF_PARTICLES; i++)
        {
            p = particles[i];

            // oldpos is a reference to the actual
            // particle center
            oldpos = p.center;
            // newpos is a temporary vector, we copy
            // the current position inside
            // and perform the updates with it
            newpos.assign(oldpos);

            vel = p.vel;

            if (VERBOSE)
                System.out.println("pre: " + p);

            cell = world.getCell(p.center);

            // Gravity must be corrected by the buoyancy (if the cell has one).
            // Note: normally this would only make sense for the "z" dimension,
            // but who are we to limit your creativity?
            buoyancy = cell.getBuoyancy(p);
            netGravity.assign(gravity).mul(buoyancy);

            force = cell.getForces(p);

            if (VERBOSE)
                System.out.println("force: " + force);

            // as the Gravitational force is equal to m*g,
            // we can add it after the cumulative force has been
            // converted to acceleration - we store it
            // as "g", so we don't have to divide by mass here
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

            // applies space-dependent correction of position.
            // for example, if the world is non-toroidal it clamps
            // newpos to be just at the border, if it was over it.
            // it also applies collision detection and resolution
            // with static objects
            world.clampMovement(oldpos, newpos);

            if (VERBOSE)
                System.out.println("newpos clamped: " + newpos);

            oldpos.assign(newpos);

            vel.x += dt * acc.x;
            vel.y += dt * acc.y;
            vel.z += dt * acc.z;

            if (VERBOSE)
                System.out.println("vel: " + vel);

            cell = world.getCell(newpos);
            buoyancy = cell.getBuoyancy(p);
            netGravity.assign(gravity).mul(buoyancy);
            force = cell.getForces(p);

            acc.x = -acc.x + (force.x * p.invmass) + netGravity.x;
            acc.y = -acc.y + (force.y * p.invmass) + netGravity.y;
            acc.z = -acc.z + (force.z * p.invmass) + netGravity.z;

            if (VERBOSE)
                System.out.println("acc: " + acc);

            vel.x += dt2 * acc.x;
            vel.y += dt2 * acc.y;
            vel.z += dt2 * acc.z;

            // FIXME: kept only for debug,
            // soon the acceleration should be removed from the Particle class
            p.acc.assign(acc);

            if (VERBOSE)
                System.out.println("post: " + p);
        }
    }
}
