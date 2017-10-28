/*
  Copyright 2015 Fabio Ticconi
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package environment;

import engine.Particle;
import utils.ImmutableVect3D;
import utils.Vect3D;

/**
 * Interface to the "environment".
 * Concrete classes should implement all influences of
 * their environment on the Particle, for example by
 * accumulating environmental forces (gravity, buoyancy etc).
 *
 * @author Fabio Ticconi
 */
public interface World
{
    /**
     * Applies environmental forces and corrects the position
     * (as conservatively as possible).
     *
     * @param p
     * @param dt
     */
    void process(final Particle p, final double dt);

    /**
     * Returns the environmental forces acting on the particle.
     *
     * @param p
     * @param dt
     * @return
     */
    Vect3D getForces(final Particle p, final double dt);

    /**
     * Returns the gravity currently set for this world.
     *
     * @return an {@link ImmutableVect3D} representing the gravity as acceleration
     */
    ImmutableVect3D getGravity();

    /**
     * Sets the gravity as an <b>acceleration</b>
     * vector.<br />
     * Example: for Earth, the vector would be (0, 0, -9.81).
     * Incidentally, this is also the default value if
     * this method is not called.
     *
     * @param gravity an {@link ImmutableVect3D} that will
     *                overwrite the current gravity
     */
    void setGravity(final ImmutableVect3D gravity);
}
