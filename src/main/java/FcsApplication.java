import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class FcsApplication extends Application {
    private final TextField sentPayload = new TextField("STATUS:OK");
    private final TextField receivedPayload = new TextField("STATUS:OK");
    private final Label sentBytes = output("-");
    private final Label sentFcs = output("-");
    private final Label receivedBytes = output("-");
    private final Label receivedFcs = output("-");
    private final Label javaFcs = output("-");
    private final Label status = new Label();
    private final Label log = new Label();
    private long transmittedFcs;

    @Override
    public void start(Stage stage) {
        Label title = text("INSPECTOR DE QUADROS", "title");
        Label subtitle = text("Laborat\u00F3rio visual para integridade de payload e valida\u00E7\u00E3o de FCS.", "subtitle");
        Button transmit = button("GERAR FCS", "primary-button"); transmit.setOnAction(e -> generateFrame());
        VBox sender = panel("TRANSMISSOR", new HBox(10, transmit), row("PAYLOAD", sentPayload), row("BYTES ASCII", sentBytes), row("FCS ANEXADO", sentFcs));

        Button validate = button("VALIDAR QUADRO", "secondary-button"); validate.setOnAction(e -> validateFrame());
        VBox receiver = panel("RECEPTOR", new HBox(10, validate), row("PAYLOAD RECEBIDO", receivedPayload), row("BYTES ASCII", receivedBytes), row("CRC RECALCULADO", receivedFcs), row("CRC32 DO JAVA", javaFcs));

        GridPane channels = new GridPane(); channels.setHgap(18); channels.add(sender, 0, 0); channels.add(receiver, 1, 0);
        ColumnConstraints left = new ColumnConstraints(); left.setPercentWidth(50); ColumnConstraints right = new ColumnConstraints(); right.setPercentWidth(50); channels.getColumnConstraints().addAll(left, right);
        VBox report = new VBox(8, text("RESULTADO DA VALIDA\u00C7\u00C3O", "section-label"), status, log); report.getStyleClass().add("report"); report.setMinHeight(96);
        VBox.setMargin(report, new Insets(0, 0, 24, 0));
        Label hint = text("* Como usar: gere o FCS no transmissor, altere o payload recebido e valide o quadro. O FCS anexado permanece igual para simular a corrup\u00E7\u00E3o.", "hint"); hint.setWrapText(true);
        VBox root = new VBox(18, title, subtitle, channels, report, hint); VBox.setMargin(channels, new Insets(26, 0, 0, 0)); root.setPadding(new Insets(54, 48, 42, 48)); root.setMaxWidth(1150);
        StackPane page = new StackPane(root); page.setAlignment(Pos.CENTER);
        Scene scene = new Scene(page, 1220, 850); scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Ethernet Frame Inspector"); stage.setScene(scene); stage.setResizable(false); stage.show(); generateFrame();
    }

    private void generateFrame() { byte[] data = ascii(sentPayload.getText()); transmittedFcs = Crc32Ethernet.calculate(data); sentBytes.setText(bytes(data)); sentFcs.setText(Crc32Ethernet.hex(transmittedFcs)); receivedPayload.setText(sentPayload.getText()); validateFrame(); log.setText("TRANSMISSOR  >  payload convertido  >  FCS anexado  >  quadro pronto"); }
    private void validateFrame() { byte[] data = ascii(receivedPayload.getText()); long manual = Crc32Ethernet.calculate(data); CRC32 java = new CRC32(); java.update(data); boolean accepted = manual == transmittedFcs; receivedBytes.setText(bytes(data)); receivedFcs.setText(Crc32Ethernet.hex(manual)); javaFcs.setText(Crc32Ethernet.hex(java.getValue())); status.setText(accepted ? "QUADRO ACEITO   /   FCS CONFERE" : "QUADRO DESCARTADO   /   FCS N\u00C3O CONFERE"); status.setTextFill(Color.web(accepted ? "#a8c686" : "#d66b5d")); if (!accepted) log.setText("RECEPTOR  >  payload alterado  >  FCS diferente  >  quadro descartado"); }
    private static byte[] ascii(String value) { return value.getBytes(StandardCharsets.US_ASCII); }
    private static String bytes(byte[] data) { StringBuilder r = new StringBuilder(); for (byte b : data) { if (r.length() > 0) r.append(' '); r.append(String.format("%02X", b)); } return r.toString(); }
    private static VBox panel(String name, javafx.scene.Node input, javafx.scene.Node... rows) { VBox box = new VBox(12); box.getStyleClass().add("panel"); box.getChildren().addAll(text(name, "section-label"), input); box.getChildren().addAll(rows); return box; }
    private static HBox row(String name, javafx.scene.Node value) { Label key = text(name, "key"); HBox row = new HBox(18, key, value); row.getStyleClass().add("data-row"); HBox.setHgrow(value, Priority.ALWAYS); return row; }
    private static Button button(String value, String style) { Button button = new Button(value); button.getStyleClass().add(style); return button; }
    private static Label text(String value, String style) { Label label = new Label(value); label.getStyleClass().add(style); return label; }
    private static Label output(String value) { return text(value, "output"); }
    public static void main(String[] args) { launch(args); }
}
