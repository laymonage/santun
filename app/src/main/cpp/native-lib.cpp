#include <algorithm>
#include <jni.h>
#include <string>

std::string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const auto stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    auto length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, nullptr);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_sage_santun_util_data_Compression_00024Companion_compress(
        JNIEnv *env,
        jobject,
        jstring input
        ) {
    std::string newInput = jstring2string(env, input);
    std::string::iterator end_pos = std::remove(newInput.begin(), newInput.end(), ' ');
    newInput.erase(end_pos, newInput.end());
    end_pos = std::remove(newInput.begin(), newInput.end(), '\n');
    newInput.erase(end_pos, newInput.end());
    return env->NewStringUTF(newInput.c_str());
}

