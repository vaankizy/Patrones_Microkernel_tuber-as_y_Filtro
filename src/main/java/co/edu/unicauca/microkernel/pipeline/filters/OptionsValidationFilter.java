package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filtro 2: Verifica la cantidad y calidad de las opciones de respuesta:
 * exactamente 4 opciones, ninguna vacía y sin opciones duplicadas.
 *
 * @author David
 */
public class OptionsValidationFilter implements QuestionFilter {

    private static final int REQUIRED_OPTIONS = 4;
    private String detalleError;

    @Override
    public boolean process(QuestionRequest request) {
        List<String> options = request.getOptions();

        if (options == null || options.size() != REQUIRED_OPTIONS) {
            detalleError = "se requieren exactamente " + REQUIRED_OPTIONS + " opciones.";
            return false;
        }

        Set<String> opcionesUnicas = new HashSet<>();
        for (String option : options) {
            if (option == null || option.trim().isEmpty()) {
                detalleError = "ninguna opción puede estar vacía.";
                return false;
            }
            if (!opcionesUnicas.add(option.trim().toLowerCase())) {
                detalleError = "no se permiten opciones duplicadas.";
                return false;
            }
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return "OptionsValidationFilter: " + (detalleError != null ? detalleError : "opciones inválidas.");
    }
}
