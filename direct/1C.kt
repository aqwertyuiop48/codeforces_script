import java.util.Scanner
import kotlin.math.*

class Vector2D(val x: Double, val y: Double) {
    val magnitude: Double
        get() {
            return sqrt(x*x+y*y)
        }

    val angle: Double
        get() {
            return atan2(y, x)
        }

    fun subtract(other: Vector2D): Vector2D {
        return Vector2D(
                x - other.x,
                y - other.y
        )
    }

    fun angleTo(other: Vector2D): Double {
        return other.angle - angle
    }

    override fun toString(): String {
        return "<${x}, ${y}>"
    }
}

val EPS = 0.0001

fun main() {
    val scan = Scanner(System.`in`)

    val vertices = listOf(
        Vector2D(scan.nextDouble(), scan.nextDouble()),
        Vector2D(scan.nextDouble(), scan.nextDouble()),
        Vector2D(scan.nextDouble(), scan.nextDouble())
    )

    for (n in 3..100) {
        // Check to see if the given vertices could represent vertices of an
        // n-gon
        val a = vertices[1].x*vertices[1].x - vertices[0].x*vertices[0].x + vertices[1].y*vertices[1].y - vertices[0].y*vertices[0].y
        val b = vertices[2].x*vertices[2].x - vertices[0].x*vertices[0].x + vertices[2].y*vertices[2].y - vertices[0].y*vertices[0].y
        val c = 2*vertices[1].x - 2*vertices[0].x
        val d = 2*vertices[2].x - 2*vertices[0].x
        val e = 2*vertices[0].y - 2*vertices[1].y
        val f = 2*vertices[0].y - 2*vertices[2].y

        val center = Vector2D(
                (a+e*((a*d-b*c)/(f*c-e*d)))/c,
                (a*d-b*c)/(f*c-e*d)
        )
        //println("Trying n ${n} with center ${center} and radius ${vertices[0].subtract(center).magnitude}")
        var pass = true
        for (i in 0..<vertices.size) {
            // The angle between all pairs should be a multiple of 360deg / n (or 2*pi/n)
            val angb = vertices[i].subtract(center).angleTo(vertices[if (i < vertices.size-1) i+1 else 0].subtract(center))
            //println(angb)
            if (abs(angb) < (2*Math.PI/n - 1e-7) || abs(angb) % (2*Math.PI/n - 1e-7) > EPS) {
                pass = false
                break
            }
        }
        if (pass) {
            val o = vertices[0].subtract(center).magnitude * sin(Math.PI / n)
            val a = vertices[0].subtract(center).magnitude * cos(Math.PI / n)
            val segmentArea = o*a
            val totalArea = segmentArea*n
            println(totalArea)
            return
        }
    }
    println("No ans for ${vertices}!")
}