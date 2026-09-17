package co.edu.unicauca.microkernel.pipeline;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionPipeline;
import co.edu.unicauca.microkernel.pipeline.filters.ClassificationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.ContentValidationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.CorrectAnswerValidationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.OptionsValidationFilter;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias del pipeline (patrón Tuberías y Filtros) y de cada uno de
 * sus filtros de manera aislada.
 *
 * @author David
 */
public class QuestionPipelineTest {

    private QuestionPipeline pipeline;

    @BeforeEach
    public void setUp() {
        pipeline = new QuestionPipeline();
        pipeline.addFilter(new ContentValidationFilter());
        pipeline.addFilter(new OptionsValidationFilter());
        pipeline.addFilter(new ClassificationFilter());
        pipeline.addFilter(new CorrectAnswerValidationFilter());
    }

    private QuestionRequest requestValida() {
        return new QuestionRequest(
                "Pregunta SOLID",
                "¿Qué representa la S en los principios SOLID de diseño?",
                "MULTIPLE_CHOICE",
                "Arquitectura de software",
                Arrays.asList("Single Responsibility", "Open Closed", "Liskov", "Interface Segregation"),
                "Single Responsibility"
        );
    }

    @Test
    public void testPipelineCompletoConSolicitudValida() {
        assertTrue(pipeline.execute(requestValida()));
    }

    @Test
    public void testContentValidationFilterInvalido() {
        ContentValidationFilter filter = new ContentValidationFilter();
        QuestionRequest requestInvalido = new QuestionRequest("", "", "MULTIPLE_CHOICE", "Arquitectura de software",
                null, "");
        assertFalse(filter.process(requestInvalido));
    }

    @Test
    public void testOptionsValidationFilterConMenosDeCuatroOpciones() {
        OptionsValidationFilter filter = new OptionsValidationFilter();
        QuestionRequest request = new QuestionRequest("T", "Contenido suficientemente largo",
                "MULTIPLE_CHOICE", "Bases de datos", Arrays.asList("A", "B"), "A");
        assertFalse(filter.process(request));
    }

    @Test
    public void testOptionsValidationFilterConOpcionesDuplicadas() {
        OptionsValidationFilter filter = new OptionsValidationFilter();
        QuestionRequest request = new QuestionRequest("T", "Contenido suficientemente largo",
                "MULTIPLE_CHOICE", "Bases de datos",
                Arrays.asList("A", "B", "A", "C"), "A");
        assertFalse(filter.process(request));
    }

    @Test
    public void testClassificationFilterConCompetenciaInvalida() {
        ClassificationFilter filter = new ClassificationFilter();
        QuestionRequest request = new QuestionRequest("T", "Contenido suficientemente largo",
                "MULTIPLE_CHOICE", "Cocina internacional",
                Arrays.asList("A", "B", "C", "D"), "A");
        assertFalse(filter.process(request));
    }

    @Test
    public void testCorrectAnswerValidationFilterConRespuestaFueraDeOpciones() {
        CorrectAnswerValidationFilter filter = new CorrectAnswerValidationFilter();
        QuestionRequest request = new QuestionRequest("T", "Contenido suficientemente largo",
                "MULTIPLE_CHOICE", "Bases de datos",
                Arrays.asList("A", "B", "C", "D"), "Z");
        assertFalse(filter.process(request));
    }

    @Test
    public void testPipelineFallaSiUnaOpcionEstaVacia() {
        QuestionRequest request = new QuestionRequest(
                "Pregunta con opción vacía",
                "Contenido suficientemente largo para pasar el primer filtro",
                "MULTIPLE_CHOICE", "Arquitectura de software",
                Arrays.asList("A", "", "C", "D"), "A");
        assertFalse(pipeline.execute(request));
    }
}
