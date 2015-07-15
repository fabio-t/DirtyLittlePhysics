package maps;
import shapes.Point;
import cells.FluidCell;
import cells.SolidCell;
import engine.Cell;
import engine.Simulator.Map;

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
    private Cell water  = new FluidCell(FluidCell.WATER_DENSITY);
    private Cell air    = new FluidCell(FluidCell.AIR_DENSITY);
    private Cell ground = new SolidCell();

    int          x_min, x_max;
    int          y_min, y_max;
    int          z_min, z_max;

    public SimpleMap(final int x_min,
                     final int x_max,
                     final int y_min,
                     final int y_max,
                     final int z_min,
                     final int z_max)
    {
        water = new FluidCell(FluidCell.WATER_DENSITY);
        air = new FluidCell(FluidCell.AIR_DENSITY);
        ground = new SolidCell();

        this.x_min = x_min;
        this.x_max = x_max;
        this.y_min = y_min;
        this.y_max = y_max;
        this.z_min = z_min;
        this.z_max = z_max;
    }

    @Override
    public boolean isOverBounds(final Point p)
    {
        final double x = p.getX();
        final double y = p.getY();
        final double z = p.getZ();

        if (x < x_min || x > x_max || y < y_min || y > y_max || z < z_min || z > z_max)
            return true;

        return false;
    }

    @Override
    public Cell getCell(final Point p)
    {
        // the Z=0 plane is the walkable plane
        if (p.getZ() < 0.0)
            return ground;

        // the rest is air
        return air;
    }

    @Override
    public boolean areNeighbours(final Point p1, final Point p2)
    {
        if (p1.getZ() < 0.0 && p2.getZ() >= 0.0)
            // we were into the ground and we want to go out
            return false;

        if (p1.getZ() >= 0.0 && p2.getZ() < 0.0)
            // we are over the ground and we want to dig in
            return false;

        // in all other cases, we are "neighbours" in this very simple
        // Map
        return true;
    }

    @Override
    public boolean canMoveTo(final Point from, final Point to)
    {
        // if the destination is over the bounds, we can't go there
        // if we are in a ground cell, we can't go there
        // if THAT point is a ground cell, we can't go there
        if (isOverBounds(to) || getCell(from) == ground || getCell(to) == ground)
            return false;

        return true;
    }
}
