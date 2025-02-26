package no.ntnu.stud.booking;

public class MovieTicketClient extends Thread {

    private MovieTicketServer server;
    private String name;
    private int orderedTickets;

    public MovieTicketClient(MovieTicketServer server, String name, int orderedTickets) {
        this.server = server;
        this.name = name;
        this.orderedTickets = orderedTickets;
    }

    @Override
    public void run() {
        int before = server.getAvailableTickets();
        server.bookTickets(orderedTickets);
        int after = server.getAvailableTickets();
        System.out.println(name + ": before = " + before + " after = " + after);
    }
}
