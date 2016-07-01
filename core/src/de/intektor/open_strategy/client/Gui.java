package de.intektor.open_strategy.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import de.intektor.open_strategy.client.component.GuiButton;
import de.intektor.open_strategy.client.component.GuiComponent;
import de.intektor.open_strategy.client.component.GuiTextField;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Intektor
 */
public abstract class Gui extends InputAdapter implements GestureDetector.GestureListener {

    public List<GuiComponent> componentList = new ArrayList<GuiComponent>();

    public Input input;
    protected boolean paused = false;

    protected int width;
    protected int height;
    protected long timeAtLastTap;

    public Gui() {
    }

    public void init() {
        input = Gdx.input;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        InputMultiplexer multiplexer = new InputMultiplexer();
        GestureDetector detecto = new GestureDetector(this);
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(detecto);
        input.setInputProcessor(multiplexer);
        reInitButtons();
    }

    public void render(ShapeRenderer renderer) {
        for (GuiComponent component : this.componentList) {
            if (component != null) {
                component.render(renderer);
            }
        }
    }

    public void update() {
        for (GuiComponent component : componentList) {
            component.updateComponent(input.getX(), input.getY());
        }
    }

    public void reInitButtons() {
        this.componentList.clear();
        this.addButtons();
    }

    public abstract void onButtonTouched(int id);

    public abstract int getID();

    public abstract int getBasicPreviewState();

    public abstract void addButtons();

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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean success = false;
        for (GuiComponent component : componentList) {
            if (component instanceof GuiButton && ((GuiButton) component).enabled) {
                if (component.isHoveredOver(screenX, screenY)) {
                    deactivateAllTextFields();
                    component.onClicked(screenX, screenY);
                    onButtonTouched(((GuiButton) component).id);
                    componentClicked(component, screenX, screenY);
                    success = true;
                }
            } else if (component instanceof GuiTextField) {
                if (component.isHoveredOver(screenX, screenY)) {
                    component.onClicked(screenX, screenY);
                    success = true;
                    componentClicked(component, screenX, screenY);
                }
            } else {
                if (component.isHoveredOver(screenX, screenY)) {
                    component.onClicked(screenX, screenY);
                    componentClicked(component, screenX, screenY);
                    deactivateAllTextFields();
                }
            }
        }
        prevX = screenX;
        prevY = screenY;
        return success;
    }

    public void deactivateAllTextFields() {
        for (GuiTextField field : getTextFields()) {
            if (field.isActive()) {
                field.setActive(false);
            }
        }
    }

    public void textFieldDeactivated(GuiTextField field) {

    }

    @Override
    public boolean keyTyped(char character) {
        if (getActiveTextField() != null) {
            getActiveTextField().addCharacter(character);
        }
        return super.keyTyped(character);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACKSPACE) {
            if (getActiveTextField() != null) {
                getActiveTextField().removeChar();
            }
        } else if (keycode == Input.Keys.ENTER) {
            input.setOnscreenKeyboardVisible(false);
            if (getActiveTextField() != null) {
                getActiveTextField().setActive(false);
            }
        } else if (keycode == Input.Keys.R) {
            reInitButtons();
        }
        return super.keyDown(keycode);
    }

    public GuiTextField getActiveTextField() {
        for (GuiComponent c : componentList) {
            if (c instanceof GuiTextField) {
                if (((GuiTextField) c).isActive()) {
                    return (GuiTextField) c;
                }
            }
        }
        return null;
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

    public int getNumberOfTouches() {
        int n = 0;
        for (int i = 0; i < 20; i++) {
            if (input.isTouched(i)) {
                n++;
            }
        }
        return n;
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

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        timeAtLastTap = System.currentTimeMillis();
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return true;
    }

    public void onArrowDragged(int arrowID, int prevX, int prevY, int cX, int cY) {
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
}
