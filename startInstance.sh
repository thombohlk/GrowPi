#!/bin/bash

ROLE=${ROLE:-client}
INFLUXDB_IP=${INFLUXDB_IP:-influxdb}
REDIS_IP=${REDIS_IP:-redis}
PROJECT_URL=$PROJECT_URL
PROJECT_AUTH=$PROJECT_AUTH
CONFIG_FILE_NAME=ini/general.ini

if [ -f /etc/boinc-client/gui_rpc_auth.cfg ]; then
	BOINC_PASSWORD=$(sudo cat /etc/boinc-client/gui_rpc_auth.cfg)
elif [ -f /var/lib/boinc/gui_rpc_auth.cfg ]; then
	BOINC_PASSWORD=$(sudo cat /var/lib/boinc/gui_rpc_auth.cfg)
else
	echo "Could not find boinc authentication file, exiting."
	exit 1
fi

# write configuration file
mkdir ini
echo "[influxdb]" > $CONFIG_FILE_NAME
echo "username = root" >> $CONFIG_FILE_NAME
echo "password = eden314@pi" >> $CONFIG_FILE_NAME
echo "ip = http://$INFLUXDB_IP:8086" >> $CONFIG_FILE_NAME
echo "dbname = grow_pi" >> $CONFIG_FILE_NAME
echo "" >> $CONFIG_FILE_NAME
echo "[redis]" >> $CONFIG_FILE_NAME
echo "ip = $REDIS_IP" >> $CONFIG_FILE_NAME
echo "" >> $CONFIG_FILE_NAME
echo "[boinc]" >> $CONFIG_FILE_NAME
echo "password = $BOINC_PASSWORD" >> $CONFIG_FILE_NAME


# start boinc and join
start-stop-daemon -u boinc -d /var/lib/boinc-client/ --start --startas /usr/bin/boinc -- --daemon
sleep 3 # so boinc has time to start up before project is added
boinccmd --project_attach $PROJECT_URL $PROJECT_AUTH


# start the app
sudo java -jar GrowPi-0.1.jar -r $ROLE
