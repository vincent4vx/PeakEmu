///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.peakemu.common.util;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Random;
//
///**
// *
// * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
// */
//public class StringUtil {
//
//    static public String generateRandString(int length) {
//        String alphabet = "abcdefghijklmnopqrstuvwxyz";
//        
//        StringBuilder hashkey = new StringBuilder(length);
//
//        Random rand = new Random();
//
//        for (int i = 0; i < length; i++) {
//            hashkey.append(alphabet.charAt(rand.nextInt(alphabet.length())));
//        }
//        
//        return hashkey.toString();
//    }
//    
//    /**
//     * Sépare une chaine en sous chaines, délimité par une chaine de caractère A
//     * utiliser à la place de String.split (quand il n'y a pas besoin de regexp,
//     * soit 99% du temps), car ~50% plus rapide
//     *
//     * @param str Chaine à découper
//     * @param delimiter séparateur
//     * @param limit Nombre de sous chaine maximum (taille maximal du tableau de
//     * fin)
//     * @return la chaine explosé
//     */
//    public static String[] split(String str, String delimiter, int limit) {
//        ArrayList<String> splited = new ArrayList<String>();
//
//        int last = 0, pos, step = 0;
//
//        if (limit < 1) {
//            limit = Integer.MAX_VALUE; //devrait suffire amplement x)
//        }
//
//        while ((pos = str.indexOf(delimiter, last)) != -1 && ++step < limit) {
//            splited.add(str.substring(last, pos));
//            last = pos + 1;
//        }
//
//        splited.add(str.substring(last));
//
//        String[] ret = new String[splited.size()];
//        return splited.toArray(ret);
//    }
//
//    /**
//     * Sépare une chaine en sous chaines, délimité par une chaine de caractère A
//     * utiliser à la place de String.split (quand il n'y a pas besoin de regexp,
//     * soit 99% du temps), car ~50% plus rapide
//     *
//     * @param str Chaine à découper
//     * @param delimiter séparateur
//     * @return la chaine explosé
//     */
//    public static String[] split(String str, String delimiter) {
//        return split(str, delimiter, 0);
//    }
//
//    /**
//     * Concatène les élément d'un tableau, en les séparant par un séparateur
//     *
//     * @param pieces Pièces à joindre
//     * @param separator Séparateur
//     * @return élément concatété
//     */
//    public static String join(Object[] pieces, String separator) {
//        StringBuilder str = new StringBuilder(pieces.length * 16); //allocation de mémoire "assez" large pour éviter un resize
//
//        for (int i = 0; i < pieces.length; ++i) {
//            if (i > 0) {
//                str.append(separator);
//            }
//
//            str.append(pieces[i]);
//        }
//
//        return str.toString();
//    }
//    
//    static public String join(Collection pieces, String separator){
//        return join(pieces.toArray(), separator);
//    }
//    
//    private static int minimum(int a, int b, int c) {                            
//        return Math.min(Math.min(a, b), c);                                      
//    }                                                                            
//                                                                                 
//    public static int levenshteinDistance(String str1,String str2) {      
//        int[][] distance = new int[str1.length() + 1][str2.length() + 1];        
//                                                                                 
//        for (int i = 0; i <= str1.length(); i++)                                 
//            distance[i][0] = i;                                                  
//        for (int j = 1; j <= str2.length(); j++)                                 
//            distance[0][j] = j;                                                  
//                                                                                 
//        for (int i = 1; i <= str1.length(); i++)                                 
//            for (int j = 1; j <= str2.length(); j++)                             
//                distance[i][j] = minimum(                                        
//                        distance[i - 1][j] + 1,                                  
//                        distance[i][j - 1] + 1,                                  
//                        distance[i - 1][j - 1] + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));
//                                                                                 
//        return distance[str1.length()][str2.length()];                           
//    }   
//}
