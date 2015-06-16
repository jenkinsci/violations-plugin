#!/bin/bash
rm -Rf work/jobs/*/builds
rm -Rf work/jobs/*/nextBuildNumber
rm -Rf work/jobs/*/modules
rm -Rf work/jobs/*/workspace/target
rm -Rf work/jobs/*/workspace/*/target
rm -Rf work/jobs/*/workspace/dist
rm -Rf work/jobs/*/workspace/build
rm -Rf work/fingerprints
rm -Rf work/queue.txt 
rm -Rf work/secret.key 
rm -Rf work/update-center.json
