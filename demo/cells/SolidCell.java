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

            // reflect the velocity vector symmetrically
            // to the normal, then scale it down with the bounciness
            final Vect3D vel = p.getVelocity();
            final Vect3D n = new Vect3D(normal);

            // COLLISION RESPONSE METHODS //

            // ########################################
            // 1) penalty-based (spring damper)
            // ########################################
            // model. K is based on experimentally found results
            // of ball-bat reactions. They found that the coefficient of
            // restitution is proportional to that stifness value.
            // http://baseball.physics.illinois.edu/CORNormalization.pdf for
            // the e-K linear relationship (although it's not the same
            // coefficient of restitution as ours..),
            // https://repositorium.sdum.uminho.pt/bitstream/1822/8883/1/ARI-05-2006.pdf
            // and http://www2.msm.ctw.utwente.nl/sluding/PAPERS/luding_alert2008.pdf
            // and http://arxiv.org/pdf/physics/0601168.pdf
            // for deriving the actual force based on K/e (ie,
            // that it needs to be multiplied by mass and by the
            // penetration length
            // NOTE: this consider the penetration length equal to the space
            // the particle WOULD HAVE traveled inside the ground
            // this is not technically correct but a good approximation for now.
            // Also, most paper use this:
            // F = K*delta + damping*v
            // but we don't need damping here, because it's naturally applied
            // in the simulator in form of drag, gravity and buoyancy.
            // n.mul(Vect3D.sub(isec, pos).length() * p.getBounciness() * 300 * p.getMass());
            // n.add(Vect3D.mul(normal, Vect3D.dot(normal, vel)));
            // n.div(dt);

            // ########################################
            // 2) impulse-based collision resolution
            // ########################################
            // taken from: http://chrishecker.com/images/e/e7/Gdmphys3.pdf
            // and various other sources.
            final double j = (Vect3D.dot(normal, vel) * -(1.0 + p.getBounciness())) / p.getInvMass();
            n.mul(j); // impulse vector

            // ########################################
            // 2.a) add force (will generate impuse in simulator)
            // ########################################
            // the impulse vector is divided
            // by the delta t (ie, the force is applied during the
            // whole simulated time step)
            // NOTE: this doesn't seem to work well,
            // it seems like j is too small
            n.div(dt); // apply force for the whole time step

            // ########################################
            // 2.b) change velocity
            // ########################################
            // the old velocity is added to the impulse vector
            // divided by the mass.
            // we are in fact calculating a new acceleration
            // in-line here
            // NOTE: this works well, but changing the velocity at this point
            // is very hacky
            // vel.add(n.mul(p.getInvMass()));

            // COLLISION RESPONSE METHODS END //

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
            final Vect3D fForce = new Vect3D(hplane).mul(vel).invert().mul(ud * p.getMass() / dt);

            if (Simulator.VERBOSE)
            {
                System.out.println("\n#solidcell#");
                System.out.format("j: %f\n", j);
                System.out.format("vel: %s\n", vel);
                System.out.format("contact force: %s\n", n);
                System.out.format("friction force: %s\n", fForce);
                System.out.format("pos: %s\n", pos);
                System.out.println();
            }

            return n.add(fForce);
        }

        return new Vect3D(ImmutableVect3D.zero);
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
