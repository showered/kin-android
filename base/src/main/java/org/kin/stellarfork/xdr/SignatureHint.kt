// Automatically generated by xdrgen 
// DO NOT EDIT or your changes may be overwritten
package org.kin.stellarfork.xdr

import java.io.IOException

// === xdr source ============================================================
//  typedef opaque SignatureHint[4];
//  ===========================================================================
class SignatureHint {
    var signatureHint: ByteArray? = null

    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun encode(stream: XdrDataOutputStream, encodedSignatureHint: SignatureHint) {
            val SignatureHintsize = encodedSignatureHint.signatureHint!!.size
            stream.write(encodedSignatureHint.signatureHint!!, 0, SignatureHintsize)
        }

        @JvmStatic
        @Throws(IOException::class)
        fun decode(stream: XdrDataInputStream): SignatureHint {
            val decodedSignatureHint = SignatureHint()
            val SignatureHintsize = 4
            decodedSignatureHint.signatureHint = ByteArray(SignatureHintsize)
            stream.read(decodedSignatureHint.signatureHint!!, 0, SignatureHintsize)
            return decodedSignatureHint
        }
    }
}