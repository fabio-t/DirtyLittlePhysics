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
package maps.cells;

import maps.Cell;
import shapes.Box;
import utils.ImmutableVect3D;
import utils.Vect3D;
import engine.Particle;

/**
 * 
 * @author Fabio Ticconi
 */
public class FluidCell extends Box implements Cell
{
    // Common densities
    public final static double AIR_DENSITY   = 1.1d;
    public final static double WATER_DENSITY = 1000d;

    private final Vect3D       flowSpeed;

    public final double        dragV;
    public final double        density;

    public FluidCell(final double density)
    {
        super(new Vect3D(), new Vect3D());

        this.density = density;
        flowSpeed = new Vect3D(ImmutableVect3D.zero);

        dragV = 0.25 * Math.PI * density;
    }

    /**
     * 
     * @param v
     */
    public void setFlowSpeed(final Vect3D v)
    {
        flowSpeed.set(v);
    }

    /*
     * (non-Javadoc)
     * 
     * @see engine.Simulator.Cell#getForces(engine.Particle)
     */
    @Override
    public Vect3D getForces(final Particle p, final double dt)
    {
        // return getDragForce(p).add(getFlowForce(p));

        final Vect3D flow = Vect3D.abs(flowSpeed).mul(flowSpeed);
        final Vect3D drag = Vect3D.abs(p.getVelocity()).invert().mul(p.getVelocity());
        return flow.add(drag).mul(dragV * p.getRadius() * p.getRadius());
    }

    /**
     * Reference: {@link http://lorien.ncl.ac.uk/ming/particle/cpe124p2.html}
     * 
     * 
     * @see maps.Cell#getBuoyancy(engine.Particle)
     */
    @Override
    public double getBuoyancy(final Particle p)
    {
        // return ((p.getDensity() - density) / p.getDensity());
        return 1.0 - (density / p.getDensity());
    }
}
