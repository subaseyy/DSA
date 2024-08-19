package main.task2;


// Question 2, b

public class movieTheater {
    public static boolean canSitTogether(int[] nums, int indexDiff, int valueDiff) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j <= i + indexDiff && j < nums.length; j++) {
                if (Math.abs(nums[i] - nums[j]) <= valueDiff) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[] nums1 = {2, 3, 5, 4, 9};
        int indexDiff1 = 2;
        int valueDiff1 = 1;
        System.out.println(canSitTogether(nums1, indexDiff1, valueDiff1));

        int[] nums2 = {1, 5, 9, 13};
        int indexDiff2 = 2;
        int valueDiff2 = 3;
        System.out.println(canSitTogether(nums2, indexDiff2, valueDiff2));

        int[] nums3 = {4, 6, 8, 10};
        int indexDiff3 = 1;
        int valueDiff3 = 2;
        System.out.println(canSitTogether(nums3, indexDiff3, valueDiff3));
    }
    
}
