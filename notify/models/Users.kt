package com.ionwayinc.project.notify.models

class Users() {
    var display_name: String? = null
    var email_address: String? = null
    var registration_id: String? = null

    constructor(display_name: String, email_address: String, registration_id: String): this() {
        this.display_name = display_name
        this.email_address = email_address
        this.registration_id = registration_id
    }
}