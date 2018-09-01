package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;

abstract public class Engine implements WindowListener {
    //constants
    private final Dimension dimension = new Dimension(800, 600);
    private final long EXPECTED_FRAMES = 60;
    private final long OPTIMAL_TIME = 1000 / EXPECTED_FRAMES;
    //constants
    public int fps = 0;
    private JFrame mainFrame;
    private String gameName = "Engine V1";
    private BufferStrategy bufferStrategy;
    private boolean active;

    public void run() {
        active = true;
        load();
        long now, lastLoop = System.currentTimeMillis();
        while (active) {
            now = System.currentTimeMillis();
            update();
            render();
            fps++;
            if (now >= lastLoop + 1000 || fps == 60) {
                // DEBUG
                System.out.println("FPS: " + fps);
                System.out.println("PASSED: " + ((lastLoop + 1000) - now));
                System.out.println("DIFF: " + (now - lastLoop));
                System.out.println("---------------");
                // DEBUG
                //FPS LIMITER
                long delta = (lastLoop + 1000) - now;
                try {Thread.sleep(delta);} catch (Exception e) {}
                //FPS LIMITER
                //RESTART
                lastLoop = now;
                fps = 0;
                //RESTART
            }
            //try{Thread.sleep(OPTIMAL_TIME);}catch (Exception e){}
        }
    }

    public Engine() {
        mainFrame = new JFrame(gameName);
        mainFrame.setSize(dimension);
        mainFrame.addWindowListener(this);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        active = false;
    }

    public void Terminate() {
        active = false;
    }

    public void update() {
        onUpdate();
        Thread.yield();
    }

    public void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        onRender(g);
        g.dispose();
        bufferStrategy.show();
    }

    public void load() {
        mainFrame.setIgnoreRepaint(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.createBufferStrategy(2);
        bufferStrategy = mainFrame.getBufferStrategy();
        onLoad();
    }

    public void unload() {
        onUnload();
        bufferStrategy.dispose();
        mainFrame.dispose();
    }

    //Getters
    public int getWidth() {
        return mainFrame.getWidth();
    }

    public int getHeight() {
        return mainFrame.getHeight();
    }

    //on game methods
    abstract public void onLoad();

    abstract public void onUnload();

    abstract public void onUpdate();

    abstract public void onRender(Graphics2D g);
}
