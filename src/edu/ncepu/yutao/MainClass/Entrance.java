package edu.ncepu.yutao.MainClass;


import edu.ncepu.yutao.Network.Connection;
import edu.ncepu.yutao.Persistence.PersistenceManager;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.OfficeBlue2007Skin;

import javax.swing.*;

/**
 * Created by AUTOY on 2016/5/5.
 */
public class Entrance {
    public static void main(String arg[]) {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            SubstanceLookAndFeel.setSkin(new OfficeBlue2007Skin());
            /*SubstanceLookAndFeel.setSkin(new FieldOfWheatSkin());
            SubstanceLookAndFeel.setCurrentTitlePainter(new ClassicTitlePainter());
            SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());
            SubstanceLookAndFeel.setFontPolicy(new MyFontPolicy());*/
            /*SubstanceLookAndFeel.setFontPolicy((s, uiDefaults) -> {
                Font font = FontManager.mainFont;
                Font controlFont = font.deriveFont(Font.BOLD, 15);
                Font menuFont = font.deriveFont(Font.BOLD, 16);
                Font smallFont = controlFont.deriveFont(controlFont.getSize2D() - 2.0F);
                return FontSets.createDefaultFontSet(controlFont, menuFont, menuFont, controlFont, smallFont, menuFont);
            });*/
            //Font f = FontManager.mainFont.deriveFont(Font.BOLD, 15);
            // Font aa = new Font("隶书", Font.BOLD, 16);
            //  UIDefaults defaults = UIManager.getLookAndFeelDefaults();
            /*Object[] os = UIManager.getLookAndFeelDefaults().keySet().toArray();
            for (Object o : os) {
                String k = o.toString();
                if (k.indexOf("font") > 0) {
                    UIManager.put(k, aa);
                    //System.out.println(k);
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        PersistenceManager.readUserList();
        SwingUtilities.invokeLater(() -> {
            LoginWindow dialog = new LoginWindow();
            dialog.setVisible(true);
            if (!dialog.isbOk()) {
                System.exit(0);
            }
            if (!Connection.initializeNetwork()) {
                System.exit(0);
            }
            new MainWindow();
        });

    }
}
