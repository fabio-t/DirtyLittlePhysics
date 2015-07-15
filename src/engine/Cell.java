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
 * Defines a generic "Cell", containing the main physical
 * properties needed for the simulation.
 */
public interface Cell
{
    /**
     * Returns a value != 1 if the cell necessitate of buoyancy-based
     * correction of gravity. This is normally true for any fluid.
     * In case of solids, the value should be 1, so that any multiplication
     * to gravity won't have any effect.
     * 
     * @return
     */
    public double getBuoyancy(Particle p);

    /**
     * Check if the particle can <i>move through</i> this block.
     * This influences both the case a particle is already inside
     * the block (and cannot go out), and when (more usually) the
     * particle is outside and cannot get in (ie, the ground).
     * 
     * @param p
     * @return
     */
    public boolean canPass(Particle p);

    /**
     * Returns the sum of all forces in this Cell that would be acting
     * on the given {@link Particle} if it was into this {@link Cell}.
     * 
     * @param p
     * @return newly instanced force vector
     */
    public Vect3D getForces(Particle p);
}
