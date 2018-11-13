#!/bin/sh

DEST_DIR="dest"

PORT=22000
APPLICATION_NAME=molecule
SERVICE_NAME="${APPLICATION_NAME}-server"

CONFIG_LABEL="default"
CONFIG_URI="http://${CLOUD_ADDRESS}:10000"


CONTAINER_NAME=${SERVICE_NAME}-${CONFIG_LABEL}
IMAGE_NAME=${SERVICE_NAME}-${CONFIG_LABEL}

BIND_PORT=${PORT}
BIND_DIR=/data/containers/${CONTAINER_NAME}-${BIND_PORT}


#build docker image
function docker_build(){
    IMAGE_NAME=$1
    docker build -t ${IMAGE_NAME} .
}

#remove all none images
function docker_rm_none_images(){
    docker images |grep none | grep -v grep
    if [ $? -ne 0 ]
    then
    echo "no none images"
    else
    echo "remove none images"
    docker images |grep none |awk '{print $3}' | xargs docker rmi
    fi
}

#remove all never use containers
function docker_rm_containers(){
    CONTAINER_NAME=$1
    BIND_PORT=$2
    echo "${CONTAINER_NAME}|${BIND_PORT}"
    docker ps -a | grep -E "${CONTAINER_NAME}|${BIND_PORT}" | grep -v grep
    if [ $? -ne 0 ]
    then
    echo "no container"
    else
    echo "remove container"
    docker ps -a |grep -E "${CONTAINER_NAME}|${BIND_PORT}" |awk '{print $1}' | xargs docker stop
    docker ps -a |grep -E "${CONTAINER_NAME}|${BIND_PORT}" |awk '{print $1}' | xargs docker rm -f
    fi
}

# run a docker container with image
function docker_run(){
    APPLICATION_NAME=$1
    SERVICE_NAME=$2
    CONTAINER_NAME=$3
    BIND_PORT=$4
    IMAGE_NAME=$5

    echo "docker run -ti -d \
    --net host \
    --name ${CONTAINER_NAME}-${BIND_PORT} \
    --publish ${BIND_PORT}:${BIND_PORT} \
    --restart always \
    -v ${BIND_DIR}/logs:/data/logs \
    --env PORT=${BIND_PORT} \
    --env CONFIG_URI=${CONFIG_URI} \
    --env CONFIG_USERNAME=${CONFIG_USERNAME} \
    --env CONFIG_PASSWORD=${CONFIG_PASSWORD} \
    --env CONFIG_LABEL=${CONFIG_LABEL} \
    --env APPLICATION_NAME=${APPLICATION_NAME} \
    --env EXECUTE_JAR=${SERVICE_NAME} \
    ${IMAGE_NAME}"

    docker run -ti -d \
    --net host \
    --name ${CONTAINER_NAME}-${BIND_PORT} \
    --publish ${BIND_PORT}:${BIND_PORT} \
    --restart always \
    -v ${BIND_DIR}/logs:/data/logs \
    --env PORT=${BIND_PORT} \
    --env CONFIG_URI=${CONFIG_URI} \
    --env CONFIG_USERNAME=${CONFIG_USERNAME} \
    --env CONFIG_PASSWORD=${CONFIG_PASSWORD} \
    --env CONFIG_LABEL=${CONFIG_LABEL} \
    --env APPLICATION_NAME=${APPLICATION_NAME} \
    --env EXECUTE_JAR=${SERVICE_NAME} \
    ${IMAGE_NAME}
}

#mvn package
function mvn_package(){
    DEST_DIR=$1
    SERVICE_NAME=$2
    WITH_TEST=$3

    mvn clean

    echo "clean compile clean package"
    if test -z "${WITH_TEST}"
    then
        mvn -U clean compile package -Dmaven.test.skip=true
    else
        mvn -U clean compile package
    fi

    #remove space
    DEST_DIR=`echo ${DEST_DIR} | sed s/[[:space:]]//g`

    if test -z "${DEST_DIR}"
    then
        echo "Error DEST_DIR empty"
        exit 1
    else
        rm -fr ${DEST_DIR}/*
        mkdir -pv ${DEST_DIR}
        cp -r scripts/* ${DEST_DIR}/
    fi


    ORG_PATH=${SERVICE_NAME}/target/${SERVICE_NAME}.jar

    # 这里的-f参数判断 ${ORG_PATH} 是否存在
    if [ -f "${ORG_PATH}" ];
    then
        echo "copy ${ORG_PATH} "
        cp ${ORG_PATH} ${DEST_DIR}/${SERVICE_NAME}.jar
        echo "success"
    else
        echo "打包失败"
        exit 1
    fi
}

#start service
function start(){
    APPLICATION_NAME=$1
    SERVICE_NAME=$2
    PORT=$3
    echo "nohup java -Dfile.encoding=UTF-8 -Xmx512m -Xms128m -Xss256k  -jar ${SERVICE_NAME}.jar --PORT=${PORT} --APPLICATION_NAME=${APPLICATION_NAME} --CONFIG_URI=${CONFIG_URI} --CONFIG_USERNAME=${CONFIG_USERNAME} --CONFIG_PASSWORD=${CONFIG_PASSWORD} --CONFIG_LABEL=${CONFIG_LABEL} &"
    nohup java -Dfile.encoding=UTF-8 -Xmx512m -Xms128m -Xss256k  -jar ${SERVICE_NAME}.jar --PORT=${PORT} --APPLICATION_NAME=${APPLICATION_NAME} --CONFIG_URI=${CONFIG_URI} --CONFIG_USERNAME=${CONFIG_USERNAME} --CONFIG_PASSWORD=${CONFIG_PASSWORD} --CONFIG_LABEL=${CONFIG_LABEL} &
}

#shutdown service
function shutdown(){
    SERVICE_NAME=$1
    echo "stop ${SERVICE_NAME}"
    #notify java process shutdown
    ps -ef | grep -E "${SERVICE_NAME}" | grep -v grep | awk '{print $2}' | xargs kill -15
    sleep 10s
    # focus kill java process
    ps -ef | grep -E "${SERVICE_NAME}" | grep -v grep | awk '{print $2}' | xargs kill -9
}