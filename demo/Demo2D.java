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
 * Taken from: http://www.java-gaming.org/index.php?topic=26885.0
 * and adapted.
 * 
 * @author Fabio Ticconi
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;

import maps.Cell;
import maps.SimpleMap;
import shapes.Box;
import utils.Vect3D;
import collision.BroadPhase;
import collision.broadphase.ArrayGrid2D;
import engine.Particle;
import engine.Simulator;

public class Demo2D extends JFrame
{
    private static final long                     serialVersionUID = -6027400479565797010L;

    private static boolean                        VERBOSE          = false;

    private final ArrayList<Particle>             particles        = new ArrayList<Particle>(500);
    private final ConcurrentLinkedQueue<Particle> newParticles     = new ConcurrentLinkedQueue<Particle>();

    private final ArrayList<Box>                  objects          = new ArrayList<Box>(500);
    private final ConcurrentLinkedQueue<Box>      newObjects       = new ConcurrentLinkedQueue<Box>();

    private BufferStrategy                        bufferstrat      = null;
    private final Canvas                          render;

    private final Simulator                       simulator;

    private final int                             width;
    private final int                             height;

    private final SimpleMap                       world;
    private final BroadPhase                      collider;

    private final int                             NUM_PARTICLES;

    public Demo2D(final int width, final int height, final String title)
    {
        super();
        setTitle(title);
        setIgnoreRepaint(true);
        setResizable(false);

        this.width = width;
        this.height = height;

        render = new Canvas();
        render.setIgnoreRepaint(true);
        int nHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int nWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        nHeight /= 2;
        nWidth /= 2;

        setBounds(nWidth - (width / 2), nHeight - (height / 2), width, height);
        render.setBounds(nWidth - (width / 2), nHeight - (height / 2), width, height);

        add(render);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        render.createBufferStrategy(2);
        bufferstrat = render.getBufferStrategy();

        NUM_PARTICLES = 1;

        world = new SimpleMap(-width / 2, width / 2, -1, 1, -height / 2, height / 2);
        collider = new ArrayGrid2D((short) (-width / 2), (short) (width / 2), (short) -10, (short) 10, (short) 150);
        simulator = new Simulator(world, collider);
    }

    public Vect3D transformToSim(final Point p)
    {
        // top left corner is (0,0)
        // bottom right corner is (width,height)

        // must become: top left = (-width/2,height/2)
        // bottom right = (width/2,-height/2)

        final Vect3D newP = new Vect3D();

        newP.x = p.x - (width / 2.0);
        newP.z = -p.y + (height / 2.0);

        return newP;
    }

    public Vect3D transformToGraphics(final Vect3D p)
    {
        // top left corner is (-width/2,height/2)
        // bottom right corner is (width/2,-height/2)

        // must become: top left = (0,0)
        // bottom right = (width,height)

        final Vect3D newP = new Vect3D();

        newP.x = p.x + (width / 2.0);
        newP.z = -(p.z - (height / 2.0));

        return newP;
    }

    public void addParticle()
    {
        final Point pos = render.getMousePosition();

        if (pos != null)
        {
            final Vect3D realPos = transformToSim(pos);

            final Particle p = new Particle();

            p.setRadius(Math.random() / 5.0 + 0.1);
            p.setMass(Math.random() * 20.0 + 50.0);
            p.setBounciness(Math.random() * 2.0);
            // p.setMass(100.0);
            // p.setRadius(0.25);
            // p.setBounciness(1.0);

            p.setCenter(realPos);
            // p.setVelocity(new Vect3D(Math.random() * 50 - 25, 0.0, 0.0));

            newParticles.add(p);

            if (VERBOSE)
                System.out.println("Adding new particle at " + p.getCenter().x + ", " + p.getCenter().z);
        }
    }

    public void addBox()
    {
        final Point pos = render.getMousePosition();

        if (pos != null)

        {
            final Vect3D realPos = transformToSim(pos);

            final Box o = new Box(realPos, realPos); // fake min-max

            final Vect3D extent = new Vect3D(Math.random() + 0.1, 0.5, Math.random() + 0.1).mul(50);
            o.setCenterExtent(realPos, extent); // will correct min-max too

            newObjects.add(o);

            if (VERBOSE)
                System.out.println("Adding new static object at " + o.getCenter().x + ", " + o.getCenter().z);
        }
    }

    public void pollInput()
    {
        render.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseClicked(final MouseEvent e)
            {
                if (e.isShiftDown())
                    addBox();
                else
                    for (int i = 0; i < NUM_PARTICLES; i++)
                        addParticle();
            }

            @Override
            public void mouseEntered(final MouseEvent e)
            {

            }

            @Override
            public void mouseExited(final MouseEvent e)
            {

            }

            @Override
            public void mousePressed(final MouseEvent e)
            {

            }

            @Override
            public void mouseReleased(final MouseEvent e)
            {

            }

        });
    }

    private void processInput()
    {
        final Particle p = newParticles.poll();
        final Box o = newObjects.poll();

        if (o != null)
        {
            objects.add(o);
            collider.add(o);
        }

        if (p != null)
        {
            simulator.addParticle(p);
            particles.add(p);
        }
    }

    public void loop()
    {
        final double FPS = 60.0;
        final double frameDuration = 1000.0 / FPS;
        final double dt = frameDuration / 1000.0;

        double previousTime = System.currentTimeMillis();
        double currentTime;

        double lag = 0.0;
        double elapsed;

        while (true)
        {
            currentTime = System.currentTimeMillis();
            elapsed = currentTime - previousTime;
            previousTime = currentTime;

            lag += elapsed;

            processInput();

            while (lag >= frameDuration)
            {
                simulator.update(dt);

                lag -= frameDuration;
            }

            render();
        }
    }

    public void render()
    {
        do
        {
            do
            {
                final Graphics2D g2d = (Graphics2D) bufferstrat.getDrawGraphics();

                g2d.setColor(Color.cyan);
                final Rectangle2D.Double air = new Rectangle2D.Double(0, 0, render.getWidth(), render.getHeight() / 2);

                g2d.fill(air);

                g2d.setColor(Color.blue);
                final Rectangle2D.Double water = new Rectangle2D.Double(0, render.getHeight() / 2,
                                                                        render.getWidth() / 2, render.getHeight());
                g2d.fill(water);

                g2d.setColor(Color.black);
                final Rectangle2D.Double ground = new Rectangle2D.Double(render.getWidth() / 2, render.getHeight() / 2,
                                                                         render.getWidth(), render.getHeight());
                g2d.fill(ground);

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                renderBoxes(g2d);
                renderParticles(g2d);

                g2d.dispose();
            } while (bufferstrat.contentsRestored());
            bufferstrat.show();
        } while (bufferstrat.contentsLost());
    }

    public void renderParticles(final Graphics2D g2d)
    {
        for (final Particle p : particles)
            renderParticle(g2d, p);
    }

    public void renderBoxes(final Graphics2D g2d)
    {
        for (final Box b : objects)
            renderBox(g2d, b);
    }

    public void renderParticle(final Graphics2D g, final Particle p)
    {
        final Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.white);

        final double radius = p.getRadius() * 25.0;

        final Vect3D newPoint = transformToGraphics(p.getCenter());

        final double x = newPoint.x - radius;
        final double z = newPoint.z - radius;
        final double diameter = radius * 2;

        if (VERBOSE)
        {
            final Cell c = world.getCell(p.getCenter());

            final String celltype = c.getClass().getName();

            System.out.println("rendering particle (" +
                               celltype +
                               "): x:" +
                               x +
                               ", z:" +
                               z +
                               ", realx:" +
                               p.getCenter().x +
                               ", realz:" +
                               p.getCenter().z +
                               ", m: " +
                               p.getMass() +
                               ", d:" +
                               p.getDensity());
        }

        final Ellipse2D.Double circle = new Ellipse2D.Double(x, z, diameter, diameter);
        g2d.fill(circle);

        // g2d.dispose();
    }

    public void renderBox(final Graphics2D g, final Box b)
    {
        final Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.red);

        final Vect3D maxExtent = new Vect3D(b.getExtent());
        final Vect3D newPoint = transformToGraphics(b.getCenter());

        final double x = newPoint.x - maxExtent.x;
        final double z = newPoint.z - maxExtent.z;
        maxExtent.mul(2.0);

        if (VERBOSE)
        {
            final Cell c = world.getCell(b.getCenter());

            final String celltype = c.getClass().getName();

            System.out.println("rendering static object (" +
                               celltype +
                               "): x:" +
                               x +
                               ", z:" +
                               z +
                               ", realx:" +
                               b.getCenter().x +
                               ", realz:" +
                               b.getCenter().z +
                               ")");
        }

        final Rectangle2D.Double rect = new Rectangle2D.Double(x, z, maxExtent.x, maxExtent.z);
        g2d.fill(rect);

        // g2d.dispose();
    }

    public static void main(final String[] args)
    {
        final Demo2D window = new Demo2D(1024, 768, "Particles: ");
        window.pollInput();
        window.loop();
    }
}
