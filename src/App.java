import javax.swing.*;
import java.awt.*;

import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
// import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel( new FlatLightFlatIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        new TinyCalculator();
    }
}
