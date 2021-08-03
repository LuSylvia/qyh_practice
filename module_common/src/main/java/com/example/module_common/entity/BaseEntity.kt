package com.example.module_common.entity

import java.io.Serializable

//定义实体类的基本行为
//实现Serializable接口
abstract class BaseEntity : Serializable {
    abstract fun uniqueKey(): Array<String>?

}