package maps;

import maps.cells.FluidCell;
import maps.cells.SolidCell;
import shapes.Box;
import utils.Forces;
import utils.ImmutableVect3D;
import utils.Vect3D;
import collision.Collider;
import engine.Particle;
import environment.World;

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

/**
 * For the purpose of testing, the simplest environment is hard-coded
 * so that both ground and fluids can be used.
 * Cells are inherently flyweight: only three Cell objects are instantiated,
 * for any environment size.
 * 
 * @author Fabio Ticconi
 */
public class SimpleMap implements World
{
    private static final FluidCell water  = new FluidCell(FluidCell.WATER_DENSITY);
    private static final FluidCell air    = new FluidCell(FluidCell.AIR_DENSITY);
    private static final SolidCell ground = new SolidCell();

    private final Vect3D           min;
    private final Vect3D           max;

    private ImmutableVect3D        gravity;

    private final double           ud;

    public SimpleMap(final int x_min,
                     final int x_max,
                     final int y_min,
                     final int y_max,
                     final int z_min,
                     final int z_max)
    {
        min = new Vect3D(x_min, y_min, z_min);
        max = new Vect3D(x_max, y_max, z_max);

        // air is when z > half
        air.setMinPoint(new Vect3D(min.x, min.y, (max.z + min.z) / 2.0));
        air.setMaxPoint(new Vect3D(max.x, max.y, max.z));
        // wind flows westward at constant X m/s
        air.setFlowSpeed(new Vect3D(-50, 0.0, 0.0));

        // water is when x < half and z < half
        water.setMinPoint(new Vect3D(min.x, min.y, min.z));
        water.setMaxPoint(new Vect3D((max.x + min.x) / 2.0, max.y, (max.z + min.z) / 2.0));
        // there is a eastward underwater current flowing
        // at a constant X m/s
        water.setFlowSpeed(new Vect3D(5.0, 0.0, 0.0));

        // ground is the rest, ie x > half and z < half
        ground.setMinPoint(new Vect3D((max.x + min.x) / 2.0, min.y, min.z));
        ground.setMaxPoint(new Vect3D(max.x, max.y, (max.z + min.z) / 2.0));

        gravity = new ImmutableVect3D(0d, 0d, -9.81d);

        ud = 0.4;
    }

    public Cell getCell(final Vect3D p)
    {
        if (Collider.test(p, air))
            return air;

        if (Collider.test(p, water))
            return water;

        return ground;
    }

    /**
     * Sets the gravity as an <b>acceleration</b>
     * vector.<br />
     * Example: for Earth, the vector would be (0, 0, -9.81).
     * Incidentally, this is also the default value is
     * this method is not called.
     * 
     * @param gravity
     *            an {@link ImmutableVect3D} that will
     *            overwrite the current gravity
     */
    @Override
    public void setGravity(final ImmutableVect3D gravity)
    {
        this.gravity = gravity;
    }

    /**
     * Returns a copy of the gravity vector.
     * 
     * @return gravity
     */
    @Override
    public ImmutableVect3D getGravity()
    {
        return gravity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see environment.World#process(engine.Particle, double)
     */
    @Override
    public void process(final Particle p, final double dt)
    {
        final Vect3D center = p.getCenter();
        final Vect3D force = p.getForce();

        // here we have a simple non-toroidal world,
        // so if we cross the boundary, the position is
        // simply clamped
        center.max(min).min(max);

        // the world handler gives us a Cell
        // using the current player position
        final Cell cell = getCell(center);

        // gravity must be corrected by the buoyancy (if the cell has one).
        // Note: normally this would only make sense for the "z" dimension,
        // but who are we to limit your creativity?
        final double buoyancy = cell.getBuoyancy(p);
        final Vect3D gForce = new Vect3D(gravity).mul(buoyancy * p.getMass());

        // many kind of environmental force
        // could be applied to the particle,
        // for example fluid drag, friction,
        // impact forces
        force.add(cell.getForces(p, dt)).add(gForce);

        if (cell instanceof SolidCell)
            Forces.processImpact(p, (Box) cell, dt, ud);
    }

    /*
     * (non-Javadoc)
     * 
     * @see environment.World#getForces(engine.Particle, double)
     */
    @Override
    public Vect3D getForces(final Particle p, final double dt)
    {
        final Vect3D center = p.getCenter();

        // the world handler gives us a Cell
        // using the current player position
        final Cell cell = getCell(center);

        // gravity must be corrected by the buoyancy (if the cell has one).
        // Note: normally this would only make sense for the "z" dimension,
        // but who are we to limit your creativity?
        final double buoyancy = cell.getBuoyancy(p);
        final Vect3D force = new Vect3D(gravity).mul(buoyancy * p.getMass());

        // many kind of environmental force
        // could be applied to the particle,
        // for example fluid drag, friction,
        // impact forces
        force.add(cell.getForces(p, dt));

        if (cell instanceof SolidCell)
            force.add(Forces.contact(p, (Box) cell, dt, ud));

        return force;
    }
}
