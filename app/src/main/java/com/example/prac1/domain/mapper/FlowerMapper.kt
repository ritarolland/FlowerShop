package com.example.prac1.domain.mapper

import com.example.prac1.data.api.model.FlowerDataModel
import com.example.prac1.data.db.entity.FlowerEntity
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

    fun mapToDomain(entity: FlowerEntity): Flower {
        return Flower(
            id = entity.id,
            name = entity.name,
            description = entity.description ?: "No description",
            price = entity.price,
            imageUrl = entity.imageUrl
        )
    }

    fun mapToEntity(domain: Flower): FlowerEntity {
        return FlowerEntity(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            price = domain.price,
            imageUrl = domain.imageUrl
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