#!/bin/sh

if [ ! -f /etc/ecuserial ]; then
	petname > /etc/ecuserial
fi

cat /etc/ecuserial
