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
package utils;

/**
 * This class implements some coordinate management utilities in
 * a four-dimensional grid world.
 * <p>
 * <br />
 * <br />
 * <p>
 * It uses quite some bitwise voodoo. Because of java's horrible lack of
 * unsigned primitives, and since I don't want to make this class even
 * lower-level, the allowed range for each of the four coordinates is [0, 2^15 -
 * 1].
 * <p>
 * <br />
 * <br />
 * <p>
 * This means half the bits for each dimension are wasted. A pity.
 *
 * @author Fabio Ticconi
 */
class Coords
{
    /**
     * The mask is a <b>long</b> with the 16 least significant bits on, the rest
     * off.
     */
    private static final long MASK = Short.MAX_VALUE;

    /**
     * Generates four coordinates from a <b>long</b> key.<br />
     * The coordinates are in the following order (from index <i>0</i>): u, x,
     * y, z.
     *
     * @param key the compacted <b>long</b>
     * @return the array of <b>short</b> coordinates in the following order
     * (from index <i>0</i>): u, x, y, z.
     */
    public static short[] makeCoords(final long key)
    {
        final short[] coords = new short[4];

        coords[0] = (short) ((key & MASK << 48) >> 48);
        coords[1] = (short) ((key & MASK << 32) >> 32);
        coords[2] = (short) ((key & MASK << 16) >> 16);
        coords[3] = (short) (key & MASK);

        return coords;
    }

    /**
     * Generates a <b>long</b> key from the four coordinates.
     *
     * @param u
     * @param x
     * @param y
     * @param z
     * @return the generated <b>long</b> key
     */
    public static long makeKey(final short u, final short x, final short y, final short z)
    {

        return (long) u << 48 | (long) x << 32 | (long) y << 16 | (long) z;
    }
}
