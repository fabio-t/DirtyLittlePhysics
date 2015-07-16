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

import shapes.Sphere;
import utils.Maths;
import utils.Vect3D;

/**
 * Particle class, including movement vectors and size/weight properties.
 * 
 * <br />
 * <br />
 * 
 * Includes some helper functions to set the mass and radius, and
 * automatically sets the density when setting either the radius or
 * the mass.
 * 
 * <br/>
 * <br/>
 * 
 * In addition, it implements the interface Point. This is used by
 * the collider.
 * 
 * @author Fabio Ticconi
 */
public class Particle implements Sphere
{
    final Vect3D oldCenter;
    final Vect3D center;

    double       radius;
    double       mass;
    double       density;

    final Vect3D vel;
    final Vect3D acc;

    public Particle()
    {
        oldCenter = new Vect3D();
        center = new Vect3D();

        radius = 1d;
        mass = 1d;
        density = Maths.sphereDensity(mass, radius);

        vel = new Vect3D();
        acc = new Vect3D();
    }

    public Particle(final double mass, final double radius, final Vect3D pos, final Vect3D vel)
    {
        oldCenter = new Vect3D(pos);
        center = pos;

        this.mass = mass;
        this.radius = radius;
        density = Maths.sphereDensity(mass, radius);

        this.vel = vel;
        acc = new Vect3D();
    }

    /**
     * Overwrites the particle's mass.
     * It recalculates the density based on mass
     * and radius.
     * 
     * @param mass
     */
    public void setMass(final double mass)
    {
        this.mass = mass;
        density = Maths.sphereDensity(mass, radius);
    }

    /**
     * Overwrites the particle's radius.
     * It recalculates the density based and mass
     * and radius.
     * 
     * @param radius
     */
    public void setRadius(final double radius)
    {
        this.radius = radius;
        density = Maths.sphereDensity(mass, radius);
    }

    /**
     * Overwrites the particle's velocity.
     * It copies the values over, doesn't assign the object.
     * 
     * @param vel
     */
    public void setVelocity(final Vect3D vel)
    {
        this.vel.assign(vel);
    }

    /**
     * Overwrites the particle's acceleration.
     * It copies the values over, doesn't assign the object.
     * 
     * @param acc
     */
    public void setAcceleration(final Vect3D acc)
    {
        this.acc.assign(acc);
    }

    /**
     * Overwrites the particle's center position.
     * It copies the values over, doesn't assign the object.
     * 
     * @param center
     */
    public void setCenter(final Vect3D center)
    {
        this.center.assign(center);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Sphere#getCenter()
     */
    @Override
    public Vect3D getCenter()
    {
        return center;
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Sphere#getRadius()
     */
    @Override
    public double getRadius()
    {
        return radius;
    }

    public double getDensity()
    {
        return density;
    }

    public double getMass()
    {
        return mass;
    }

    public Vect3D getVelocity()
    {
        return vel;
    }

    @Override
    public String toString()
    {
        return String.format("particle: cent: %s, vel: %s, acc: %s", center, vel, acc);
    }
}
