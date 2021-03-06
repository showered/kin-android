package org.kin.sdk.base.tools

import java.util.concurrent.CopyOnWriteArrayList

interface Disposable<T> {
    fun dispose()
    fun disposedBy(disposeBag: DisposeBag): Observer<T>
    fun doOnDisposed(onDisposed: () -> Unit): Observer<T>
}

class DisposeBag {
    private val lock = Any()
    private val disposables = mutableListOf<Disposable<*>>()

    fun add(disposable: Disposable<*>) {
        synchronized(lock) {
            disposables.add(disposable)
        }
    }

    fun dispose() {
        synchronized(lock) {
            disposables.apply {
                forEach {
                    it.dispose()
                }
                clear()
            }
        }
    }
}

interface Observer<T> : Disposable<T> {
    fun add(listener: (T) -> Unit): Observer<T>
    fun remove(listener: (T) -> Unit): Observer<T>
    fun listenerCount(): Int
    fun requestInvalidation(): Observer<T>
    fun <V> mapPromise(map: (T) -> V): Promise<V>
    fun <V> flatMapPromise(promise: (T) -> Promise<V>): Promise<V>
}

/**
 * [onCompleted] to be called when callback is complete with
 * _either_ a non null value or an error but never both.
 */
interface Callback<T> : Function<T> {
    fun onCompleted(value: T? = null, error: Throwable? = null)
}

/**
 * May call [onNext] or [onError] in a sequence of value updates.
 * Should not emit onNext updates after an onError event.
 */
interface ValueListener<T> {
    fun onNext(value: T)
    fun onError(error: Throwable)
}

fun <T> Promise<T>.callback(callback: Callback<T>) {
    then({ callback.onCompleted(it, null) }, { callback.onCompleted(null, it) })
}

fun <T> Observer<T>.listen(listener: ValueListener<T>): Observer<T> {
    return add { listener.onNext(it) }
}

fun <T> ListObserver<T>.listen(listener: ValueListener<List<T>>): ListObserver<T> {
    return add { listener.onNext(it) }
}

open class ValueSubject<T>(
    private val triggerInvalidation: (() -> Unit)? = null
) : Observer<T> {
    private val listeners = CopyOnWriteArrayList<(T) -> Unit>()
    private var currentValue: T? = null
    private var onDisposed = mutableListOf<() -> Unit>()

    override fun add(listener: (T) -> Unit): Observer<T> {
        listeners.add(listener)
        currentValue?.let { listener(it) }
        return this
    }

    fun onNext(newValue: T) =
        listeners.forEach { it(newValue) }
            .also { currentValue = newValue }

    override fun remove(listener: (T) -> Unit): Observer<T> {
        listeners.remove(listener)
        return this
    }

    override fun listenerCount(): Int = listeners.size

    override fun dispose() = listeners.forEach {
        remove(it)
        onDisposed.forEach { it.invoke() }
        onDisposed.clear()
    }

    override fun disposedBy(disposeBag: DisposeBag): Observer<T> {
        disposeBag.add(this)
        return this
    }

    override fun requestInvalidation(): Observer<T> {
        triggerInvalidation?.invoke()
        return this
    }

    override fun doOnDisposed(onDisposed: () -> Unit): Observer<T> {
        this.onDisposed.add(onDisposed)
        return this
    }

    override fun <V> mapPromise(map: (T) -> V): Promise<V> {
        return Promise.create { resolve, reject ->
            add {
                try {
                    map(it)
                    resolve(map(it))
                } catch (t: Throwable) {
                    reject(t)
                }
            }
        }
    }

    override fun <V> flatMapPromise(promise: (T) -> Promise<V>): Promise<V> {
        return Promise.create { resolve, reject ->
            add { promise(it).then(resolve, reject) }
        }
    }
}

interface ListOperations<T> {
    fun requestNextPage(): ListObserver<T>
    fun requestPreviousPage(): ListObserver<T>
}

interface ListObserver<T> : Observer<List<T>>, ListOperations<T> {
    override fun add(listener: (List<T>) -> Unit): ListObserver<T>
}

class ListSubject<T>(
    private val fetchNextPage: (() -> Unit)? = null,
    private val fetchPreviousPage: (() -> Unit)? = null,
    private val triggerInvalidation: (() -> Unit)? = null
) : ValueSubject<List<T>>(triggerInvalidation), ListObserver<T> {

    override fun add(listener: (List<T>) -> Unit): ListObserver<T> {
        super.add(listener)
        return this
    }

    override fun requestNextPage(): ListObserver<T> {
        fetchNextPage?.let { it() }
        return this
    }

    override fun requestPreviousPage(): ListObserver<T> {
        fetchPreviousPage?.let { it() }
        return this
    }
}
