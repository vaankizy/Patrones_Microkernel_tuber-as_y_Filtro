package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;

/**
 * Filtro 1: Verifica que la pregunta tenga texto válido y cumpla reglas
 * básicas (texto no vacío y longitud mínima).
 *
 * @author David
 */
public class ContentValidationFilter implements QuestionFilter {

    private static final int MIN_CONTENT_LENGTH = 10;

    @Override
    public boolean process(QuestionRequest request) {
        if (request == null) {
            return false;
        }

        boolean tituloValido = request.getTitle() != null
                && !request.getTitle().trim().isEmpty();

        boolean contenidoValido = request.getContent() != null
                && request.getContent().trim().length() >= MIN_CONTENT_LENGTH;

        return tituloValido && contenidoValido;
    }

    @Override
    public String getErrorMessage() {
        return "ContentValidationFilter: el título no puede estar vacío y el contenido debe "
                + "tener al menos " + MIN_CONTENT_LENGTH + " caracteres.";
    }
}
