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
package collision;

import shapes.Box;
import shapes.Sphere;
import utils.ImmutableVect3D;
import utils.Vect3D;

/**
 * 
 * @author Fabio Ticconi
 */
public abstract class Collider
{
    public static boolean testBoxBox(final Box b1, final Box b2)
    {
        final Vect3D b1_max = b1.getMaxPoint();
        final Vect3D b1_min = b1.getMinPoint();

        final Vect3D b2_max = b2.getMaxPoint();
        final Vect3D b2_min = b2.getMinPoint();

        if (b1_max.x < b2_min.x || b1_min.x > b2_max.x)
            return false;
        if (b1_max.y < b2_min.y || b1_min.y > b2_max.y)
            return false;
        if (b1_max.z < b2_min.z || b1_min.z > b2_max.z)
            return false;

        return true;
    }

    public static boolean testSphereSphere(final Sphere s1, final Sphere s2)
    {
        final Vect3D d = Vect3D.sub(s1.getCenter(), s2.getCenter());

        final double dist2 = Vect3D.dot(d, d);
        final double radSum = s1.getRadius() + s2.getRadius();

        return dist2 < (radSum * radSum);
    }

    public static boolean testSphereBox(final Sphere s, final Box b)
    {
        final Vect3D closestPoint = Vect3D.max(s.getCenter(), b.getMinPoint()).min(b.getMaxPoint());

        // calculate distance vector
        closestPoint.sub(s.getCenter());

        return testPointSphere(closestPoint, s);
    }

    public static boolean testPointSphere(final Vect3D p, final Sphere s)
    {
        final double dist2 = Vect3D.dot(p, p);
        final double radSum = s.getRadius() + s.getRadius();

        return dist2 < (radSum * radSum);
    }

    public static boolean testPointBox(final Vect3D p, final Box b)
    {
        final Vect3D max = b.getMaxPoint();
        final Vect3D min = b.getMinPoint();

        if (max.x < p.x || min.x > p.x)
            return false;
        if (max.y < p.y || min.y > p.y)
            return false;
        if (max.z < p.z || min.z > p.z)
            return false;

        return true;
    }

    public static boolean testPointBoxExclusive(final Vect3D p, final Box b)
    {
        final Vect3D max = b.getMaxPoint();
        final Vect3D min = b.getMinPoint();

        if (p.x > min.x && p.x < max.x && p.y > min.y && p.y < max.y && p.z > min.z && p.z < max.z)
            return true;

        return false;
    }

    public static void clampPointOutsideBox(final Vect3D from, final Vect3D to, final Box b)
    {
        final boolean verb = false;

        ImmutableVect3D normal;

        final Vect3D min = b.getMinPoint();
        final Vect3D max = b.getMaxPoint();

        if (verb)
            System.out.format("\nfrom: %s\nto: %s\nbmin: %s\nbmax: %s\n", from, to, min, max);

        // final Vect3D direction = Vect3D.sub(to, from).normalised();
        final Vect3D direction = Vect3D.sub(to, from);
        if (verb)
            System.out.format("direction: %s (%f)\n", direction, direction.getLength());
        final Vect3D invDir = Vect3D.reciprocal(direction);
        if (verb)
            System.out.format("invDir: %s\n", invDir);

        final boolean signDirX = invDir.x < 0;
        final boolean signDirY = invDir.y < 0;
        final boolean signDirZ = invDir.z < 0;

        Vect3D bbox = signDirX ? max : min;
        final double xmindist = bbox.x - from.x;
        double tmin = xmindist * invDir.x;

        normal = signDirX ? ImmutableVect3D.xaxis : ImmutableVect3D.xaxisinv;

        bbox = signDirX ? min : max;
        double tmax = (bbox.x - from.x) * invDir.x;

        bbox = signDirY ? max : min;
        final double ymindist = bbox.y - from.y;
        final double tymin = ymindist * invDir.y;

        bbox = signDirY ? min : max;
        final double tymax = (bbox.y - from.y) * invDir.y;

        if ((tmin > tymax) || (tymin > tmax))
            return;

        // take the maximum of the t(x)min and tymin,
        // and take the correct normal now to save later
        // computations
        if (tymin > tmin)
        {
            tmin = tymin;

            normal = signDirY ? ImmutableVect3D.yaxis : ImmutableVect3D.yaxisinv;
        }

        // take the minimum of t(x)max and tymax,
        // we don't need the normal here
        tmax = Math.min(tymax, tmax);

        bbox = signDirZ ? max : min;
        final double zmindist = bbox.z - from.z;
        final double tzmin = zmindist * invDir.z;

        bbox = signDirZ ? min : max;
        final double tzmax = (bbox.z - from.z) * invDir.z;

        if ((tmin > tzmax) || (tzmin > tmax))
            return;

        // take the maximum of (txmin,tymin) and tzmin,
        // and take the correct normal now to save later
        // computations
        if (tzmin > tmin)
        {
            tmin = tzmin;

            normal = signDirZ ? ImmutableVect3D.zaxis : ImmutableVect3D.zaxisinv;
        }

        // take the minimum of (txmax,tymax) and tzmax,
        // we don't need the normal here
        tmax = Math.min(tzmax, tmax);

        final Vect3D isec = Vect3D.add(from, Vect3D.mul(direction, tmin));
        if (verb)
            System.out.format("isec: %s\n", isec);

        if (verb)
            System.out.format("tmin: %f\n", tmin);
        if (verb)
            System.out.format("tmax: %f\n", tmax);

        isec.add(new Vect3D(normal).mul(0.1));
        if (verb)
            System.out.format("isec2: %s\n", isec);
        to.assign(isec.sub(direction.mul(1.0)));
        if (verb)
            System.out.format("to: %s\n\n", to);
    }
}
