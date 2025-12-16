package com.example
import java.util.concurrent.Executors

class testing_kotlin {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println(2)
            println(sortArray(intArrayOf(3, 1, 2)).contentToString())
        }

        @JvmStatic
        fun sortArray(nums: IntArray): IntArray {
            Executors.newVirtualThreadPerTaskExecutor().use { executor ->
                val future = executor.submit<IntArray> {
                    java.util.Arrays.sort(nums);
                    nums;
                };
                return future.get();
            };
        }
    }

}