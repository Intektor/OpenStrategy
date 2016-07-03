package de.intektor.open_strategy.net.server;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Intektor
 */
public class MainThread extends Thread {

    public volatile OpenStrategyServer server;
    public volatile Queue<Runnable> tasks = new LinkedBlockingQueue<>();

    public MainThread(OpenStrategyServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (server.runServer) {
            serverTick();
        }
    }

    public synchronized void serverTick() {
        Runnable t;
        while ((t = tasks.poll()) != null) {
            t.run();
        }
    }

    public synchronized void addScheduledTask(Runnable task) {
        tasks.offer(task);
    }
}
