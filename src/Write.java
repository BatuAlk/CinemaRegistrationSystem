import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Write {
	public static void writeToBackup() {
		try {
			String curDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
			FileWriter writer = new FileWriter(Read.fileBackup);
			writer.write("Change made at: " + curDate + "\n");

			for (User user : Read.usersList) {
				writer.append("user\t" + user.toString() + "\n");
			}

			for (Film film : Read.filmsList) {
				writer.append("film\t" + film.toString() + "\n");
				for (Hall hall : Read.hallsList) {
					if (hall.getFilmName().equals(film.getTitle())) {
						writer.append("hall\t" + hall.toString() + "\n");
						for (Seat seat : Read.seatsList) {
							if (seat.getHallName().equals(hall.getHallName())) {
								writer.append("seat\t" + seat.toString() + "\n");
							}
						}
					}
				}
			}

			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
