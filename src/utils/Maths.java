/**
 * Copyright 2014 Fabio Ticconi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package utils;

/**
 * Helper class for math operations relevant to the engine.
 * 
 * @author Fabio Ticconi
 */
public class Maths
{    
    public static double sphereDensity(double mass, double radius)
    {
        return mass / sphereVolume(radius);
    }
    
    public static double sphereVolume(double radius)
    {
        return (4d / 3d) * Math.PI * radius * radius * radius;
    }
}
