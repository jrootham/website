#!/bin/bash

ln -sf /home/jrootham/dev/website/servers/nopassword/src/nopassword/devstuff.clj \
	/home/jrootham/dev/website/servers/nopassword/src/nopassword/stuff.clj
lein uberjar
