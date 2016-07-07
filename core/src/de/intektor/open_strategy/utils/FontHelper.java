package de.intektor.open_strategy.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class FontHelper {

    public static float stringWidth(String string, BitmapFont font) {
        return new GlyphLayout(font, string).width;
    }

    public static float stringHeight(String string, BitmapFont font) {
        return new GlyphLayout(font, string).height;
    }

    public static List<String> splitString(String string, float width, BitmapFont font) {
        List<String> list = new ArrayList<>();
        int currentIndex = 1;
        while (stringWidth(string, font) > width) {
            while (stringWidth(string.substring(0, currentIndex), font) < width) {
                currentIndex++;
            }
            CharSequence local = string.substring(0, currentIndex);
            list.add((String) local);
            string = string.substring(currentIndex, string.length());
            currentIndex = 0;
        }
        if (string.trim().length() > 0) {
            list.add(string);
        }
        return list;
    }
}
