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
package utils;

import shapes.Point;

/**
 * 
 * Vect3D
 * 
 * @author Fabio Ticconi
 */
public class Vect3D implements Point
{
    public double x;
    public double y;
    public double z;

    public Vect3D()
    {
        x = 0d;
        y = 0d;
        z = 0d;
    }

    public Vect3D(final Point p)
    {
        x = p.getX();
        y = p.getY();
        z = p.getZ();
    }

    public Vect3D(final double x, final double y, final double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vect3D(final Vect3D v)
    {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    @Override
    public String toString()
    {
        return String.format("(%f, %f, %f)", x, y, z);
    }

    public Vect3D assign(final Vect3D v)
    {
        x = v.x;
        y = v.y;
        z = v.z;

        return this;
    }

    public Vect3D assign(final double d)
    {
        x = d;
        y = d;
        z = d;

        return this;
    }

    public Vect3D add(final Vect3D v)
    {
        x += v.x;
        y += v.y;
        z += v.z;

        return this;
    }

    public Vect3D add(final double d)
    {
        x += d;
        y += d;
        z += d;

        return this;
    }

    public Vect3D sub(final Vect3D v)
    {
        x -= v.x;
        y -= v.y;
        z -= v.z;

        return this;
    }

    public Vect3D sub(final double d)
    {
        x -= d;
        y -= d;
        z -= d;

        return this;
    }

    public Vect3D mul(final Vect3D v)
    {
        x *= v.x;
        y *= v.y;
        z *= v.z;

        return this;
    }

    public Vect3D mul(final double d)
    {
        x *= d;
        y *= d;
        z *= d;

        return this;
    }

    public Vect3D div(final Vect3D v)
    {
        x /= v.x;
        y /= v.y;
        z /= v.z;

        return this;
    }

    public Vect3D div(final double d)
    {
        x /= d;
        y /= d;
        z /= d;

        return this;
    }

    public Vect3D max(final Vect3D v1)
    {
        x = x > v1.x ? x
                    : v1.x;
        y = y > v1.y ? y
                    : v1.y;
        z = z > v1.z ? z
                    : v1.z;

        return this;
    }

    public Vect3D min(final Vect3D v1)
    {
        x = x < v1.x ? x
                    : v1.x;
        y = y < v1.y ? y
                    : v1.y;
        z = z < v1.z ? z
                    : v1.z;

        return this;
    }

    public static Vect3D add(final Vect3D v1, final Vect3D v2)
    {
        final Vect3D v = new Vect3D(v1);

        v.add(v2);

        return v;
    }

    public static Vect3D sub(final Vect3D v1, final Vect3D v2)
    {
        final Vect3D v = new Vect3D(v1);

        v.sub(v2);

        return v;
    }

    public static Vect3D mul(final Vect3D v1, final Vect3D v2)
    {
        final Vect3D v = new Vect3D(v1);

        v.mul(v2);

        return v;
    }

    public static Vect3D div(final Vect3D v1, final Vect3D v2)
    {
        final Vect3D v = new Vect3D(v1);

        v.div(v2);

        return v;
    }

    public static Vect3D max(final Vect3D v1, final Vect3D v2)
    {
        final Vect3D v = new Vect3D(v1);

        v.max(v2);

        return v;
    }

    public static Vect3D min(final Vect3D v1, final Vect3D v2)
    {
        final Vect3D v = new Vect3D(v1);

        v.min(v2);

        return v;
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Point#getX()
     */
    @Override
    public double getX()
    {
        return x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Point#getY()
     */
    @Override
    public double getY()
    {
        return y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Point#getZ()
     */
    @Override
    public double getZ()
    {
        return z;
    }
}
