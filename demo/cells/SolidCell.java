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
package cells;

import map.Cell;
import shapes.Box;
import utils.ImmutableVect3D;
import utils.Vect3D;
import collision.Collider;
import engine.Particle;
import engine.Simulator;

/**
 * 
 * @author Fabio Ticconi
 */
public class SolidCell implements Cell, Box
{
    private final Vect3D min;
    private final Vect3D max;

    public SolidCell()
    {
        min = new Vect3D();
        max = new Vect3D();
    }

    /*
     * (non-Javadoc)
     * 
     * @see engine.Simulator.Cell#getForces(engine.Particle)
     */
    @Override
    public Vect3D getForces(final Particle p, final double dt)
    {
        final Vect3D oldpos = p.getOldCenter();
        final Vect3D pos = p.getCenter();

        // if we are within the ground, we can't move at all
        if (Collider.testPointBox(oldpos, this))
        {
            // remove the velocity completely, too
            // this is generally not advised, but in this
            // case we want to impose absolute immobility
            p.setVelocity(ImmutableVect3D.zero);
            pos.set(oldpos);

            return new Vect3D(ImmutableVect3D.zero);
        }
        // if we are outside but we want to move inside the ground,
        // we should move away a bit in the correct direction
        else if (Collider.testPointBox(pos, this))
        {
            final Vect3D isec = new Vect3D();
            final Vect3D normal = new Vect3D();

            final Vect3D direction = Vect3D.sub(pos, oldpos).normalise();

            final double t = Collider.intersectRayBox(oldpos, direction, this, isec, normal);

            if (t == 0.0)
            {
                System.out.println("ERROR: there should be a collision");

                return new Vect3D(ImmutableVect3D.zero);
            }

            // reflect the velocity vector symmetrically
            // to the normal, then scale it down with the bounciness
            final Vect3D vel = p.getVelocity();
            final Vect3D n = new Vect3D(normal);
            final double j = (Vect3D.dot(normal, vel) * -(1.0 + p.getBounciness())) / p.getInvMass();
            n.mul(j); // impulse vector
            // vel.set(0.0);

            vel.add(n.mul(p.getInvMass()));

            // move the particle to a point very close to
            // to the intersection point, but outside the box.
            // also, the only component we want to affect
            // is the one along the normal
            pos.set(isec.add(new Vect3D(normal).mul(0.0000001)));

            if (Simulator.VERBOSE)
            {
                System.out.println();
                System.out.println(j);
                System.out.println(vel);
                System.out.println(n);
                System.out.println(pos);
                System.out.println();
            }

            // return n;
        }

        return new Vect3D(ImmutableVect3D.zero);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Box#getMinPoint()
     */
    @Override
    public Vect3D getMinPoint()
    {
        return min;
    }

    public void setMinPoint(final Vect3D v)
    {
        min.set(v);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Box#getMaxPoint()
     */
    @Override
    public Vect3D getMaxPoint()
    {
        return max;
    }

    public void setMaxPoint(final Vect3D v)
    {
        max.set(v);
    }

    /*
     * (non-Javadoc)
     * 
     * @see map.Cell#getBuoyancy(engine.Particle)
     */
    @Override
    public double getBuoyancy(final Particle p)
    {
        return 1.0;
    }
}
