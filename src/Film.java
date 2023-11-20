
import java.util.ArrayList;

public class Film {
	private String title;
	private String trailerName;
	private int duration;
	static ArrayList<String> filmNamesList = new ArrayList<>();
	static ArrayList<String> trailerPathsList = new ArrayList<>();

	public Film(String title, String tName, int dur) {
		setTitle(title);
		setDuration(dur);
		setTrailerName(tName);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTrailerName() {
		return trailerName;
	}

	public void setTrailerName(String trailerName) {
		this.trailerName = trailerName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public static Film getFilm(String filmName) {
		// get item from film list with a string
		return Read.filmsList.stream().filter(p -> p.getTitle().equals(filmName)).findAny().orElse(null);
	}

	@Override
	public String toString() {
		return getTitle() + "\t" + getTrailerName() + "\t" + getDuration();
	}

}
