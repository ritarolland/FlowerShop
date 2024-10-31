package com.example.prac1.domain.mapper

import com.example.prac1.data.model.FlowerDataModel
import com.example.prac1.domain.model.Flower

object FlowerMapper {
    fun mapToDomain(dataModel: FlowerDataModel): Flower {
        return Flower(
            id = dataModel.id,
            name = dataModel.name,
            description = dataModel.description ?: "No description",
            price = dataModel.price,
            imageUrl = dataModel.image_url
        )
    }

    fun mapToDataModel(domain: Flower): FlowerDataModel {
        return FlowerDataModel(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            price = domain.price,
            image_url = domain.imageUrl
        )
    }
}