#!/bin/bash

lein uberjar

ln -sf /home/jrootham/dev/website/servers/hello/target/uberjar/hello-0.1.0-standalone.jar \
	/home/jrootham/dev/website/servers/hello.jar
