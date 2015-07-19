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

/**
 * 
 * Vect3D
 * 
 * @author Fabio Ticconi
 */
public class Vect3D
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

    public Vect3D(final ImmutableVect3D v)
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

    public double getLength()
    {
        return Math.sqrt(dot(this, this));
    }

    public double selfDot()
    {
        return dot(this, this);
    }

    public boolean equals(final Vect3D v)
    {
        if (x != v.x || y != v.y || z != v.z)
            return false;

        return true;
    }

    public Vect3D reciprocal()
    {
        x = 1.0 / x;
        y = 1.0 / y;
        z = 1.0 / z;

        return this;
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

    public Vect3D max(final Vect3D v)
    {
        x = Math.max(x, v.x);
        y = Math.max(y, v.x);
        z = Math.max(z, v.z);

        return this;
    }

    public Vect3D max(final double d)
    {
        x = Math.max(x, d);
        y = Math.max(y, d);
        z = Math.max(z, d);

        return this;
    }

    public Vect3D min(final Vect3D v)
    {
        x = Math.min(x, v.x);
        y = Math.min(y, v.x);
        z = Math.min(z, v.z);

        return this;
    }

    public Vect3D min(final double d)
    {
        x = Math.min(x, d);
        y = Math.min(y, d);
        z = Math.min(z, d);

        return this;
    }

    public Vect3D abs()
    {
        x = Math.abs(x);
        y = Math.abs(y);
        z = Math.abs(z);

        return this;
    }

    public Vect3D normalised()
    {
        div(getLength());

        return this;
    }

    public Vect3D inverse()
    {
        x = -x;
        y = -y;
        z = -z;

        return this;
    }

    // ---------------------------------------------------------
    // STATIC METHODS
    // ---------------------------------------------------------

    public static Vect3D add(final Vect3D v1, final Vect3D v2)
    {
        return new Vect3D(v1).add(v2);
    }

    public static Vect3D add(final Vect3D v, final double d)
    {
        return new Vect3D(v).add(d);
    }

    public static Vect3D sub(final Vect3D v1, final Vect3D v2)
    {
        return new Vect3D(v1).sub(v2);
    }

    public static Vect3D sub(final Vect3D v, final double d)
    {
        return new Vect3D(v).sub(d);
    }

    public static Vect3D mul(final Vect3D v1, final Vect3D v2)
    {
        return new Vect3D(v1).mul(v2);
    }

    public static Vect3D mul(final Vect3D v, final double d)
    {
        return new Vect3D(v).mul(d);
    }

    public static Vect3D div(final Vect3D v1, final Vect3D v2)
    {
        return new Vect3D(v1).div(v2);
    }

    public static Vect3D div(final Vect3D v, final double d)
    {
        return new Vect3D(v).div(d);
    }

    public static Vect3D max(final Vect3D v1, final Vect3D v2)
    {
        return new Vect3D(v1).max(v2);
    }

    public static Vect3D max(final Vect3D v, final double d)
    {
        return new Vect3D(v).max(d);
    }

    public static Vect3D min(final Vect3D v1, final Vect3D v2)
    {
        return new Vect3D(v1).min(v2);
    }

    public static Vect3D min(final Vect3D v, final double d)
    {
        return new Vect3D(v).min(d);
    }

    public static double dot(final Vect3D v1, final Vect3D v2)
    {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static Vect3D abs(final Vect3D v)
    {
        return new Vect3D(Math.abs(v.x), Math.abs(v.y), Math.abs(v.z));
    }

    public static Vect3D normalised(final Vect3D v)
    {
        return new Vect3D(v).div(v.getLength());
    }

    public static Vect3D reciprocal(final Vect3D v)
    {
        return new Vect3D(v).reciprocal();
    }
}
