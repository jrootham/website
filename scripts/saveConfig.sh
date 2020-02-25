#!/bin/bash

# backup config files

cp /etc/nginx/sites-available/* /home/jrootham/dev/website/config/local/nginx
cp /etc/supervisor/supervisord.conf /home/jrootham/dev/website/config/local/supervisor
cp -r /etc/supervisor/conf.d /home/jrootham/dev/website/config/local/supervisor

scp jrootham@jrootham.ca:/etc/nginx/sites-available/\* /home/jrootham/dev/website/config/remote/nginx
scp jrootham@jrootham.ca:/etc/supervisor/supervisord.conf /home/jrootham/dev/website/config/remote/supervisor
scp -r jrootham@jrootham.ca:/etc/supervisor/conf.d /home/jrootham/dev/website/config/remote/supervisor
