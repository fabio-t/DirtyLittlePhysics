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
package environment;

import utils.ImmutableVect3D;
import utils.Vect3D;
import collision.Collider;
import collision.Static;
import engine.Particle;
import engine.Simulator;

/**
 * 
 * @author Fabio Ticconi
 */
public class Forces
{
    /**
     * When a particle has both old and current position within a box,
     * we must zero the velocity because in that case no further movement is allowed:
     * the particle is effectively stuck.
     * 
     * In the more common case of the old position outside the box, if the current position
     * is within the box then we must move back along the contact normal and apply a
     * a repelling force (as well as set the position to just outside the box itself).
     * 
     * We also have to apply friction on the contact surface, if the particle is moving
     * there too.
     * 
     * @param p
     * @param box
     * @param dt
     * @param ud
     * @return
     */
    public static boolean processImpact(final Particle p, final Static box, final double dt)
    {
        final Vect3D force = p.getForce();
        final Vect3D vel = p.getVelocity();
        final Vect3D pos = p.getCenter();
        final Vect3D oldpos = p.getOldCenter();
        final double b = p.getBounciness();
        final double m = p.getMass();
        final double ud = (p.getFriction() + box.getFriction()) / 2.0;

        // if the previous position is in the box, then
        // we shouldn't do anything: we are stuck
        if (Collider.test(oldpos, box))
        {
            vel.set(ImmutableVect3D.zero);

            // we reset the position to the previous one
            pos.set(oldpos);

            return true;
        }
        // if the previous position is NOT in the box,
        // and the old position is, then we
        else if (Collider.test(pos, box))
        {
            // our movement has brought us inside a box,
            // we must move back towards the old position and stop just outside the box,
            // then add a contact force

            final Vect3D isec = new Vect3D();
            final Vect3D normal = new Vect3D();
            final Vect3D direction = new Vect3D(pos).sub(oldpos).normalise();

            // FIXME: this should be extended to other Shapes!
            Collider.intersectRayBox(oldpos, direction, box, isec, normal);

            // normal component of velocity relative to the contact surface
            final Vect3D normalComponent = new Vect3D(normal).mul(Vect3D.dot(normal, vel));
            // final Vect3D normalComponentForce = new Vect3D(normal).mul(Vect3D.dot(normal, force));
            // tangential component of velocity relative to the contact surface
            final Vect3D tangentialComponent = new Vect3D(vel).sub(normalComponent);

            // FIXME: that "times 2.0" is totally made up: without it, the force is not enough
            // to change velocity, even with bounciness=1. With this multiplication, it seems to work fine..
            final Vect3D f = new Vect3D(normalComponent).mul(Math.round(-(1.0 + b) * m)).mul(2.0);
            // FIXME: this is wrong. It should technically be:
            // f.add(new Vect3D(tangentialComponent).invert().mul(normalComponentForce).mul(ud * m));
            // but if the particle force is still zero, it won't work.
            // in general, friction should NOT be proportional to velocity (even just the tangential component)
            // but in practice, it does the job.
            f.add(new Vect3D(tangentialComponent).invert().mul(ud * m));
            f.div(dt);
            force.add(f);

            // move the particle just before the Box
            pos.set(isec.add(normal.mul(0.01)));

            // vel.set(ImmutableVect3D.zero);

            if (Simulator.VERBOSE)
            {
                System.out.println("\n#process impact#");
                System.out.format("normal comp: %s\n", normalComponent);
                System.out.format("tangential comp: %s\n", tangentialComponent);
                System.out.format("vel: %s\n", vel);
                System.out.format("impact force: %s\n", f);
                System.out.format("pos: %s\n", pos);
                System.out.println();
            }

            return true;
        }

        return false;
    }

    public static Vect3D contact(final Particle p, final Static box, final double dt)
    {
        if (Collider.test(p.getCenter(), box))
        {
            // our movement has brought us inside a box,
            // we must move back towards the old position and stop just outside the box,
            // then add a contact force

            final Vect3D force = p.getForce();
            final Vect3D vel = p.getVelocity();
            final Vect3D pos = p.getCenter();
            final Vect3D oldpos = p.getOldCenter();
            final double b = p.getBounciness();
            final double m = p.getMass();
            final double ud = (p.getFriction() + box.getFriction()) / 2.0;

            final Vect3D isec = new Vect3D();
            final Vect3D normal = new Vect3D();
            final Vect3D direction = new Vect3D(pos).sub(oldpos).normalise();

            // FIXME: this should be extended to other Shapes!
            Collider.intersectRayBox(oldpos, direction, box, isec, normal);

            // normal component of velocity relative to the contact surface
            final Vect3D normalComponent = new Vect3D(normal).mul(Vect3D.dot(normal, vel));
            // final Vect3D normalComponentForce = new Vect3D(normal).mul(Vect3D.dot(normal, force));
            // tangential component of velocity relative to the contact surface
            final Vect3D tangentialComponent = new Vect3D(vel).sub(normalComponent);

            // FIXME: that "times 2.0" is totally made up: without it, the force is not enough
            // to change velocity, even with bounciness=1. With this multiplication, it seems to work fine..
            final Vect3D f = new Vect3D(normalComponent).mul(Math.round(-(1.0 + b) * m)).mul(2.0);
            // FIXME: this is wrong. It should technically be:
            // f.add(new Vect3D(tangentialComponent).invert().mul(normalComponentForce).mul(ud * m));
            // but if the particle force is still zero, it won't work.
            // in general, friction should NOT be proportional to velocity (even just the tangential component)
            // but in practice, it does the job.
            f.add(new Vect3D(tangentialComponent).invert().mul(ud * m));
            f.div(dt);
            force.add(f);

            // move the particle just before the Box
            pos.set(isec.add(normal.mul(0.01)));

            // vel.set(ImmutableVect3D.zero);

            if (Simulator.VERBOSE)
            {
                System.out.println("\n#process impact#");
                System.out.format("normal comp: %s\n", normalComponent);
                System.out.format("tangential comp: %s\n", tangentialComponent);
                System.out.format("vel: %s\n", vel);
                System.out.format("impact force: %s\n", f);
                System.out.format("pos: %s\n", pos);
                System.out.println();
            }

            return force;
        }

        return new Vect3D(ImmutableVect3D.zero);
    }

    /**
     * Force caused by fluid pushing against a sphere. It acts in the same direction of the fluid velocity.
     * 
     * @param fluidVelocity
     * @param fluidDensity
     * @param radius
     * @return
     */
    public static Vect3D
            sphereQuadraticFlow(final Vect3D fluidVelocity, final double fluidDensity, final double radius)
    {
        final Vect3D flow = Vect3D.abs(fluidVelocity);
        flow.mul(fluidVelocity).mul(Math.PI * radius * radius * fluidDensity * 0.25);

        return flow;
    }

    /**
     * Force caused by sphere moving through a fluid. It acts in the opposite direction of the fluid velocity.
     * 
     * Implements Stoke's Law.
     * 
     * @param velocity
     * @param viscosity
     * @param radius
     * @return
     */
    public static Vect3D sphereStokeDrag(final Vect3D velocity, final double viscosity, final double radius)
    {
        return new Vect3D(velocity).mul(-6.0 * Math.PI * radius * viscosity);
    }

    /**
     * Force caused by sphere moving through a fluid. It acts in the opposite direction of the fluid velocity.
     * 
     * Implements a simplified quadratic drag (assuming a fixed coefficient).
     * 
     * @param velocity
     * @param fluidDensity
     * @param radius
     * @return
     */
    public static Vect3D sphereQuadraticDrag(final Vect3D velocity, final double fluidDensity, final double radius)
    {
        final Vect3D drag = Vect3D.abs(velocity).invert();
        drag.mul(velocity).mul(Math.PI * radius * radius * fluidDensity * 0.25);

        return drag;
    }

    /**
     * http://chrishecker.com/images/e/e7/Gdmphys3.pdf
     * 
     * @param normal
     * @param velocity
     * @param bounciness
     * @param mass
     * 
     * @return force in the direction of the contact normal
     */
    public static Vect3D impact(final Vect3D normal, final Vect3D velocity, final double bounciness, final double mass)
    {
        return new Vect3D(normal).mul(Math.round(Vect3D.dot(normal, velocity) * -(1.0 + bounciness) * mass));
    }

    /**
     * Force caused by a body moving on a surface. It acts in the opposite direction of the tangential components of
     * velocity.
     * 
     * @param surface
     * @param ud
     * @param mass
     * @return
     */
    public static Vect3D friction(final Vect3D surface, final double ud, final double mass)
    {
        return new Vect3D(surface).invert().mul(ud * mass);
    }
}
