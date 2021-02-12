package com.eaglesakura.armyknife.android.extensions

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

/**
 * LiveData factory for some use case.
 */
@Suppress("unused")
@Deprecated("https://github.com/eaglesakura/livedata-factory")
object LiveDataFactory {

    /**
     * Context LiveData from Fragment.
     * when the Fragment will destroyed, then set null.
     *
     * @see Fragment.contextInto
     */
    fun contextFrom(fragment: Fragment): LiveData<Context> {
        return MutableLiveData<Context>()
            .also { fragment.contextInto(it) }
    }

    /**
     * Context LiveData from Activity.
     * when the Activity will destroyed, then set null.
     *
     * @see FragmentActivity.contextInto
     */
    fun contextFrom(activity: FragmentActivity): LiveData<Context> {
        return MutableLiveData<Context>()
            .also { activity.contextInto(it) }
    }

    /**
     * Live data write filter.
     * when `source` data on updated, then check filter function.
     * If it returns `true`, then write dst live data.
     *
     * e.g.)
     *
     * val origin = MutableLiveData<String>()
     * val secureUrl = LiveDataFactory.filter(origin) { it.startsWith("https://") }
     * assertNotEquals(origin, secureUrl)
     *
     * origin.value = "http://example.com"
     * assertNull(secureUrl.value)
     *
     * origin.value = "https://example.com"
     * assertEquals("https://example.com", secureUrl.value)
     *
     * @param source original LiveData
     * @param filter if write dst value, then return true.
     */
    @Deprecated("use LiveData.where {}")
    fun <T> filter(source: LiveData<T>, filter: (value: T?) -> Boolean): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(source) {
                if (filter(it)) {
                    result.value = it
                }
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, T> transformInto(
        a: LiveData<A>,
        inject: (dst: MutableLiveData<T>, a: A) -> Unit
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                inject(result, a.value ?: return@addSource)
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, T> transformNullableInto(
        a: LiveData<A>,
        inject: (dst: MutableLiveData<T>, a: A?) -> Unit
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                inject(result, a.value)
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, B, T> transformInto(
        a: LiveData<A>,
        b: LiveData<B>,
        inject: (dst: MutableLiveData<T>, a: A, b: B) -> Unit
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource
                )
            }
            result.addSource(b) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource
                )
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, B, T> transformNullableInto(
        a: LiveData<A>,
        b: LiveData<B>,
        inject: (dst: MutableLiveData<T>, a: A?, b: B?) -> Unit
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                inject(
                    result,
                    a.value,
                    b.value
                )
            }
            result.addSource(b) {
                inject(
                    result,
                    a.value,
                    b.value
                )
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, B, C, T> transformInto(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        inject: (dst: MutableLiveData<T>, a: A, b: B, c: C) -> Unit
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource
                )
            }
            result.addSource(b) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource
                )
            }
            result.addSource(c) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource
                )
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, B, C, T> transformNullableInto(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        inject: (dst: MutableLiveData<T>, a: A?, b: B?, c: C?) -> Unit
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value
                )
            }
            result.addSource(b) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value
                )
            }
            result.addSource(c) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value
                )
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, B, C, D, T> transformInto(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        d: LiveData<D>,
        inject: (dst: MutableLiveData<T>, a: A, b: B, c: C, d: D) -> Unit
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource
                )
            }
            result.addSource(b) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource
                )
            }
            result.addSource(c) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource
                )
            }
            result.addSource(d) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource
                )
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, B, C, D, T> transformNullableInto(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        d: LiveData<D>,
        inject: (dst: MutableLiveData<T>, a: A?, b: B?, c: C?, d: D?) -> Unit
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value,
                    d.value
                )
            }
            result.addSource(b) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value,
                    d.value
                )
            }
            result.addSource(c) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value,
                    d.value
                )
            }
            result.addSource(d) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value,
                    d.value
                )
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, B, C, D, E, T> transformInto(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        d: LiveData<D>,
        e: LiveData<E>,
        inject: (dst: MutableLiveData<T>, a: A, b: B, c: C, d: D, e: E) -> Unit
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource
                )
            }
            result.addSource(b) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource
                )
            }
            result.addSource(c) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource
                )
            }
            result.addSource(d) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource
                )
            }
            result.addSource(e) {
                inject(
                    result,
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource
                )
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, B, C, D, E, T> transformNullableInto(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        d: LiveData<D>,
        e: LiveData<E>,
        inject: (dst: MutableLiveData<T>, a: A?, b: B?, c: C?, d: D?, e: E?) -> Unit
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value
                )
            }
            result.addSource(b) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value
                )
            }
            result.addSource(c) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value
                )
            }
            result.addSource(d) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value
                )
            }
            result.addSource(e) {
                inject(
                    result,
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value
                )
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, T> transform(
        a: LiveData<A>,
        mapper: (a: A) -> T
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(a.value ?: return@addSource)
            }
        }
    }

    /**
     * 1 live data to 1 data.
     */
    fun <A, T> transformNullable(
        a: LiveData<A>,
        mapper: (a: A?) -> T?
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(a.value)
            }
        }
    }

    /**
     * 2 live data to 1 data.
     */
    fun <A, B, T> transformNullable(
        a: LiveData<A>,
        b: LiveData<B>,
        mapper: (a: A?, b: B?) -> T?
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(a.value, b.value)
            }
            result.addSource(b) {
                result.value = mapper(a.value, b.value)
            }
        }
    }

    /**
     * 2 live data to 1 data.
     */
    fun <A, B, T> transform(
        a: LiveData<A>,
        b: LiveData<B>,
        mapper: (a: A, b: B) -> T?
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(a.value ?: return@addSource, b.value ?: return@addSource)
            }
            result.addSource(b) {
                result.value = mapper(a.value ?: return@addSource, b.value ?: return@addSource)
            }
        }
    }

    /**
     * 3 live data to 1 data.
     */
    fun <A, B, C, T> transformNullable(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        mapper: (a: A?, b: B?, c: C?) -> T?
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value
                )
            }
            result.addSource(b) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value
                )
            }
            result.addSource(c) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value
                )
            }
        }
    }

    /**
     * 3 live data to 1 data.
     */
    fun <A, B, C, T> transform(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        mapper: (a: A, b: B, c: C) -> T
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource
                )
            }
            result.addSource(b) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource
                )
            }
            result.addSource(c) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource
                )
            }
        }
    }

    /**
     * 4 live data to 1 data.
     */
    fun <A, B, C, D, T> transformNullable(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        d: LiveData<D>,
        mapper: (a: A?, b: B?, c: C?, d: D?) -> T?
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value
                )
            }
            result.addSource(b) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value
                )
            }
            result.addSource(c) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value
                )
            }
            result.addSource(d) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value
                )
            }
        }
    }

    /**
     * 4 live data to 1 data.
     */
    fun <A, B, C, D, T> transform(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        d: LiveData<D>,
        mapper: (a: A, b: B, c: C, d: D) -> T
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource
                )
            }
            result.addSource(b) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource
                )
            }
            result.addSource(c) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource
                )
            }
            result.addSource(d) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource
                )
            }
        }
    }

    /**
     * 5 live data to 1 data.
     */
    fun <A, B, C, D, E, T> transformNullable(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        d: LiveData<D>,
        e: LiveData<E>,
        mapper: (a: A?, b: B?, c: C?, d: D?, e: E?) -> T?
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value
                )
            }
            result.addSource(b) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value
                )
            }
            result.addSource(c) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value
                )
            }
            result.addSource(d) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value
                )
            }
            result.addSource(e) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value
                )
            }
        }
    }

    /**
     * 5 live data to 1 data.
     */
    fun <A, B, C, D, E, T> transform(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        d: LiveData<D>,
        e: LiveData<E>,
        mapper: (a: A, b: B, c: C, d: D, e: E) -> T
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource
                )
            }
            result.addSource(b) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource
                )
            }
            result.addSource(c) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource
                )
            }
            result.addSource(d) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource
                )
            }
            result.addSource(e) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource
                )
            }
        }
    }

    /**
     * 6 live data to 1 data.
     */
    fun <A, B, C, D, E, F, T> transformNullable(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        d: LiveData<D>,
        e: LiveData<E>,
        f: LiveData<F>,
        mapper: (a: A?, b: B?, c: C?, d: D?, e: E?, f: F?) -> T?
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value,
                    f.value
                )
            }
            result.addSource(b) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value,
                    f.value
                )
            }
            result.addSource(c) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value,
                    f.value
                )
            }
            result.addSource(d) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value,
                    f.value
                )
            }
            result.addSource(e) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value,
                    f.value
                )
            }
            result.addSource(f) {
                result.value = mapper(
                    a.value,
                    b.value,
                    c.value,
                    d.value,
                    e.value,
                    f.value
                )
            }
        }
    }

    /**
     * 6 live data to 1 data.
     */
    fun <A, B, C, D, E, F, T> transform(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>,
        d: LiveData<D>,
        e: LiveData<E>,
        f: LiveData<F>,
        mapper: (a: A, b: B, c: C, d: D, e: E, f: F) -> T
    ): MutableLiveData<T> {
        return MediatorLiveData<T>().also { result ->
            result.addSource(a) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource,
                    f.value ?: return@addSource
                )
            }
            result.addSource(b) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource,
                    f.value ?: return@addSource
                )
            }
            result.addSource(c) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource,
                    f.value ?: return@addSource
                )
            }
            result.addSource(d) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource,
                    f.value ?: return@addSource
                )
            }
            result.addSource(e) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource,
                    f.value ?: return@addSource
                )
            }
            result.addSource(f) {
                result.value = mapper(
                    a.value ?: return@addSource,
                    b.value ?: return@addSource,
                    c.value ?: return@addSource,
                    d.value ?: return@addSource,
                    e.value ?: return@addSource,
                    f.value ?: return@addSource
                )
            }
        }
    }
}
