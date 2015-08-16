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
        final Vect3D flow = Vect3D.abs(fluidVelocity);
        flow.mul(fluidVelocity).mul(Math.PI * radius * radius * fluidDensity * 0.25);

        return flow;
    }

    public static Vect3D sphereDragForce(final Vect3D velocity, final double fluidDensity, final double radius)
    {
        final Vect3D drag = Vect3D.abs(velocity).invert();
        drag.mul(velocity).mul(Math.PI * radius * radius * fluidDensity * 0.25);

        return drag;
    }

    public static Vect3D sphereStokeDragForce(final Vect3D velocity, final double viscosity, final double radius)
    {
        return new Vect3D(velocity).mul(-6.0 * Math.PI * radius * viscosity);
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
    public static Vect3D impactForce(final Vect3D normal,
                                     final Vect3D velocity,
                                     final double bounciness,
                                     final double mass)
    {
        return new Vect3D(normal).mul(Math.round(Vect3D.dot(normal, velocity) * -(1.0 + bounciness) * mass));
    }

    public static Vect3D frictionForce(final Vect3D surface, final double ud, final double mass)
    {
        return new Vect3D(surface).invert().mul(ud * mass);
    }
}
