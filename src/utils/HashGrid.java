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

import java.util.ArrayList;
import java.util.List;

import shapes.Point;

/**
 * A HashGrid implementation that flattens out 3D Points
 * into 1D cells. This allows for relatively fast
 * collision detection.
 * 
 * @author Fabio Ticconi
 */
public class HashGrid
{
    int                cellSize;
    double             xFactor;
    double             yFactor;

    int                length;
    int                width;

    ArrayList<Point>[] cells;

    /**
     * Creates a new Grid World with the given dimensions
     * and cell size. The objects will be partitioned
     * in the respective cell using their own positions.
     * For simplicity, only two of the three dimensions
     * are taken into consideration when creating the HashGrid
     * (generally speaking, the missing one should be the Up-Down
     * dimension, so that a "cell" actually represents all objects
     * in a specific 3D column in the world. The assumption is that
     * underground and overground objects are rare).
     * 
     * <br />
     * <br />
     * 
     * Note how the dimensions of the world are short variables.
     * This is because for my needs that's large enough, and allows me
     * to avoid using an hash function. If you hit a limit
     * because of world size, or you want to use additional dimensions,
     * then you need to create a hash function and "module" it down
     * to a fixed number of buckets. Know that collision detection
     * will be slightly trickier than here. An excellent source of
     * information in case you want to go down that route is
     * "Morgan Kaufmann - Real time collision detection".
     * 
     * @param x
     *            the absolute "length" of the world
     * @param y
     *            the absolute "width" of the world
     * @param cellSize
     *            the length of one (squared) cell
     */
    @SuppressWarnings("unchecked")
    public HashGrid(final short x_min, final short x_max, final short y_min, final short y_max, final short cellSize)
    {
        this.cellSize = cellSize;

        length = (x_max - x_min) / cellSize;
        width = (y_max - y_min) / cellSize;

        xFactor = 1d / cellSize;
        yFactor = xFactor * width;

        cells = new ArrayList[length * width];
    }

    /**
     * Add all points in input to the respective
     * cells.
     * 
     * @param points
     */
    public void addAll(final Point... points)
    {
        for (int i = 0; i < points.length; i++)
            add(points[i]);
    }

    /**
     * Add all points in input to the respective cells.
     * 
     * @param points
     */
    public void addAll(final List<Point> points)
    {
        for (final Point p : points)
            add(p);
    }

    /**
     * Add a single Point to its cell.
     * 
     * @param p
     */
    public void add(final Point p)
    {
        final int index = getIndex(p);

        if (cells[index] == null)
            cells[index] = new ArrayList<Point>();

        cells[index].add(p);
    }

    /**
     * Returns all points sharing the same
     * coordinates of p.
     * 
     * @param p
     */
    public List<Point> getCollisions(final Point p)
    {
        final int index = getIndex(p);

        if (cells[index] == null || cells[index].isEmpty())
            return null;

        final ArrayList<Point> points = cells[index];

        final ArrayList<Point> collidingPoints = new ArrayList<Point>(points.size());

        for (final Point p2 : points)
            if (p.getX() == p2.getX() && p.getY() == p2.getY() && p.getZ() == p2.getZ())
                collidingPoints.add(p2);

        return collidingPoints;
    }

    /**
     * Removes all points from all cells.
     */
    public void clearAll()
    {
        for (final ArrayList<Point> cell : cells)
            if (cell != null)
                cell.clear();
    }

    private int getIndex(final Point p)
    {
        return (int) (p.getX() * xFactor + p.getY() * yFactor);
    }
}
