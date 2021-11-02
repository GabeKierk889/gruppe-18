public class Rectangle extends Shape {
    private float sideOne, sideTwo;

    public Rectangle() {
        sideTwo = 0;
        sideTwo = 0;
    }

    public void setSides(float sideOne, float sideTwo) {
        this.sideOne = sideOne;
        this.sideTwo = sideTwo;
    }

    public float getArea() {
        return sideOne * sideTwo;
    }
}
