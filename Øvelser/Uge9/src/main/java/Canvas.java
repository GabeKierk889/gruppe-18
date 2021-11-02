public class Canvas {
    private Circle circle;
    private Square square;

    public Canvas() {
        circle = new Circle();
        square = new Square();
    }

    public float calculateTotal() {
        return circle.getArea() + square.getArea();
    }

    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        canvas.circle.getArea();
        canvas.square.getArea();

        canvas.calculateTotal();
    }
}