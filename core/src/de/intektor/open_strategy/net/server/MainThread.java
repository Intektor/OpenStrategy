package de.intektor.open_strategy.net.server;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class MainThread extends Thread {

    OpenStrategyServer server;
    List<Runnable> scheduledTasks = new ArrayList<>();

    public MainThread(OpenStrategyServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (server.runServer) {
            serverTick();
        }
    }

    public void serverTick() {
        scheduledTasks.forEach(Runnable::run);
        scheduledTasks.clear();
    }

    public void addScheduledTask(Runnable task) {
        scheduledTasks.add(task);
    }
}
