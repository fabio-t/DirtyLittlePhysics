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
package collision;

import java.util.ArrayList;
import java.util.List;

import utils.Vect3D;

/**
 * A ArrayGrid2D implementation that inserts 3-dimensional
 * objects into 1D cells, but only using two components.
 * This allows for fast broad-phase collision detection.
 * 
 * @author Fabio Ticconi
 */
public class ArrayGrid2D
{
    int                       cellSize;
    double                    xFactor;
    double                    yFactor;

    int                       length;
    int                       width;

    ArrayList<StaticObject>[] cells;

    /**
     * Creates a new Grid World with the given dimensions
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
     * @param x
     *            the absolute "length" of the world
     * @param y
     *            the absolute "width" of the world
     * @param cellSize
     *            the length of one (square) cell
     */
    @SuppressWarnings("unchecked")
    public ArrayGrid2D(final short x_min, final short x_max, final short y_min, final short y_max, final short cellSize)
    {
        this.cellSize = cellSize;

        length = (x_max - x_min) / cellSize;
        width = (y_max - y_min) / cellSize;

        xFactor = 1.0 / cellSize;
        yFactor = xFactor * width;

        cells = new ArrayList[length * width];
    }

    /**
     * Add all StaticObjects in input to the respective
     * cells.
     * 
     * @param Objects
     */
    public void addAll(final StaticObject... Objects)
    {
        for (int i = 0; i < Objects.length; i++)
            add(Objects[i]);
    }

    /**
     * Add all StaticObjects in input to the respective cells.
     * 
     * @param Objects
     */
    public void addAll(final List<StaticObject> Objects)
    {
        for (final StaticObject p : Objects)
            add(p);
    }

    /**
     * Add a single StaticObject to its cell.
     * 
     * @param p
     */
    public void add(final StaticObject p)
    {
        final int index = getIndex(p);

        if (cells[index] == null)
            cells[index] = new ArrayList<StaticObject>();

        // FIXME: should we check that the box is not bigger than a cell?

        cells[index].add(p);
    }

    /**
     * Returns all StaticObjects overlapping with
     * the given object.
     * 
     * @param p
     */
    public List<StaticObject> getCollisions(final StaticObject p)
    {
        final int index = getIndex(p);

        final List<StaticObject> objects = getObjectsAround(index);

        final ArrayList<StaticObject> collidingObjects = new ArrayList<StaticObject>(objects.size());

        for (final StaticObject p2 : objects)
            if (p2.intersects(p))
                collidingObjects.add(p2);

        return collidingObjects;
    }

    public List<StaticObject> getCollisions(final Vect3D p)
    {
        final int index = getIndex(p);

        final List<StaticObject> objects = getObjectsAround(index);

        final ArrayList<StaticObject> collidingObjects = new ArrayList<StaticObject>(objects.size());

        for (final StaticObject p2 : objects)
            if (p2.intersects(p))
                collidingObjects.add(p2);

        return collidingObjects;
    }

    // TODO
    public List<StaticObject> getObjectsAround(final int index)
    {
        final ArrayList<StaticObject> objects = new ArrayList<StaticObject>();

        for (int i = -1; i <= 1; i++)
            for (int ii = -1; ii <= 1; ii++)
                if (cells[getIndex(index * i, index * ii)] != null)
                    objects.addAll(cells[index]);

        return objects;
    }

    /**
     * Removes all StaticObjects from all cells.
     */
    public void clearAll()
    {
        for (final ArrayList<StaticObject> cell : cells)
            if (cell != null)
                cell.clear();
    }

    private int getIndex(final int x, final int y)
    {
        return (int) (x * xFactor + y * yFactor);
    }

    private int getIndex(final Vect3D p)
    {
        return (int) (p.x * xFactor + p.y * yFactor);
    }

    private int getIndex(final StaticObject p)
    {
        final Vect3D c = p.getCenter();

        return getIndex(c);
    }
}
