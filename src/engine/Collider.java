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
package engine;

import shapes.Box;
import shapes.Sphere;
import utils.Vect3D;

/**
 * 
 * @author Fabio Ticconi
 */
public class Collider
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
        final Vect3D b_max = b.getMaxPoint();
        final Vect3D b_min = b.getMinPoint();

        if (b_max.x < p.x || b_min.x > p.x)
            return false;
        if (b_max.y < p.y || b_min.y > p.y)
            return false;
        if (b_max.z < p.z || b_min.z > p.z)
            return false;

        return true;
    }
}
