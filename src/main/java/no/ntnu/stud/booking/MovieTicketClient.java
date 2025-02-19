package no.ntnu.stud.booking;

public class MovieTicketClient extends Thread {

    private MovieTicketServer server;
    private String name;
    private int someNumber;

    public MovieTicketClient(MovieTicketServer server, String name, int someNumber) {
        this.server = server;
        this.name = name;
        this.someNumber = someNumber;
    }
}
