package com.eg.app.util

import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 * 工具类  安全加密
 */
class MmkvUtil(context: Context) {

    companion object {
        private var mInstance: MmkvUtil? = null
        private lateinit var mv: MMKV

        /**
         * 初始化MMKV,只需要初始化一次，建议在Application中初始化
         */
        fun getInstance(context: Context): MmkvUtil? {
            if (mInstance == null) {
                synchronized(MmkvUtil::class.java) {
                    if (mInstance == null) {
                        mInstance = MmkvUtil(context)
                    }
                }
            }
            return mInstance
        }

        /**
         * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
         *
         * @param key
         * @param object
         */
        fun encode(key: String, value: Any) {
            if (value is String) {
                mv.encode(key, value)
            } else if (value is Int) {
                mv.encode(key, value)
            } else if (value is Boolean) {
                mv.encode(key, value)
            } else if (value is Float) {
                mv.encode(key, value)
            } else if (value is Long) {
                mv.encode(key, value)
            } else if (value is Double) {
                mv.encode(key, value)
            } else if (value is ByteArray) {
                mv.encode(key, value)
            } else {
                mv.encode(key, value.toString())
            }
        }

        fun encodeSet(key: String, sets: Set<String?>?) {
            mv.encode(key, sets)
        }

        fun encodeParcelable(key: String?, obj: Parcelable?) {
            mv.encode(key, obj)
        }

        /**
         * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
         */
        fun decodeInt(key: String, defaultValue: Int = 0): Int {
            return mv.decodeInt(key, defaultValue)
        }

        fun decodeDouble(key: String): Double {
            return mv.decodeDouble(key, 0.00)
        }

        fun decodeLong(key: String): Long {
            return mv.decodeLong(key, 0L)
        }

        fun decodeBoolean(key: String, defaultValue: Boolean = false): Boolean {
            return mv.decodeBool(key, defaultValue)
        }

        fun decodeFloat(key: String): Float {
            return mv.decodeFloat(key, 0f)
        }

        fun decodeBytes(key: String): ByteArray {
            return mv.decodeBytes(key)!!
        }

        fun decodeString(key: String, defaultValue: String = ""): String {
            return mv.decodeString(key, defaultValue)!!
        }

        fun decodeStringSet(key: String, defaultValue: Set<String> = emptySet()): Set<String> {
            return mv.decodeStringSet(key, defaultValue)!!
        }

        fun decodeParcelable(key: String): Parcelable {
            return mv.decodeParcelable(key, null)!!
        }

        /**
         * 移除某个key对
         *
         * @param key
         */
        fun removeKey(key: String) {
            mv.removeValueForKey(key)
        }

        /**
         * 清除所有key
         */
        fun clearAll() {
            mv.clearAll()
        }
    }

    init {
        MMKV.initialize(context)
        mv = MMKV.defaultMMKV()
    }
}