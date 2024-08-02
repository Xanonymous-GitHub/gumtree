package editscript

import helpers.gumTree

/**
 * Sample tree 1 for test.
 * Mentioned in Chawathe's paper Figure 2.
 * */
private val sampleTree1L =
    gumTree("1") {
        child("2")
        child("3") {
            child("5")
        }
        child("4")
    }

/**
 * Sample tree 1 for test.
 * Mentioned in Chawathe's paper Figure 2.
 * */
private val sampleTree1R =
    gumTree("1") {
        child("2")
        child("4")
        child("3") {
            child("5")
            child("6")
        }
    }

/**
 * Sample tree 2 for test.
 * Mentioned in Chawathe's paper Figure 3.
 * */
private val sampleTree2L =
    gumTree("1", "D") {
        child("2", "S(1)")
        child("3", "Sec") {
            child("4", "S(x)")
            child("5", "P") {
                child("6", "S(a)")
                child("7", "S(b)")
            }
        }
        child("8", "P") {
            child("9", "S(m)")
            child("10", "S(n)")
        }
    }

/**
 * Sample tree 2 for test.
 * Mentioned in Chawathe's paper Figure 3.
 * */
private val sampleTree2R =
    gumTree("1", "D") {
        child("3", "Sec") {
            child("4", "S(x)")
        }
        child("8", "P") {
            child("9", "S(m)")
            child("10", "S(n)")
        }
        child("11", "Sec(foo)") {
            child("5", "P") {
                child("6", "S(a)")
                child("7", "S(b)")
            }
        }
    }

internal val sampleTree1 = sampleTree1L to sampleTree1R
internal val sampleTree2 = sampleTree2L to sampleTree2R