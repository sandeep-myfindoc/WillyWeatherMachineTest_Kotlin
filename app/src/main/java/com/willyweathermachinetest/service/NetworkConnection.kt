package com.willyweathermachinetest.service

import com.willyweathermachinetest.BuildConfig

class NetworkConnection {
    var baseUrl: String? = null

    init {
        if (BuildConfig.DEBUG) {
            baseUrl = BuildConfig.baseUrl
        } else {
            baseUrl = BuildConfig.baseUrl
        }
    }
}