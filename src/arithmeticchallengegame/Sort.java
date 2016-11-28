package arithmeticchallengegame;

import java.util.ArrayList;

/**
 * References: http://www.java2novice.com/java-sorting-algorithms,
 * MathBits_Java_BubbleSort.pdf, MathBits_Java_InsertionSort.pdf,
 * MathBits_Java_SelectionSort.pdf,
 * http://alvinalexander.com/java/edu/pj/pj010018 (Ternary operator examples)
 *
 * @author Daniel Field
 */
public class Sort {

    // Sorts ArrayList of Equations by operator or answer, in ascending or descending order
    // Returns the sorted ArrayList
    public static ArrayList<Equation> BubbleSort(ArrayList<Equation> arr, boolean sortByOperator, boolean desc) {
        for (int j = 0; j < arr.size(); j++) {
            for (int i = j + 1; i < arr.size(); i++) {
                if (comparator(arr.get(j), arr.get(i), sortByOperator, desc)) {

                    Equation temp = arr.get(j);
                    arr.set(j, arr.get(i));
                    arr.set(i, temp);
                }
            }
        }
        return arr;
    }

    // Sorts ArrayList of Equations by operator or answer, in ascending or descending order
    // Returns the sorted ArrayList
    public static ArrayList<Equation> InsertionSort(ArrayList<Equation> arr, boolean sortByOperator, boolean desc) {
        Equation key;
        int i;
        for (int j = 1; j < arr.size(); j++) {
            key = arr.get(j);
            for (i = j - 1; (i >= 0) && comparator(arr.get(i), key, sortByOperator, desc); i--) {
                arr.set(i + 1, arr.get(i));
            }
            arr.set(i + 1, key);
        }
        return arr;
    }

    // Sorts ArrayList of Equations by operator or answer, in ascending or descending order
    // Returns the sorted ArrayList
    public static ArrayList<Equation> SelectionSort(ArrayList<Equation> arr, boolean sortByOperator, boolean desc) {
        for (int i = 0; i < arr.size() - 1; i++) {
            int index = i;
            for (int j = i + 1; j < arr.size(); j++) {
                if (comparator(arr.get(j), arr.get(index), sortByOperator, desc)) {
                    index = j;
                }
            }

            Equation temp = arr.get(i);
            arr.set(i, arr.get(index));
            arr.set(index, temp);
        }
        return arr;
    }

    // If sortByOperator is false
    //     if desc is true
    //         return true if eq's answer is greater than eqTwo's answer
    //     if desc is false
    //         return true if eq's answer is less than eqTwo's answer
    // If sortByOperator is true
    //     if desc is true
    //         return true if the return value of comparing eq's operator with eqTwo's operator is less than zero
    //     if desc is false
    //         return true if the return value of comparing eq's operator with eqTwo's operator is greater than zero
    private static boolean comparator(Equation eq, Equation eqTwo, boolean sortByOperator, boolean greaterThan) {
        return sortByOperator == false
                ? greaterThan == true ? eq.getAnswer() > eqTwo.getAnswer()
                    : eq.getAnswer() < eqTwo.getAnswer()
                : greaterThan == true ? ((Character) eq.getOperator()).compareTo((Character) eqTwo.getOperator()) < 0
                    : ((Character) eq.getOperator()).compareTo((Character) eqTwo.getOperator()) > 0;
    }
}
