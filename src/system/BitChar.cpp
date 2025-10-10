#include "BitChar.h"
#include "bit.h"

jlong Java_com_apjc_loculus_BitChar_create(JNIEnv*, jobject, jint size){
	Bits* bits = new Bits(size);
	return (jlong)bits;
}

void Java_com_apjc_loculus_BitChar_delete(JNIEnv* env, jobject obj){
	Bits* bits = importField<Bits>(env, obj, "bitID");
	delete bits;
}

void Java_com_apjc_loculus_BitChar_setBit(JNIEnv* env, jobject obj, jint index, jboolean isActive){
	Bits* bits = importField<Bits>(env, obj, "bitID");
	bits->setBit(index, isActive);
}

jboolean Java_com_apjc_loculus_BitChar_isActive(JNIEnv* env, jobject obj, jint index){
	Bits* bits = importField<Bits>(env, obj, "bitID");
	return bits->isActive(index);
}

jstring Java_com_apjc_loculus_BitChar_getCode(JNIEnv* env, jobject obj){
	Bits* bits = importField<Bits>(env, obj, "bitID");
	return env->NewStringUTF(bits->toString());
}

jint Java_com_apjc_loculus_BitChar_size(JNIEnv* env, jobject obj){
	Bits* bits = importField<Bits>(env, obj, "bitID");
	return bits->size();
}

jlong Java_com_apjc_loculus_BitChar_merge(JNIEnv*, jobject, jlong a, jlong b){
	Bits* bitsA = reinterpret_cast<Bits*>(a);
	Bits* bitsB = reinterpret_cast<Bits*>(b);
	Bits* newBits = new Bits(*bitsA + *bitsB);
	return (jlong)newBits;
}
