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
package cells;

import utils.Vect3D;
import engine.Cell;
import engine.Particle;

/**
 * 
 * @author Fabio Ticconi
 */
public class FluidCell implements Cell
{
    // Common densities
    public final static double AIR_DENSITY   = 1.2d; // pretty dense air
    public final static double WATER_DENSITY = 1000d;

    public final double        density;

    public FluidCell(final double density)
    {
        this.density = density;
    }

    /*
     * (non-Javadoc)
     * 
     * @see engine.Simulator.Cell#getForces(engine.Particle)
     */
    @Override
    public Vect3D getForces(final Particle p)
    {
        return getDragForce(p);
    }

    /**
     * <p>
     * Variation on the classic quadratic drag formula (not Stoke's!).
     * </p>
     * 
     * @return newly instanced force opposite to the particle's direction
     */
    private Vect3D getDragForce(final Particle p)
    {
        final Vect3D vel = new Vect3D(p.getVelocity());

        final Vect3D force = vel.mul(vel).mul(Math.PI * p.getRadius() * p.getRadius() * density);

        if (vel.x > 0)
            force.x = -force.x;

        if (vel.y > 0)
            force.y = -force.y;

        if (vel.z > 0)
            force.z = -force.z;

        return force;
    }

    /*
     * (non-Javadoc)
     * 
     * @see engine.Simulator.Cell#canPass(engine.Particle)
     */
    @Override
    public boolean canPass(final Particle p)
    {
        return true;
    }

    /**
     * Reference: {@link http://lorien.ncl.ac.uk/ming/particle/cpe124p2.html}
     * 
     * 
     * @see engine.Cell#getBuoyancy(engine.Particle)
     */
    @Override
    public double getBuoyancy(final Particle p)
    {
        return ((p.getDensity() - density) / p.getDensity());
    }
}
