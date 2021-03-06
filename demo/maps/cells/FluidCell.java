/*
  Copyright 2015 Fabio Ticconi
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package maps.cells;

import collision.Static;
import engine.Particle;
import maps.Cell;
import utils.ImmutableVect3D;
import utils.Vect3D;

/**
 * @author Fabio Ticconi
 */
public class FluidCell extends Static implements Cell
{
    // Common densities
    public final static double AIR_DENSITY   = 1.1d;
    public final static double WATER_DENSITY = 1000d;
    private final double dragV;
    private final double density;
    private final Vect3D flowSpeed;

    public FluidCell(final double density)
    {
        super(new Vect3D(), new Vect3D());

        this.density = density;
        flowSpeed = new Vect3D(ImmutableVect3D.zero);

        dragV = 0.25 * Math.PI * density;

        friction = 0.0;
    }

    /**
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
     * Reference: <a href="http://lorien.ncl.ac.uk/ming/particle/cpe124p2.html"></a>
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
