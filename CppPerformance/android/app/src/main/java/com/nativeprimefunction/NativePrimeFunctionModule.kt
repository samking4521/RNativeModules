package com.nativeprimefunction

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext

import kotlin.math.sqrt

class NativePrimeFunctionModule(reactContext: ReactApplicationContext) : NativePrimeFunctionSpec(reactContext){

    override fun getName() = NAME

    private fun computePrimes(maxN: Double): List<Int> {
        // JS number → Kotlin Double → convert ONCE
        val n = maxN.toInt()

        // Boolean array: index = number, value = isPrime
        val isPrime = BooleanArray(n + 1) { true }

        // 0 and 1 are not prime
        if (n >= 0) isPrime[0] = false
        if (n >= 1) isPrime[1] = false

        val limit = sqrt(n.toDouble()).toInt()

        for (i in 2..limit) {
            if (isPrime[i]) {
                var j = i * i
                while (j <= n) {
                    isPrime[j] = false
                    j += i
                }
            }
        }

        val primes = mutableListOf<Int>()
        for (i in 2..n) {
            if (isPrime[i]) {
                primes.add(i)
            }
        }

        return primes
    }

    override fun getPrimeNumbers(value: Double, promise: Promise?) {
        try{
            val startTime = System.currentTimeMillis()
            val primeList = computePrimes(value)
            val endTime = System.currentTimeMillis()

            val timeTaken = endTime - startTime

            val map = Arguments.createMap()
            map.putString("language", "Kotlin")
            map.putInt("value", primeList.size)
            map.putInt("time", timeTaken.toInt())

            // Resolve the promise so JS receives the object
            promise?.resolve(map)

        }catch (e: Exception){
            // Reject the promise if something goes wrong
            promise?.reject("PRIME_ERROR", e)
        }
    }

    companion object {
        const val NAME = "NativePrimeFunction"
    }

}