package com.hsappdev.ahs.newCache;

/**
 * Diagram of the cache system
 *
 * abstract data obj <- a wrapper used to store "loading" info about a data class
 * data obj <- the actual data class (ex. Article, Category)
 *
 * start loading of arbitrary data
 * -> first get firebase ref
 * -> then load article from firebase
 *      -> handle firebase callback
 *              1. call the extract data method of the abstract data obj
 *              2. initialize params of the data obj with default values
 *
 * -> send callback to all observers (live data)
 * -> then load article from local db (if needed)
 *      -> handle local db callback
 *          1. update the default local params (that were set earlier)
 * -> send callback to all observers again (live data)
 */
public class CacheSchema {

}
