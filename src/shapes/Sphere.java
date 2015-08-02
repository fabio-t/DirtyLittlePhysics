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
package shapes;

import utils.Vect3D;
import collision.Collider;

/**
 * Defines a spherical shape.
 */
public class Sphere implements Shape
{
    protected Vect3D center;
    protected Vect3D extent;

    protected double radius;

    public Sphere(final Vect3D center, final double radius)
    {
        this.center = new Vect3D(center);
        extent = new Vect3D().add(radius);

        this.radius = radius;
    }

    @Override
    public Vect3D getCenter()
    {
        return center;
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Shape#getExtent()
     */
    @Override
    public Vect3D getExtent()
    {
        return extent;
    }

    public double getRadius()
    {
        return radius;
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Shape#intersects(shapes.Box)
     */
    @Override
    public boolean intersects(final Box b)
    {
        return Collider.test(this, b);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Shape#intersects(shapes.Sphere)
     */
    @Override
    public boolean intersects(final Sphere s)
    {
        return Collider.test(this, s);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Shape#intersects(utils.Vect3D)
     */
    @Override
    public boolean intersects(final Vect3D p)
    {
        return Collider.test(p, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Shape#intersects(shapes.Shape)
     */
    @Override
    public boolean intersects(final Shape s)
    {
        return s.intersects(this);
    }
}
