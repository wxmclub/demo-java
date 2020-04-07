#!/bin/sh

docker run -d --name=mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123 mysql:5.7
