import java.io.File;
import java.util.Iterator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class HallPage {
	public static void addingHall(Stage stage, Button addHall, Scene trailerScene, MediaPlayer trailerPlay, Text text,
			ChoiceBox<String> hallsBox, Film pickedFilm) {
		addHall.setOnAction(e -> {
			GridPane addHallPane = new GridPane();
			addHallPane.setVgap(10);
			GridPane infoBox = new GridPane();
			Text infos = new Text(pickedFilm.getTitle() + "\n(" + pickedFilm.getDuration() + " minutes)");
			infoBox.setPadding(new Insets(9, 0, 0, 45));
			infos.setTextAlignment(TextAlignment.CENTER);
			infoBox.setVgap(10);
			infoBox.add(infos, 0, 0);
			infoBox.setAlignment(Pos.CENTER);

			GridPane rowBox = new GridPane();
			Label row = new Label("Row:");
			ChoiceBox<Integer> rowIndex = new ChoiceBox<>();
			rowIndex.getItems().addAll(3, 4, 5, 6, 7, 8, 9, 10);
			rowIndex.setValue(3);
			rowBox.setHgap(137);
			rowBox.addRow(0, row, rowIndex);
			rowBox.setPadding(new Insets(0, 0, 0, 60));

			GridPane colBox = new GridPane();
			Label col = new Label("Column:");
			ChoiceBox<Integer> colIndex = new ChoiceBox<>();
			colIndex.getItems().addAll(3, 4, 5, 6, 7, 8, 9, 10);
			colIndex.setValue(3);
			colBox.setHgap(115);
			colBox.addRow(0, col, colIndex);
			colBox.setPadding(new Insets(0, 0, 0, 60));

			GridPane nameBox = new GridPane();
			Label name = new Label("Name:");
			nameBox.setHgap(35);
			TextField nameField = new TextField();
			nameBox.addRow(0, name, nameField);
			nameBox.setPadding(new Insets(0, 0, 0, 60));

			GridPane priceBox = new GridPane();
			Label price = new Label("Price:");
			priceBox.setHgap(40);
			TextField priceField = new TextField();
			priceBox.addRow(0, price, priceField);
			priceBox.setPadding(new Insets(0, 0, 0, 60));

			HBox buttonBox = new HBox();
			buttonBox.setSpacing(120);
			buttonBox.setPadding(new Insets(0, 0, 0, 60));
			Button backHall = new Button("< BACK");
			backHall.setOnAction(a -> {
				stage.setScene(trailerScene);
				trailerPlay.stop();
			});
			Button addHallOK = new Button("OK");
			addHallOK.setOnAction(add -> {
				MediaPlayer errorplay = new MediaPlayer(
						new Media(new File("assets/effects/error.mp3").toURI().toString()));
				GridPane textPane = new GridPane();
				String hallName = nameField.getText();
				int rowInfo = rowIndex.getSelectionModel().getSelectedItem();
				int colInfo = colIndex.getSelectionModel().getSelectedItem();
				Integer priceInfo;
				try {
					priceInfo = Integer.parseInt(priceField.getText());
				} catch (NumberFormatException n) {
					priceInfo = -1;
				}
				if (hallName.equals("")) {
					errorplay.play();
					text.setText("Hall name cannot be empty!");
				} else if (priceInfo < 0) {
					errorplay.play();
					text.setText("Price must be positive integer!");
				} else {
					text.setText(" Hall added successfully!");
					Read.hallsList.add(new Hall(Welcome.pickedFilm.getTitle(), hallName, priceInfo, rowInfo, colInfo));
					Hall.hallNamesList.add(hallName);
					hallsBox.getItems().add(hallName);
					for (int i = 0; i < rowInfo; i++) {
						for (int j = 0; j < colInfo; j++) {
							Read.seatsList
									.add(new Seat(Welcome.pickedFilm.getTitle(), hallName, i, j, "null", priceInfo));
						}
					}
				}
				textPane.add(text, 0, 0);
				textPane.setPadding(new Insets(5, 0, 0, 53));
				textPane.setAlignment(Pos.CENTER);
				addHallPane.add(textPane, 0, 6);

			});
			buttonBox.getChildren().addAll(backHall, addHallOK);

			addHallPane.addColumn(0, infoBox, rowBox, colBox, nameBox, priceBox, buttonBox);

			Scene addHallScene = new Scene(addHallPane, 350, 280, Color.GHOSTWHITE);
			stage.setScene(addHallScene);
		});
	}

	public static void removingHall(Stage stage, Button removeHall, Scene trailerScene, MediaPlayer trailerPlay,
			Text text, ChoiceBox<String> hallsBox, Film pickedFilm) {
		removeHall.setOnAction(remove -> {
			String pickedFilmName = pickedFilm.getTitle();
			GridPane removeHallPane = new GridPane();
			removeHallPane.setVgap(20);
			VBox textBox = new VBox();
			textBox.setPadding(new Insets(9, 0, 0, 40));
			textBox.setSpacing(2);
			Text removeText1 = new Text("Select the hall you desire to remove from");
			Text removeText2 = new Text(pickedFilmName);
			Text removeText3 = new Text("then click OK.");
			removeText2.setTextAlignment(TextAlignment.CENTER);
			removeText3.setTextAlignment(TextAlignment.CENTER);
			textBox.getChildren().addAll(removeText1, removeText2, removeText3);
			textBox.setAlignment(Pos.CENTER);
			removeHallPane.add(textBox, 0, 0);

			GridPane hallsPane = new GridPane();
			ChoiceBox<String> hallBox = new ChoiceBox<>();
			for (Hall hall : Read.hallsList) {
				if (hall.getFilmName().equals(pickedFilmName)) {
					hallBox.getItems().add(hall.getHallName());
				}
			}
			try {
				hallBox.setValue(hallBox.getItems().get(0));
			} catch (IndexOutOfBoundsException i) {

			}

			hallsPane.add(hallBox, 0, 0);
			hallsPane.setPadding(new Insets(0, 0, 0, 45));
			hallsPane.setAlignment(Pos.CENTER);
			removeHallPane.add(hallsPane, 0, 1);

			GridPane buttons = new GridPane();
			Button back = new Button("< BACK");
			Button OK = new Button("OK");
			buttons.setHgap(40);
			back.setOnAction(b -> {
				stage.setScene(trailerScene);
				trailerPlay.stop();
			});
			OK.setOnAction(e -> {
				String pickedHallName = hallBox.getSelectionModel().getSelectedItem();
				Hall pickedHall = Hall.getHall(pickedHallName);
				Read.hallsList.remove(pickedHall);
				for (Iterator<Seat> seatIt = Read.seatsList.iterator(); seatIt.hasNext();) {
					Seat s = seatIt.next();
					if (s.getHallName().equals(pickedHallName)) {
						seatIt.remove();
					}
				}
				hallBox.getItems().remove(pickedHallName);
				hallsBox.getItems().remove(pickedHallName);
				try {
					hallBox.setValue(hallBox.getItems().get(0));
					hallsBox.setValue(hallsBox.getItems().get(0));
				} catch (IndexOutOfBoundsException i) {

				}

			});
			buttons.add(back, 0, 0);
			buttons.add(OK, 1, 0);
			buttons.setPadding(new Insets(0, 0, 0, 25));
			buttons.setAlignment(Pos.CENTER);
			removeHallPane.add(buttons, 0, 2);
			Scene removeHallScene = new Scene(removeHallPane, 350, 200, Color.GHOSTWHITE);
			stage.setScene(removeHallScene);

		});
	}
}
