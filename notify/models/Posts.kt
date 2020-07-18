package com.ionwayinc.project.notify.models

class Posts() {
    var desc: String? = null
    var image: String? = null
    var time: String? = null

    constructor(desc: String, image: String, time: String): this() {
        this.desc = desc
        this.image = image
        this.time = time
    }
}