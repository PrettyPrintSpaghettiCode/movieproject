#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>
#include "sort.h"

// Logging macro
#define LOGV(fmt, ...) ((void)__android_log_print(ANDROID_LOG_VERBOSE, "LOG_TAG", fmt, ## __VA_ARGS__))

// Function declarations
float getRating(JNIEnv *env, jobject json);

// Global variables
static jmethodID getsMID;
static jstring titleKey;
static jstring ratingKey;


JNIEXPORT void JNICALL
Java_com_thalesgroup_restlib_TmdbRestApi_sortArray(JNIEnv *env, jobject thiz, jobjectArray jsonArr) {

    // get the Class object for JSONObject
    jclass JSONObjectClass = (*env)->FindClass(env, "org/json/JSONObject");

    // get the ID of the "getString" method
    getsMID = (*env)->GetMethodID(env, JSONObjectClass, "getString",
                                  "(Ljava/lang/String;)Ljava/lang/String;");
    if (getsMID == NULL) return;

    // create java.lang.String instances for the key to pass to "getString" method
    ratingKey = (*env)->NewStringUTF(env, "vote_average");
    if (ratingKey == NULL) return;

    // create temporary buffer
    jmethodID constructorID = (*env)->GetMethodID(env, JSONObjectClass, "<init>", "()V");
    jobject tempObj = (*env)->NewObject(env, JSONObjectClass, constructorID);

    // do bubble sort
    jsize length = (*env)->GetArrayLength(env, jsonArr);
    for (int i = 0; i < length - 1; i++) {
        for(int j = 0; j < length - i - 1; j++) {

            // get json object of this element
            jobject json1 = (*env)->GetObjectArrayElement(env, jsonArr, j);
            if(json1 == NULL) return;
            // get json object of next element
            jobject json2 = (*env)->GetObjectArrayElement(env, jsonArr, j+1);
            if(json2 == NULL) return;
            // if this element has lower rating, move to next element position
            if (getRating(env, json1) < getRating(env, json2)) {
                tempObj = (*env)->GetObjectArrayElement(env, jsonArr, j);
                (*env)->SetObjectArrayElement(env, jsonArr, j, json2);
                (*env)->SetObjectArrayElement(env, jsonArr, j+1, tempObj);
            }
        }
    }

    return;
}

float getRating(JNIEnv *env, jobject json) {
    // get the rating value
    jstring rating = (*env)->CallObjectMethod(env, json, getsMID, ratingKey);
    if (rating == NULL) return 0;

    // get a C string that can be used with the usual C functions
    const char *rating_cstr = (*env)->GetStringUTFChars(env, rating, NULL);
    if (rating_cstr == NULL) return 0;

    // convert C string to float
    float rating_cfl = strtof(rating_cstr, NULL);
//    LOGV("rating = %f\n", rating_cfl);

    // release allocated string buffer
    (*env)->ReleaseStringUTFChars(env, rating, rating_cstr);
    rating_cstr = NULL;

    return rating_cfl;
}