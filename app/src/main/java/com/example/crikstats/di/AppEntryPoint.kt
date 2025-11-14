package com.example.crikstats.di

import com.example.crikstats.repository.GetDataRepo
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppEntryPoint {
    fun getRepo(): GetDataRepo
}