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

import map.Cell;
import shapes.Box;
import utils.ImmutableVect3D;
import utils.Maths;
import utils.Vect3D;
import engine.Particle;

/**
 * 
 * @author Fabio Ticconi
 */
public class FluidCell implements Cell, Box
{
    // Common densities
    public final static double AIR_DENSITY   = 1.1d;
    public final static double WATER_DENSITY = 1000d;

    public final double        density;
    public final Vect3D        flowSpeed;

    private final Vect3D       min;
    private final Vect3D       max;

    public FluidCell(final double density)
    {
        this.density = density;
        flowSpeed = new Vect3D(ImmutableVect3D.zero);

        min = new Vect3D();
        max = new Vect3D();
    }

    /**
     * 
     * @param v
     */
    public void setFlowSpeed(final Vect3D v)
    {
        flowSpeed.assign(v);
    }

    /*
     * (non-Javadoc)
     * 
     * @see engine.Simulator.Cell#getForces(engine.Particle)
     */
    @Override
    public Vect3D getForces(final Particle p)
    {
        // return getDragForce(p).add(getFlowForce(p));

        final Vect3D flow = Vect3D.abs(flowSpeed).mul(flowSpeed);
        final Vect3D drag = Vect3D.abs(p.getVelocity()).inverse().mul(p.getVelocity());
        return flow.add(drag).mul(Math.PI * p.getRadius() * p.getRadius() * density * 0.25);
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
        return Maths.sphereDragForce(p.getVelocity(), density, p.getRadius());
    }

    private Vect3D getFlowForce(final Particle p)
    {
        return Maths.sphereFlowForce(flowSpeed, density, p.getRadius());
    }

    /**
     * Reference: {@link http://lorien.ncl.ac.uk/ming/particle/cpe124p2.html}
     * 
     * 
     * @see map.Cell#getBuoyancy(engine.Particle)
     */
    @Override
    public double getBuoyancy(final Particle p)
    {
        // return ((p.getDensity() - density) / p.getDensity());
        return 1.0 - (density / p.getDensity());
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
