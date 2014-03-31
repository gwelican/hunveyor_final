#define BDIR	0x07
#define CDIR	0x01
#define DDIR	0x00	//port directions
#define BINIT	0x07
#define CINIT	0x00
#define DINIT	0x00	//port initial states

#define SLAVE_ADDR 0xA

#define GET_VIBRATION 0xA0
#define TEST_BYTE 0xAA
#define SWITCH_OFF 0xA0
#define SWITCH_ON 0xA1

#define ADC_PRESCALER ((1<<ADPS2) | (1<<ADPS1)) // prescale 64

#define ADC_REF_EXTERNAL (0)
#define ADC_REF_POWER ((1<<REFS0)|(0<<REFS1)) // AVCC as refernce value
#define ADC_REF_INTERNAL ((1<<REFS1) | (1<<REFS0))

#include <avr/io.h>
#include <compat/twi.h>
#include <avr/interrupt.h>


#include "TWI_slave.h"
#include <util/twi.h>
#include <util/delay.h>
#include "reg.h"

void initMeasure();

#define AMBIENT_COUNT 10
#define HIST 20			//érzékelõsi küszöb
#define TOUT 100		//idõtúllépés
#define REGSTART 0x10	//innentõl adatok
//#define REG_LENGTH 128
//extern unsigned char regmap[REG_LENGTH];


//char ReadVibration();

