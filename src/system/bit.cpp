#include "bit.h"

Bits::Bits(int size) : sizeBuffer{size}, buffer{new char[calculateSize(size)]}{}

Bits::~Bits(){
	delete [] buffer;
}

int Bits::calculateSize(int size){
	int i = size / 8;
	if (i % 8 > 0){
		i++;
	}
	return i;
}

void Bits::setBit(int index, bool isActive){
	int position = index / 8;
	int bit = index % 8;
	if(isActive){
		buffer[position] |= (1 << bit);
	}else{
		buffer[position] &= ~(1 << bit);
	}
}

bool Bits::isActive(int index){
	int position = index / 8;
	int bit = index % 8;
	return (buffer[position] & (1 << bit)) != 0;
}

const char* Bits::toString(){
	return buffer;
}

int Bits::size(){
	return sizeBuffer;
}

void Bits::merge(Bits& bits, int position){
	for(int i = 0; i < this->sizeBuffer; i++){
		bits.setBit(position + i, this->isActive(i));
	}
}

Bits Bits::operator +(Bits& bits){
	Bits b(this->sizeBuffer + bits.sizeBuffer);
	this->merge(b, 0);
	bits.merge(b, this->sizeBuffer);
	return b;
}
