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
package collision.broadphase;

import java.util.ArrayList;
import java.util.List;

import shapes.Shape;
import utils.Vect3D;
import collision.BroadPhase;

/**
 * A ArrayGrid2D implementation that inserts 3-dimensional
 * objects into 1D maps.cells, but only using two components.
 * This allows for fast broad-phase collision detection.
 * 
 * @author Fabio Ticconi
 */
public class ArrayGrid2D implements BroadPhase
{
    int                cellSize;
    double             invCellSize;

    int                rows;
    int                cols;

    int                offset;

    ArrayList<Shape>[] cells;

    /**
     * Creates a grid with the given dimensions
     * and cell size. The objects will be partitioned
     * in the respective cell using their own positions.
     * For simplicity, only two of the three dimensions
     * are taken into consideration when creating the ArrayGrid2D
     * (generally speaking, the missing one should be the Up-Down
     * dimension, so that a "cell" actually represents all objects
     * in a specific 3D column in the world. The assumption is that
     * underground and overground objects are rare).
     * 
     * <br />
     * <br />
     * 
     * Note how the dimensions of the world are short variables.
     * With big enough cells a short is large enough, and it doesn't
     * require an hash function. If you hit a limit
     * because of world size, or you want to use additional dimensions,
     * then you need to create a hash function and "module" it down
     * to a fixed number of buckets. Know that collision detection
     * will be slightly trickier than here. An excellent source of
     * information in case you want to go down that route is
     * Ericson, "Real-time collision detection".
     * 
     * @param cellSize
     *            the length of one (square) cell
     */
    @SuppressWarnings("unchecked")
    public ArrayGrid2D(final short x_min, final short x_max, final short y_min, final short y_max, final short cellSize)
    {
        this.cellSize = cellSize;

        cols = (int) Math.ceil((double) (x_max - x_min) / cellSize);
        rows = (int) Math.ceil((double) (y_max - y_min) / cellSize);

        offset = -y_min * cols - x_min;

        invCellSize = 1.0 / cellSize;

        // FIXME: check why +1
        cells = new ArrayList[cols * rows + 1];

        // System.out.format("cols: %d, rows: %d, cells: %d\n", cols, rows, cols * rows + 1);
    }

    /**
     * Add all Shapes in input to the respective
     * maps.cells.
     * 
     * @param Objects
     */
    public void addAll(final Shape... Objects)
    {
        for (int i = 0; i < Objects.length; i++)
            add(Objects[i]);
    }

    /**
     * Add all Shapes in input to the respective maps.cells.
     * 
     * @param Objects
     */
    public void addAll(final List<Shape> Objects)
    {
        for (final Shape p : Objects)
            add(p);
    }

    /**
     * Add a single Shape to its cell.
     * 
     * @param p
     */
    @Override
    public void add(final Shape s)
    {
        final Vect3D extent = s.getExtent();

        if (extent.x > cellSize / 2.0 || extent.y > cellSize / 2.0)
        {
            System.out.format("ERROR: object '%s' is bigger than a cell", s);

            return;
        }

        final int index = getIndex(s);

        // System.out.println("s: " + s + " -> " + index);

        if (cells[index] == null)
            cells[index] = new ArrayList<Shape>();

        cells[index].add(s);
    }

    /*
     * (non-Javadoc)
     * 
     * @see collision.BroadPhase#remove(shapes.Shape)
     */
    @Override
    public void remove(final Shape s)
    {
        final int index = getIndex(s);

        if (cells[index] == null)
            return;

        cells[index].remove(s);
    }

    /**
     * Returns all Shapes overlapping with
     * the given object.
     * 
     * @param p
     */
    public List<Shape> getCollisions(final Shape b)
    {
        final int index = getIndex(b);

        final List<Shape> objects = getObjectsAround(index);

        final ArrayList<Shape> collidingObjects = new ArrayList<Shape>(objects.size());

        for (final Shape obj : objects)
            if (obj.intersects(b))
                collidingObjects.add(obj);

        return collidingObjects;
    }

    @Override
    public List<Shape> getCollisions(final Vect3D p)
    {
        final int index = getIndex(p);

        // System.out.println("p: " + p + " -> " + index);

        final List<Shape> objects = getObjectsAround(index);

        final ArrayList<Shape> collidingObjects = new ArrayList<Shape>(objects.size());

        for (final Shape obj : objects)
            if (obj.intersects(p))
                collidingObjects.add(obj);

        return collidingObjects;
    }

    /*
     * (non-Javadoc)
     * 
     * @see collision.BroadPhase#getPossibleCollisions(utils.Vect3D)
     */
    @Override
    public List<Shape> getPossibleCollisions(final Vect3D p)
    {
        final int index = getIndex(p);

        // System.out.println("p: " + p + " -> " + index);

        final List<Shape> objects = getObjectsAround(index);

        return objects;
    }

    public List<Shape> getObjectsAround(final int index)
    {
        final int x = index / rows;
        final int y = index % rows;

        int tempx;
        int tempy;
        int temp;

        final ArrayList<Shape> objects = new ArrayList<Shape>();

        for (int i = -1; i <= 1; i++)
        {
            tempx = x + i;

            // FIXME: was tempx >= cols: check why not
            if (tempx < 0 || tempx > cols)
                continue;

            for (int ii = -1; ii <= 1; ii++)
            {
                tempy = y + ii;

                if (tempy < 0 || tempy >= rows)
                    continue;

                temp = tempx * rows + tempy;

                if (temp < 0 || temp >= cells.length)
                    continue;

                if (cells[temp] != null)
                    objects.addAll(cells[temp]);
            }
        }

        return objects;
    }

    /**
     * Removes all stored objects.
     */
    public void clearAll()
    {
        for (final ArrayList<Shape> cell : cells)
            if (cell != null)
                cell.clear();
    }

    private int getIndex(final Vect3D p)
    {
        return (int) Math.floor((p.y * cols + p.x + offset) * invCellSize);
    }

    private int getIndex(final Shape s)
    {
        return getIndex(s.getCenter());
    }
}
