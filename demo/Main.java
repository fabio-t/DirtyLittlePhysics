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
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;

import utils.Vect3D;
import engine.Particle;
import engine.Simulator;
import engine.Simulator.SimpleMap;

public class Main extends JFrame
{
    private static final long                     serialVersionUID = -6027400479565797010L;

    private static boolean                        VERBOSE          = false;

    private final ArrayList<Particle>             particles        = new ArrayList<Particle>(500);
    private final ConcurrentLinkedQueue<Particle> newParticles     = new ConcurrentLinkedQueue<Particle>();

    private BufferStrategy                        bufferstrat      = null;
    private final Canvas                          render;

    private final Simulator                       simulator;

    private final int                             width;
    private final int                             height;

    public Main(final int width, final int height, final String title)
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

        simulator = new Simulator(new SimpleMap(0, width, 0, 0, 0, height));
    }

    public void addParticle()
    {
        final java.awt.Point pos = render.getMousePosition();
        if (pos != null)
        {
            final Particle p = new Particle();

            p.setRadius(Math.random() / 5.0 + 0.1);
            p.setMass(Math.random() * 20.0 + 500.0);
            p.setCenter(new Vect3D(pos.getX(), 0.0, height - pos.getY()));
            p.setVelocity(new Vect3D(Math.random() * 50 - 25, 0.0, 0.0));

            newParticles.add(p);

            if (VERBOSE)
                System.out.println("Adding new particle at " + p.getCenter().getX() + ", " + p.getCenter().getZ());
        }
    }

    public void pollInput()
    {
        render.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseClicked(final MouseEvent e)
            {
                addParticle();
                addParticle();
                addParticle();
                addParticle();
                addParticle();
                addParticle();
                addParticle();
                addParticle();
                addParticle();
                addParticle();
                addParticle();
                addParticle();
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

        if (p != null)
        {
            particles.add(p);
            simulator.addParticle(p);
        }
    }

    public void loop()
    {
        final Particle p;

        final double FPS = 20.0;
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

            // sleep for a frame if we haven't done any
            // job
            if (elapsed < 50)
                try
                {
                    if (VERBOSE)
                        System.out.println("Sleeping for " + frameDuration + " ms");

                    Thread.sleep((long) frameDuration);
                } catch (final InterruptedException e)
                {
                    e.printStackTrace();
                }

            // if (elapsed > 1000)
            // elapsed = frameDuration;

            lag += elapsed;

            processInput();

            if (lag >= frameDuration)
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
                g2d.fillRect(0, 0, render.getWidth(), render.getHeight());

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

    public void renderParticle(final Graphics2D g, final Particle p)
    {
        final Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.white);

        final double radius = p.getRadius() * 25.0;

        final double x = p.getCenter().getX() - radius;
        final double z = height - p.getCenter().getZ() - radius;
        final double diameter = radius * 2;

        if (VERBOSE)
            System.out.println("rendering particle: x:" +
                               x +
                               ", z:" +
                               z +
                               ", m: " +
                               p.getMass() +
                               ", d:" +
                               p.getDensity());

        final Ellipse2D.Double circle = new Ellipse2D.Double(x, z, diameter, diameter);
        g2d.fill(circle);

        g2d.dispose();
    }

    public static void main(final String[] args)
    {
        final Main window = new Main(800, 600, "Particles: ");
        window.pollInput();
        window.loop();
    }
}
