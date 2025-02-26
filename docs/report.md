## What does volatile do?
It ensures that the value of a *field* marked `volatile` is always read from and written to the *main memory* (not from and to the thread cache).
It can still be read and written to asynchronously by any thread.

## What does synchronized do?
It ensures that for all threads calling a *method* (not a field) which is marked as `synchronized`, only one can call it at a time.
The method is "blocked" for all others once a thread calls it, and "unblocked" once the current thread's execution completes.
Once a new thread begins excuting it, it is once again "blocked" for all others.
However, this does not order the threads a particular way.


## Make note of the "Available Tickets" value for each thread

This is without volatile and synchronized keywords.
Thread "order" is displayed in the order of the text output (not the order they started).
Threads are always started in numeric order.

```java
// Creating 4 threads
Thread t1 = new MovieTicketClient(movieTicketServer, "Xiangming", 3);
Thread t2 = new MovieTicketClient(movieTicketServer, "Ilaria", 2);
Thread t3 = new MovieTicketClient(movieTicketServer, "Sam", 3);
Thread t4 = new MovieTicketClient(movieTicketServer, "Andreas", 4);
```

In all our tests Xiangming subtracts 3 tickets, Ilaria subtracts 2 tickets, Sam subtracts 3 tickets, and Andreas subtract 4 tickets.
If there are not enough tickets left they will not book any tickets at all.

Test 1:

* Xiangming: before = 10 after = 7
* Andreas: before = 5 after = 1
* Sam: before = 1 after = 1
* Ilaria: before = 7 after = 5

Test 2:

* Andreas: before = 4 after = 0
* Ilaria: before = 0 after = 0
* Xiangming: before = 10 after = 7
* Sam: before = 7 after = 4

Test 3:

* Xiangming: before = 10 after = 7
* Andreas: before = 2 after = 2
* Ilaria: before = 7 after = 5
* Sam: before = 5 after = 2

Even though the order is a bit weird, the math always adds up.
We will be adding a small delay due to the results being *way* too perfect for a real multithreading scenario.
This is likely because our operations are so light they don't ever happen at the same time.
A new round of tests with delay produced the following result: (we only take the time to explain one)

* Andreas: before = 10 after = 4
* Xiangming: before = 10 after = 1
* Ilaria: before = 10 after = 4
* Sam: before = 10 after = 6

The output does not make sense if we were ordering tickets in real life, but it makes very much sense taken into consideration that this is (naive) multithreading.
Two things (at least) contribute to making this confusing.
Since all threads start simultaneously and don't finish quickly enough to update `availableTickets` before another thread starts, all threads believe there are 10 tickets available (which is technically true but not necessarily intuitive).
Furthermore, the order things happen in can also be very unintuitive.
Here is a attempt at making sense of the output:

1. Sam begins booking 3 tickets (availableTickets = 10)
1. Andreas begins booking 4 tickets (availableTickets = 10)
1. Sam finishes booking 3 tickets (availableTickets = 7)
1. Andreas finishes booking 4 tickets (availableTickets = 6)
1. Sam asks server for tickets left (availableTickets = 6)
1. Ilaria begins booking 2 tickets (availableTickets = 6)
1. Ilaria finishes booking 2 tickets (avalableTickets = 4)
1. Ilaria asks server for tickets left (availableTickets = 4)
1. Andreas asks server for tickets left (availableTickets = 4)
1. Xianming begins booking 3 tickets (availableTickets = 4)
1. Xiangming finishes booking 3 tickets (availableTickets = 1)
1. Xiangming asks server for tickets left (availableTickets = 1)

Here we see that the threads override one another, creating inconsistent results where the math doesn't add up.
One thing that further battles our intuition here is that this is a *non-volatile* field.
Why would we not expect "consistent" results like the following?

* Andreas: before = 10 after = 6
* Xiangming: before = 10 after = 7
* Ilaria: before = 10 after = 8
* Sam: before = 10 after = 7

Here they would all start with caching the value 10 and then subtract their respective numbers from it without reference to main memory.
This tells us that there perhaps is some ambiguity as to how the flow between thread cache and the main memory works.

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

* Andreas: before = 10 after = 5
* Ilaria: before = 10 after = 8
* Sam: before = 10 after = 5
* Xiangming: before = 10 after = 2

Explanation attempt:

1. Ilaria starts booking 2 tickets (availableTickets = 10)
1. Ilaria finishes booking 2 tickets (availableTickets = 8)
1. Ilaria asks server for tickets left (availableTickets = 8)
1. Sam begins booking 3 tickets (availableTickets = 8)
1. Andreas begins booking 4 tickets (availableTickets = 8)
1. Andreas finishes booking (availableTickets = 4)
1. Sam finishes booking 3 tickets (availableTickets = 5)
1. Andreas asks server for tickets left (availableTickets = 5)
1. Sam asks server for tickets left (availableTickets = 5)
1. Xiangming begins booking 3 tickets (availableTickets = 5)
1. Xiangming finishes booking 3 tickets (availableTickets = 2)
1. Xianming asks server for tickets left (availableTickets = 2)


We do not see any meaningful difference compared to the previous test for our specific use-case.
But, we actually expect this kind of output *here* (and not in the previous test).
We still see threads overriding one another because of timing.

\
\
\
\
\
\
\

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

* Sam: before = 10 after = 1
* Andreas: before = 10 after = 4
* Xiangming: before = 10 after = 1
* Ilaria: before = 10 after = 8

Test 2:

* Ilaria: before = 10 after = 5
* Andreas: before = 10 after = 2
* Sam: before = 10 after = 7
* Xiangming: before = 10 after = 2

Test 3:
* Andreas: before = 10 after = 2
* Ilaria: before = 10 after = 8
* Xiangming: before = 10 after = 5
* Sam: before = 10 after = 2

We will not do a equally in-depth analysis of this test as the preceeding (for obvious reasons).
However we want to note that there's no cases of threads overriding one another here.
Since the code-block that updates `availableTickets` has been forced to run by one thread at a time, the math now adds up.

\
\
\
\
\
\
\
\
\
\
\
\

## Remove the volatile keyword and note the difference

Test 1:

* Sam: before = 10 after = 7
* Xiangming: before = 10 after = 4
* Ilaria: before = 10 after = 2
* Andreas: before = 10 after = 2

Test 2:

* Andreas: before = 10 after = 1
* Xiangming: before = 10 after = 7
* Sam: before = 10 after = 1
* Ilaria: before = 10 after = 5

Test 3:

* Ilaria: before = 10 after = 1
* Andreas: before = 10 after = 6
* Xiangming: before = 10 after = 3
* Sam: before = 10 after = 1

Similarly to the foregoing test, the math adds up in all cases.
We are unable to see any difference after we removed the `volatile` keyword.
Perhaps this would have been more visible if the use-case made threads live longer and read a lot more from the shared variable.
Then the results should have been a lot less consistent without `volatile` as far we understand it.

## Conclusion
During these tests we could clearly see which tests were affected by synchronization,
However, we failed to note a difference between when a field was marked `volatile` and when it was not. 
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
