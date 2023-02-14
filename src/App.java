import javax.swing.*;
import com.formdev.flatlaf.intellijthemes.*;
// import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
// FlatHighContrastIJTheme
// FlatMaterialDesignDarkIJTheme
// FlatSolarizedDarkIJTheme
// FlatSolarizedLightIJTheme
public class App {
    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel( new FlatSolarizedLightIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        new TinyCalculator();
    }
}
