package no.ntnu.stud.booking;

public class MovieTicketServer {

    private String movieName;
    private int availableTickets;

    public MovieTicketServer(String movieName, int availableTickets) {
        this.movieName = movieName;
        this.availableTickets = availableTickets;
    }

    public synchronized void bookTickets(int orderedTickets) {
        if (availableTickets >= orderedTickets) {
            availableTickets -= orderedTickets;
        }
    }

    public int getAvailableTickets() {
        return availableTickets;
    }
}
