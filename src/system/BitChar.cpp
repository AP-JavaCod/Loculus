#include "BitChar.h"
#include "bit.h"

jlong Java_com_apjc_loculus_BitChar_create(JNIEnv*, jobject, jint size){
	Bits* bits = new Bits(size);
	return reinterpret_cast<jlong>(bits);
}

void Java_com_apjc_loculus_BitChar_delete(JNIEnv* env, jobject obj){
	Bits* bits = importField<Bits>(env, obj, "bitID");
	delete bits;
}

void Java_com_apjc_loculus_BitChar_setBit(JNIEnv* env, jobject obj, jint index, jboolean isActive){
	Bits* bits = importField<Bits>(env, obj, "bitID");
	if(bits != nullptr){
		bits->setBit(index, isActive);
	}
}

jboolean Java_com_apjc_loculus_BitChar_isActive(JNIEnv* env, jobject obj, jint index){
	Bits* bits = importField<Bits>(env, obj, "bitID");
	return bits != nullptr && bits->isActive(index);
}

jstring Java_com_apjc_loculus_BitChar_getCode(JNIEnv* env, jobject obj){
	Bits* bits = importField<Bits>(env, obj, "bitID");
	return bits != nullptr ? env->NewStringUTF(bits->toString()) : env->NewStringUTF("");
}

jint Java_com_apjc_loculus_BitChar_size(JNIEnv* env, jobject obj){
	Bits* bits = importField<Bits>(env, obj, "bitID");
	return bits != nullptr ? bits->size() : -1;
}

jlong Java_com_apjc_loculus_BitChar_merge(JNIEnv* env, jobject, jlong a, jlong b){
	Bits* bitsA = convertField<Bits>(a);
	Bits* bitsB = convertField<Bits>(b);
	Bits* newBits = new Bits(*bitsA + *bitsB);
	return reinterpret_cast<jlong>(newBits);
}
