package com.hsappdev.ahs.newCache;

/**
 * Diagram of the cache system (its a data loading mechanism, PLS DONT CALL IT A CACHE ANY LONGER)
 *
 * abstract data obj <- a wrapper used to store "loading" info about a data class
 * data obj <- the actual data class (ex. Article, Category)
 *
 * start loading of arbitrary data
 * -> return an "invalid" article that has blank data (isLoading property is true of the CacheType) in order to implement async load
 * -> first get firebase ref
 * -> then load article from firebase
 *      -> handle firebase callback
 *              1. call the extract data method of the abstract data obj
 *              2. initialize params of the data obj with default values
 * -> send callback to all observers again (live data)
 */
public class CacheSchema {

}
