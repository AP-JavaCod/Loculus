#pragma once
#include "JNI.h"

#ifndef JSYSTEM_H_
#define JSYSTEM_H_

template <typename T>
T* importField(JNIEnv* env, jobject obj, const char* nameField){
	jclass classJ = env->GetObjectClass(obj);
	jfieldID id = env->GetFieldID(classJ, nameField, "J");
	jlong b = env->GetLongField(obj, id);
	return reinterpret_cast<T*>(b);
}



#endif
