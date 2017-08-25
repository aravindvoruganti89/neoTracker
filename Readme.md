### Introduction
NEO (Near Earth Objects) are asteroids or comets which falls under the earth's orbit.
This REST API project communicates with NASA's NEO web service to retrieve the closest NEO of the current day along with the largest NEO and the count of total NEOs.

### How to Run

#### To run from IDE
* Import the code from github into your IDE as a Maven project
* Create the logs folder if not exists already
* Set the Run configuration to `com.test.ExecuteNeoTracker`
* Edit the `api_key` and other params in `conf/neoSettings.json`

#### To run from CLI
* Download the code into your server.
* Ensure you have `bin`, `conf`, `logs`, `src`, `lib` folders exists
* Run `mvn clean package` from your project directory
* Edit the `$JAVABIN` in `bin/ExecuteNeoTracker.sh`
* Run the `ExecuteNeoTracker.sh` script

### Notes
The `api_key` can be set to `DEMO_KEY` for simpler tests. The rate limit on thie key is very limited. If you want to make rigorous API tests with a better rate limit then signup for an API key on the NASA NEO Authentication webpage.

If `closestNeoToday` is enabled, then the `neoTracker` app will find the closest NEO for the current day. The distance is measured in the missile distance/lunar distance.

If `largestNeoToday` is enabled, then the `neoTracker` app will find the largest NEO for the current day. It uses the diameter to determine the size of the asteroid.

If `largestNeoAllTime` is enabled, then the `neoTracker` app will find the largest NEO by its diameter from all the NEOs available in the NASA NEO webservice. Be careful when enabling this request as this request would quickly crosses the `api_key` rate limit.

Search for `Total near_earth_object_count` in the `logs/execute_neo_tracker.log` to find the total NEO count.

Search for `The Shortest NEO from Earth` in the `logs/execute_neo_tracker.log` to find the shortest NEO from earth with detailed view.

Search for `Largest NEO` in the `logs/execute_neo_tracker.log` to find the largest NEO with detailed view

