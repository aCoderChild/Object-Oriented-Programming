public class Week4 {
    public static int max2Int(int a, int b) {
        // Tim gia tri lon nhat cua hai so nguyen
        return a > b ? a : b;
    }

    public static int minArray (int[] array) {
        // Tim gia tri nho nhat cua 1 mang so nguyen
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) min = array[i];
        }
        return min;
    }

    public static String calculateBMI(double weight, double height){
        // Tinh BMI
        double state = Math.round(weight / (height * height)) * 10.0 / 10.0;
        if (state < 18.5) return "Thiếu cân";
        if (state < 22.9) return "Bình thường";
        if (state <= 24.9) return "Thừa cân";
        return "Béo phì";
    }
}