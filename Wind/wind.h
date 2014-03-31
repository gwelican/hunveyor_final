define BDIR	0x00
#define CDIR	0x00
#define DDIR	0x0E0	//port directions
#define BINIT	0x00
#define CINIT	0x00
#define DINIT	0x00	//port initial states

#define SLAVE_ADDR 0x9



#define GET_WINDSPEED 0x20
#define GET_RADIATION 0x21
#define GET_WINDDIRECTION 0x22
#define GET_LIGHTNING 0x23

#define WIND_ON 0x24
#define WIND_OFF 0x25
#define RADIATION_ON 0x26
#define RADIATION_OFF 0x27
#define LIGHTNING_ON 0x28
#define LIGHTNING_OFF 0x29

#define TEST_BYTE 0xAA

#define ADC_PRESCALER ((1<<ADPS2) | (1<<ADPS1)) // prescale 64

#define ADC_REF_EXTERNAL (0)
#define ADC_REF_POWER ((1<<REFS0)|(0<<REFS1)) // AVCC as refernce value
#define ADC_REF_INTERNAL ((1<<REFS1) | (1<<REFS0))

#include <avr/io.h>
#include <compat/twi.h>
#include <avr/interrupt.h>


#include "TWI_slave.h"
#include <util/twi.h>
#include "measure.h"


uint16_t ReadWindSpeed();
uint16_t ReadWindDirection();
uint16_t ReadLightning();
uint16_t ReadRadiation();
void Lightning(int toggle);
void Radiation(int toggle);
void Wind(int toggle);
void initMeasure();
void measure10sec();
