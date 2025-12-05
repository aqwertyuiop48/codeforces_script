package com.example;

import java.util.*;
import java.util.stream.*;

public class Testing {
    public static void main(String[] args) {

        Question35.main();
        // Practice
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5));
        list.add(2);
        System.out.println(Arrays.toString(list.toArray()));

        int[][] arr = new int[][]{{1,3},{3,4},{6,7}};
        System.out.println(Arrays.deepToString(arr));
        List<List<Integer>> list1 = (Arrays.stream(arr).map(i->Arrays.stream(i).boxed().collect(Collectors.toList())).collect(Collectors.toList()));

        System.out.println(Arrays.toString(list1.toArray()));

        int[][] matrix = {{1,1,1},{1,0,1},{1,1,1}};
        int[][] rows = matrix, columns = matrix;
        for(int i=0;i<matrix.length;i++){
            if(Arrays.asList(matrix[i]).contains(0))
                rows[i] = new ArrayList<>(Collections.nCopies(matrix.length,0)).stream().mapToInt(Integer::intValue).toArray();
            else rows[i] = matrix[i];
        }
        System.out.println(Arrays.deepToString(rows));

        ArrayList<Integer> list2 = new ArrayList<>();
        list2.addAll(new ArrayList<>(Arrays.asList(7,8,6,5,34)));
        System.out.println(list2);

        System.out.println(add(2,3));
    }

    public static int add(int a, int b){
        return a+b;
    }

    public class Question35 {

        public static void main() {
            int a[] = {10, 20, 30};
            int i = 5, j = 0;

            try {

                System.out.println(a[3]);
                System.out.println(i / j);

            } catch (ArithmeticException e) {
                System.out.println("Line 1");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Line 2");
            } catch (Exception e) {
                System.out.println("Line 3");
            }
            // unreachable code (runtime exception)
//            catch (NumberFormatException e) {
//                System.out.println("Line 4");
//            }
        }
    }

}