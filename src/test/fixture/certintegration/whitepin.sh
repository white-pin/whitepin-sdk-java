#!/usr/bin/env bash

if [[ ! -e "docker-compose.yaml" ]];then
  echo "docker-compose.yaml not found."
  exit 8
fi

function clean(){
  docker-compose  -f docker-compose.yaml down
}

function up(){
  docker-compose up --force-recreate
}

function down(){
  docker-compose down;
}

for opt in "$@"
do
    case "$opt" in
        up)
            up
            ;;
        down)
            down
            ;;
        stop)
            down
            ;;
        start)
            up
            ;;
        clean)
            clean
            ;;
        restart)
            down
            clean
            up
            ;;

        *)
            echo $"Usage: $0 {up|down|start|stop|clean|restart}"
            exit 1

esac
done
