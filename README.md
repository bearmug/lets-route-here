https://travis-ci.com/bearmug/lets-route-here.svg?token=spKKuEnWo9hJNxYZZjsf&branch=master

# Solved problem
This repo contains solution for routing problem with two cases:
 - find best route between two points
 - find nearest points
 
# Usage
Just type *gradle run* to start application. Default Java implementation
to be used at the moment. Please follow provided instructions then.

## Choose implementation
At the moment *gradle run* using Java implementation only.

## Populate engine with data
## Calculate route
## Calculate nearest points
# Functional
## Java Solution
Java functional complete and tested for now. It is provided with 
*EngineClassic* class.

## Interoperable solution
Generated for Java/Scala compatibility demo at *EngineInteropFunc*. Java 
POJO beans used for simplification. Also tail recursion approach used. 
Requires output transformation for proper usage.

## Pure Scala solution
Located under *EnginePureFunc*. At the moment has implementation with
non-String output

# Testing approach
Travis CI build and Codecov code coverage tools configured for continuous 
quality monitoring.

At the moment there is unit-tests source-set.

# Benchmarks
JMH benchmark supposed to be configured to compare 3 implementations 
performance.