#include <stdio.h>
#include <string.h>
#include <strings.h>
#include <usb.h>
#include <errno.h>
#include <unistd.h>
#include <stdlib.h>

/*
 * Temper.c by Robert Kavaler (c) 2009 (relavak.com)
 * All rights reserved.
 *
 * Modified by Sylvain Leroux (c) 2012 (sylvain@chicoree.fr)
 *
 * Temper driver for linux. This program can be compiled either as a library
 * or as a standalone program (-DUNIT_TEST). The driver will work with some
 * TEMPer usb devices from RDing (www.PCsensor.com).
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHORS ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Robert kavaler BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

#include "comm.h"

#if !defined TEMPER_TIMEOUT
#define TEMPER_TIMEOUT 1000    /* milliseconds */
#endif

#if !defined TEMPER_DEBUG
#define TEMPER_DEBUG 1
#endif

typedef enum { false, true } bool;

double getMeasurement(Temper *t)
{
    int ret;
    TemperData data[2];

    TemperSendCommand8(t, 0x01, 0x80, 0x33, 0x01, 0x00, 0x00, 0x00, 0x00);
    if (0) {
        unsigned char buf[8];
        TemperInterruptRead(t, buf, sizeof(buf));
    }
    else {
        const unsigned int count = sizeof(data)/sizeof(TemperData);
        ret = TemperGetData(t, data, count);
        if (ret == -1) {
            printf("Failed to get Temper data.");
        } else {
            //printf("Inside temperature:  %f %s\n", data[0].value, TemperUnitToString(data[0].unit));
            //printf("Outside temperature: %f %s\n", data[1].value, TemperUnitToString(data[1].unit));
            printf("%f\n", data[0].value);
            printf("%f\n", data[1].value);
        }

        //printf("\n\n");
    }

    return data[1].value;
}

void startBoinc()
{
    printf("Starting boinc-client... ");
    int ret = system("sudo service boinc-client start");
    if (ret == -1) {
        printf("Failed.\n");
    } else {
        printf("Succes.\n");
    }
}

void stopBoinc()
{
    printf("Stopping boinc-client... ");
    int ret = system("sudo service boinc-client stopt");
    if (ret == -1) {
        printf("Failed.\n");
    } else {
        printf("Succes.\n");
    }
}

int main(void)
{
    Temper *t;

    usb_set_debug(0);
    usb_init();
    usb_find_busses();
    usb_find_devices();

    t = TemperCreateFromDeviceNumber(0, TEMPER_TIMEOUT, TEMPER_DEBUG);
    if(!t) {
        perror("TemperCreate");
        exit(-1);
    }

/*
    TemperSendCommand(t, 10, 11, 12, 13, 0, 0, 2, 0);
    TemperSendCommand(t, 0x43, 0, 0, 0, 0, 0, 0, 0);
    TemperSendCommand(t, 0, 0, 0, 0, 0, 0, 0, 0);
    TemperSendCommand(t, 0, 0, 0, 0, 0, 0, 0, 0);
    TemperSendCommand(t, 0, 0, 0, 0, 0, 0, 0, 0);
    TemperSendCommand(t, 0, 0, 0, 0, 0, 0, 0, 0);
    TemperSendCommand(t, 0, 0, 0, 0, 0, 0, 0, 0);
    TemperSendCommand(t, 0, 0, 0, 0, 0, 0, 0, 0);
*/

//    TemperSendCommand2(t, 0x01,0x01);
//    TemperSendCommand8(t, 0x48, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00);
    //int i = 0;
    //double temp = 0.;
    //bool running = false;
    //while (1) {
        getMeasurement(t);
        getMeasurement(t);
        
        //if (temp < 26 && running == false) {
            //startBoinc();
            //printf("Starting boinc.");
        //} else if (temp > 28 && running == true) {
            //stopBoinc();
            //printf("Stopping boinc.");
        //}

        //i++;
        //sleep(3);
    //}

    //TemperFree(t);

    return 0;
}

