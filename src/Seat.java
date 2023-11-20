public class Seat {
	private String filmName;
	private String hallName;
	private int row_index;
	private int col_index;
	private String ownerName;
	private int boughtPrice;

	public Seat(String fN, String hN, int r, int c, String owner, int bPrice) {
		setFilmName(fN);
		setHallName(hN);
		setBoughtPrice(bPrice);
		setRow_index(r);
		setCol_index(c);
		setOwnerName(owner);
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

	public int getRow_index() {
		return row_index;
	}

	public void setRow_index(int row_index) {
		this.row_index = row_index;
	}

	public int getCol_index() {
		return col_index;
	}

	public void setCol_index(int col_index) {
		this.col_index = col_index;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getBoughtPrice() {
		return boughtPrice;
	}

	public void setBoughtPrice(int boughtPrice) {
		this.boughtPrice = boughtPrice;
	}

	public static Seat getSeat(Hall hall, int row, int col) {
		// get item from seat list with proper values
		return Read.seatsList.stream().filter(
				p -> p.getHallName().equals(hall.getHallName()) && p.getRow_index() == row && p.getCol_index() == col)
				.findAny().orElse(null);
	}

	@Override
	public String toString() {
		return getFilmName() + "\t" + getHallName() + "\t" + getRow_index() + "\t" + getCol_index() + "\t"
				+ getOwnerName() + "\t" + getBoughtPrice();
	}

}
