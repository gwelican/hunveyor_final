#include "wind.h"

unsigned int wind_s; 	 //szélsebesség
unsigned int wind_s_tmp;
unsigned int rad;	 	 //sugárzás
unsigned int rad_tmp;
unsigned int lightning_tmp;
unsigned int lightning_last10sec;
unsigned int rad_last10sec;
unsigned int windspeed_last10sec;

int measurecount;

void measure10sec() {
	rad_last10sec = rad-rad_tmp;
	lightning_last10sec = TCNT0 - lightning_tmp;
	windspeed_last10sec = wind_s - wind_s_tmp;

	wind_s_tmp = wind_s;
	rad_tmp = rad;
	lightning_tmp = TCNT0;
	if (measurecount >= 6) {
		measurecount = 0;
		wind_s_tmp = 0;
		lightning_tmp = 0;
		rad_tmp = 0;
	}
}


void initMeasure() {
	TCCR0 = 0x07;	 //timer0 external clock
	TIMSK |= 0x01; //timer0 interrupt enable
	MCUCR |= 0x0F;
	GICR |= 0xC0; //int1 int2 enable
	measurecount = 0;
	lightning_tmp = 0;
	TCNT0=0;
	wind_s = 0;
	rad = 0;
	PORTD = 0x0E0; // enable devices
}

void Wind(int toggle) {
	if (toggle == 1) {
		PORTD |= 0x20;
	} else {
		PORTD &= (~0x20);
	}
}
void Radiation(int toggle) {
	if (toggle == 1) {
		PORTD |= 0x40;
	} else {
		PORTD &= (~0x40);
	}
}
void Lightning(int toggle) {
	if (toggle == 1) {
		PORTD |= 0x80;
	} else {
		PORTD &= (~0x80);
	}
}


uint16_t ReadWindSpeed() {

	return windspeed_last10sec;
}

uint16_t ReadRadiation() {
	return rad_last10sec;
}
uint16_t ReadLightning() {
	return lightning_last10sec;
}

uint16_t direction(unsigned char in)
{
	switch(in)
	{
		case 0x01:
		return 0;
		case 0x03:
		return 23;
		case 0x02:
		return 45;
		case 0x06:
		return 68;
		case 0x04:
		return 90;
		case 0x0c:
		return 113;
		case 0x08:
		return 135;
		case 0x18:
		return 158;
		case 0x10:
		return 180;
		case 0x30:
		return 203;
		case 0x20:
		return 225;
		case 0x60:
		return 248;
		case 0x40:
		return 270;
		case 0x0C0:
		return 293;
		case 0x80:
		return 315;
		case 0x81:
		return 338;
	}
	return 0;
}
uint16_t ReadWindDirection() {
	int dir=PIND&0x03;
	dir|=PINB<<2;
	return direction(dir);
}

ISR(INT0_vect)
{
	if(wind_s==0x0FFFF)
	{
		wind_s=0;
	}
	else
	{
		wind_s++;
	}
}

ISR(INT1_vect)
{
	if(rad==0x0FFFF)
	{
		rad=0;
	}
	else
	{
		rad++;
	}
}

