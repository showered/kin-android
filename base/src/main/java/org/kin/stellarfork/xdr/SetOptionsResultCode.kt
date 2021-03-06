// Automatically generated by xdrgen 
// DO NOT EDIT or your changes may be overwritten
package org.kin.stellarfork.xdr

import java.io.IOException

// === xdr source ============================================================
//  enum SetOptionsResultCode
//  {
//      // codes considered as "success" for the operation
//      SET_OPTIONS_SUCCESS = 0,
//      // codes considered as "failure" for the operation
//      SET_OPTIONS_LOW_RESERVE = -1,      // not enough funds to add a signer
//      SET_OPTIONS_TOO_MANY_SIGNERS = -2, // max number of signers already reached
//      SET_OPTIONS_BAD_FLAGS = -3,        // invalid combination of clear/set flags
//      SET_OPTIONS_INVALID_INFLATION = -4,      // inflation account does not exist
//      SET_OPTIONS_CANT_CHANGE = -5,            // can no longer change this option
//      SET_OPTIONS_UNKNOWN_FLAG = -6,           // can't set an unknown flag
//      SET_OPTIONS_THRESHOLD_OUT_OF_RANGE = -7, // bad value for weight/threshold
//      SET_OPTIONS_BAD_SIGNER = -8,             // signer cannot be masterkey
//      SET_OPTIONS_INVALID_HOME_DOMAIN = -9     // malformed home domain
//  };
//  ===========================================================================
enum class SetOptionsResultCode(val value: Int) {
    SET_OPTIONS_SUCCESS(0),
    SET_OPTIONS_LOW_RESERVE(-1),
    SET_OPTIONS_TOO_MANY_SIGNERS(-2),
    SET_OPTIONS_BAD_FLAGS(-3),
    SET_OPTIONS_INVALID_INFLATION(-4),
    SET_OPTIONS_CANT_CHANGE(-5),
    SET_OPTIONS_UNKNOWN_FLAG(-6),
    SET_OPTIONS_THRESHOLD_OUT_OF_RANGE(-7),
    SET_OPTIONS_BAD_SIGNER(-8),
    SET_OPTIONS_INVALID_HOME_DOMAIN(-9);

    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun decode(stream: XdrDataInputStream): SetOptionsResultCode {
            val value = stream.readInt()
            return when (value) {
                0 -> SET_OPTIONS_SUCCESS
                -1 -> SET_OPTIONS_LOW_RESERVE
                -2 -> SET_OPTIONS_TOO_MANY_SIGNERS
                -3 -> SET_OPTIONS_BAD_FLAGS
                -4 -> SET_OPTIONS_INVALID_INFLATION
                -5 -> SET_OPTIONS_CANT_CHANGE
                -6 -> SET_OPTIONS_UNKNOWN_FLAG
                -7 -> SET_OPTIONS_THRESHOLD_OUT_OF_RANGE
                -8 -> SET_OPTIONS_BAD_SIGNER
                -9 -> SET_OPTIONS_INVALID_HOME_DOMAIN
                else -> throw RuntimeException("Unknown enum value: $value")
            }
        }

        @JvmStatic
        @Throws(IOException::class)
        fun encode(stream: XdrDataOutputStream, value: SetOptionsResultCode) {
            stream.writeInt(value.value)
        }
    }
}
