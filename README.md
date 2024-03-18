# Longest bus stops on the route

Find out which bus lines have the most bus stops on their route, and list the top n as clear text. Application should also list the names of every bus stop of the bus line that has the most stops. There is no requirement on how the bus stops are sorted.

# Instructions

## Obtain a Trafiklab API key

This program use the [Trafiklab’s open API](https://www.trafiklab.se). For this to work you need an API key by
registering your own account at  http://www.trafiklab.se/api/sl-hallplatser-och-linjer-2.

## Compile
```shell
mvn clean verify
```
## Execute
```shell
java -jar target/longest-bus-route-1.0-SNAPSHOT.jar ${n} ${API-key} 
```
Where:
* `n` is the number of routes wanted to be displayed,
* `API-key` is the API key that you obtained [previously](#obtain-a-trafiklab-api-key)
