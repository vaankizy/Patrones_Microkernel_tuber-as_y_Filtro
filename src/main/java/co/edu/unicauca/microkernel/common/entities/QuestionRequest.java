package co.edu.unicauca.microkernel.common.entities;

import java.util.List;

/**
 * Objeto de transporte de datos (DTO) con la información solicitada por el
 * núcleo hacia los plugins para generar una nueva pregunta.
 *
 * @author David
 */
public class QuestionRequest {

    private String title;
    private String content;
    private String type;
    private String classification;
    private List<String> options;
    private String correctAnswer;
    /**
     * URL o ruta del recurso multimedia (imagen, audio o video), usado solo
     * por preguntas de tipo MULTIMEDIA.
     */
    private String mediaUrl;

    public QuestionRequest(String title, String content, String type, String classification,
            List<String> options, String correctAnswer) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.classification = classification;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public QuestionRequest(String title, String content, String type, String classification,
            List<String> options, String correctAnswer, String mediaUrl) {
        this(title, content, type, classification, options, correctAnswer);
        this.mediaUrl = mediaUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public String getClassification() {
        return classification;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }
}
