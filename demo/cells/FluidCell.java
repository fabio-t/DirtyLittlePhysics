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

import shapes.Box;
import utils.Maths;
import utils.Vect3D;
import engine.Cell;
import engine.Particle;

/**
 * 
 * @author Fabio Ticconi
 */
public class FluidCell implements Cell, Box
{
    // Common densities
    public final static double AIR_DENSITY     = 1.2d;      // pretty dense air
    public final static double WATER_DENSITY   = 1000d;

    public final static double AIR_VISCOSITY   = 0.00001983;
    public final static double WATER_VISCOSITY = 0.001002;

    public final double        density;
    public final double        viscosity;

    private final Vect3D       min;
    private final Vect3D       max;

    public FluidCell(final double density)
    {
        this.density = density;
        viscosity = 0.0;

        min = new Vect3D();
        max = new Vect3D();
    }

    public FluidCell(final double density, final double viscosity)
    {
        this.density = density;
        this.viscosity = viscosity;

        min = new Vect3D();
        max = new Vect3D();
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
        final Vect3D vel = p.getVelocity();

        final Vect3D force = Maths.sphereDragForce(vel, density, p.getRadius());
        // final Vect3D force = Maths.sphereStokeDragForce(vel, viscosity, p.getRadius());

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

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Box#getMinPoint()
     */
    @Override
    public Vect3D getMinPoint()
    {
        return min;
    }

    public void setMinPoint(final Vect3D v)
    {
        min.assign(v);
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Box#getMaxPoint()
     */
    @Override
    public Vect3D getMaxPoint()
    {
        return max;
    }

    public void setMaxPoint(final Vect3D v)
    {
        max.assign(v);
    }
}
