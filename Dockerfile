FROM jsurf/rpi-java

RUN [ "cross-build-start" ]

ENV LANG C.UTF-8 
ENV TZ Europe/Berlin

# Boinc, from laurentmalvert/docker-boinc

RUN apt-get update &&                           \
    apt-get -q install -y boinc-client libusb-dev &&       \
    apt-get clean &&                            \
    rm -rf /var/lib/apt/lists/*

RUN [ "cross-build-end" ]

EXPOSE \ 
    31416 \ 
    80 \ 
    443

# default environment variables
ENV ROLE = client

# eden314
ADD build/libs/GrowPi-0.1.jar .
ADD startInstance.sh .

# start
ENTRYPOINT ["./startInstance.sh"]
