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
package map;

import utils.ImmutableVect3D;
import utils.Vect3D;
import engine.Particle;

/**
 * Manages all 3D terrain data, allowing the physics to retrieve
 * Cell types.
 * 
 * @author Fabio Ticconi
 */
public interface World
{
    /**
     * Returns the Cell object including the given Vect3D.
     * 
     * @param p
     *            a {@link Vect3D} to get the Cell of
     */
    public Cell getCell(Vect3D p);

    /**
     * If we are trying to move outside the world or within
     * unreacheable zones, the new position is corrected as
     * conservatively as possible.
     * 
     * @param p
     *            initial position
     */
    public void correctPosition(final Particle p);

    /**
     * Returns the gravity currently set for this world.
     * 
     * @return an {@link ImmutableVect3D} representing the gravity as acceleration
     */
    public ImmutableVect3D getGravity();

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
    public void setGravity(final ImmutableVect3D gravity);
}
