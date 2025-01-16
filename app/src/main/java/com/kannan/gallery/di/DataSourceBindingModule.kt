package com.kannan.gallery.di

import com.kannan.gallery.data.contentResolver.ContentResolverDataSource
import com.kannan.gallery.data.contentResolver.ContentResolverDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceBindingModule {

    @Binds
    @Singleton
    fun bindContentResolverDataSource(contentResolverDataSource: ContentResolverDataSourceImpl): ContentResolverDataSource
}