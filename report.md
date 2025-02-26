## What does volatile do?
It ensures that the value of a *field* marked `volatile` is always read from and written to the *main memory* (not from the thread cache).
It can still be read and written to asynchronously by any thread.

## What does synchronized do?
It ensures that for all threads calling a *method* (not a field) which is marked as `synchronized`, only one can call it at a time.
The method is "blocked" for all others once a thread calls it, and "unblocked" once the current thread's execution completes.
Once a new thread begins excuting it, it is once again "blocked" for all others.
However, this does not order the threads a particular way.


## Make note of the "Available Tickets" value for each thread

This is without volatile and synchronized keywords.
Thread "order" is displayed in the order they finish (not the order they started).
Threads are always started in numeric order.


Test 1:

* Xiangming: before = 10 after = 7
* Ilaria: before = 7 after = 5
* Sam: before = 5 after = 2
* Andreas: before = 2 after = 2

Test 2:

* Xiangming: before = 10 after = 7
* Sam: before = 5 after = 2
* Ilaria: before = 7 after = 5
* Andreas: before = 2 after = 2

Test 3:

* Xiangming: before = 10 after = 7
* Andreas: before = 2 after = 2
* Ilaria: before = 7 after = 5
* Sam: before = 5 after = 2

The output does not make sense if we were ordering tickets in real life, but it makes very much sense taken into consideration that this is multithreading:

```java
// Creating 4 threads
Thread t1 = new MovieTicketClient(movieTicketServer, "Xiangming", 3);
Thread t2 = new MovieTicketClient(movieTicketServer, "Ilaria", 2);
Thread t3 = new MovieTicketClient(movieTicketServer, "Sam", 3);
Thread t4 = new MovieTicketClient(movieTicketServer, "Andreas", 4);
```

In all our tests Xiangming subtracts three tickets, Ilaria subtracts two tickets, Sam subtracts 3 tickets, and Andreas (attempts to) subtract 4 tickets.
What does not "make sense" is the values each thread starts with (the thread cache).


## Change the “Available tickets” shared variable to “volatile”

Here we are noting what differences it makes when the shared variable is prefixed with the keyword `volatile`
This means we have modified the following line in `MovieTickerServer.java`:
```java
// original line of code
private int availableTickets;

// modified line of code
private volatile int availableTickets;
```

Test 1:

* Xiangming: before = 10 after = 7
* Ilaria: before = 7 after = 5
* Sam: before = 5 after = 2
* Andreas: before = 2 after = 2

Test 2:

* Xiangming: before = 10 after = 7
* Ilaria: before = 7 after = 5
* Andreas: before = 2 after = 2
* Sam: before = 5 after = 2

Test 3:

* Xiangming: before = 10 after = 7
* Ilaria: before = 4 after = 2
* Sam: before = 7 after = 4
* Andreas: before = 2 after = 2

We do not see any meaningful difference for our specific use-case.
The `volatile` keyword would have made a difference for let's say a application where threads needed to access a shared date variable (if the threads run for longer than one day).
Then the date variable would not have been cached (and not updated) and instead read directly from the main memory where it is updated.


## Add thread synchronization and note the difference

For these tests we will *add* the `synchronized` keyword and *keep* the `volatile` keyword.
This means we have modified the following line in `MovieTicketServer.java`:

```java
// original line of code
public void bookTickets(int orderedTickets) {

// modified line of code
public synchronized void bookTickets(int orderedTickets) {
```

Test 1:

* Xiangming: before = 10 after = 7
* Sam: before = 5 after = 2
* Ilaria: before = 7 after = 5
* Andreas: before = 2 after = 2

Test 2:

* Xiangming: before = 10 after = 7
* Ilaria: before = 7 after = 5
* Sam: before = 5 after = 2
* Andreas: before = 2 after = 2

Test 3:

* Xiangming: before = 10 after = 7
* Andreas: before = 2 after = 2
* Sam: before = 7 after = 4
* Ilaria: before = 4 after = 2



## Remove the volatile keyword and note the difference
Now we are performing the tests where we don't use volatile, but we still use synchronization.

Even though we have removed the volatile keyword, we still cannot see any signs of volatility taking effect.
If a result for example the result after Thread-2 in test 1 were to be 7 as well as Thread-0, 
then we would be able to conclude that volatility had taken effect. 
We don't see any results that would indicate that the threads are not reading from the main memory.

Test 1:

- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-2 Available tickets before: 7 Available tickets left: 4
- Thread-1 Available tickets before: 4 Available tickets left: 2
- Thread-3 Available tickets before: 2 Available tickets left: 2


Test 2:

- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-1 Available tickets before: 7 Available tickets left: 5
- Thread-2 Available tickets before: 5 Available tickets left: 2
- Thread-3 Available tickets before: 2 Available tickets left: 2

Test 3:

- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-2 Available tickets before: 7 Available tickets left: 4
- Thread-3 Available tickets before: 4 Available tickets left: 0
- Thread-1 Available tickets before: 0 Available tickets left: 0

Test 4:

- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-2 Available tickets before: 7 Available tickets left: 4
- Thread-1 Available tickets before: 4 Available tickets left: 2
- Thread-3 Available tickets before: 2 Available tickets left: 2

## Conclusion
During these tests we could clearly see which tests were affected by synchronization,
however, we were not able to see any signs of volatility taking effect.
This might be because we didn't test enough, or some other pre-existing conditions that we are not aware of.

Overall the group is satisfied with the results of the tests, and we are confident that we have understood the concepts of synchronization and volatility.


## Group Members and Contributions
Elias Flåte Ekroll
- Wrote code
- Wrote report
- Tested code
- 
Henrik Aamot
- Wrote code
- Wrote report
- Tested code

All group members contributed equally to the project.
