#include "measure.h"
#include "vibration.h"
#include "reg.h"

unsigned char measure_on;
unsigned char regcount;
unsigned char AD1m;
unsigned char AD2m;
unsigned char AD3m;
unsigned char ADstat;
unsigned char AD1[AMBIENT_COUNT];
unsigned char AD2[AMBIENT_COUNT];
unsigned char AD3[AMBIENT_COUNT];
unsigned char avgCount;
unsigned char ambientAD1;
unsigned char ambientAD2;
unsigned char ambientAD3;
unsigned char AD1max;
unsigned char AD2max;
unsigned char AD3max;
unsigned char T1;
unsigned char T2;
unsigned char first;
unsigned char second;

unsigned char regmap[REG_LENGTH];

unsigned char ReadVibration(int regcount) {
	return regmap[regcount];
}
void initMeasure(void) {

	PORTB = 0x07;	//AD off
	measure_on = 0;
	ADstat = 0;
	regcount = REGSTART;
	avgCount = 0;
	T1 = 0;
	T2 = 0;
	ambientAD1 = 0;
	ambientAD2 = 0;
	ambientAD3 = 0;
	first = 0;
	second = 0;
	AD1m = 0;
	AD2m = 0;
	AD3m = 0;

	TCCR0 = 0x00;	 //timer0 off
	TCNT0 = 0x00;
	TIMSK |= 0x01; //timer0 interrupt enable
	setMeasure(1);
}

void setMeasure(unsigned char st) {
	measure_on = st;
	if (measure_on != 0) {
		readAD();	//mérés indítása
	}

}

void averageAmbient(unsigned char A1, unsigned char A2, unsigned char A3) //nyugalmi szint átlagolás
{
	unsigned int sum1 = 0;
	unsigned int sum2 = 0;
	unsigned int sum3 = 0;
	unsigned char i;

	AD1[avgCount] = A1;
	AD2[avgCount] = A2;
	AD3[avgCount] = A3;

	for (i = 0; i < AMBIENT_COUNT; i++) {
		sum1 += AD1[i];
		sum2 += AD2[i];
		sum3 += AD3[i];
	}

	ambientAD1 = (unsigned char) sum1 / AMBIENT_COUNT;
	ambientAD2 = (unsigned char) sum2 / AMBIENT_COUNT;
	ambientAD3 = (unsigned char) sum3 / AMBIENT_COUNT;

	avgCount++;
	if (avgCount >= AMBIENT_COUNT)
		avgCount = 0;
}
void compare(unsigned char A1, unsigned char A2, unsigned char A3) {
	if (first == 0)	//elsõ rezgés
			{
		if (A1 > (ambientAD1 + HIST))	//egyes érzékelõ
				{
			first = 1;
			AD1max = A1;
		} else if (A2 > (ambientAD2 + HIST))	//kettes érzékelõ
				{
			first = 2;
			AD2max = A2;
		} else if (A3 > (ambientAD3 + HIST))	//kettes érzékelõ
				{
			first = 3;
			AD3max = A3;
		}

	} else if (second == 0)	//második érzékelõre vár
			{
		T1++;
		T2++;
		if (T1 >= TOUT)	//hiba, mérés törlése
		{
			clear();
		}
		switch (first) {
		case 1:
			if (A2 > (ambientAD2 + HIST))	//kettes érzékelõ
					{
				second = 2;
				AD2max = A2;
			} else if (A3 > (ambientAD3 + HIST))	//kettes érzékelõ
					{
				second = 3;
				AD3max = A3;
			}

			break;

		case 2:
			if (A1 > (ambientAD1 + HIST))	//egyes érzékelõ
					{
				second = 1;
				AD1max = A1;
			} else if (A3 > (ambientAD3 + HIST))	//kettes érzékelõ
					{
				second = 3;
				AD3max = A3;
			}

			break;

		case 3:
			if (A1 > (ambientAD1 + HIST))	//egyes érzékelõ
					{
				second = 1;
				AD1max = A1;
			} else if (A2 > (ambientAD2 + HIST))	//kettes érzékelõ
					{
				second = 2;
				AD2max = A2;
			}
			break;

		default:
			clear();
			break;
		}

	} else	//utolsó érzékelõ várás
	{
		T2++;
		if (T1 >= TOUT)	//hiba, mérés törlése
		{
			clear();
		}

		switch (first + second) {
		case 3:	//1+2 3.érzékelõ
			if (A3 > (ambientAD3 + HIST)) {
				AD3max = A3;
				calculate();
			}
			break;

		case 4: //3+1 2.érzékelõ
			if (A2 > (ambientAD2 + HIST)) {
				AD2max = A2;
				calculate();
			}
			break;

		case 5: //2+3 1.érzékelõ
			if (A1 > (ambientAD1 + HIST)) {
				AD1max = A1;
				calculate();
			}
			break;

		default:
			clear();
			break;
		}
	}
}

void clear(void)	//hiba, aktív mérés törlése
{
	first = 0;
	second = 0;
	T1 = 0;
	T2 = 0;
}

void calculate() {
	int arany;	//idõarány
	int avgMax = 0;
	unsigned char szog1;

	arany = (int) ((float) (T1 / T2) * 10);

	switch (arany) {
	case 10:
		szog1 = 0;
		break;
	case 9:
		szog1 = 5;
		break;
	case 8:
		szog1 = 10;
		break;
	case 7:
		szog1 = 20;
		break;
	case 6:
		szog1 = 25;
		break;
	case 5:
		szog1 = 30;
		break;
	case 4:
		szog1 = 35;
		break;
	case 3:
		szog1 = 40;
		break;
	case 2:
		szog1 = 50;
		break;
	case 1:
		szog1 = 55;
		break;
	case 0:
		szog1 = 60;
		break;
	default:
		szog1 = 255;
		break;
	}

	if (szog1 < 100) {

		if (first == 1) {
			if (second == 2) {
			} else if (second == 3) {
				szog1 = 360 - szog1;
			}
		} else if (first == 2) {
			if (second == 1) {
				szog1 = 120 - szog1;
			} else if (second == 3) {
				szog1 += 120;
			}
		} else if (first == 3) {
			if (second == 1) {
				szog1 += 240;
			} else if (second == 2) {
				szog1 = 240 - szog1;
			}
		}

		regmap[regcount] = szog1;
		regcount++;
		avgMax += AD1max;
		avgMax += AD2max;
		avgMax += AD3max;
		regmap[regcount] = (avgMax / 3);
		regcount++;
		if (regcount >= REG_LENGTH)
			regcount = REGSTART;
	}
	clear();
}

void readAD() {

	while (1) {
		if (ADstat == 0)	//olvasás indítása
				{
			PORTB = 0x00;	//minden AD CS aktív
		} else if (ADstat >= 3) {
			if (!PINC & 0x01) {
				if (PIND & 0x01) {
					AD1m = (AD1m << 1) | 1;
				} else {
					AD1m = (AD1m << 1);
				}
				if (PIND & 0x02) {
					AD2m = (AD2m << 1) | 1;
				} else {
					AD2m = (AD2m << 1);
				}
				if (PIND & 0x04) {
					AD3m = (AD3m << 1) | 1;
				} else {
					AD3m = (AD3m << 1);
				}

				PORTC |= 0x01;	//clk Hi
			} else {

				PORTC &= (~0x01);	//clk Low

				if (ADstat >= 18) {
					PORTB = 0x07;	//minden AD CS inaktív
					ADstat = 0;
					TCNT0 = 0x00;
					TCCR0 = 0x05;	//start timer0
					//konverzió********************

					///***********teszt**************

					regmap[regcount] = AD1m;
					regcount++;
					regmap[regcount] = AD2m;
					regcount++;
					regmap[regcount] = AD3m;
					regcount++;
					if (regcount >= REG_LENGTH - 5)
						regcount = REGSTART;
					break;

					///***********teszt**************
				}

			}
		}

		ADstat++;
		_delay_us(1);
	}
}

ISR(TIMER0_OVF_vect) {
	TCCR0 = 0x00;		//timer1 ki
	if (measure_on) {
		//measure_on=0;
		readAD();
	}
}
