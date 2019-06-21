package org.peakemu.common;

import org.peakemu.world.GameMap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.peakemu.Peak;

import org.peakemu.world.MapCell;

public class CryptManager {

    public static String CryptPassword(String Key, String Password) {
        char[] HASH = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};

        String _Crypted = "#1";

        for (int i = 0; i < Password.length(); i++) {
            char PPass = Password.charAt(i);
            char PKey = Key.charAt(i);

            int APass = (int) PPass / 16;

            int AKey = (int) PPass % 16;

            int ANB = (APass + (int) PKey) % HASH.length;
            int ANB2 = (AKey + (int) PKey) % HASH.length;

            _Crypted += HASH[ANB];
            _Crypted += HASH[ANB2];
        }
        return _Crypted;
    }

    public static String decryptPass(String pass, String key) {
        if (pass.startsWith("#1")) {
            pass = pass.substring(2);
        }
        String Chaine = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";

        char PPass, PKey;
        int APass, AKey, ANB, ANB2, somme1, somme2;

        String decrypted = "";

        for (int i = 0; i < pass.length(); i += 2) {
            PKey = key.charAt(i / 2);
            ANB = Chaine.indexOf(pass.charAt(i));
            ANB2 = Chaine.indexOf(pass.charAt(i + 1));

            somme1 = ANB + Chaine.length();
            somme2 = ANB2 + Chaine.length();

            APass = somme1 - (int) PKey;
            if (APass < 0) {
                APass += 64;
            }
            APass *= 16;

            AKey = somme2 - (int) PKey;
            if (AKey < 0) {
                AKey += 64;
            }

            PPass = (char) (APass + AKey);

            decrypted += PPass;
        }

        return decrypted;
    }

    public static String CryptIP(String IP) {
        String[] Splitted = IP.split("\\.");
        String Encrypted = "";
        int Count = 0;
        for (int i = 0; i < 50; i++) {
            for (int o = 0; o < 50; o++) {
                if (((i & 15) << 4 | o & 15) == Integer.parseInt(Splitted[Count])) {
                    Character A = (char) (i + 48);
                    Character B = (char) (o + 48);
                    Encrypted += A.toString() + B.toString();
                    i = 0;
                    o = 0;
                    Count++;
                    if (Count == 4) {
                        return Encrypted;
                    }
                }
            }
        }
        return "DD";
    }

    public static String CryptPort(int config_game_port) {
        char[] HASH = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        int P = config_game_port;
        String nbr64 = "";
        for (int a = 2; a >= 0; a--) {
            nbr64 += HASH[(int) (P / (java.lang.Math.pow(64, a)))];
            P = (int) (P % (int) (java.lang.Math.pow(64, a)));
        }
        return nbr64;
    }

    public static String cellID_To_Code(int cellID) {
        char[] HASH = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};

        int char1 = cellID / 64, char2 = cellID % 64;
        return HASH[char1] + "" + HASH[char2];
    }

    public static int cellCode_To_ID(String cellCode) {
        char[] HASH = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        char char1 = cellCode.charAt(0), char2 = cellCode.charAt(1);
        int code1 = 0, code2 = 0, a = 0;
        while (a < HASH.length) {
            if (HASH[a] == char1) {
                code1 = a * 64;
            }
            if (HASH[a] == char2) {
                code2 = a;
            }
            a++;
        }
        return (code1 + code2);
    }

    public static int getIntByHashedValue(char c) {
        if (c >= 'a' && c <= 'z') {
            return (byte) (c - 'a');
        }
        if (c >= 'A' && c <= 'Z') {
            return (byte) (c - 'A' + 26);
        }
        if (c >= '0' && c <= '9') {
            return (byte) (c - '0' + 52);
        }
        if (c == '-') {
            return 62;
        }
        if (c == '_') {
            return 63;
        }

        return -1;
    }

    public static char getHashedValueByInt(int c) {
        char[] HASH = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        return HASH[c];
    }

    public static ArrayList<MapCell> parseStartCell(GameMap map, int num) {
        ArrayList<MapCell> list = null;
        String infos = null;
        if (!map.get_placesStr().equalsIgnoreCase("-1")) {
            infos = map.get_placesStr().split("\\|")[num];
            int a = 0;
            list = new ArrayList<>();
            while (a < infos.length()) {
                list.add(map.getCell(map.getGoodCellid((getIntByHashedValue(infos.charAt(a)) << 6) + getIntByHashedValue(infos.charAt(a + 1)))));
                a = a + 2;
            }
        }
        return list;
    }

    //Fonction qui convertis tout les textes ANSI(Unicode) en UTF-8. Les fichiers doivent �tre cod� en ANSI sinon les phrases seront illisible.
    public static String toUtf(String _in) {
        String _out = "";

        try {
            _out = new String(_in.getBytes("UTF8"));

        } catch (Exception e) {
            System.out.println("Conversion en UTF-8 echoue! : " + e.getMessage());
        }

        return _out;
    }

    //Utilis� pour convertir les inputs UTF-8 en String normal.

    public static String toUnicode(String _in) {
        String _out = "";

        try {
            _out = new String(_in.getBytes(), "UTF8");

        } catch (Exception e) {
            System.out.println("Conversion en UTF-8 echoue! : " + e.getMessage());
        }

        return _out;
    }

    public static String CryptSHA512(String message) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");

            md.update(message.getBytes());
            byte[] mb = md.digest();
            String out = "";
            for (int i = 0; i < mb.length; i++) {
                byte temp = mb[i];
                String s = Integer.toHexString(new Byte(temp));
                while (s.length() < 2) {
                    s = "0" + s;
                }
                s = s.substring(s.length() - 2);
                out += s;
            }
            return out;

        } catch (NoSuchAlgorithmException e) {
            Peak.errorLog.addToLog(e);
            System.exit(1);
        }
        return null;
    }

}
