import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Trailer {
	public static void trailerWorks(Stage stage, boolean isAdmin) {
		Read.effectHandle(Welcome.butOK);
		Welcome.butOK.setOnAction(tr -> {

			try {
				String pickedFilmName = Welcome.filmbox.getSelectionModel().getSelectedItem();
				Film pickedFilm = Film.getFilm(pickedFilmName);
				GridPane TrailerPane = new GridPane();

				TrailerPane.setVgap(20);

				GridPane infoPane = new GridPane();
				Label info = new Label(pickedFilm.getTitle() + " (" + pickedFilm.getDuration() + " minutes)");
				infoPane.add(info, 0, 0);
				infoPane.setAlignment(Pos.CENTER);
				infoPane.setPadding(new Insets(5, 0, 0, 10));
				TrailerPane.add(infoPane, 0, 0);

				GridPane mediaPane = new GridPane();
				mediaPane.setHgap(10);
				Media trailer = new Media(
						new File("assets/trailers/" + pickedFilm.getTrailerName()).toURI().toString());
				MediaPlayer trailerPlay = new MediaPlayer(trailer);
				MediaView trailerShow = new MediaView(trailerPlay);
				trailerPlay.setAutoPlay(true);
				trailerShow.setFitWidth(500);
				trailerShow.setFitHeight(300);

				mediaPane.add(trailerShow, 0, 0);
				mediaPane.setPadding(new Insets(0, 0, 0, 30));

				VBox buttonsBox = new VBox();
				buttonsBox.setSpacing(10);
				buttonsBox.setPadding(new Insets(5, 0, 0, 0));

				Button playOrPause = new Button("||");
				Read.effectHandle(playOrPause);
				playOrPause.setOnAction(e -> {
					if (playOrPause.getText().equals(">")) {
						trailerPlay.play();
						playOrPause.setText("||");
					} else if (playOrPause.getText().equals("||")) {
						trailerPlay.pause();
						playOrPause.setText(">");
					}
				});
				buttonsBox.getChildren().add(playOrPause);

				Button forward = new Button(">>");
				Button backward = new Button("<<");
				Button begin = new Button("|<<");
				Read.effectHandle(forward);
				Read.effectHandle(backward);
				Read.effectHandle(begin);
				forward.setOnAction(e -> {
					trailerPlay.seek(trailerPlay.getCurrentTime().add(Duration.seconds(5)));
				});
				backward.setOnAction(e -> {
					trailerPlay.seek(trailerPlay.getCurrentTime().add(Duration.seconds(-5)));
				});
				begin.setOnAction(e -> {
					trailerPlay.seek(trailerPlay.getStartTime());
				});

				Slider volSet = new Slider();
				volSet.setValue(trailerPlay.getVolume() * 100);
				volSet.valueProperty().addListener(vc -> {
					trailerPlay.setVolume(volSet.getValue() / 100);
				});
				volSet.setOrientation(Orientation.VERTICAL);
				buttonsBox.getChildren().addAll(forward, backward, begin, volSet);
				buttonsBox.setAlignment(Pos.CENTER);

				mediaPane.add(buttonsBox, 1, 0);
				TrailerPane.add(mediaPane, 0, 1);

				GridPane buttonsAndHall = new GridPane();
				buttonsAndHall.setHgap(20);
				buttonsAndHall.setPadding(new Insets(0, 0, 0, 20));
				Button butHallBACK = new Button("< BACK");
				Read.effectHandle(butHallBACK);
				Welcome.backToWelcome(stage, butHallBACK);
				Button hallOK = new Button("OK");

				ChoiceBox<String> hallsBox = new ChoiceBox<String>();
				for (Hall hall : Read.hallsList) {
					if (hall.getFilmName().equals(pickedFilmName)) {
						hallsBox.getItems().add(hall.getHallName());
					}
				}
				try {
					hallsBox.setValue(hallsBox.getItems().get(0));
				} catch (IndexOutOfBoundsException i) {
				}

				Scene trailerScene = new Scene(TrailerPane, 600, 400, Color.GHOSTWHITE);
				stage.setScene(trailerScene);

				buttonsAndHall.add(butHallBACK, 0, 0);
				buttonsAndHall.setAlignment(Pos.CENTER);
				if (isAdmin) {
					Button addHall = new Button("Add Hall");
					Button removeHall = new Button("Remove Hall");
					Text text = new Text();

					HallPage.addingHall(stage, addHall, trailerScene, trailerPlay, text, hallsBox, pickedFilm);
					HallPage.removingHall(stage, removeHall, trailerScene, trailerPlay, text, hallsBox, pickedFilm);

					buttonsAndHall.add(addHall, 1, 0);
					buttonsAndHall.add(removeHall, 2, 0);
					buttonsAndHall.add(hallsBox, 3, 0);
					buttonsAndHall.add(hallOK, 4, 0);
				} else {
					buttonsAndHall.add(hallsBox, 1, 0);
					buttonsAndHall.add(hallOK, 2, 0);
				}

				TrailerPane.add(buttonsAndHall, 0, 2);
				hallOK.setOnAction(ok -> {
					if (hallsBox.getSelectionModel().getSelectedItem() == null) {
						MediaPlayer errorplay = new MediaPlayer(
								new Media(new File("assets/effects/error.mp3").toURI().toString()));
						errorplay.play();
					} else {
						SeatsPage.seatWorks(stage, trailerScene, hallsBox, pickedFilm);
					}
				});
			} catch (NullPointerException n) {
				MediaPlayer errorplay = new MediaPlayer(
						new Media(new File("assets/effects/error.mp3").toURI().toString()));
				errorplay.play();
			}
		});

	}
}
