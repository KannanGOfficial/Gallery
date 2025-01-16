package com.kannan.gallery.di

import com.kannan.gallery.data.repository.GalleryRepositoryImpl
import com.kannan.gallery.domain.GalleryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindingModule {

    @Binds
    @Singleton
    fun bindGalleryRepository(galleryRepository: GalleryRepositoryImpl): GalleryRepository
}