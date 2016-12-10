# OkHttp-TimeWait-Test

An isolated test case reproducing [the TIME_WAIT problem](http://stackoverflow.com/questions/41011287/why-okhttp-doesnt-reuse-its-connections).

### Build:

`gradlew jar`

### Launch:

`java -jar _dist/okhttp_time_wait.jar`

or

`java -jar _dist/okhttp_time_wait.jar <port> <thread-count>`

### Benchmark result:

```
5359 req/sec
3927 req/sec
3104 req/sec
2690 req/sec
1705 req/sec
1405 req/sec
1230 req/sec
724 req/sec
579 req/sec
529 req/sec
java.net.SocketTimeoutException: connect timed out
533 req/sec
669 req/sec
628 req/sec
583 req/sec
java.net.SocketTimeoutException: connect timed out
684 req/sec
374 req/sec
0 req/sec
java.net.SocketTimeoutException: connect timed out
27 req/sec
0 req/sec
0 req/sec
0 req/sec
```
