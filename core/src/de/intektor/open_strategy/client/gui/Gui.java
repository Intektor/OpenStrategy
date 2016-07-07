package de.intektor.open_strategy.client.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.gui.component.GuiButton;
import de.intektor.open_strategy.client.gui.component.GuiComponent;
import de.intektor.open_strategy.client.gui.component.GuiMultiSwitch;
import de.intektor.open_strategy.client.gui.component.GuiTextField;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Intektor
 */
public abstract class Gui extends InputAdapter {

    public List<GuiComponent> componentList = new ArrayList<GuiComponent>();

    public Input input;

    protected int width;
    protected int height;
    protected long timeAtLastTap;

    protected OpenStrategy os;

    public Gui() {
    }

    public void init() {
        os = OpenStrategy.getOpenStrategy();
        input = Gdx.input;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        input.setInputProcessor(multiplexer);
        reInitButtons();
    }

    public void render(ShapeRenderer renderer, int mouseX, int mouseY) {
        GuiComponent prioritizedComponent = getPrioritizedComponent();
        for (GuiComponent component : this.componentList) {
            if (component != null && component != prioritizedComponent) {
                component.render(renderer, mouseX, mouseY);
            }
        }
        if (prioritizedComponent != null) {
            prioritizedComponent.render(renderer, mouseX, mouseY);
        }
    }

    public void update() {
        for (GuiComponent component : componentList) {
            component.updateComponent(input.getX(), input.getY());
        }
    }

    public void reInitButtons() {
        this.componentList.clear();
        this.addComponents();
    }

    public abstract void onButtonTouched(int id);

    public abstract int getID();

    public abstract void addComponents();

    public void addComponent(GuiComponent component) {
        componentList.add(component);
        component.setGui(this);
    }

    public GuiButton getButtonByID(int id) {
        for (GuiComponent c : componentList) {
            if (c instanceof GuiButton) {
                if (((GuiButton) c).id == id) {
                    return (GuiButton) c;
                }
            }
        }
        return null;
    }

    public GuiTextField getTextFieldByID(int id) {
        for (GuiComponent c : componentList) {
            if (c instanceof GuiTextField) {
                if (((GuiTextField) c).getId() == id) {
                    return (GuiTextField) c;
                }
            }
        }
        return null;
    }

    public GuiMultiSwitch getMultiSwitchByID(int id) {
        for (GuiComponent c : componentList) {
            if (c instanceof GuiMultiSwitch) {
                if (((GuiMultiSwitch) c).id == id) {
                    return (GuiMultiSwitch) c;
                }
            }
        }
        return null;
    }

    public void setComponentPrioritized(GuiComponent component) {
        for (GuiComponent guiComponent : componentList) {
            if (guiComponent == component) {
                guiComponent.setPrioritized(true);
            } else {
                guiComponent.setPrioritized(false);
            }
        }
    }

    @Override
    public boolean scrolled(int amount) {
        GuiComponent prioritizedComponent = getPrioritizedComponent();
        for (GuiComponent guiComponent : componentList) {
            guiComponent.mouseScrolled(Gdx.input.getX(), Gdx.input.getY(), amount, guiComponent == prioritizedComponent);
        }
        return false;
    }

    public boolean clickedOnComponent(int x, int y) {
        for (GuiComponent component : componentList) {
            if (component instanceof GuiButton) {
                if (component.isHoveredOver(x, y) && ((GuiButton) component).enabled) {
                    return true;
                }
            } else if (component instanceof GuiTextField) {
                if (component.isHoveredOver(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public GuiComponent getPrioritizedComponent() {
        for (GuiComponent guiComponent : componentList) {
            if (guiComponent.isPrioritized()) {
                return guiComponent;
            }
        }
        return null;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean success = false;
        GuiComponent prioritizedComponent = getPrioritizedComponent();
        for (GuiComponent component : componentList) {
            if (prioritizedComponent == null || component == prioritizedComponent) {
                component.clicked(screenX, screenY, button);
                if (component instanceof GuiButton && ((GuiButton) component).enabled) {
                    if (component.isHoveredOver(screenX, screenY)) {
                        setComponentPrioritized(null);
                        component.clickedOnComponent(screenX, screenY, button);
                        onButtonTouched(((GuiButton) component).id);
                        componentClicked(component, screenX, screenY);
                        success = true;
                    }
                } else if (component instanceof GuiTextField) {
                    if (component.isHoveredOver(screenX, screenY)) {
                        component.clickedOnComponent(screenX, screenY, button);
                        success = true;
                        componentClicked(component, screenX, screenY);
                    } else {
                        if (component == prioritizedComponent) {
                            setComponentPrioritized(null);
                        }
                    }
                } else {
                    if (component.isHoveredOver(screenX, screenY)) {
                        component.clickedOnComponent(screenX, screenY, button);
                        componentClicked(component, screenX, screenY);
                    } else {
                        setComponentPrioritized(null);
                    }
                }
            }
        }
        prevX = screenX;
        prevY = screenY;
        return success;
    }

    public void textFieldDeactivated(GuiTextField field) {
    }

    public void multiSwitchChanged(GuiMultiSwitch multiSwitch, int prevChoice, int currentChoice) {
    }

    @Override
    public boolean keyTyped(char character) {
        GuiComponent prioritizedComponent = getPrioritizedComponent();
        for (GuiComponent guiComponent : componentList) {
            guiComponent.keyTyped(character, prioritizedComponent == guiComponent);
        }
        return super.keyTyped(character);
    }

    @Override
    public boolean keyDown(int keycode) {
        GuiComponent prioritizedComponent = getPrioritizedComponent();
        for (GuiComponent guiComponent : componentList) {
            guiComponent.keyDown(keycode, guiComponent == prioritizedComponent);
        }
        return super.keyDown(keycode);
    }

    public List<GuiTextField> getTextFields() {
        List<GuiTextField> list = new ArrayList<GuiTextField>();
        for (GuiComponent component : componentList) {
            if (component instanceof GuiTextField) {
                list.add((GuiTextField) component);
            }
        }
        return list;
    }

    protected int prevX, prevY;

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for (GuiComponent component : componentList) {
            if (component.isHoveredOver(screenX, screenY)) {
                component.onDragged(prevX, prevY, screenX, screenY);
            }
        }
        prevX = screenX;
        prevY = screenY;
        return true;
    }

    public boolean hoveredOverComponent(int x, int y) {
        for (GuiComponent component : componentList) {
            if (component.isHoveredOver(x, y)) return true;
        }
        return false;
    }

    /**
     * Returns the ID of the hovered button. Special case: -1 if no button hovered
     *
     * @param x the mouseX
     * @param y the mouseY
     * @return the id of the hovered button or -1
     */
    public int hoveredButtonID(int x, int y) {
        for (GuiComponent component : componentList) {
            if (component instanceof GuiButton && component.isHoveredOver(x, y))
                return ((GuiButton) component).id;
        }
        return -1;
    }

    public GuiComponent getHoveredComponent(int x, int y) {
        for (GuiComponent component : componentList) {
            if (component.isHoveredOver(x, y)) return component;
        }
        return null;
    }

    public void componentClicked(GuiComponent component, int mouseX, int mouseY) {
    }

    public static boolean isPointInRegion(int x, int y, int x2, int y2, int px, int py) {
        return px >= x && px <= x2 && py >= y && py <= y2;
    }
}
