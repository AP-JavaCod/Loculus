#pragma once
#include <JNI.h>
#include <exception>

#ifndef JSYSTEM_H_
#define JSYSTEM_H_

template <typename T>
T* convertField(jlong field){
	return field > 0 ? reinterpret_cast<T*>(field) : nullptr;
}

template <typename T>
T* importField(JNIEnv* env, jobject obj, const char* nameField){
	jclass classJ = env->GetObjectClass(obj);
	jfieldID id = env->GetFieldID(classJ, nameField, "J");
	jlong b = env->GetLongField(obj, id);
	return convertField<T>(b);
}

#endif
