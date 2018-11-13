#FROM docker.io/xl19870217/centos6.6-jdk1.8
#FROM java:8u111-jre-alpine

#setup timezone
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV APPLICATION_NAME=molecule
ENV EXECUTE_JAR=${APPLICATION_NAME}-server
    CONFIG_LABEL="default"


COPY . /data

#
#RUN locale-gen zh_CN.UTF-8 &&\
#  DEBIAN_FRONTEND=noninteractive dpkg-reconfigure locales
#RUN locale-gen zh_CN.UTF-8
#ENV LANG zh_CN.UTF-8
#ENV LANGUAGE zh_CN:zh
#ENV LC_ALL zh_CN.UTF-8

VOLUME /data/logs

WORKDIR /data

CMD exec java -Dfile.encoding=UTF-8 -Xmx512m -Xms128m -Xss256k  -jar ${EXECUTE_JAR}.jar --PORT=${PORT} --APPLICATION_NAME=${APPLICATION_NAME} --CONFIG_URI=${CONFIG_URI} --CONFIG_USERNAME=${CONFIG_USERNAME} --CONFIG_PASSWORD=${CONFIG_PASSWORD} --CONFIG_LABEL=${CONFIG_LABEL}
