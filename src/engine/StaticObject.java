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
package engine;

import shapes.Box;
import shapes.Point;
import utils.Vect3D;

/**
 * Represents a static object, not participating in the
 * simulation but still placed on the world and included
 * in the collision checks.
 * 
 * <br/>
 * <br/>
 * 
 * Static objects are stored as AABB, axis-aligned
 * bounding boxes.
 * 
 * @author Fabio Ticconi
 */
public class StaticObject implements Box
{
    final Point min;
    final Point max;

    public StaticObject()
    {
        min = new Vect3D();
        max = new Vect3D();
    }

    // TODO: density, at least

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Box#getMinPoint()
     */
    public Point getMinPoint()
    {
        return min;
    }

    /*
     * (non-Javadoc)
     * 
     * @see shapes.Box#getMaxPoint()
     */
    public Point getMaxPoint()
    {
        return max;
    }
}
