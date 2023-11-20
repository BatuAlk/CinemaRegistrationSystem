
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Welcome {
	static Button butBack = new Button("< BACK"); // back button at addFilmScene , static for easy access
	static Button butBackrem = new Button("< BACK"); // back button at removeFilmScene , static for easy access
	static Button butaddOK = new Button("OK"); // ok button at addFilmScene , static for easy access
	static Button butOK = new Button("OK"); // ok button at welcomeScene , static for easy access
	static User pickedUserInTable;
	static Film pickedFilm;
	static ChoiceBox<String> filmbox = new ChoiceBox<String>(FXCollections.observableList(Film.filmNamesList));
	static GridPane welcomePane = new GridPane();
	static Scene welcomeScene = new Scene(welcomePane, 450, 180, Color.GHOSTWHITE);

	public static void welcomePane(String username, Boolean isCM, Boolean isAdmin, Stage stage, Scene loginScene,
			Text welcomeText) {
		welcomePane.getChildren().clear();
		welcomePane.setVgap(20);

		String title = "";
		String welcomeinfo = "";
		if (isCM && isAdmin) {
			title = "(Admin - Club Member)";
			welcomeinfo = " You can either select film below or do edits.";
		} else if (isAdmin && !isCM) {
			title = "(Admin)";
			welcomeinfo = " You can either select film below or do edits.";
		} else if (isCM && !isAdmin) {
			title = "(Club Member)";
			welcomeinfo = "   Select a film then click OK to continue.";
		} else if (title.equals("")) {
			welcomeinfo = "   Select a film then click OK to continue.";
		}

		welcomeText.setText(" Welcome " + username + " " + title + "!\n" + welcomeinfo);
		VBox welcomeBox = new VBox();
		welcomeBox.getChildren().add(welcomeText);
		welcomeText.setTextAlignment(TextAlignment.CENTER);
		welcomeBox.setAlignment(Pos.CENTER);
		welcomeBox.setPadding(new Insets(5, 0, 0, 0));
		if (welcomePane.getChildren().contains(welcomeBox)) {
			welcomePane.getChildren().remove(welcomeBox);
		}
		welcomePane.add(welcomeBox, 0, 0);

		GridPane filmsPane = getFilmListPane();
		welcomePane.add(filmsPane, 0, 1);

		GridPane logOutPane = new GridPane();
		Button butOUT = new Button("LOG OUT");
		Read.effectHandle(butOUT);
		logOutPane.add(butOUT, 0, 0);
		logOutPane.setPadding(new Insets(0, 0, 10, 0));
		logOutPane.setAlignment(Pos.CENTER_RIGHT);

		stage.setScene(welcomeScene);

		GridPane adminPane = new GridPane();
		if (isAdmin) {
			// set admin special buttons and view
			Button butAdd = new Button("Add Film");
			Button butRem = new Button("Remove Film");
			Button butEdit = new Button("Edit Users");
			adminPane.setHgap(10);
			Read.effectHandle(butAdd);
			Read.effectHandle(butRem);
			Read.effectHandle(butEdit);
			adminPane.add(butAdd, 0, 0);
			adminPane.add(butRem, 1, 0);
			adminPane.add(butEdit, 2, 0);
			adminPane.setPadding(new Insets(0, 0, 0, 0));
			welcomePane.add(adminPane, 0, 2);
			welcomePane.add(logOutPane, 0, 3);
			adminPane.setAlignment(Pos.CENTER);
			try {
				String pickedFilmname = filmbox.getSelectionModel().getSelectedItem();
				pickedFilm = Film.getFilm(pickedFilmname);
			} catch (IndexOutOfBoundsException i) {
				MediaPlayer errorplay = new MediaPlayer(
						new Media(new File("assets/effects/error.mp3").toURI().toString()));
				errorplay.play();
			}

			butAdd.setOnAction(x -> addFilm(stage, butAdd));
			butRem.setOnAction(y -> removeFilm(stage, butRem));
			butEdit.setOnAction(z -> editUsers(stage, butEdit));
		} else {
			welcomePane.add(logOutPane, 0, 2);
		}
		if (welcomePane.getChildren().contains(adminPane) && !isAdmin) {
			welcomePane.getChildren().remove(adminPane);
			try {
				welcomePane.getChildren().remove(3);
			} catch (ArrayIndexOutOfBoundsException arr) {
			}
		}

		// return back to login
		Login.backToLogin(butOUT, stage);
		if (filmbox.getSelectionModel().getSelectedItem() == null) {
			MediaPlayer errorplay = new MediaPlayer(new Media(new File("assets/effects/error.mp3").toURI().toString()));
			errorplay.play();
		} else {
			Trailer.trailerWorks(stage, isAdmin);
		}

	}

	public static void backToWelcome(Stage stage, Button but) {
		// set scene to welcomeScene
		but.setOnAction(back -> stage.setScene(welcomeScene));
	}

	public static GridPane getButtonPane() {
		// get buttons
		GridPane buttonsPane = new GridPane();
		buttonsPane.setPadding(new Insets(0, 0, 0, 90));
		buttonsPane.setHgap(180);
		buttonsPane.add(butBack, 0, 0);
		buttonsPane.add(butaddOK, 1, 0);
		Read.effectHandle(butBack);
		Read.effectHandle(butaddOK);
		return buttonsPane;
	}

	public static GridPane getFilmListPane() {
		// get films as a choicebox
		GridPane filmsPane = new GridPane();
		try {
			filmbox.setValue(Film.filmNamesList.get(ThreadLocalRandom.current().nextInt(0, Film.filmNamesList.size())));
		} catch (IllegalArgumentException i) {
			MediaPlayer errorplay = new MediaPlayer(new Media(new File("assets/effects/error.mp3").toURI().toString()));
			errorplay.play();
		}
		filmsPane.setHgap(10);
		filmsPane.add(filmbox, 0, 0);
		filmsPane.add(butOK, 1, 0);
		filmsPane.setPadding(new Insets(0, 0, 0, 40));
		filmsPane.setAlignment(Pos.CENTER);
		return filmsPane;
	}

	public static void addFilm(Stage stage, Button butAdd) {
		// adding film method
		GridPane addFilmPane = new GridPane();
		addFilmPane.setVgap(15);
		Label addFText = new Label("Please give name, relative trailer path and duration of the film.");
		GridPane addTextPane = new GridPane();
		addTextPane.setPadding(new Insets(5, 0, 0, 25));
		addTextPane.add(addFText, 0, 0);
		addFilmPane.add(addTextPane, 0, 0);

		GridPane addName = new GridPane();
		addName.setPadding(new Insets(0, 0, 0, 90));
		addName.setHgap(95);
		Label nameAdd = new Label("Name:");
		TextField nameInput = new TextField();
		addName.add(nameAdd, 0, 0);
		addName.add(nameInput, 1, 0);
		addFilmPane.add(addName, 0, 1);

		GridPane addTrailer = new GridPane();
		addTrailer.setPadding(new Insets(0, 0, 0, 90));
		addTrailer.setHgap(50);
		Label trAdd = new Label("Trailer (Path):");
		TextField trInput = new TextField();
		addTrailer.add(trAdd, 0, 0);
		addTrailer.add(trInput, 1, 0);
		addFilmPane.add(addTrailer, 0, 2);

		GridPane addDur = new GridPane();
		addDur.setHgap(51);
		addDur.setPadding(new Insets(0, 0, 0, 90));
		Label durAdd = new Label("Duration (m):");
		TextField durInput = new TextField();
		addDur.add(durAdd, 0, 0);
		addDur.add(durInput, 1, 0);
		addFilmPane.add(addDur, 0, 3);
		GridPane buttonsPane = getButtonPane();
		Scene addFilmScene = new Scene(addFilmPane, 450, 250, Color.GHOSTWHITE);
		addFilmPane.getChildren().remove(buttonsPane);
		addFilmPane.add(buttonsPane, 0, 4);
		stage.setScene(addFilmScene);
		addFilmPane.getChildren().remove(buttonsPane);
		addFilmPane.add(buttonsPane, 0, 4);
		stage.setScene(addFilmScene);
		Label infotext = new Label();

		// button action
		butaddOK.setOnAction(action -> {
			GridPane infoPane = getAddInfoPane(nameInput, trInput, durInput, infotext, getFilmListPane(), stage);
			addFilmPane.add(infoPane, 0, 5);

		});
		// if clicked back
		backToWelcome(stage, butBack);
	}

	public static void removeFilm(Stage stage, Button butRem) {
		// remove film method
		GridPane removeFilmPane = new GridPane();
		removeFilmPane.setVgap(15);
		Label removeFilmText = new Label("Select the film that you desire to remove then click OK.");
		GridPane TextPane = new GridPane();
		TextPane.setPadding(new Insets(5, 0, 0, 22));
		TextPane.add(removeFilmText, 0, 0);
		removeFilmPane.add(TextPane, 0, 0);

		GridPane filmList = new GridPane();
		// create second choicebox to prevent the previous one to disappear
		filmList.setPadding(new Insets(0, 0, 0, 40));
		ChoiceBox<String> filmsbox = new ChoiceBox<String>(FXCollections.observableArrayList(Film.filmNamesList));
		filmsbox.setValue(Film.filmNamesList.get(ThreadLocalRandom.current().nextInt(0, Film.filmNamesList.size())));
		filmList.add(filmsbox, 0, 0);
		removeFilmPane.add(filmList, 0, 1);

		GridPane buttonsPane = new GridPane();
		Button butremOK = new Button("OK");
		buttonsPane.setHgap(10);
		buttonsPane.add(butBackrem, 0, 0);
		buttonsPane.add(butremOK, 1, 0);
		buttonsPane.setPadding(new Insets(0, 0, 0, 140));
		removeFilmPane.add(buttonsPane, 0, 2);

		Scene removeFilmScene = new Scene(removeFilmPane, 400, 150, Color.GHOSTWHITE);
		removeFilmPane.getChildren().remove(buttonsPane);
		removeFilmPane.add(buttonsPane, 0, 2);
		stage.setScene(removeFilmScene);
		removeFilmPane.getChildren().remove(buttonsPane);
		removeFilmPane.add(buttonsPane, 0, 2);
		stage.setScene(removeFilmScene);
		butremOK.setOnAction(a -> {
			// update lists and checkboxes
			String pickedFilmname = filmsbox.getSelectionModel().getSelectedItem();
			pickedFilm = Film.getFilm(pickedFilmname);
			filmsbox.getItems().remove(pickedFilmname);
			Film.filmNamesList.remove(pickedFilmname);
			Read.filmsList.remove(pickedFilm);
			removeFilmPane.getChildren().remove(filmList);

			GridPane newFilmList = new GridPane();
			newFilmList.add(filmsbox, 0, 0);
			newFilmList.setAlignment(Pos.CENTER);
			removeFilmPane.add(newFilmList, 0, 1);
			// set random value of choicebox
			try {
				filmsbox.setValue(
						Film.filmNamesList.get(ThreadLocalRandom.current().nextInt(0, Film.filmNamesList.size())));
			} catch (IllegalArgumentException i) {
				MediaPlayer errorplay = new MediaPlayer(
						new Media(new File("assets/effects/error.mp3").toURI().toString()));
				errorplay.play();
			}
			welcomePane.getChildren().remove(getFilmListPane());
			// update choicebox
			filmbox = new ChoiceBox<String>(FXCollections.observableArrayList(Film.filmNamesList));
			welcomePane.add(getFilmListPane(), 0, 1);

		});
		backToWelcome(stage, butBackrem);
	}

	public static GridPane getAddInfoPane(TextField nameInput, TextField trInput, TextField durInput, Label infotext,
			GridPane welcomeFilmBox, Stage stage) {
		// action pane for adding film
		MediaPlayer errorplay = new MediaPlayer(new Media(new File("assets/effects/error.mp3").toURI().toString()));
		GridPane infoPane = new GridPane();
		String filmInfo = nameInput.getText();
		String trInfo = trInput.getText();
		Integer durInfo;
		try {
			durInfo = Integer.parseInt(durInput.getText());
		} catch (NumberFormatException n) {
			durInfo = -1;
		}
		boolean valid = durInfo > 0;

		if (filmInfo.equals("")) {
			errorplay.play();
			infotext.setText("Film name cannot be empty!");
			infoPane.add(infotext, 0, 0);
			infoPane.setPadding(new Insets(0, 0, 0, 150));

		} else if (trInfo.equals("")) {
			errorplay.play();
			infotext.setText("Trailer path cannot be empty!");
			infoPane.add(infotext, 0, 0);
			infoPane.setPadding(new Insets(0, 0, 0, 150));
		} else if (!new File("assets/trailers/" + trInfo).exists()) {
			errorplay.play();
			infotext.setText("Trailer path is not valid!");
			infoPane.add(infotext, 0, 0);
			infoPane.setPadding(new Insets(0, 0, 0, 150));
		} else if (!valid) {
			errorplay.play();
			infotext.setText("Duration must be a positive integer!");
			infoPane.add(infotext, 0, 0);
			infoPane.setPadding(new Insets(0, 0, 0, 130));

		} else {
			infotext.setText("Film added successfully.");
			Film newFilm = new Film(filmInfo, trInfo, durInfo);
			infoPane.add(infotext, 0, 5);
			infoPane.setPadding(new Insets(0, 0, 0, 150));
			// add film to checkbox and lists
			Read.filmsList.add(newFilm);
			Film.filmNamesList.add(filmInfo);
			filmbox = new ChoiceBox<String>(FXCollections.observableArrayList(Film.filmNamesList));
			welcomePane.getChildren().remove(getFilmListPane());
			welcomePane.add(getFilmListPane(), 0, 1);
		}
		return infoPane;
	}

	@SuppressWarnings("unchecked")
	public static void editUsers(Stage stage, Button butEdit) {
		ArrayList<User> usersList = new ArrayList<>(Read.usersList);
		usersList.remove(Login.loginedUser);
		GridPane usersGrid = new GridPane();
		usersGrid.setVgap(15);

		// create observable list of users
		ObservableList<User> usersObList = FXCollections.observableArrayList(usersList);
		TableView<User> userTable = new TableView<>();
		userTable.setItems(usersObList);
		VBox table = new VBox();

		// add username col
		TableColumn<User, String> usernameCol = new TableColumn<>("Username");
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
		usernameCol.setMinWidth(150);

		// add CLUBMEMBER col
		TableColumn<User, Boolean> clubMemberCol = new TableColumn<>("Club Member");
		clubMemberCol.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isClubMember()));
		clubMemberCol.setMinWidth(150);

		// add ADMIN col
		TableColumn<User, Boolean> adminCol = new TableColumn<>("Admin");
		adminCol.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isAdmin()));
		adminCol.setMinWidth(150);

		userTable.getColumns().addAll(usernameCol, clubMemberCol, adminCol);
		table.getChildren().addAll(userTable);
		// add tableview to gridpane

		GridPane tableGrid = new GridPane();
		tableGrid.add(table, 0, 0);
		tableGrid.setPadding(new Insets(15, 0, 0, 75));
		usersGrid.add(tableGrid, 0, 0);

		// action buttons
		Button backEdit = new Button("< BACK");
		Button clubChange = new Button("Promote/Demote Club Member");
		Button adminChange = new Button("Promote/Demote Admin");
		Read.effectHandle(backEdit);
		Read.effectHandle(adminChange);
		Read.effectHandle(clubChange);

		GridPane buttonsPane = new GridPane();
		buttonsPane.setHgap(10);
		buttonsPane.add(backEdit, 0, 0);
		buttonsPane.add(clubChange, 1, 0);
		buttonsPane.add(adminChange, 2, 0);
		buttonsPane.setPadding(new Insets(0, 0, 0, 65));
		usersGrid.add(buttonsPane, 0, 1);

		userTable.setRowFactory(tableView -> {
			// get row and pickeduser from the table
			TableRow<User> row = new TableRow<User>();
			row.setOnMouseClicked(event -> {
				pickedUserInTable = row.getItem();
			});
			return row;
		});

		clubChange.setOnAction(c -> {
			// change club member values
			int userIndex = Read.usersList.indexOf(pickedUserInTable);
			User userInList = Read.usersList.get(userIndex);
			userInList.setClubMember(!userInList.isClubMember());
			userTable.getColumns().remove(clubMemberCol);
			// update table
			clubMemberCol.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isClubMember()));
			userTable.getColumns().add(1, clubMemberCol);
		});

		adminChange.setOnAction(a -> {
			// change admin values
			int userIndex = Read.usersList.indexOf(pickedUserInTable);
			User userInList = Read.usersList.get(userIndex);
			userInList.setAdmin(!userInList.isAdmin());
			userTable.getColumns().remove(adminCol);
			// update table
			adminCol.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isAdmin()));
			userTable.getColumns().add(2, adminCol);
		});

		// set scene to editUsersScene
		Scene userScene = new Scene(usersGrid, 600, 500, Color.GHOSTWHITE);
		stage.setScene(userScene);
		backToWelcome(stage, backEdit);
	}

}
