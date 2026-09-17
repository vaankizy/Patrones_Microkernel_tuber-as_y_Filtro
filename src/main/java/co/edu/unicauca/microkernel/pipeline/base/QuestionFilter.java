package co.edu.unicauca.microkernel.pipeline.base;

import co.edu.unicauca.microkernel.common.entities.QuestionRequest;

/**
 * Abstracción de un filtro dentro del pipeline de validación de preguntas
 * (patrón Tuberías y Filtros).
 *
 * @author David
 */
public interface QuestionFilter {

    /**
     * Procesa (valida) la solicitud de pregunta.
     *
     * @param request solicitud a validar.
     * @return true si la solicitud pasa la validación de este filtro.
     */
    boolean process(QuestionRequest request);

    /**
     * @return mensaje descriptivo del motivo de fallo, útil para mostrar al
     *         usuario cuando process() devuelve false. Por defecto retorna un
     *         mensaje genérico basado en el nombre de la clase.
     */
    default String getErrorMessage() {
        return "La validación falló en el filtro: " + this.getClass().getSimpleName();
    }
}
