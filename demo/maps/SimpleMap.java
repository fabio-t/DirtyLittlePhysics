package maps;

import shapes.Box;
import utils.Vect3D;
import cells.FluidCell;
import cells.SolidCell;
import engine.Cell;
import engine.Collider;
import engine.Map;

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
public class SimpleMap implements Map, Box
{
    private static final FluidCell water  = new FluidCell(FluidCell.WATER_DENSITY, FluidCell.WATER_VISCOSITY);
    private static final FluidCell air    = new FluidCell(FluidCell.AIR_DENSITY, FluidCell.AIR_VISCOSITY);
    private static final SolidCell ground = new SolidCell();

    private final Vect3D           min;
    private final Vect3D           max;

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

        // water is when x < half
        water.setMinPoint(new Vect3D(min.x, min.y, min.z));
        water.setMaxPoint(new Vect3D((max.x + min.x) / 2.0, max.y, max.z));

        // ground is the rest, ie z < half and x > half
        ground.setMinPoint(new Vect3D((max.x + min.x) / 2.0, min.y, min.z));
        ground.setMaxPoint(new Vect3D(max.x, max.y, (max.z + min.z) / 2.0));
    }

    @Override
    public boolean isOverBounds(final Vect3D p)
    {
        if (Collider.testPointBox(p, this))
            return false;

        return true;
    }

    @Override
    public Cell getCell(final Vect3D p)
    {
        if (Collider.testPointBox(p, air))
            return air;

        if (Collider.testPointBox(p, water))
            return water;

        return ground;
    }

    @Override
    public boolean areNeighbours(final Vect3D p1, final Vect3D p2)
    {
        final Cell c1 = getCell(p1);
        final Cell c2 = getCell(p2);

        if (c1 == c2)
            return true;

        return false;
    }

    @Override
    public boolean canMoveTo(final Vect3D from, final Vect3D to)
    {
        // if the destination is over the bounds, we can't go there
        // if we are in a ground cell, we can't go there
        // if THAT point is a ground cell, we can't go there
        if (isOverBounds(to) || Collider.testPointBox(from, ground) || Collider.testPointBox(to, ground))
            return false;

        return true;
    }

    @Override
    public void clampMovement(final Vect3D from, final Vect3D to)
    {
        // makes it impossible to move outside the world
        to.max(min).min(max);

        // if we are within the ground, we can't move at all
        if (Collider.testPointBox(from, ground))
            to.assign(from);
        // if we are outside but we want to move inside the ground,
        // we can only go as far as the border
        else if (Collider.testPointBox(to, ground))
            to.max(ground.getMinPoint()).min(ground.getMaxPoint());
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
}
