## What does volatile do?
The volatile keyword ensures that the process of reading and writing a variable will always be based on the latests result of an operation performed by a thread.
It ensures that the value of the variable is always read from the main memory and not from the cache.

## What does synchronized do?
The synchronized keyword ensures that only one thread can access a shared resource at a time.
This is done by locking the resource, and unlocking it when the thread is complete.


## Make note of the "Available Tickets" value for each thread

This is without volatile and synchronized keywords.
The "thread number" means the order of execution as noted from console print:

Test 1:

1. Thread: 10 before, 7 after
1. Thread: 7 before, 5 after
1. Thread: 5 before, 2 after
1. Thread: 2 before, 0 after

(Potential math error. 10 - 3 = 7, 7 - 2 = 5, 5 - 3 = 2, 2 - 4 = 0? Supposed to be 2 still.)

Test 2:

1. Thread: 10 before, 7 after
1. Thread: 7 before, 4 after
1. Thread: 4 before, 2 after
1. Thread: 2 before, 2 after

Here we kept track of the thread number as they were created in the code.
Thus we know in what order the threads *actually* completed:

Test 1:

* Thread-0: 10 before, 7 after
* Thread-1: 7 before, 5 after
* Thread-2: 5 before, 2 after
* Thread-3: 2 before, 2 after

Test 2:

* Thread-1: 7 before, 5 after
* Thread-0: 10 before, 7 after
* Thread-2: 5 before, 2 after
* Thread-3: 2 before, 2 after

As we can see the order is not consistent, and the math does not always add up "chronologically" (the order in which the threads completed).
Test 1 makes perfect sense, but test 2 doesn't.
Test 2 would have made sense if threads 0 and 1 "swapped" order of execution.


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

Thread-0: 10 before, 7 after
Thread-2:  3 before, 0 after
Thread-3:  7 before, 3 after
Thread-1:  0 before, 0 after

Test 2:

Thread-0: 10 before, 7 after
Thread-1: 7 before, 5 after
Thread-2: 5 before, 2 after
Thread-3: 2 before, 2 after

We still see that neither the order of execution nor the math always adds up.
The math would have added up in test 1 if threads 2 and 3 "swapped" order of execution.
Test 2 is however one of the cases where everything adds up.


## Add thread synchronization and note the difference

We did the volatile-only test in the previous section as we never had thread synchronization in the first place.
For these tests we *will* use thread synchronization.
For this test we have modified the following line in `Math`
