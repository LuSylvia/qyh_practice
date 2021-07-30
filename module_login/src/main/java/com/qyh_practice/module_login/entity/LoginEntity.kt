package com.qyh_practice.module_login.entity

import com.example.module_common.entity.ResponseEntity

data class LoginEntity(val accountExist: Boolean, val temporaryToken: String) :
    ResponseEntity.Data()
