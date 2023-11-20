import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Start {
	public static void start(Stage stage) {
		// get inputs and infos
		Read.readProperties();
		Read.readBackup();

		// app image
		Image icon = new Image("file:assets/icons/logo.png");
		Label text = new Label();

		// setting up buttons
		Button butSignup = Signup.signButton;
		Button butLogin = Login.loginButton;
		Read.effectHandle(butLogin);
		Read.effectHandle(butSignup);

		// add buttons to loginPane
		GridPane buttonsPane = Login.getButtonPane();
		Login.loginPane.add(buttonsPane, 0, 3);

		// get signPane and signScene
		GridPane signPane = Signup.getSignPane();
		Scene signScene = new Scene(signPane, 450, 300, Color.GHOSTWHITE);

		// button actions
		butSignup.setOnAction(Signup.handleEvent(stage, signScene, signPane, text));
		butLogin.setOnAction(Login.handleEvent(stage, text, butLogin));

		stage.setResizable(false);
		stage.setTitle(Read.title);
		stage.getIcons().add(icon);
		stage.setScene(Login.loginScene);
		stage.show();

		stage.setOnCloseRequest(e -> {
			Write.writeToBackup();
		});
	}
}
