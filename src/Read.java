
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;

public class Read {
	static File fileBackup;
	static int maxErrorCount;
	static double discount;
	static String title;
	static int blockTime;
	static ArrayList<User> usersList = new ArrayList<>();
	static ArrayList<Film> filmsList = new ArrayList<>();
	static ArrayList<Hall> hallsList = new ArrayList<>();
	static ArrayList<Seat> seatsList = new ArrayList<>();

	public static void readProperties() {
		try {
			File fileProp = new File("assets/data/properties.dat");
			Scanner scan = new Scanner(new FileReader(fileProp));
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				if (line.startsWith("maximum-error-without-getting-blocked")) {
					maxErrorCount = Integer.parseInt(line.split("=")[1]);
				} else if (line.startsWith("title")) {
					title = line.split("=")[1];
				} else if (line.startsWith("discount-percentage")) {
					discount = (100 - Double.parseDouble(line.split("=")[1])) / 100;
				} else if (line.startsWith("block-time")) {
					blockTime = Integer.parseInt(line.split("=")[1]);
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void readBackup() {
		try {
			fileBackup = new File("assets/data/backup.dat");
			Scanner scan = new Scanner(new FileReader(fileBackup));
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				if (line.startsWith("user")) {
					String uname = line.split("\t")[1];
					String pword = line.split("\t")[2];
					boolean isCM = Boolean.parseBoolean(line.split("\t")[3]);
					boolean isAdmin = Boolean.parseBoolean(line.split("\t")[4]);
					usersList.add(new User(uname, pword, isCM, isAdmin));

				} else if (line.startsWith("film")) {
					String title = line.split("\t")[1];
					String tName = line.split("\t")[2];
					int dur = Integer.parseInt(line.split("\t")[3]);
					filmsList.add(new Film(title, tName, dur));
					Film.filmNamesList.add(title);
					Film.trailerPathsList.add(tName);

				} else if (line.startsWith("hall")) {
					String fN = line.split("\t")[1];
					String hN = line.split("\t")[2];
					int price = Integer.parseInt(line.split("\t")[3]);
					int r = Integer.parseInt(line.split("\t")[4]);
					int c = Integer.parseInt(line.split("\t")[5]);
					hallsList.add(new Hall(fN, hN, price, r, c));
					Hall.hallNamesList.add(hN);

				} else if (line.startsWith("seat")) {
					String fN = line.split("\t")[1];
					String hN = line.split("\t")[2];
					int r = Integer.parseInt(line.split("\t")[3]);
					int c = Integer.parseInt(line.split("\t")[4]);
					String owner = line.split("\t")[5];
					int bPrice = Integer.parseInt(line.split("\t")[6]);
					seatsList.add(new Seat(fN, hN, r, c, owner, bPrice));
				}
			}

		} catch (FileNotFoundException e) {
			usersList.add(new User("admin", Read.hashPassword("password"), true, true));
		}
	}

	public static void effectHandle(Button button) {
		Effect shadow = new DropShadow();
		button.setOnMouseEntered(e -> button.setEffect(shadow));
		button.setOnMouseExited(e -> button.setEffect(null));
	}

	public static String hashPassword(String password) {
		byte[] bytesOfPassword = password.getBytes(StandardCharsets.UTF_8);
		byte[] md5Digest = new byte[0];
		try {
			md5Digest = MessageDigest.getInstance("MD5").digest(bytesOfPassword);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return Base64.getEncoder().encodeToString(md5Digest);
	}

}
