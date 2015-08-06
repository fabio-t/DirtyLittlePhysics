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
package maps.cells;

import maps.Cell;
import shapes.Box;
import utils.ImmutableVect3D;
import utils.Maths;
import utils.Vect3D;
import collision.Collider;
import engine.Particle;
import engine.Simulator;

/**
 * 
 * @author Fabio Ticconi
 */
public class SolidCell extends Box implements Cell
{
    private final ImmutableVect3D hplane;
    private final double          ud;

    public SolidCell()
    {
        super(new Vect3D(), new Vect3D());

        hplane = new ImmutableVect3D(1.0, 1.0, 0.0);
        ud = 0.4; // dynamic friction
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
        if (Collider.test(oldpos, this))
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
        else if (Collider.test(pos, this))
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

            // generate a sort of "impulse vector", but a force, not a velocity
            final Vect3D contactForce = Maths.contactForce(p.getVelocity(), normal, p.getBounciness(), p.getMass());

            // ########################################
            // add force (will generate "impuse" in simulator)
            // ########################################
            // the impulse vector is divided
            // by the delta t (ie, the force is applied during the
            // whole simulated time step)
            contactForce.div(dt); // apply force for the whole time step

            // move the particle to a point very close to
            // to the intersection point, but outside the box.
            // also, the only component we want to affect
            // is the one along the normal
            pos.set(isec.add(normal.mul(0.01)));

            // since we are in contact, we must include friction
            // with the ground.
            // we take the velocity components parallel to the ground
            // and opposite to movement, convert them to the force that generated
            // them. Now to completely stop movement we could just use ud=1, but
            // generally we want a lower dynamic friction constant
            final Vect3D frictionForce = new Vect3D(hplane).mul(p.getVelocity()).invert().mul(ud * p.getMass() / dt);

            if (Simulator.VERBOSE)
            {
                System.out.println("\n#solidcell#");
                System.out.format("vel: %s\n", p.getVelocity());
                System.out.format("contact force: %s\n", contactForce);
                System.out.format("friction force: %s\n", frictionForce);
                System.out.format("pos: %s\n", pos);
                System.out.println();
            }

            return contactForce.add(frictionForce);
        }

        return new Vect3D(ImmutableVect3D.zero);
    }

    /*
     * (non-Javadoc)
     * 
     * @see environment.Cell#getBuoyancy(engine.Particle)
     */
    @Override
    public double getBuoyancy(final Particle p)
    {
        return 1.0;
    }
}
