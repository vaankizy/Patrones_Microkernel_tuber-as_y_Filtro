package co.edu.unicauca.microkernel.common.interfaces;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;

/**
 * Contrato común que deben cumplir todos los plugins de generación de
 * preguntas del sistema (patrón Microkernel).
 *
 * @author David
 */
public interface QuestionPlugin {

    /**
     * @return nombre identificador del plugin.
     */
    String getName();

    /**
     * Indica si este plugin sabe procesar el tipo de pregunta indicado.
     *
     * @param type tipo de pregunta (ej. "MULTIPLE_CHOICE", "CASE_STUDY",
     *             "MULTIMEDIA").
     * @return true si el plugin soporta el tipo.
     */
    boolean supports(String type);

    /**
     * Genera una nueva pregunta a partir de la solicitud recibida.
     *
     * @param request datos de la pregunta a generar.
     * @return la pregunta generada, o null si la solicitud no superó las
     *         validaciones del plugin.
     */
    Question generate(QuestionRequest request);
}
