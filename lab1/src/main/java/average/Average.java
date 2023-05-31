package average;

public class Average {
    /**
     * Returns the average of an array of numbers
     *
     * @param nums the array of integer numbers
     * @return the average of the numbers
     */
    public float computeAverage(int[] nums) {
        float result = 0;
        // Add your code
        for (int i = 0; i < nums.length; i++) {
            result += nums[i];
        }
        result = result / nums.length;
        return result;
    }

    public static void main(String[] args) {
        // Add your code
        // initialise an array of the numbers 1 - 6 (integers, inclusive)
        int[] nums = new int[6];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = i + 1;
        }
        // create an instance of the Average class
        Average average = new Average();
        // call the computeAverage method
        float result = average.computeAverage(nums);
        // print the result
        System.out.println("The average is " + result);
    }
}
