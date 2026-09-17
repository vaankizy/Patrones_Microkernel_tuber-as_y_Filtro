package co.edu.unicauca.microkernel.core;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.common.interfaces.QuestionPlugin;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Núcleo del sistema (patrón Microkernel).
 *
 * Responsabilidades:
 * <ul>
 *   <li>Almacenar el banco de preguntas en un Map.</li>
 *   <li>Registrar los plugins declarados en el archivo plugins.properties.</li>
 *   <li>Ejecutar los plugins (delegar la generación de una pregunta al plugin
 *       adecuado según el tipo solicitado).</li>
 *   <li>Gestionar el ciclo de vida de los plugins (cargarlos al iniciar,
 *       recargarlos en caliente y consultarlos en cualquier momento).</li>
 * </ul>
 *
 * La carga de los plugins se hace de forma dinámica mediante Reflexión, tal
 * como en el ejemplo teórico de envío de paquetes a distintos países visto en
 * clase (DeliveryPluginManager).
 *
 * @author David
 */
public class QuestionMicrokernel {

    private static final String PLUGINS_FILE = "plugins.properties";
    private static final Logger LOGGER = Logger.getLogger(QuestionMicrokernel.class.getName());

    /**
     * Banco de preguntas: id de la pregunta -> Question.
     */
    private final Map<String, Question> questions = new HashMap<>();

    /**
     * Plugins registrados y cargados dinámicamente en el núcleo.
     */
    private final List<QuestionPlugin> plugins = new ArrayList<>();

    public QuestionMicrokernel() {
        loadPlugins();
    }

    /**
     * Lee plugins.properties y, mediante Reflexión, instancia cada plugin
     * declarado y lo registra en el núcleo. Este es el mecanismo que evita
     * modificar el núcleo cada vez que aparece un nuevo tipo de pregunta.
     */
    private void loadPlugins() {
        plugins.clear();

        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PLUGINS_FILE)) {

            if (input == null) {
                LOGGER.severe("No se encontró el archivo " + PLUGINS_FILE + " en el classpath.");
                return;
            }

            prop.load(input);

            for (String key : prop.stringPropertyNames()) {
                String className = prop.getProperty(key);
                try {
                    // Uso obligatorio de Reflexión para instanciación dinámica del plugin.
                    Class<?> pluginClass = Class.forName(className);
                    Object pluginObject = pluginClass.getDeclaredConstructor().newInstance();

                    if (pluginObject instanceof QuestionPlugin) {
                        plugins.add((QuestionPlugin) pluginObject);
                        LOGGER.info("Plugin registrado: " + className);
                    } else {
                        LOGGER.warning(className + " no implementa QuestionPlugin y fue ignorado.");
                    }

                } catch (ReflectiveOperationException ex) {
                    LOGGER.log(Level.SEVERE, "Error al cargar el plugin " + className + " vía reflexión", ex);
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al leer " + PLUGINS_FILE, ex);
        }
    }

    /**
     * Permite recargar los plugins en caliente (gestión del ciclo de vida),
     * por ejemplo si plugins.properties cambia en tiempo de ejecución.
     */
    public void reloadPlugins() {
        loadPlugins();
    }

    /**
     * Ejecuta el plugin adecuado (aquel cuyo supports(type) sea true) para
     * generar y almacenar una nueva pregunta en el banco.
     *
     * @param type    tipo de pregunta solicitado.
     * @param request datos de la pregunta.
     * @return la pregunta generada y almacenada.
     * @throws IllegalArgumentException si ningún plugin soporta el tipo.
     * @throws IllegalStateException    si el plugin rechazó la solicitud
     *                                  (no pasó sus validaciones internas).
     */
    public Question executePlugin(String type, QuestionRequest request) {

        for (QuestionPlugin plugin : plugins) {
            if (plugin.supports(type)) {

                Question question = plugin.generate(request);

                if (question == null) {
                    throw new IllegalStateException(
                            "El plugin '" + plugin.getName() + "' rechazó la solicitud: "
                            + "no superó las validaciones requeridas.");
                }

                questions.put(question.getId(), question);
                return question;
            }
        }

        throw new IllegalArgumentException("No hay ningún plugin registrado que soporte el tipo: " + type);
    }

    /**
     * @return el banco de preguntas almacenado (vista de solo lectura).
     */
    public Map<String, Question> getQuestions() {
        return Collections.unmodifiableMap(questions);
    }

    /**
     * @return la lista de plugins actualmente registrados en el núcleo.
     */
    public List<QuestionPlugin> getPlugins() {
        return Collections.unmodifiableList(plugins);
    }
}
