package co.edu.unicauca.microkernel.plugins;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;
import java.util.UUID;

/**
 * Plugin que genera preguntas de análisis de casos / basadas en escenarios.
 * Realiza una validación simple propia (contenido con longitud suficiente
 * para describir un caso) sin necesidad de reutilizar el pipeline completo.
 *
 * @author David
 */
public class CaseQuestionPlugin implements QuestionPlugin {

    private static final int MIN_CASE_LENGTH = 30;

    @Override
    public String getName() {
        return "case-study";
    }

    @Override
    public boolean supports(String type) {
        return "CASE_STUDY".equalsIgnoreCase(type);
    }

    @Override
    public Question generate(QuestionRequest request) {

        System.out.println("Generando pregunta de análisis de caso...");

        if (request == null
                || request.getTitle() == null || request.getTitle().trim().isEmpty()
                || request.getContent() == null || request.getContent().trim().length() < MIN_CASE_LENGTH) {
            System.err.println("CaseQuestionPlugin: el caso/escenario debe describirse con al menos "
                    + MIN_CASE_LENGTH + " caracteres.");
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
