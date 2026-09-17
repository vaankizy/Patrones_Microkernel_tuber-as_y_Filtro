package co.edu.unicauca.microkernel.pipeline.base;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Tubería (Pipeline) que conecta una secuencia de filtros de validación y los
 * ejecuta en orden sobre una QuestionRequest. Si algún filtro falla, la
 * ejecución se detiene inmediatamente (fail-fast).
 *
 * @author David
 */
public class QuestionPipeline {

    private final List<QuestionFilter> filters = new ArrayList<>();
    private String lastErrorMessage;

    public void addFilter(QuestionFilter filter) {
        filters.add(filter);
    }

    /**
     * Ejecuta todos los filtros registrados, en el orden en que fueron
     * agregados.
     *
     * @param request solicitud a validar.
     * @return true si la solicitud pasó todos los filtros.
     */
    public boolean execute(QuestionRequest request) {
        for (QuestionFilter filter : filters) {
            if (!filter.process(request)) {
                lastErrorMessage = filter.getErrorMessage();
                System.err.println(lastErrorMessage);
                return false;
            }
        }
        lastErrorMessage = null;
        return true;
    }

    /**
     * @return el mensaje de error del último filtro que falló en la última
     *         llamada a execute(), o null si la última ejecución fue
     *         exitosa.
     */
    public String getLastErrorMessage() {
        return lastErrorMessage;
    }
}
