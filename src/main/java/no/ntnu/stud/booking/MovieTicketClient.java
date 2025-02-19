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

    public synchronized String bookTickets() {
        return server.bookTickets(orderedTickets);
    }

    @Override
    public void run() {
        int availableTickets = server.getAvailableTickets();
        System.out.println(Thread.currentThread().getName() + " Available tickets before: " + availableTickets + " Available tickets left: " + bookTickets());
    }
}
