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
package engine;

import utils.Vect3D;
import environment.World;

/**
 * 
 * @author Fabio Ticconi
 */
public class VerletVelocity
{
    private final Vect3D acc = new Vect3D();

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
    public void update(final double dt, final World world, final Particle... particles)
    {
        // TODO: if particles will begin interacting with each other (attractors for example), this will need some
        // modifications (updating all positions before recalculating new accelerations?)

        // halve the delta t, to save
        // a few divisions
        final double dt2 = dt / 2.0;

        Particle p;
        Vect3D force;
        Vect3D vel;
        Vect3D pos;
        for (int i = 0; i < particles.length; i++)
        {
            p = particles[i];
            vel = p.vel;

            // newpos will be updated in the following code,
            // and thus update the actual particle position
            pos = p.getCenter();
            // save current position as "previous", before we
            // modify anything
            p.oldCenter.set(pos);

            force = world.getForces(p, dt);

            // calculate acceleration from the accumulated forces
            acc.x = (force.x * p.invmass);
            acc.y = (force.y * p.invmass);
            acc.z = (force.z * p.invmass);

            pos.x += dt * (vel.x + (dt2 * acc.x));
            pos.y += dt * (vel.y + (dt2 * acc.y));
            pos.z += dt * (vel.z + (dt2 * acc.z));

            vel.x += dt * acc.x;
            vel.y += dt * acc.y;
            vel.z += dt * acc.z;

            force = world.getForces(p, dt);

            acc.x = -acc.x + (force.x * p.invmass);
            acc.y = -acc.y + (force.y * p.invmass);
            acc.z = -acc.z + (force.z * p.invmass);

            vel.x += dt2 * acc.x;
            vel.y += dt2 * acc.y;
            vel.z += dt2 * acc.z;
        }
    }
}
