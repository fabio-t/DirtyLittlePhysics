package maps;

import map.Cell;
import map.Map;
import utils.ImmutableVect3D;
import utils.Vect3D;
import cells.FluidCell;
import cells.SolidCell;
import collision.Collider;

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
 * For the purpose of testing, the simplest map is hard-coded
 * so that both ground and fluids can be used.
 * Cells are inherently flyweight: only three Cell objects are instantiated,
 * for any map size.
 * 
 * @author Fabio Ticconi
 */
public class SimpleMap implements Map
{
    private static final FluidCell water  = new FluidCell(FluidCell.WATER_DENSITY);
    private static final FluidCell air    = new FluidCell(FluidCell.AIR_DENSITY);
    private static final SolidCell ground = new SolidCell();

    private final Vect3D           min;
    private final Vect3D           max;

    private ImmutableVect3D        gravity;

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
    }

    @Override
    public Cell getCell(final Vect3D p)
    {
        if (Collider.test(p, air))
            return air;

        if (Collider.test(p, water))
            return water;

        return ground;
    }

    @Override
    public void correctPositions(final Vect3D from, final Vect3D to)
    {
        // makes it impossible to move outside the world
        to.max(min).min(max);
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
}
