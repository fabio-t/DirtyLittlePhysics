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

import utils.Vect3D;

/**
 * Manages all 3D terrain data, allowing the physics to retrieve
 * Cell types.
 * 
 * @author Fabio Ticconi
 */
public interface Map
{
    /**
     * Returns true if the Vect3D is outside of the Map,
     * false otherwise.
     * 
     * @param p
     * @return
     */
    public boolean isOverBounds(final Vect3D p);

    /**
     * Returns the Cell object including the given Vect3D.
     * 
     * @param p
     *            a {@link Vect3D} to get the Cell of
     */
    public Cell getCell(Vect3D p);

    /**
     * Returns true if the two Vect3Ds belong to the same cell.
     * 
     * @param p1
     * @param p2
     * @return
     */
    public boolean areNeighbours(final Vect3D p1, final Vect3D p2);

    /**
     * Returns true if the Vect3D can move to the destination,
     * false otherwise.
     * 
     * @param from
     * @param to
     * @return
     */
    public boolean canMoveTo(final Vect3D from, final Vect3D to);

    /**
     * If we are trying to move outside the world or within
     * unreacheable zones, the new position is corrected as
     * conservatively as possible.
     * 
     * @param from
     * @param to
     */
    public void clampMovement(final Vect3D from, final Vect3D to);
}
