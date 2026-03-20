package Coding;

interface Drawable {
    void draw();
}

abstract class Shape {
    protected String color;

    public Shape(String color) {
        this.color = color;
    }

    public abstract double getArea();

    public abstract double getPerimeter();

    public String getColor() {
        return color;
    }
}

class Rectangle extends Shape implements Drawable {
    private double width;
    private double height;

    public Rectangle(String color, double witdth, double height) {
        super(color);
        this.width = witdth;
        this.height = height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }

    @Override
    public void draw() {
        System.out.println("Drawing a " + getColor() + " rectangle.");
    }

}

class Circle extends Shape implements Drawable {
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a " + getColor() + " circle.");
    }
}

public class ShapeDemo {

    public static void main(String[] args) {
        Shape[] shapes = {
            new Rectangle("Red", 4.0, 6.0),
            new Circle("Blue", 3.0)
        };

        for (Shape shape : shapes) {
            System.out.println("Color: " + shape.getColor());
            System.out.println("Area: " + shape.getArea());
            System.out.println("Perimeter: " + shape.getPerimeter());

            if (shape instanceof Drawable) {
                ((Drawable) shape).draw();
            }

            System.out.println();
        }
    }
}
