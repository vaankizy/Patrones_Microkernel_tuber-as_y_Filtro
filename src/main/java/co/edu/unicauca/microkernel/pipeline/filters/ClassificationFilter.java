package co.edu.unicauca.microkernel.pipeline.filters;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.pipeline.base.QuestionFilter;
import java.util.Arrays;
import java.util.List;

/**
 * Filtro 3: Asigna/valida la competencia (área de conocimiento) y el nivel de
 * dificultad de la pregunta. Verifica que la clasificación pertenezca al
 * conjunto de competencias válidas del Banco de Preguntas Saber Pro.
 *
 * @author David
 */
public class ClassificationFilter implements QuestionFilter {

    private static final List<String> COMPETENCIAS_VALIDAS = Arrays.asList(
            "arquitectura de software",
            "ingeniería de software",
            "bases de datos",
            "estructuras de datos",
            "sistemas operativos",
            "redes de computadores",
            "matemáticas",
            "razonamiento cuantitativo"
    );

    @Override
    public boolean process(QuestionRequest request) {
        String classification = request.getClassification();

        return classification != null
                && COMPETENCIAS_VALIDAS.contains(classification.trim().toLowerCase());
    }

    @Override
    public String getErrorMessage() {
        return "ClassificationFilter: la clasificación/competencia indicada no es válida. "
                + "Valores permitidos: " + COMPETENCIAS_VALIDAS;
    }
}
