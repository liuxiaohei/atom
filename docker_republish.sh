#!/bin/sh

#set -e

basepath=$(cd `dirname $0`; pwd)
cd ${basepath}

IN_LABEL=$1
IN_PORT=$2

git pull

source ./scripts/config.sh

if test -z "${DEST_DIR}"
then
    echo "Error DEST_DIR empty"
    exit 1
fi

mvn_package ${DEST_DIR} ${SERVICE_NAME}

if test -z "${IN_LABEL}"
then
    echo "LABEL not set"
else
    CONFIG_LABEL=${IN_LABEL}
    CONTAINER_NAME=${SERVICE_NAME}-${CONFIG_LABEL}
    IMAGE_NAME=${SERVICE_NAME}-${CONFIG_LABEL}
fi

if test -z "${IN_PORT}"
then
    echo "PORT not set"
else
    BIND_PORT=${IN_PORT}
fi

BIND_DIR=/data/containers/${CONTAINER_NAME}-${BIND_PORT}

cd ${DEST_DIR}

docker_build ${IMAGE_NAME};

docker_rm_containers ${CONTAINER_NAME} ${BIND_PORT};

docker_run ${APPLICATION_NAME} ${SERVICE_NAME} ${CONTAINER_NAME} ${BIND_PORT} ${IMAGE_NAME} ;

docker_rm_none_images ;

sleep 10s

tail -f ${BIND_DIR}/logs/${SERVICE_NAME}.log

