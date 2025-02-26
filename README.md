# Movie Ticket Booking

This is a simple project aiming at synchronizing threads and using the volatile keyword in java.

## How to run

### If your using an IDE
1. Clone the repository
2. Open the project in your favorite IDE
3. Run the `App.java` file


### If you want to run a jar file
1. Clone the repository
2. run `mvn clean package`
3. run `java -jar target/thread-sync-volatile-1.0-SNAPSHOT.jar`


### The changes you have to make to test all conditions that are mentioned in the report

#### With synchronization and without volatile
1. Run the program as is (no changes needed)

#### With synchronization and volatile
1. Open the `MovieTicketServer.java` file
2. Change the 'availableTickets' variable to be `private volatile int availableTickets;`

#### Without synchronization and with volatile (After having made the changes for the previous test)
1. Open the `MovieTicketServer.java` file
2. Change the 'bookTickets' method to be `public void bookTickets(int orderedTickets) {`

#### Without synchronization and without volatile (After having made the changes for the previous test)
1. Open the `MovieTicketServer.java` file
2. Change the 'bookTickets' method to be `public void bookTickets(int orderedTickets) {`
3. Change the 'availableTickets' variable to be `private int availableTickets;`