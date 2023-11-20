
import java.util.ArrayList;

public class Hall {
	private String filmName;
	private String hallName;
	private int seatPrice;
	private int row_count;
	private int col_count;
	static ArrayList<String> hallNamesList = new ArrayList<>();

	public Hall(String fN, String hN, int price, int r, int c) {
		setFilmName(fN);
		setHallName(hN);
		setSeatPrice(price);
		setRow_count(r);
		setCol_count(c);
	}

	public String getFilmName() {
		return filmName;
	}

	public void setFilmName(String filmName) {
		this.filmName = filmName;
	}

	public String getHallName() {
		return hallName;
	}

	public void setHallName(String hallName) {
		this.hallName = hallName;
	}

	public int getSeatPrice() {
		return seatPrice;
	}

	public void setSeatPrice(int seatPrice) {
		this.seatPrice = seatPrice;
	}

	public int getRow_count() {
		return row_count;
	}

	public void setRow_count(int row_count) {
		this.row_count = row_count;
	}

	public int getCol_count() {
		return col_count;
	}

	public void setCol_count(int col_count) {
		this.col_count = col_count;
	}

	public static Hall getHall(String hallName) {
		// get item from hall list with a string
		return Read.hallsList.stream().filter(p -> p.getHallName().equals(hallName)).findAny().orElse(null);
	}

	@Override
	public String toString() {
		return getFilmName() + "\t" + getHallName() + "\t" + getSeatPrice() + "\t" + getRow_count() + "\t"
				+ getCol_count();
	}
}
