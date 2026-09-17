package co.edu.unicauca.microkernel.core;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias del núcleo QuestionMicrokernel: verifica la carga
 * dinámica de plugins vía Reflexión (a partir de plugins.properties) y el
 * correcto despacho/almacenamiento de preguntas en el banco.
 *
 * @author David
 */
public class QuestionMicrokernelTest {

    private QuestionMicrokernel microkernel;

    @BeforeEach
    public void setUp() {
        microkernel = new QuestionMicrokernel();
    }

    @Test
    public void testCargaDinamicaDePluginsDesdePropertiesViaReflexion() {
        assertEquals(3, microkernel.getPlugins().size(),
                "Se deben cargar los 3 plugins declarados en plugins.properties");
    }

    @Test
    public void testExecutePluginGeneraYAlmacenaPreguntaValida() {
        QuestionRequest request = new QuestionRequest(
                "Pregunta SOLID",
                "¿Qué representa la S en los principios SOLID de diseño?",
                "MULTIPLE_CHOICE",
                "Arquitectura de software",
                Arrays.asList("Single Responsibility", "Open Closed", "Liskov", "Interface Segregation"),
                "Single Responsibility"
        );

        Question pregunta = microkernel.executePlugin("MULTIPLE_CHOICE", request);

        assertNotNull(pregunta);
        assertTrue(microkernel.getQuestions().containsKey(pregunta.getId()));
    }

    @Test
    public void testExecutePluginLanzaExcepcionParaTipoNoSoportado() {
        QuestionRequest request = new QuestionRequest("T", "Contenido", "TIPO_INEXISTENTE",
                "Arquitectura de software", null, null);

        assertThrows(IllegalArgumentException.class,
                () -> microkernel.executePlugin("TIPO_INEXISTENTE", request));
    }

    @Test
    public void testExecutePluginLanzaExcepcionSiElPluginRechazaLaSolicitud() {
        // Solicitud MULTIPLE_CHOICE sin opciones ni respuesta correcta: el
        // pipeline de validación del plugin debe rechazarla.
        QuestionRequest requestInvalida = new QuestionRequest(
                "", "", "MULTIPLE_CHOICE", "Arquitectura de software", null, "");

        assertThrows(IllegalStateException.class,
                () -> microkernel.executePlugin("MULTIPLE_CHOICE", requestInvalida));
    }
}
