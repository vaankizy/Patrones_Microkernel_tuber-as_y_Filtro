package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;
import co.edu.unicauca.microkernel.pipeline.base.QuestionPipeline;
import co.edu.unicauca.microkernel.pipeline.filters.ClassificationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.ContentValidationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.CorrectAnswerValidationFilter;
import co.edu.unicauca.microkernel.pipeline.filters.OptionsValidationFilter;
import java.util.UUID;

/**
 * Plugin que genera preguntas de selección múltiple.
 *
 * Este es el plugin que implementa el pipeline de validación completo
 * (Tuberías y Filtros): antes de generar la pregunta, ejecuta en orden los
 * cuatro filtros requeridos por el taller.
 *
 * @author David
 */
public class MultipleChoiceQuestionPlugin implements QuestionPlugin {

    private final QuestionPipeline pipeline;

    public MultipleChoiceQuestionPlugin() {
        pipeline = new QuestionPipeline();
        pipeline.addFilter(new ContentValidationFilter());
        pipeline.addFilter(new OptionsValidationFilter());
        pipeline.addFilter(new ClassificationFilter());
        pipeline.addFilter(new CorrectAnswerValidationFilter());
    }

    @Override
    public String getName() {
        return "multiple-choice";
    }

    @Override
    public boolean supports(String type) {
        return "MULTIPLE_CHOICE".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {

        System.out.println("Generando pregunta de selección múltiple...");

        // Ejecución del pipeline de validación previo a la generación.
        if (!pipeline.execute(request)) {
            return null;
        }

        return new Question(
                UUID.randomUUID().toString(),
                request.getTitle(),
                request.getContent(),
                request.getType()
        );
    }
}
