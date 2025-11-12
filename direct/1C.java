import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var poles = new Pos[3];
        for (int i = 0; i < 3; i++)
            poles[i] = Pos.fromScanner(scanner);

        var circle = circleFromPoints(poles[0], poles[1], poles[2]);

        double area;
        outer: for (var sides = 3;; sides++) {
            var pairs = new Pos[][] { { poles[0], poles[1] }, { poles[1], poles[2] }, { poles[2], poles[0] } };
            for (var pair : pairs) {
                var angleA = Math.atan2(pair[0].y - circle.center.y, pair[0].x - circle.center.x);
                var angleB = Math.atan2(pair[1].y - circle.center.y, pair[1].x - circle.center.x);
                var angle = Math.abs(angleA - angleB);

                if (Math.abs(Math.sin(sides * angle / 2)) > 1e-3)
                    continue outer;
            }

            area = (square(circle.radius) * sides * Math.sin((2 * Math.PI) / sides)) / 2;
            break;
        }

        System.out.printf("%.6f\n", area);
    }

    static double square(double x) {
        return x * x;
    }

    static Circle circleFromPoints(Pos a, Pos b, Pos c) {
        var calcA = a.x * (b.y - c.y) - a.y * (b.x - c.x) + b.x * c.y - c.x * b.y;
        var calcB = (Math.pow(a.x, 2) + Math.pow(a.y, 2)) * (c.y - b.y) +
                (Math.pow(b.x, 2) + Math.pow(b.y, 2)) * (a.y - c.y) +
                (Math.pow(c.x, 2) + Math.pow(c.y, 2)) * (b.y - a.y);
        var calcC = (Math.pow(a.x, 2) + Math.pow(a.y, 2)) * (b.x - c.x) +
                (Math.pow(b.x, 2) + Math.pow(b.y, 2)) * (c.x - a.x) +
                (Math.pow(c.x, 2) + Math.pow(c.y, 2)) * (a.x - b.x);
        var calcD = (Math.pow(a.x, 2) + Math.pow(a.y, 2)) * (c.x * b.y - b.x * c.y) +
                (Math.pow(b.x, 2) + Math.pow(b.y, 2)) * (a.x * c.y - c.x * a.y) +
                (Math.pow(c.x, 2) + Math.pow(c.y, 2)) * (b.x * a.y - a.x * b.y);

        var x = -calcB / (2 * calcA);
        var y = -calcC / (2 * calcA);
        var radius = Math.sqrt(Math.pow(calcB, 2) + Math.pow(calcC, 2) - 4 * calcA * calcD) / (2 * Math.abs(calcA));

        return new Circle(new Pos(x, y), radius);
    }

    static class Circle {
        Pos center;
        double radius;

        Circle(Pos center, double radius) {
            this.center = center;
            this.radius = radius;
        }
    }

    static class Pos {
        double x;
        double y;

        Pos(double x, double y) {
            this.x = x;
            this.y = y;
        }

        static Pos fromScanner(Scanner scanner) {
            return new Pos(scanner.nextDouble(), scanner.nextDouble());
        }

        double dist(Pos other) {
            return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
        }
    }
}