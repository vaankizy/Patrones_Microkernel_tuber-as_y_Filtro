package co.edu.unicauca.microkernel.app;

import co.edu.unicauca.microkernel.common.entities.Question;
import co.edu.unicauca.microkernel.common.entities.QuestionRequest;
import co.edu.unicauca.microkernel.core.QuestionMicrokernel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Interfaz de escritorio (Java Swing) del Banco de Preguntas Saber Pro.
 *
 * La ventana permite generar preguntas de distintos tipos a través del
 * QuestionMicrokernel (que despacha la solicitud al plugin correspondiente,
 * cargado dinámicamente por Reflexión) y muestra el banco de preguntas
 * resultante en una tabla.
 *
 * @author David
 */
public class MainFrame extends JFrame {

    private final QuestionMicrokernel microkernel;

    private JTextField txtTitulo;
    private JTextArea txtContenido;
    private JComboBox<String> cmbTipo;
    private JComboBox<String> cmbClasificacion;
    private JTextField[] txtOpciones;
    private JTextField txtRespuestaCorrecta;
    private JTextField txtMediaUrl;

    private DefaultTableModel tableModel;
    private JTable table;

    public MainFrame(QuestionMicrokernel microkernel) {
        this.microkernel = microkernel;

        setTitle("Banco de Preguntas Saber Pro — Microkernel + Tuberías y Filtros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 620);
        setLocationRelativeTo(null);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                buildFormPanel(), buildTablePanel());
        splitPane.setResizeWeight(0.55);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Generar nueva pregunta"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Tipo de pregunta:"), gbc);
        cmbTipo = new JComboBox<>(new String[]{"MULTIPLE_CHOICE", "CASE_STUDY", "MULTIMEDIA"});
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(cmbTipo, gbc);
        gbc.gridwidth = 1;
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Título:"), gbc);
        txtTitulo = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtTitulo, gbc);
        gbc.gridwidth = 1;
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Contenido:"), gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        txtContenido = new JTextArea(3, 30);
        txtContenido.setLineWrap(true);
        txtContenido.setWrapStyleWord(true);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(new JScrollPane(txtContenido), gbc);
        gbc.gridwidth = 1;
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Clasificación / competencia:"), gbc);
        cmbClasificacion = new JComboBox<>(new String[]{
            "Arquitectura de software", "Ingeniería de software", "Bases de datos",
            "Estructuras de datos", "Sistemas operativos", "Redes de computadores",
            "Matemáticas", "Razonamiento cuantitativo"
        });
        cmbClasificacion.setEditable(true);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(cmbClasificacion, gbc);
        gbc.gridwidth = 1;
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Opciones (para MULTIPLE_CHOICE):"), gbc);
        JPanel opcionesPanel = new JPanel(new GridLayout(2, 2, 4, 4));
        txtOpciones = new JTextField[4];
        for (int i = 0; i < 4; i++) {
            txtOpciones[i] = new JTextField();
            opcionesPanel.add(txtOpciones[i]);
        }
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(opcionesPanel, gbc);
        gbc.gridwidth = 1;
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Respuesta correcta:"), gbc);
        txtRespuestaCorrecta = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtRespuestaCorrecta, gbc);
        gbc.gridwidth = 1;
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("URL/ruta multimedia (para MULTIMEDIA):"), gbc);
        txtMediaUrl = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtMediaUrl, gbc);
        gbc.gridwidth = 1;
        row++;

        JButton btnGenerar = new JButton("Generar pregunta");
        btnGenerar.addActionListener(e -> onGenerarPregunta());
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnGenerar, gbc);

        return panel;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Banco de preguntas"));

        tableModel = new DefaultTableModel(new Object[]{"ID", "Tipo", "Título", "Contenido"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JLabel lblInfo = new JLabel(
                "Plugins registrados en el núcleo: " + microkernel.getPlugins().size(),
                SwingConstants.LEFT);
        panel.add(lblInfo, BorderLayout.SOUTH);

        return panel;
    }

    private void onGenerarPregunta() {
        try {
            String tipo = (String) cmbTipo.getSelectedItem();
            String titulo = txtTitulo.getText();
            String contenido = txtContenido.getText();
            String clasificacion = (String) cmbClasificacion.getSelectedItem();
            String respuestaCorrecta = txtRespuestaCorrecta.getText();
            String mediaUrl = txtMediaUrl.getText();

            List<String> opciones = new ArrayList<>();
            for (JTextField campo : txtOpciones) {
                String valor = campo.getText();
                if (valor != null && !valor.trim().isEmpty()) {
                    opciones.add(valor.trim());
                }
            }

            QuestionRequest request = new QuestionRequest(
                    titulo, contenido, tipo, clasificacion,
                    opciones.isEmpty() ? null : opciones,
                    respuestaCorrecta, mediaUrl);

            Question pregunta = microkernel.executePlugin(tipo, request);

            tableModel.addRow(new Object[]{
                pregunta.getId(), pregunta.getType(), pregunta.getTitle(), pregunta.getContent()
            });

            JOptionPane.showMessageDialog(this,
                    "Pregunta agregada exitosamente al banco con ID: " + pregunta.getId(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();

        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No fue posible generar la pregunta", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtContenido.setText("");
        txtRespuestaCorrecta.setText("");
        txtMediaUrl.setText("");
        for (JTextField campo : txtOpciones) {
            campo.setText("");
        }
    }
}
