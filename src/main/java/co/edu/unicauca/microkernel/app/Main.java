package co.edu.unicauca.microkernel.app;

import co.edu.unicauca.microkernel.core.QuestionMicrokernel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación de escritorio (Swing) del Banco de
 * Preguntas Saber Pro.
 *
 * @author David
 */
public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Si el look and feel del sistema no está disponible, se usa el por defecto.
        }

        SwingUtilities.invokeLater(() -> {
            QuestionMicrokernel microkernel = new QuestionMicrokernel();
            MainFrame frame = new MainFrame(microkernel);
            frame.setVisible(true);
        });
    }
}
