#!/bin.sh

# Deploy a release to bintray and a snapshopt to jfrog OJO.

# Only the base benchmark library needs to be deployed.

cd benchmark
mvn deploy
