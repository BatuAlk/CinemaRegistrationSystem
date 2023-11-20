
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Login {
	static User loginedUser;
	static TextField unTF = new TextField(); // username text input
	static PasswordField pwTF = new PasswordField(); // password input
	static Button loginButton = new Button("LOGIN");
	static GridPane loginPane = getLoginPane();
	static Scene loginScene = new Scene(loginPane, 450, 270, Color.GHOSTWHITE);
	static int errorCount = 0;

	public static GridPane getActionPane(Label text, Stage stage, Scene loginScene) {
		// returns the result of the button click (e.g. Error if fields are empty)
		GridPane actionPane = new GridPane();
		MediaPlayer errorplay = new MediaPlayer(new Media(new File("assets/effects/error.mp3").toURI().toString()));
		String username = unTF.getText();
		String password = pwTF.getText();

		if (username.equals("") && password.equals("")) {
			errorplay.play();
			text.setText("ERROR! Username and password cannot be empty!");
			actionPane.add(text, 0, 0);
			actionPane.setPadding(new Insets(0, 0, 0, 60));

		} else if (password.equals("") && !username.equals("")) {
			errorplay.play();
			text.setText("ERROR! Password cannot be empty!");
			actionPane.add(text, 0, 0);
			actionPane.setPadding(new Insets(0, 0, 0, 110));

		} else if (username.equals("") && !password.equals("")) {
			errorplay.play();
			text.setText("ERROR! Username cannot be empty!");
			actionPane.add(text, 0, 0);
			actionPane.setPadding(new Insets(0, 0, 0, 110));

		} else {
			HashMap<String, String> infos = User.getUserInfos();
			// check if credentials are the same
			if (infos.containsKey(username)) {
				if (infos.get(username).equals(Read.hashPassword(password))) {
					errorCount = 0;
					loginedUser = User.getUser(username);
					Text welcometext = new Text();
					try {
						if (!Read.fileBackup.exists()) {
							throw new FileNotFoundException();
						}
						// go to welcome scene
						Welcome.welcomePane(username, loginedUser.isClubMember(), loginedUser.isAdmin(), stage,
								loginScene, welcometext);

					} catch (FileNotFoundException e) {
						// if backup.dat doesnt exist or there's no film
						errorplay.play();
						VBox errorBox = new VBox();
						errorBox.setPadding(new Insets(10, 20, 0, 20));
						errorBox.setSpacing(80);
						Text errorText = new Text("ERROR! Cannot find any films! \n Terminating in 3 seconds..");
						Image errorImage = new Image("file:assets/extra_effects/error.png");
						ImageView errorShow = new ImageView(errorImage);
						errorShow.setFitHeight(100);
						errorShow.setFitWidth(100);
						errorBox.getChildren().addAll(errorShow, errorText);
						errorBox.setAlignment(Pos.CENTER);
						Scene errorScene = new Scene(errorBox, 400, 300, Color.FLORALWHITE);
						stage.setScene(errorScene);
						PauseTransition delay = new PauseTransition(Duration.seconds(3));
						delay.setOnFinished(event -> stage.close());
						delay.play();
					}
				} else {
					errorplay.play();
					text.setText("ERROR! No such credentials!");
					errorCount++; // get count of error done repeatedly
					actionPane.add(text, 0, 0);
					actionPane.setPadding(new Insets(0, 0, 0, 142));
				}

			} else if (!infos.containsKey(username)) {
				errorplay.play();
				text.setText("ERROR! User does not exist!");
				actionPane.add(text, 0, 0);
				actionPane.setPadding(new Insets(0, 0, 0, 142));
			}
		}
		return actionPane;
	}

	public static GridPane getLoginPane() {
		// return login pane without the buttons
		GridPane loginPane = new GridPane();
		loginPane.setVgap(20);

		GridPane textPane = new GridPane();
		textPane.setPadding(new Insets(5, 0, 0, 52));
		Text welcometext = new Text("   Welcome to the HUCS Cinema Reservation System!" + "\n"
				+ "  Please enter your credentials below and click LOGIN." + "\n"
				+ "You can create a new account by clicking SIGN UP button.");

		textPane.add(welcometext, 0, 0);
		loginPane.add(textPane, 0, 0);

		Label un = new Label("Username:");

		GridPane userInputPane = new GridPane();
		userInputPane.setHgap(15);
		userInputPane.add(un, 0, 0);
		userInputPane.add(unTF, 1, 0);
		userInputPane.setPadding(new Insets(0, 0, 0, 107.5));
		loginPane.add(userInputPane, 0, 1);

		Label pw = new Label("Password:");

		GridPane passwordInputPane = new GridPane();
		passwordInputPane.setHgap(17);
		passwordInputPane.add(pw, 0, 0);
		passwordInputPane.add(pwTF, 1, 0);
		passwordInputPane.setPadding(new Insets(0, 0, 0, 107.5));
		loginPane.add(passwordInputPane, 0, 2);

		return loginPane;
	}

	public static EventHandler<ActionEvent> handleEvent(Stage stage, Label text, Button butLogin) {
		// setting button click event
		EventHandler<ActionEvent> handler = e -> {
			try {
				loginPane.getChildren().remove(4);
			} catch (IndexOutOfBoundsException i) {
			}
			if (errorCount == Read.maxErrorCount) {
				// reached maximum error count , ban
				MediaPlayer error10play = new MediaPlayer(
						new Media(new File("assets/extra_effects/win10.mp3").toURI().toString()));
				error10play.play();
				VBox errorBox = new VBox();
				errorBox.setSpacing(5);

				// stop button for block-time seconds
				Thread thread = new Thread() {
					@Override
					public void run() {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								butLogin.setDisable(true);
								Text error1 = new Text("You have entered wrong credentials " + Read.maxErrorCount
										+ " times in a row.");
								Text error2 = new Text("You have been banned for " + Read.blockTime + " seconds.");
								errorBox.getChildren().addAll(error1, error2);
								unTF.clear();
								pwTF.clear();
								loginPane.add(errorBox, 0, 4);
								errorBox.setAlignment(Pos.CENTER);
								errorBox.setPadding(new Insets(0, 0, 0, 10));
							}
						});
						try {
							Thread.sleep(Read.blockTime * 1000);
						} catch (InterruptedException i) {
						}
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								loginPane.getChildren().remove(errorBox);
								butLogin.setDisable(false);
							}
						});
					}
				};
				thread.start();
				errorCount = 0;

			} else {
				if (!stage.getScene().equals(loginScene)) {
					// set scene to loginScene and add buttons if scene is not loginScene
					loginPane.getChildren().remove(3);
					GridPane buttonsPane = getButtonPane();
					loginPane.add(buttonsPane, 0, 3);
					stage.setScene(loginScene);
					unTF.clear();
					pwTF.clear();
				} else {
					// add actionPane to loginPane

					GridPane actionPane = Login.getActionPane(text, stage, loginScene);
					loginPane.add(actionPane, 0, 4);

				}
			}
		};
		return handler;
	}

	public static GridPane getButtonPane() {
		// return buttons
		GridPane buttonsPane = new GridPane();
		buttonsPane.setHgap(105);
		buttonsPane.add(Signup.signButton, 0, 0);
		buttonsPane.add(loginButton, 1, 0);
		buttonsPane.setPadding(new Insets(0, 0, 0, 107.5));
		return buttonsPane;
	}

	public static void backToLogin(Button butOUT, Stage stage) {
		// change scene to loginScene and add buttons
		butOUT.setOnAction(e -> {

			stage.setScene(loginScene);
			unTF.clear();
			pwTF.clear();
		});
	}
}
