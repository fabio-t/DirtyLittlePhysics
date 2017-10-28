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
package utils;

/**
 * @author Fabio Ticconi
 */
public class ImmutableVect3D
{
    public static final ImmutableVect3D zero     = new ImmutableVect3D(0.0, 0.0, 0.0);
    public static final ImmutableVect3D unit     = new ImmutableVect3D(1.0, 1.0, 1.0);
    public static final ImmutableVect3D xaxis    = new ImmutableVect3D(1.0, 0.0, 0.0);
    public static final ImmutableVect3D xaxisinv = new ImmutableVect3D(-1.0, 0.0, 0.0);
    public static final ImmutableVect3D yaxis    = new ImmutableVect3D(0.0, 1.0, 0.0);
    public static final ImmutableVect3D yaxisinv = new ImmutableVect3D(0.0, -1.0, 0.0);
    public static final ImmutableVect3D zaxis    = new ImmutableVect3D(0.0, 0.0, 1.0);
    public static final ImmutableVect3D zaxisinv = new ImmutableVect3D(0.0, 0.0, -1.0);

    public final double x;
    public final double y;
    public final double z;

    public ImmutableVect3D(final double x, final double y, final double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ImmutableVect3D(final ImmutableVect3D v)
    {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public ImmutableVect3D(final Vect3D v)
    {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    @Override
    public String toString()
    {
        return String.format("i(%f, %f, %f)", x, y, z);
    }
}
