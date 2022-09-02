package util;

import java.util.Random;

public class Utils {


    public static String getRandomString(int NoOfChars) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int N = alphabet.length();
        Random RandomObj = new Random();
        char[] nameCharArr = new char[NoOfChars];

        for (int i = 0; i < NoOfChars; i++) {
            nameCharArr[i] = alphabet.charAt(RandomObj.nextInt(N));
        }

        String nameString = String.copyValueOf(nameCharArr);
        nameString = "AT" + nameString.toLowerCase();
        return nameString;
    }
}
