package student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cipher {
	private static String[] encryptionPoints= {"a", "b", "c", "d", "e", "f", "k", "m", "n", "s", "t", "u", "v", "w", "!", "@", "#", "$", "%", "^"}; //which letters to throw in an encryption filler
	private static String[] fillers = {"#FF00H9", "apass", "foD", "2edalo", "14903", "lokik", "DIL,", ".IL", "DLL.OP"}; //random character sequences to throw in after certain letters
	private static Random gen = new Random();
	
	public static String encrypt(String str){
		String encryptedString = ""; //string to be returned
		
		//actually encrypt string by throwing in fillers every time a certain predefined character appears
		for (int i = 0; i < str.length(); i++){
			String letter = str.substring(i, i+1);
			encryptedString += letter;
			
			int index = gen.nextInt(fillers.length);
			encryptedString += fillers[index];
		}
		
		return encryptedString;
	}
	
	public static String decrypt(String str){
		for (String filler : fillers){
			String[] segments = str.split(filler); //take out all the fillers by splitting the string at every filler
			str = "";
			//stitch string back together from split parts
			for (String s : segments){
				str += s;
			}
		}
		
		return str;
	}
	
	//for testing purposes
	public static void main(String[] args){
		String message = "#&*Astronomy41529";
		String encrypted = Cipher.encrypt(message);
		System.out.println(encrypted);
		System.out.println(Cipher.decrypt(encrypted));
	}
}
