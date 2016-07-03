package de.intektor.open_strategy.utils.tick;

/**
 * @author Intektor
 */
public class TickTimer {

    int maxTicks;
    int currentTicks;

    public TickTimer(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    public void tick() {
        currentTicks++;
    }

    public boolean finished() {
        return maxTicks <= currentTicks;
    }

    public void reset() {
        reset(maxTicks);
    }

    public void reset(int newMaxTicks) {
        this.maxTicks = newMaxTicks;
        currentTicks = 0;
    }
}
