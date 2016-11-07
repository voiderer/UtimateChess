package edu.ncepu.yutao.PublicComponent;

import edu.ncepu.yutao.MainClass.Entrance;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yutao on 16-6-19.
 */
public class FontManager {
    public static Font mainFont;

    public static void initialize() {
        InputStream in = Entrance.class.getResourceAsStream("/font/simkai.ttf");
        try {
            mainFont = Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
