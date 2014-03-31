#include "vibration.h"
#include "measure.h"

unsigned char regmap[REG_LENGTH];

void init_i2c() {
	// resetting ports to default

	DDRB = BDIR;
	DDRC = CDIR;
	DDRD = DDIR;
	PORTB = BINIT;
	PORTC = CINIT;
	PORTD = DINIT;

	TWI_Slave_Initialise((SLAVE_ADDR << 1) | (FALSE << TWI_GEN_BIT)); // initialize TWI interface with slave_addr
}

/*void SendDataBack(uint16_t data) {
	unsigned char messagebuf[2];

	uint8_t lsb = (uint8_t)data;
	uint8_t msb = data >> 8;
	messagebuf[0] = lsb;
	messagebuf[1] = msb;
	TWI_Start_Transceiver_With_Data(messagebuf,2); // sending the reply
}*/

void SendDataBack(unsigned char data) {
	unsigned char messagebuf[2];
	if (data != 0xaa) {
		messagebuf[0] = regmap[data];
	} else {
		messagebuf[0] = 0xAA;
	}
	TWI_Start_Transceiver_With_Data(messagebuf, 1); // sending the reply
}

int main(void) {

	init_i2c();
	unsigned char messagebuf[8];  // message buffer to receive commands

	sei();
	// allow interrupts
	TWI_Start_Transceiver(); // start up the communications
	//init_systimer();
	initMeasure();
	do {
		if (!TWI_Transceiver_Busy()) {
			if (TWI_statusReg.RxDataInBuf) { // there is data in the receive buff and last transaction was okay

				TWI_Get_Data_From_Transceiver(messagebuf, 2); // fetch the data to messagebuff
				switch (messagebuf[0]) {
				case TEST_BYTE:
					SendDataBack(0xAA);
					break;
				case SWITCH_OFF:
					setMeasure(0);
					break;
				case SWITCH_ON:
					setMeasure(1);
					break;
				default:
					SendDataBack(messagebuf[0]);

				}
			}
			if (!TWI_Transceiver_Busy()) {
				TWI_Start_Transceiver(); // restart the transceiver
			}
		}
	} while (1); // infinite loop
	return 1;
}
