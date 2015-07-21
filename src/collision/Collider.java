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
import engine.Simulator;

/**
 * 
 * @author Fabio Ticconi
 */
public abstract class Collider
{
    public static boolean test(final Box b1, final Box b2)
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

    public static boolean test(final Sphere s1, final Sphere s2)
    {
        final Vect3D d = Vect3D.sub(s1.getCenter(), s2.getCenter());

        final double dist2 = Vect3D.dot(d, d);
        final double radSum = s1.getRadius() + s2.getRadius();

        return dist2 < (radSum * radSum);
    }

    public static boolean test(final Sphere s, final Box b)
    {
        final Vect3D closestPoint = Vect3D.max(s.getCenter(), b.getMinPoint()).min(b.getMaxPoint());

        // calculate distance vector
        closestPoint.sub(s.getCenter());

        return test(closestPoint, s);
    }

    public static boolean test(final Vect3D p, final Sphere s)
    {
        final double dist2 = Vect3D.dot(p, p);
        final double radSum = s.getRadius() + s.getRadius();

        return dist2 < (radSum * radSum);
    }

    public static boolean test(final Vect3D p, final Box b)
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

    /**
     * Finds the closest point from the given point to the given segment, if it exists.
     * Taken from Ericson, "Real-time collision detection".
     * 
     * @param p
     *            point outside of the segment
     * @param start
     *            one of the ends of the segment
     * @param end
     *            the other end of the segment
     * @param isec
     *            will contain the closest point to p on the segment. It
     *            will always overwrite the content: if the point is outside
     *            the segment interval, it will be clamped to either start or end
     * 
     * @return t as in d(t) = a + t*(b - a), or 0.0 or 1.0 if outside
     */
    public static double closestPointToSegment(final Vect3D p, final Vect3D start, final Vect3D end, final Vect3D isec)
    {
        final Vect3D segment = Vect3D.sub(end, start);
        final Vect3D pointToStart = Vect3D.sub(start, p);

        // project p onto the segment, but deferring a division
        double t = Vect3D.dot(pointToStart, segment);

        if (t <= 0.0)
        {
            // p projects outside the segment interval, on the start side; clamp to start
            isec.set(start);
            return 0.0;
        }
        else
        {
            final double denom = Vect3D.dot(segment, segment); // Always nonnegative since denom = ||segment||^2

            if (t >= denom)
            {
                // p projects outside the segment interval, on the end side; clamp to end
                isec.set(end);
                return 1.0;
            }
            else
            {
                // p projects inside the segment interval; must do deferred divide now
                t = t / denom;
                isec.set(segment).mul(t).add(start);
                return t;
            }
        }
    }

    /**
     * Returns the squared distance between a point p and a segment.
     * Taken from Ericson, "Real-time collision detection".
     * 
     * @param p
     *            the point
     * @param start
     *            one of the ends of the segment
     * @param end
     *            the other end of the segment
     * 
     * @return squared distance
     */
    public static double sqDistPointSegment(final Vect3D p, final Vect3D start, final Vect3D end)
    {
        final Vect3D segment = Vect3D.sub(end, start);

        final Vect3D pStart = Vect3D.sub(p, start);
        final Vect3D pEnd = Vect3D.sub(p, end);

        final double e = Vect3D.dot(pStart, segment);

        // Handle cases where p projects outside the segment
        if (e <= 0.0)
            return Vect3D.dot(pStart, pStart);

        final double f = Vect3D.dot(segment, segment);

        if (e >= f)
            return Vect3D.dot(pEnd, pEnd);

        // Handle cases where p projects onto segment
        return Vect3D.dot(pStart, pStart) - e * e / f;
    }

    /**
     * Takes a Ray (composed of an origin vector and a normalised
     * direction vector) as well as a {@link Box}, and finds the
     * point of interesection of the ray with the box if one
     * exists. Code adapted from toxiclibs.
     * 
     * @param origin
     *            start point of the ray
     * @param direction
     *            normalised direction vector
     * @param b
     *            the AABB box to check against
     * @param isec
     *            will contain the intersection point if one exists
     * @param normalOut
     *            will contain the normal containing isec if found
     * 
     * @return the distance between origin and isec
     */
    public static double intersectRayBox(final Vect3D origin,
                                         final Vect3D direction,
                                         final Box b,
                                         final Vect3D isec,
                                         final Vect3D normalOut)
    {
        ImmutableVect3D normal;

        final Vect3D min = b.getMinPoint();
        final Vect3D max = b.getMaxPoint();

        if (Simulator.VERBOSE)
            System.out.format("\n#collider#\norigin: %s\ndirection: %s\ndir. length: %s\nbmin: %s\nbmax: %s\n", origin,
                              direction, direction.length(), min, max);

        final Vect3D invDir = Vect3D.reciprocal(direction);
        if (Simulator.VERBOSE)
            System.out.format("invDir: %s\n", invDir);

        final boolean signDirX = invDir.x < 0;
        final boolean signDirY = invDir.y < 0;
        final boolean signDirZ = invDir.z < 0;

        Vect3D bbox = signDirX ? max : min;
        final double xmindist = bbox.x - origin.x;
        double tmin = xmindist * invDir.x;

        normal = signDirX ? ImmutableVect3D.xaxis : ImmutableVect3D.xaxisinv;

        bbox = signDirX ? min : max;
        double tmax = (bbox.x - origin.x) * invDir.x;

        bbox = signDirY ? max : min;
        final double ymindist = bbox.y - origin.y;
        final double tymin = ymindist * invDir.y;

        bbox = signDirY ? min : max;
        final double tymax = (bbox.y - origin.y) * invDir.y;

        if ((tmin > tymax) || (tymin > tmax))
            return 0.0;

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
        final double zmindist = bbox.z - origin.z;
        final double tzmin = zmindist * invDir.z;

        bbox = signDirZ ? min : max;
        final double tzmax = (bbox.z - origin.z) * invDir.z;

        if ((tmin > tzmax) || (tzmin > tmax))
            return 0.0;

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

        isec.set(origin).add(Vect3D.mul(direction, tmin));

        if (Simulator.VERBOSE)
        {
            System.out.format("isec: %s\n", isec);
            System.out.format("tmin: %f\n", tmin);
            System.out.format("tmax: %f\n", tmax);
            System.out.format("normal: %s\n", normal);
        }

        normalOut.set(normal);

        return tmin;
    }
}
