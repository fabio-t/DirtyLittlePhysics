import maps.SimpleMap;
import utils.Vect3D;
import engine.Particle;
import engine.Simulator;

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

/**
 * 
 * @author Fabio Ticconi
 */
public class TextSimulation
{
    public static void main(final String[] args)
    {
        final double STEP = 0.01d;

        final Simulator w = new Simulator(new SimpleMap(-100, 100, -100, 100, -100, 100));

        final Particle p = new Particle();

        p.setCenter(new Vect3D(0.0, 0.0, 10000.0));
        p.setMass(70d);
        p.setRadius(0.2505887894d);

        // System.out.println("Mass: " + p.getMass() + ", Radius: " + p.radius + ", Density: " + p.density);
        // p.vel.x = 5d;
        // System.out.println("First particle: initial values");
        // System.out.println("Pos: " + p.center.getX() + " " + p.center.getY() + " " + p.center.getZ());
        // System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        // System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        // w.addParticle(p);
        // w.update(STEP);
        // System.out.println("After 1 step (" + STEP + " seconds):");
        // System.out.println("Pos: " + p.center.getX() + " " + p.center.getY() + " " + p.center.getZ());
        // System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        // System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        // w.update(STEP);
        // System.out.println("After 1 step (" + STEP + " seconds):");
        // System.out.println("Pos: " + p.center.getX() + " " + p.center.getY() + " " + p.center.getZ());
        // System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        // System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        // w.update(STEP);
        // System.out.println("After 1 step (" + STEP + " seconds):");
        // System.out.println("Pos: " + p.center.getX() + " " + p.center.getY() + " " + p.center.getZ());
        // System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        // System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        //
        // long time1 = System.currentTimeMillis();
        // System.out.print("Adding particles.. ");
        // for (int i = 0; i < 100000; i++)
        // {
        // final Particle p2 = new Particle();
        // p2.vel.x = 1d;
        // w.addParticle(p2);
        // }
        // long time2 = System.currentTimeMillis();
        // System.out.println((time2 - time1) + " ms");
        //
        // time1 = System.currentTimeMillis();
        // System.out.print("Moving forward the simulation.. ");
        // for (double t = 0d; t < 1d; t = t + STEP)
        // w.update(STEP);
        // time2 = System.currentTimeMillis();
        // System.out.println((time2 - time1) + " ms");
        //
        // System.out.println("Pos: " + p.center.getX() + " " + p.center.getY() + " " + p.center.getZ());
        // System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        // System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        //
        // time1 = System.currentTimeMillis();
        // System.out.print("Moving forward the simulation.. ");
        // for (double t = 0d; t < 10d; t = t + STEP)
        // w.update(STEP);
        // time2 = System.currentTimeMillis();
        // System.out.println((time2 - time1) + " ms");
        //
        // System.out.println("Pos: " + p.center.getX() + " " + p.center.getY() + " " + p.center.getZ());
        // System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        // System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        //
        // time1 = System.currentTimeMillis();
        // System.out.print("Moving forward the simulation.. ");
        // for (double t = 0d; t < 10d; t = t + STEP)
        // w.update(STEP);
        // time2 = System.currentTimeMillis();
        // System.out.println((time2 - time1) + " ms");
        //
        // System.out.println("Pos: " + p.center.getX() + " " + p.center.getY() + " " + p.center.getZ());
        // System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        // System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        //
        // time1 = System.currentTimeMillis();
        // System.out.print("Moving forward the simulation.. ");
        // for (double t = 0d; t < 100d; t = t + STEP)
        // w.update(STEP);
        // time2 = System.currentTimeMillis();
        // System.out.println((time2 - time1) + " ms");
        //
        // System.out.println("Pos: " + p.center.getX() + " " + p.center.getY() + " " + p.center.getZ());
        // System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        // System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        //
        // time1 = System.currentTimeMillis();
        // System.out.print("Moving forward the simulation.. ");
        // for (double t = 0d; t < 100d; t = t + STEP)
        // w.update(STEP);
        // time2 = System.currentTimeMillis();
        // System.out.println((time2 - time1) + " ms");
        //
        // System.out.println("Pos: " + p.center.getX() + " " + p.center.getY() + " " + p.center.getZ());
        // System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        // System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
        //
        // time1 = System.currentTimeMillis();
        // System.out.print("Moving forward the simulation.. ");
        // for (double t = 0d; t < 1000d; t = t + STEP)
        // w.update(STEP);
        // time2 = System.currentTimeMillis();
        // System.out.println((time2 - time1) + " ms");
        //
        // System.out.println("Pos: " + p.center.getX() + " " + p.center.getY() + " " + p.center.getZ());
        // System.out.println("Vel: " + p.vel.x + " " + p.vel.y + " " + p.vel.z);
        // System.out.println("Acc: " + p.acc.x + " " + p.acc.y + " " + p.acc.z);
    }
}
