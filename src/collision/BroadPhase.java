/*
 * Copyright (c) 2014-2017 Fabio Ticconi
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package collision;

import utils.Vect3D;

import java.util.List;

/**
 * Interface to a broad phase collision system.
 * Concrete classes should aim to provide as fast as possible
 * implementations of these methods.
 *
 * @author Fabio Ticconi
 */
public interface BroadPhase
{
    void add(final Static s);

    void remove(final Static s);

    List<Static> getPossibleCollisions(final Vect3D p);

    List<Static> getCollisions(final Vect3D p);
}
