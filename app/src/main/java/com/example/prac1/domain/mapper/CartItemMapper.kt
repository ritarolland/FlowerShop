package com.example.prac1.domain.mapper

import com.example.prac1.data.model.CartItemDataModel
import com.example.prac1.domain.model.CartItem

object CartItemMapper {
    fun mapToDomain(dataModel: CartItemDataModel): CartItem {
        return CartItem(
            id = dataModel.id,
            flowerId = dataModel.flower_id,
            userId = dataModel.user_id,
            quantity = dataModel.quantity
        )
    }
    fun mapToDataModel(domain: CartItem): CartItemDataModel {
        return CartItemDataModel(
            id = domain.id,
            flower_id = domain.flowerId,
            quantity = domain.quantity,
            user_id = domain.userId
        )
    }
}