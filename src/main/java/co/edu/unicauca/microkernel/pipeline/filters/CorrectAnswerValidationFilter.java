package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

/**
 * Filtro 4: Verifica que exista una respuesta correcta, que sea consistente
 * (pertenezca a la lista de opciones) y que los datos de la pregunta estén
 * completos.
 *
 * @author David
 */
public class CorrectAnswerValidationFilter implements QuestionFilter {

    @Override
    public boolean process(QuestionRequest request) {
        String correctAnswer = request.getCorrectAnswer();

        if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
            return false;
        }

        if (request.getOptions() == null) {
            return false;
        }

        return request.getOptions().stream()
                .anyMatch(option -> option != null && option.trim().equalsIgnoreCase(correctAnswer.trim()));
    }

    @Override
    public String getErrorMessage() {
        return "CorrectAnswerValidationFilter: la respuesta correcta debe estar presente y "
                + "coincidir exactamente con una de las opciones registradas.";
    }
}
