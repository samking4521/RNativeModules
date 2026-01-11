package com.cppperformanceapp.nativeprimefunction

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.nativeprimefunction.NativePrimeFunctionSpec
import java.util.BitSet
import kotlin.math.min
import kotlin.math.sqrt

class NativePrimeFunctionModule(reactContext: ReactApplicationContext): NativePrimeFunctionSpec(reactContext) {
    override fun getName() = NAME


//    private fun computePrimes(maxN: Double): Int {
//        // JS number → Kotlin Double → convert ONCE
//        val n = maxN.toInt()
//
//        // Boolean array: index = number, value = isPrime
////        val isPrime = BooleanArray(n + 1) { true }
////
////        // 0 and 1 are not prime
////        if (n >= 0) isPrime[0] = false
////        if (n >= 1) isPrime[1] = false
//
//        val isPrime = BitSet(n + 1)
//        isPrime.set(0, false)
//        isPrime.set(1, false)
//
//
//        val limit = sqrt(n.toDouble()).toInt()
//
//        for (i in 2..limit) {
//            if (isPrime[i]) {
//                var j = i * i
//                while (j <= n) {
//                    isPrime[j] = false
//                    j += i
//                }
//            }
//        }
//
//       var primeCountNo = 0
//        for (i in 2..n) {
//            if (isPrime[i]) {
//                primeCountNo += 1
//            }
//        }
//
//        return primeCountNo
//    }

    /**
     * Compute the number of primes <= maxN
     * Uses BitSet + segmented sieve to reduce memory usage
     */
//    private fun computePrimes(maxN: Double): Int {
//        val n = maxN.toInt()
//        if (n < 2) return 0
//
//        val sqrtN = sqrt(n.toDouble()).toInt()
//
//        // Step 1: Compute small primes up to sqrt(n)
//        val isPrimeSmall = BitSet(sqrtN + 1)
//        isPrimeSmall.set(2, sqrtN + 1) // initialize all to true
//
//        for (i in 2..sqrtN) {
//            if (isPrimeSmall[i]) {
//                var j = i * i
//                while (j <= sqrtN) {
//                    isPrimeSmall.clear(j)
//                    j += i
//                }
//            }
//        }
//
//        // Collect small primes for segmented sieve
//        val smallPrimes = mutableListOf<Int>()
//        for (i in 2..sqrtN) {
//            if (isPrimeSmall[i]) smallPrimes.add(i)
//        }
//
//        // Step 2: Segmented sieve for [2..n]
//        val segmentSize = 1_000_000 // 1M numbers per segment
//        var primeCount = 0
//
//        var low = 2
//        while (low <= n) {
//            val high = min(low + segmentSize - 1, n)
//            val isPrimeSegment = BitSet(high - low + 1)
//            isPrimeSegment.set(0, high - low + 1) // all true initially
//
//            // mark multiples of small primes in this segment
//            for (p in smallPrimes) {
//                var start = ((low + p - 1) / p) * p // first multiple in segment
//                if (start < p * p) start = p * p
//                var j = start
//                while (j <= high) {
//                    isPrimeSegment.clear(j - low)
//                    j += p
//                }
//            }
//
//            // count primes in this segment
//            for (i in 0..(high - low)) {
//                if (isPrimeSegment[i]) primeCount++
//            }
//
//            low += segmentSize
//        }
//
//        return primeCount
//    }

    /**
     * Compute number of primes <= maxN using segmented BooleanArray
     */
  private fun computePrimes(maxN: Double): Int {
        val n = maxN.toInt()
        if (n < 2) return 0

        // Step 1: Compute small primes up to sqrt(n)
        val sqrtN = sqrt(n.toDouble()).toInt()
        val isPrimeSmall = BooleanArray(sqrtN + 1) { true }
        isPrimeSmall[0] = false
        isPrimeSmall[1] = false

        for (i in 2..sqrtN) {
            if (isPrimeSmall[i]) {
                var j = i * i
                while (j <= sqrtN) {
                    isPrimeSmall[j] = false
                    j += i
                }
            }
        }

        // Collect small primes
        val smallPrimes = mutableListOf<Int>()
        for (i in 2..sqrtN) {
            if (isPrimeSmall[i]) smallPrimes.add(i)
        }

        // Step 2: Segmented sieve for [2..n]
        val segmentSize = 1_000_000 // 1M numbers per segment
        var primeCount = 0

        var low = 2
        while (low <= n) {
            val high = min(low + segmentSize - 1, n)
            val isPrimeSegment = BooleanArray(high - low + 1) { true }

            // mark multiples of small primes
            for (p in smallPrimes) {
                var start = ((low + p - 1) / p) * p // first multiple in segment
                if (start < p * p) start = p * p
                var j = start
                while (j <= high) {
                    isPrimeSegment[j - low] = false
                    j += p
                }
            }

            // count primes in this segment
            for (i in 0 until (high - low + 1)) {
                if (isPrimeSegment[i]) primeCount++
            }

            low += segmentSize
        }

        return primeCount
    }

    override fun getPrimeNumbers(value: Double, promise: Promise?) {
        try{
            val startTime = System.currentTimeMillis()
            val primeSize = computePrimes(value)
            val endTime = System.currentTimeMillis()

            val timeTaken = endTime - startTime

            val map = Arguments.createMap()
            map.putString("language", "Kotlin")
            map.putInt("value", primeSize)
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