## What does volatile do?
The volatile keyword ensures that the process of reading and writing a variable will always be based on the latest result of an operation performed by a thread.
It ensures that the value of the variable is always read from the main memory and not from the cache.

## What does synchronized do?
The synchronized keyword ensures that only one thread can access a shared resource at a time.
This is done by locking the resource, and unlocking it when the thread is complete.


## Make note of the "Available Tickets" value for each thread

This is without volatile and synchronized keywords.
During these tests, we cannot see any signs of volatility taking effect. 

We can however see signs that synchronization is not in place, as we can see that both Thread-2 and Thread-0 in Test 1 register that there are 4 tickets left.
This means that both of these operations were performed before they could deliver the result. If they were synchronized, one of them would give the result of 10 and 7, and the other of 10 and 4.
Since the combination of these operations would lead to 4 anyway we can tell that it is an error of synchronization, and not of volatility.

Test 1:

- Thread-2 Available tickets before: 7 Available tickets left: 4
- Thread-0 Available tickets before: 10 Available tickets left: 4
- Thread-3 Available tickets before: 4 Available tickets left: 0
- Thread-1 Available tickets before: 0 Available tickets left: 0

Test 2:

- Thread-1 Available tickets before: 7 Available tickets left: 5
- Thread-2 Available tickets before: 5 Available tickets left: 2
- Thread-0 Available tickets before: 10 Available tickets left: 5
- Thread-3 Available tickets before: 2 Available tickets left: 2

Test 3:

- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-1 Available tickets before: 7 Available tickets left: 5
- Thread-2 Available tickets before: 5 Available tickets left: 2
- Thread-3 Available tickets before: 2 Available tickets left: 2

Test 4:

- Thread-1 Available tickets before: 7 Available tickets left: 5
- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-2 Available tickets before: 5 Available tickets left: 2
- Thread-3 Available tickets before: 2 Available tickets left: 2


## Change the “Available tickets” shared variable to “volatile”

Here we are noting what differences it makes when the shared variable is prefixed with the keyword `volatile`
This means we have modified the following line in `MovieTickerServer.java`:
```java
// original line of code
private int availableTickets;

// modified line of code
private volatile int availableTickets;
```

Here we can still note the difference of synchronization, in the same way that we could in the first tests.
Thread-0 would remove 3, and Thread-1 would remove 2, meaning the result would be 5, which is the correct result.
Test 1:

- Thread-0 Available tickets before: 10 Available tickets left: 5
- Thread-1 Available tickets before: 7 Available tickets left: 5
- Thread-2 Available tickets before: 5 Available tickets left: 2
- Thread-3 Available tickets before: 2 Available tickets left: 2

Test 2:

- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-2 Available tickets before: 5 Available tickets left: 2
- Thread-3 Available tickets before: 2 Available tickets left: 2
- Thread-1 Available tickets before: 7 Available tickets left: 2

Test 3:

- Thread-1 Available tickets before: 7 Available tickets left: 5
- Thread-0 Available tickets before: 10 Available tickets left: 5
- Thread-3 Available tickets before: 5 Available tickets left: 1
- Thread-2 Available tickets before: 1 Available tickets left: 1

Test 4:

- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-1 Available tickets before: 7 Available tickets left: 5
- Thread-2 Available tickets before: 5 Available tickets left: 2
- Thread-3 Available tickets before: 2 Available tickets left: 2

## Add thread synchronization and note the difference

We did the volatile-only test in the previous section as we never had thread synchronization in the first place.
For these tests we *will* use thread synchronization.
For this test we have modified the following line in `Math`

When both volatile and synchronized are in place, we can see that the results are correct.
Even if some of the threads run out of order, and it is reflected in the final output. 
After each operation the correct result is displayed.
In Test 3 is a good example.
Thread-3 is run before Thread-2 resulting in 1 ticket left instead of 2. 
Even so the result still stands as the math for these operations are correct.

Test 1:

- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-1 Available tickets before: 7 Available tickets left: 5
- Thread-2 Available tickets before: 5 Available tickets left: 2
- Thread-3 Available tickets before: 2 Available tickets left: 2

Test 2:

- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-1 Available tickets before: 7 Available tickets left: 5
- Thread-2 Available tickets before: 5 Available tickets left: 2
- Thread-3 Available tickets before: 2 Available tickets left: 2


Test 3:

- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-1 Available tickets before: 7 Available tickets left: 5
- Thread-3 Available tickets before: 5 Available tickets left: 1
- Thread-2 Available tickets before: 1 Available tickets left: 1

Test 4:

- Thread-0 Available tickets before: 10 Available tickets left: 7
- Thread-1 Available tickets before: 7 Available tickets left: 5
- Thread-2 Available tickets before: 5 Available tickets left: 2
- Thread-3 Available tickets before: 2 Available tickets left: 2


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
