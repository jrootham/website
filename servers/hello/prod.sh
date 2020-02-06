#!/bin/bash

lein uberjar

scp /home/jrootham/dev/website/servers/hello/target/uberjar/hello-0.1.0-standalone.jar \
	jrootham@jrootham.ca:/home/jrootham/servers/hello.jar

scp -r /home/jrootham/dev/website/docroot/hello \
	jrootham@jrootham.ca:/home/jrootham/http/docroot/
