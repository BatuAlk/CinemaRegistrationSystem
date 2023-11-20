import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class SeatsPage {
	static User pickedUserFromBox;

	public static void seatWorks(Stage stage, Scene trailerScene, ChoiceBox<String> hallsBox, Film pickedFilm) {
		GridPane seatPane = new GridPane();
		seatPane.setVgap(10);

		String pickedHallName = hallsBox.getSelectionModel().getSelectedItem();
		Hall pickedHall = Hall.getHall(pickedHallName);
		GridPane entryPane = new GridPane();
		Text entry = new Text(
				pickedFilm.getTitle() + " (" + pickedFilm.getDuration() + " minutes) Hall: " + pickedHallName);
		entryPane.setPadding(new Insets(9, 10, 0, 10));
		entry.setTextAlignment(TextAlignment.CENTER);
		entryPane.setAlignment(Pos.CENTER);
		entryPane.add(entry, 0, 0);
		seatPane.add(entryPane, 0, 0);
		GridPane seats = new GridPane();
		seats.setPadding(new Insets(10, 30, 0, 30));
		Image emptySeat = new Image("file:assets/icons/empty_seat.png");
		Image reservedSeat = new Image("file:assets/icons/reserved_seat.png");

		Seat[][] seatsArray = new Seat[pickedHall.getRow_count()][pickedHall.getCol_count()];
		HBox texts = new HBox();
		Text text = new Text("");
		HBox actionBox = new HBox();
		Text actext = new Text();

		for (int r = 0; r < pickedHall.getRow_count(); r++) {
			for (int c = 0; c < pickedHall.getCol_count(); c++) {
				Seat currentSeat = Seat.getSeat(pickedHall, r, c);
				seatsArray[r][c] = currentSeat;
				ImageView emptyView = new ImageView(emptySeat);
				emptyView.setFitHeight(40);
				emptyView.setFitWidth(40);

				ImageView reservedView = new ImageView(reservedSeat);
				reservedView.setFitHeight(40);
				reservedView.setFitWidth(40);

				int rowIndex = pickedHall.getRow_count() - r;
				int colIndex = c + 1;

				// set buttons and seat
				Button emptyButton = new Button("", emptyView);
				Button reservedButton = new Button("", reservedView);
				if (Seat.getSeat(pickedHall, r, c).getOwnerName().equals("null")) {
					seats.add(emptyButton, c, r);
				} else {
					seats.add(reservedButton, c, r);
				}

				HBox usersBox = new HBox();
				ChoiceBox<String> users = new ChoiceBox<>(
						FXCollections.observableArrayList(User.getUserInfos().keySet()));
				users.setValue(users.getItems().get(0));
				pickedUserFromBox = User.getUser(users.getValue());

				User currentUser = User.getUser(currentSeat.getOwnerName());
				int discounted_price = (int) Math.ceil(pickedHall.getSeatPrice() * Read.discount);

				if (Login.loginedUser.isAdmin()) {

					usersBox.getChildren().add(users);
					usersBox.setAlignment(Pos.CENTER);
					seatPane.add(usersBox, 0, 3);
					Read.effectHandle(emptyButton);
					emptyButton.setOnMouseEntered(e -> {
						text.setText("Not bought yet!");
						texts.getChildren().add(text);
					});

					clearText(emptyButton, text, texts);

					Read.effectHandle(reservedButton);
					reservedButton.setOnMouseEntered(e -> {
						if (currentUser.isClubMember()) {
							text.setText("Bought by " + currentSeat.getOwnerName() + " for "
									+ currentSeat.getBoughtPrice() + " TL!");
						} else {
							text.setText("Bought by " + currentSeat.getOwnerName() + " for "
									+ currentSeat.getBoughtPrice() + " TL!");
						}
						texts.getChildren().add(text);
					});

					clearText(reservedButton, text, texts);

					users.setOnAction(e -> {
						pickedUserFromBox = User.getUser(users.getValue());

					});

					emptyButton.setOnAction(e -> {
						if (emptyButton.getGraphic().equals(emptyView)) {
							emptyButton.setGraphic(reservedView);
							if (pickedUserFromBox.isClubMember()) {
								actext.setText("Seat at " + rowIndex + "-" + colIndex + " bought for "
										+ pickedUserFromBox.getUsername() + " for " + discounted_price
										+ " TL successfully!");
								currentSeat.setOwnerName(pickedUserFromBox.getUsername());
								currentSeat.setBoughtPrice(discounted_price);
							} else {
								actext.setText("Seat at " + rowIndex + "-" + colIndex + " bought for "
										+ pickedUserFromBox.getUsername() + " for " + pickedHall.getSeatPrice()
										+ " TL successfully!");
								currentSeat.setOwnerName(pickedUserFromBox.getUsername());
								currentSeat.setBoughtPrice(pickedHall.getSeatPrice());
							}

							emptyButton.setOnMouseEntered(ee -> {
								text.setText("Bought by " + currentSeat.getOwnerName() + " for "
										+ currentSeat.getBoughtPrice() + " TL!");
								texts.getChildren().add(text);
							});

							clearText(emptyButton, text, texts);
						} else {
							emptyButton.setGraphic(emptyView);
							actext.setText("Seat at " + rowIndex + "-" + colIndex + " refunded successfully!");
							currentSeat.setBoughtPrice(0);
							currentSeat.setOwnerName("null");

							emptyButton.setOnMouseEntered(er -> {
								text.setText("Not bought yet!");
								texts.getChildren().add(text);
							});
							clearText(emptyButton, text, texts);
						}
					});

					reservedButton.setOnAction(e -> {
						if (reservedButton.getGraphic().equals(reservedView)) {
							reservedButton.setGraphic(emptyView);
							actext.setText("Seat at " + rowIndex + "-" + colIndex + " refunded successfully!");
							currentSeat.setBoughtPrice(0);
							currentSeat.setOwnerName("null");

							reservedButton.setOnMouseEntered(er -> {
								text.setText("Not bought yet!");
								texts.getChildren().add(text);
							});
							clearText(reservedButton, text, texts);

						} else {
							reservedButton.setGraphic(reservedView);
							if (pickedUserFromBox.isClubMember()) {
								actext.setText("Seat at " + rowIndex + "-" + colIndex + " bought for "
										+ pickedUserFromBox.getUsername() + " for " + discounted_price
										+ " TL successfully!");
								currentSeat.setOwnerName(pickedUserFromBox.getUsername());
								currentSeat.setBoughtPrice(discounted_price);
							} else {
								actext.setText("Seat at " + rowIndex + "-" + colIndex + " bought for "
										+ pickedUserFromBox.getUsername() + " for " + pickedHall.getSeatPrice()
										+ " TL successfully!");
								currentSeat.setOwnerName(pickedUserFromBox.getUsername());
								currentSeat.setBoughtPrice(pickedHall.getSeatPrice());
							}

							reservedButton.setOnMouseEntered(ee -> {
								text.setText("Bought by " + currentSeat.getOwnerName() + " for "
										+ currentSeat.getBoughtPrice() + " TL!");

								texts.getChildren().add(text);
							});

							clearText(reservedButton, text, texts);
						}

					});

				} else {
					if (!currentSeat.getOwnerName().equals(Login.loginedUser.getUsername())) {
						reservedButton.setDisable(true);
					}
					emptyButton.setOnAction(e -> {
						if (emptyButton.getGraphic().equals(emptyView)) {
							emptyButton.setGraphic(reservedView);
							if (Login.loginedUser.isClubMember()) {
								actext.setText("Seat at " + rowIndex + "-" + colIndex + " bought for "
										+ discounted_price + " TL successfully!");
								currentSeat.setOwnerName(Login.loginedUser.getUsername());
								currentSeat.setBoughtPrice(discounted_price);
							} else {
								actext.setText("Seat at " + rowIndex + "-" + colIndex + " bought for "
										+ pickedHall.getSeatPrice() + " TL successfully!");
								currentSeat.setOwnerName(Login.loginedUser.getUsername());
								currentSeat.setBoughtPrice(pickedHall.getSeatPrice());
							}

						} else {
							emptyButton.setGraphic(emptyView);
							actext.setText("Seat at " + rowIndex + "-" + colIndex + " refunded successfully!");
							currentSeat.setBoughtPrice(0);
							currentSeat.setOwnerName("null");

						}
					});

					reservedButton.setOnAction(e -> {
						if (reservedButton.getGraphic().equals(reservedView)) {
							reservedButton.setGraphic(emptyView);
							actext.setText("Seat at " + rowIndex + "-" + colIndex + " refunded successfully!");
							currentSeat.setBoughtPrice(0);
							currentSeat.setOwnerName("null");

						} else {
							reservedButton.setGraphic(reservedView);
							if (Login.loginedUser.isClubMember()) {
								actext.setText("Seat at " + rowIndex + "-" + colIndex + " bought for "
										+ discounted_price + " TL successfully!");
								currentSeat.setOwnerName(Login.loginedUser.getUsername());
								currentSeat.setBoughtPrice(discounted_price);
							} else {
								actext.setText("Seat at " + rowIndex + "-" + colIndex + " bought for "
										+ Login.loginedUser.getUsername() + " for " + pickedHall.getSeatPrice()
										+ " TL successfully!");
								currentSeat.setOwnerName(Login.loginedUser.getUsername());
								currentSeat.setBoughtPrice(pickedHall.getSeatPrice());
							}
						}
					});

				}

			}
		}
		actionBox.getChildren().add(actext);
		actionBox.setAlignment(Pos.CENTER);
		texts.setAlignment(Pos.CENTER);
		seats.setAlignment(Pos.CENTER);

		GridPane buttonPane = new GridPane();
		Button button = new Button("< BACK");
		buttonPane.add(button, 0, 0);
		buttonPane.setPadding(new Insets(0, 0, 20, 30));
		button.setOnAction(e -> {
			stage.setScene(trailerScene);
		});
		buttonPane.setAlignment(Pos.CENTER_LEFT);

		seatPane.add(seats, 0, 1);
		seatPane.add(texts, 0, 2);
		seatPane.add(actionBox, 0, 4);
		seatPane.add(buttonPane, 0, 5);
		seatPane.setAlignment(Pos.CENTER);
		seatPane.setPadding(new Insets(10, 50, 10, 50));

		Scene seatScene = new Scene(seatPane, Color.GHOSTWHITE);
		stage.setScene(seatScene);

	}

	public static void clearText(Button button, Text text, HBox texts) {
		button.setOnMouseExited(e -> {
			text.setText("");
			texts.getChildren().clear();
			texts.getChildren().add(new Text(""));
		});
	}

}
