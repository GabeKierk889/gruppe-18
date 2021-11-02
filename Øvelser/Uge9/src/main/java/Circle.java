public class Circle extends Shape {
    private float radius;

    public Circle() {
        radius = 0;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getArea() {
        return radius * radius * PI;
    }
}
