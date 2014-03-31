#include "wind.h"


#define COUNT_N  0x0E4E1
#define COUNT_L  0x0E4E7

unsigned char systime;

void init_systimer(void)
{
	TCCR1A = 0x00;	// Normal(not PWM) mode
	systime	= 0;
	OCR1A = COUNT_N; //first count
	TIMSK &= ~0x3C;
	TIMSK |= (1<<OCIE1A);
	TCCR1B = 0x0C;	// CTC, /256 prescaler
}


ISR(TIMER1_COMPA_vect)
{
	if(systime<6)
	{
		systime++;
	}
	else
	if(systime==6)
	{
		OCR1A = COUNT_L; //last count
		systime++;
	}
	else
	{
		OCR1A = COUNT_N;	//first count
		systime=0;
		measure10sec();		//fire!!!!
	}

}


void init_i2c() {
	// resetting ports to default

	DDRB = BDIR;
	DDRC = CDIR;
	DDRD = DDIR;
	PORTB = BINIT;
	PORTC = CINIT;
	PORTD = DINIT;

	TWI_Slave_Initialise( (SLAVE_ADDR<<1) | (FALSE<<TWI_GEN_BIT) ); // initialize TWI interface with slave_addr
}


void SendDataBack(uint16_t data) {
	unsigned char messagebuf[2];
	uint8_t lsb = (uint8_t)data;
	uint8_t msb = data >> 8;
	messagebuf[0] = lsb;
	messagebuf[1] = msb;
	TWI_Start_Transceiver_With_Data(messagebuf,2); // sending the reply
}
int main(void)
{

	init_i2c();
	unsigned char messagebuf[8];  // message buffer to receive commands

	sei(); // allow interrupts
	TWI_Start_Transceiver(); // start up the communications
	init_systimer();
	initMeasure();
	do {
		if (!TWI_Transceiver_Busy()) {
			if ( TWI_statusReg.RxDataInBuf) { // there is data in the receive buff and last transaction was okay

				TWI_Get_Data_From_Transceiver(messagebuf, 2); // fetch the data to messagebuff
				switch(messagebuf[0]) {
					case TEST_BYTE: SendDataBack(0xAA);break;
					case GET_WINDSPEED: SendDataBack(ReadWindSpeed()); break;
					case GET_RADIATION: SendDataBack(ReadRadiation()); break;
					case GET_WINDDIRECTION: SendDataBack(ReadWindDirection());break;
					case GET_LIGHTNING: SendDataBack(ReadLightning());break;
					case WIND_ON: Wind(1);break;
					case WIND_OFF: Wind(0);break;
					case RADIATION_ON: Radiation(1);break;
					case RADIATION_OFF: Radiation(0);break;
					case LIGHTNING_ON: Lightning(1);break;
					case LIGHTNING_OFF: Lightning(0);break;
					default: SendDataBack(0xab << 8 | messagebuf[0]);
				}
			}
			if (! TWI_Transceiver_Busy()) {
				TWI_Start_Transceiver(); // restart the transceiver
			}
		}
	} while(1); // infinite loop
	return 1;
}
