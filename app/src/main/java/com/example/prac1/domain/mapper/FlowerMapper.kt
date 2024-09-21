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
        )
    }
    fun mapToDataModel(dataModel: FlowerDataModel): FlowerDataModel {
        return FlowerDataModel(
            id = dataModel.id,
            name = dataModel.name,
            description = dataModel.description,
            price = dataModel.price,
        )
    }
}