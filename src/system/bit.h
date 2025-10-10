#pragma once

#ifndef SRC_SYSTEM_BIT_H_
#define SRC_SYSTEM_BIT_H_

class Bits{

public:

	Bits(int);
	~Bits();

	void setBit(int, bool);
	bool isActive(int);

	const char* toString();
	int size();

	Bits operator + (Bits bits);

private:

	int sizeBuffer;
	char* buffer;

	static int calculateSize(int);
	void merge(Bits& bits, int& position);

};



#endif
