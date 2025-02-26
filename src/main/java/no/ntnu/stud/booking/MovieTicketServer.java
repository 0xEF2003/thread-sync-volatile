package no.ntnu.stud.booking;

public class MovieTicketServer {

    private String movieName;
    private volatile int availableTickets;

    public MovieTicketServer(String movieName, int availableTickets) {
        this.movieName = movieName;
        this.availableTickets = availableTickets;
    }

    public synchronized String bookTickets(int orderedTickets) {
        int before = availableTickets;
        if (availableTickets >= orderedTickets) {
            availableTickets -= orderedTickets;
        }
      return " Available tickets before: " + before + " Available tickets left: " + availableTickets;
    }
}
