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

import shapes.Point;
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

    public Particle(final double mass, final double radius, final Vect3D pos, final Vect3D vel, final Vect3D acc)
    {
        oldCenter = new Vect3D(pos);
        center = pos;

        this.mass = mass;
        this.radius = radius;
        density = Maths.sphereDensity(mass, radius);

        this.vel = vel;
        this.acc = acc;
    }

    public void setMass(final double mass)
    {
        this.mass = mass;
        density = Maths.sphereDensity(mass, radius);
    }

    public void setRadius(final double radius)
    {
        this.radius = radius;
        density = Maths.sphereDensity(mass, radius);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Sphere#getCenter()
     */
    public Point getCenter()
    {
        return center;
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Sphere#getRadius()
     */
    public double getRadius()
    {
        return radius;
    }
}
