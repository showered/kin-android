package org.kin.sdk.base.models

import org.kin.stellarfork.KeyPair

data class KinAccount @JvmOverloads constructor(
    val key: Key,
    val id: Id = Id(
        key.asPublicKey().value
    ),
    val balance: KinBalance = KinBalance(),
    val status: Status = Status.Unregistered
) {
    data class Id(val value: ByteArray) {

        constructor(accountIdString: String) : this(KeyPair.fromAccountId(accountIdString).publicKey)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Id

            if (!value.contentEquals(other.value)) return false

            return true
        }

        override fun hashCode(): Int {
            return value.contentHashCode()
        }

        override fun toString(): String {
            return "Id(value=${encodeAsString()})"
        }

        fun encodeAsString(): String = toKeyPair().accountId
    }

    sealed class Status(val value: Int) {
        object Unregistered : Status(0)
        data class Registered(val sequence: Long) : Status(1)
    }
}

fun KinAccount.merge(account: KinAccount): KinAccount {
    return KinAccount(key, id, account.balance, account.status)
}
