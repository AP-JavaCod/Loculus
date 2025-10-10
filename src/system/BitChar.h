#pragma once
#include "jsystem.h"

#ifndef _Included_com_apjc_loculus_BitChar
#define _Included_com_apjc_loculus_BitChar
#ifdef __cplusplus
extern "C" {
#endif

	JNIEXPORT jlong JNICALL Java_com_apjc_loculus_BitChar_create(JNIEnv*, jobject, jint);
	JNIEXPORT void JNICALL Java_com_apjc_loculus_BitChar_delete(JNIEnv*, jobject);
	JNIEXPORT void JNICALL Java_com_apjc_loculus_BitChar_setBit(JNIEnv*, jobject, jint, jboolean);
	JNIEXPORT jboolean JNICALL Java_com_apjc_loculus_BitChar_isActive(JNIEnv*, jobject, jint);
	JNIEXPORT jstring JNICALL Java_com_apjc_loculus_BitChar_getCode(JNIEnv*, jobject);
	JNIEXPORT jint JNICALL Java_com_apjc_loculus_BitChar_size(JNIEnv*, jobject);
	JNIEXPORT jlong JNICALL Java_com_apjc_loculus_BitChar_merge(JNIEnv*, jobject, jlong, jlong);

#ifdef __cplusplus
}
#endif
#endif
