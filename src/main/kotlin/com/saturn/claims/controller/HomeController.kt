package com.saturn.claims.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/home")
class HomeController {

    @RequestMapping("/index")
    fun index(): String {
        return "index"
    }

}

