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
        System.out.println("The sum was 2: " + (countTwo / 1000) * 100 + " percentage of times");
        System.out.println("The sum was 3: " + (countThree / 1000) * 100 + " percentage of times");
        System.out.println("The sum was 4: " + (countFour / 1000) * 100 + " percentage of times");
        System.out.println("The sum was 5: " + (countFive / 1000) * 100 + " percentage of times");
        System.out.println("The sum was 6: " + (countSix / 1000) * 100 + " percentage of times");
        System.out.println("The sum was 7: " + (countSeven / 1000) * 100 + " percentage of times");
        System.out.println("The sum was 8: " + (countEight / 1000) * 100 + " percentage of times");
        System.out.println("The sum was 9: " + (countNine / 1000) * 100 + " percentage of times");
        System.out.println("The sum was 10: " + (countTen / 1000) * 100 + " percentage of times");
        System.out.println("The sum was 11: " + (countEleven / 1000) * 100 + " percentage of times");
        System.out.println("The sum was 12: " + (countTwelve / 1000) * 100 + " percentage of times");
        System.out.println("The two dice show the same facevalue: " + (countDuplicate / 1000) * 100 + " percentage of times");
    }
}
