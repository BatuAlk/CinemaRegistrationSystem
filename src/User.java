
import java.util.HashMap;

public class User {
	private String username;
	private String password;
	private boolean isAdmin;
	private boolean isClubMember;

	public User(String uname, String pword, boolean isCM, boolean isAdmin) {
		setUsername(uname);
		setPassword(pword);
		setClubMember(isCM);
		setAdmin(isAdmin);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isClubMember() {
		return isClubMember;
	}

	public void setClubMember(boolean isClubMember) {
		this.isClubMember = isClubMember;
	}

	public static HashMap<String, String> getUserInfos() {
		// stıre credentials accordingly
		HashMap<String, String> infos = new HashMap<>();
		for (User user : Read.usersList) {
			infos.put(user.getUsername(), user.getPassword());
		}
		return infos;
	}

	public static User getUser(String username) {
		// get item from user list with a string
		return Read.usersList.stream().filter(p -> p.getUsername().equals(username)).findAny().orElse(null);
	}

	@Override
	public String toString() {
		return getUsername() + "\t" + getPassword() + "\t" + isClubMember() + "\t" + isAdmin();
	}

}
