import java.text.DecimalFormat;

public class DiceGameTest {
    public static void main(String[] args) {
        double countTwo = 0;
        double countThree = 0;
        double countFour = 0;
        double countFive = 0;
        double countSix = 0;
        double countSeven = 0;
        double countEight = 0;
        double countNine = 0;
        double countTen = 0;
        double countEleven = 0;
        double countTwelve = 0;
        double countDuplicate = 0;

        DiceCup cup = new DiceCup();

        for (int i = 0; i < 1000; i++) {
            cup.roll();

            switch (cup.getSum()) {
                case 2:
                    countTwo++;
                    break;
                case 3:
                    countThree++;
                    break;
                case 4:
                    countFour++;
                    break;
                case 5:
                    countFive++;
                    break;
                case 6:
                    countSix++;
                    break;
                case 7:
                    countSeven++;
                    break;
                case 8:
                    countEight++;
                    break;
                case 9:
                    countNine++;
                    break;
                case 10:
                    countTen++;
                    break;
                case 11:
                    countEleven++;
                    break;
                case 12:
                    countTwelve++;
                    break;
            }
            countDuplicate = countDuplicate + cup.sameFaceValue();
        }
        DecimalFormat fmt1 = new DecimalFormat("#.##%");
        System.out.println("The sum was 2: " + fmt1.format(countTwo / 1000) + " of times");
        System.out.println("The sum was 3: " + fmt1.format(countThree / 1000) + " of times");
        System.out.println("The sum was 4: " + fmt1.format(countFour / 1000) + " of times");
        System.out.println("The sum was 5: " + fmt1.format(countFive / 1000) + " of times");
        System.out.println("The sum was 6: " + fmt1.format(countSix / 1000) + " of times");
        System.out.println("The sum was 7: " + fmt1.format(countSeven / 1000) + " of times");
        System.out.println("The sum was 8: " + fmt1.format(countEight / 1000) + " of times");
        System.out.println("The sum was 9: " + fmt1.format(countNine / 1000) + " of times");
        System.out.println("The sum was 10: " + fmt1.format(countTen / 1000) + " of times");
        System.out.println("The sum was 11: " + fmt1.format(countEleven / 1000) + " of times");
        System.out.println("The sum was 12: " + fmt1.format(countTwelve / 1000) + " of times");
        System.out.println("The two dice show the same facevalue: " + fmt1.format(countDuplicate / 1000) + " of times");
    }
}
