# OkHttp-TimeWait-Test

An isolated test case reproducing [the TIME_WAIT problem](http://stackoverflow.com/questions/41011287/why-okhttp-doesnt-reuse-its-connections).

### Build:

`gradlew jar`

### Launch:

`java -jar _dist/okhttp_time_wait.jar`

or

`java -jar _dist/okhttp_time_wait.jar <port> <thread-count>`
