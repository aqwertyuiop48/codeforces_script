fun main() {
    val (n, m, a) = readln().split(" ")
        .map {it.toLong()}
    println(((n-1)/a + 1) * ((m-1)/a + 1))
}