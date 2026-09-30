package br.com.tupidigital;
public class TestRegexQuotes {
    public static void main(String[] args) {
        System.out.println("\"sandro@gmail.com\"".matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"));
    }
}
