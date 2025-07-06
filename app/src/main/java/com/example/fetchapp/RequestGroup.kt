package com.example.fetchapp

interface RequestGroup {
    data class GroupItem(val header: String) : RequestGroup
    data class DataItem(val request: Request) : RequestGroup
}