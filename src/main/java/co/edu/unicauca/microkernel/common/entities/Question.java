package co.edu.unicauca.microkernel.common.entities;

/**
 * Representa una pregunta ya generada y almacenada en el banco de preguntas
 * del núcleo (QuestionMicrokernel).
 *
 * @author David
 */
public class Question {

    private String id;
    private String title;
    private String content;
    private String type;

    public Question(String id, String title, String content, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "[" + type + "] " + title;
    }
}
