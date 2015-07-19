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
 * Helper class for math operations relevant to the engine.
 * 
 * @author Fabio Ticconi
 */
public class Maths
{
    public static double sphereDensity(final double mass, final double radius)
    {
        return mass / sphereVolume(radius);
    }

    public static double sphereVolume(final double radius)
    {
        return (4d / 3d) * Math.PI * radius * radius * radius;
    }

    public static Vect3D sphereFlowForce(final Vect3D fluidVelocity, final double fluidDensity, final double radius)
    {
        // final Vect3D flow = new Vect3D(Math.signum(fluidVelocity.x), Math.signum(fluidVelocity.y),
        // Math.signum(fluidVelocity.z));

        final Vect3D flow = Vect3D.abs(fluidVelocity);
        flow.mul(fluidVelocity).mul(Math.PI * radius * radius * fluidDensity * 0.25);

        /*
         * // we could have first normalised the copied
         * // velocity vector in "flow", then multiplied that by the other factors,
         * // however that's more costly and to similar effect
         * if (fluidVelocity.x < 0.0)
         * flow.x = -flow.x;
         * if (fluidVelocity.y < 0.0)
         * flow.y = -flow.y;
         * if (fluidVelocity.z < 0.0)
         * flow.z = -flow.z;
         */

        return flow;
    }

    public static Vect3D sphereDragForce(final Vect3D vel, final double fluidDensity, final double radius)
    {
        // final Vect3D drag = new Vect3D(-Math.signum(vel.x), -Math.signum(vel.y), -Math.signum(vel.z));
        final Vect3D drag = Vect3D.abs(vel).invert();
        drag.mul(vel).mul(Math.PI * radius * radius * fluidDensity * 0.25);

        /*
         * // we could have first normalised the copied
         * // velocity vector in "flow", then multiplied that by the other factors,
         * // however that's more costly and to similar effect
         * if (vel.x > 0.0)
         * drag.x = -drag.x;
         * if (vel.y > 0.0)
         * drag.y = -drag.y;
         * if (vel.z > 0.0)
         * drag.z = -drag.z;
         */

        return drag;
    }

    public static Vect3D sphereStokeDragForce(final Vect3D vel, final double viscosity, final double radius)
    {
        return new Vect3D(vel).mul(-6.0 * Math.PI * radius * viscosity);
    }
}
