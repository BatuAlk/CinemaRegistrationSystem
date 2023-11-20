
import java.io.File;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Signup {
	static TextField unTFS = new TextField(); // username text field
	static PasswordField pwTFS1 = new PasswordField(); // password field 1
	static PasswordField pwTFS2 = new PasswordField(); // password field 2
	static Button signButton = new Button("SIGN UP"); // sign button to use in Start

	public static GridPane getSignPane() {
		// create and/or return signPane
		GridPane signPane = new GridPane();
		signPane.setVgap(20);
		GridPane wPS = new GridPane();
		wPS.setPadding(new Insets(5, 0, 0, 52));
		Text welcomelogintext = new Text();
		welcomelogintext.setText("   Welcome to the HUCS Cinema Reservation System!" + "\n"
				+ "       Fill the form below to create a new account." + "\n"
				+ " You can go to Log In page by clicking LOGIN button.");

		wPS.add(welcomelogintext, 0, 0);
		signPane.add(wPS, 0, 0);

		Label usernameLabel = new Label();
		usernameLabel.setText("Username:");

		GridPane usernamePane = new GridPane();
		unTFS.clear();
		usernamePane.setHgap(15);
		usernamePane.add(usernameLabel, 0, 0);
		usernamePane.add(unTFS, 1, 0);
		usernamePane.setPadding(new Insets(0, 0, 0, 107.5));
		signPane.add(usernamePane, 0, 1);

		Label passwordlabel1 = new Label();
		passwordlabel1.setText("Password:");
		Label passwordlabel2 = new Label();
		passwordlabel2.setText("Password:");

		GridPane passwordPane1 = new GridPane();
		passwordPane1.setHgap(17);
		passwordPane1.add(passwordlabel1, 0, 0);
		passwordPane1.add(pwTFS1, 1, 0);
		passwordPane1.setPadding(new Insets(0, 0, 0, 107.5));
		signPane.add(passwordPane1, 0, 2);

		GridPane passwordPane2 = new GridPane();
		passwordPane2.setHgap(17);
		passwordPane2.add(passwordlabel2, 0, 0);
		passwordPane2.add(pwTFS2, 1, 0);
		passwordPane2.setPadding(new Insets(0, 0, 0, 107.5));
		signPane.add(passwordPane2, 0, 3);

		return signPane;
	}

	public static GridPane getActionPane(Label text) {
		// returns the result of the button click (e.g. Error if fields are empty)
		MediaPlayer errorplay = new MediaPlayer(new Media(new File("assets/effects/error.mp3").toURI().toString()));
		GridPane aPS = new GridPane();
		String username = unTFS.getText();
		String pw1 = pwTFS1.getText();
		String pw2 = pwTFS2.getText();
		if (username.equals("") && pw1.equals("") && pw2.equals("")) {
			errorplay.play();
			text.setText("ERROR! Username and password cannot be empty!");
			aPS.add(text, 0, 0);
			aPS.setPadding(new Insets(0, 0, 0, 60));
		} else if (pw1.equals("") && !username.equals("")) {
			errorplay.play();
			text.setText("ERROR! Password cannot be empty!");
			aPS.add(text, 0, 0);
			aPS.setPadding(new Insets(0, 0, 0, 110));

		} else if (pw2.equals("") && !username.equals("")) {
			errorplay.play();
			text.setText("ERROR! Password cannot be empty!");
			aPS.add(text, 0, 0);
			aPS.setPadding(new Insets(0, 0, 0, 110));

		} else if (username.equals("") && (!pw1.equals("") || !pw2.equals(""))) {
			errorplay.play();
			text.setText("ERROR! Username cannot be empty!");
			aPS.add(text, 0, 0);
			aPS.setPadding(new Insets(0, 0, 0, 110));
		} else if (!pw1.equals(pw2)) {
			errorplay.play();
			text.setText("ERROR! Passwords does not match!");
			aPS.add(text, 0, 0);
			aPS.setPadding(new Insets(0, 0, 0, 110));
		} else {
			HashMap<String, String> infos = User.getUserInfos();
			// aalready existing credentials
			if (infos.containsKey(username)) {
				errorplay.play();
				text.setText("ERROR! Username already exists!");
				aPS.add(text, 0, 0);
				aPS.setPadding(new Insets(0, 0, 0, 115));
			} else {
				// store new credentials
				text.setText("SUCCESS! Successfully registered your account!");
				Read.usersList.add(new User(username, Read.hashPassword(pw1), false, false));
				aPS.add(text, 0, 0);
				aPS.setPadding(new Insets(0, 0, 0, 75));
			}
		}
		return aPS;
	}

	public static EventHandler<ActionEvent> handleEvent(Stage stage, Scene signScene, GridPane signPane, Label text) {
		// add buttons and action pane to signPane
		EventHandler<ActionEvent> handler = e -> {
			try {
				signPane.getChildren().remove(5);
			} catch (IndexOutOfBoundsException i) {
			}
			if (!stage.getScene().equals(signScene)) {
				stage.setScene(signScene);
				GridPane bPS = getButtonPane();
				signPane.add(bPS, 0, 4);
				unTFS.clear();
				pwTFS1.clear();
				pwTFS2.clear();

			} else {
				GridPane bPS = getButtonPane();
				signPane.add(bPS, 0, 4);
				GridPane aPS = Signup.getActionPane(text);
				signPane.add(aPS, 0, 5);
			}
		};
		return handler;
	}

	public static GridPane getButtonPane() {
		// get buttons
		GridPane bPS = new GridPane();
		bPS.setHgap(100);
		bPS.add(signButton, 1, 0);
		bPS.add(Login.loginButton, 0, 0);
		bPS.setPadding(new Insets(0, 0, 0, 107.5));
		return bPS;
	}
}
