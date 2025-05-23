package com.example.bankcards.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    CREATE_CARD("create_card_for_user"),
    CHANGE_CARD_STATUS("change_users_card_status"),
    DELETE_CARD("delete_users_card"),
    DEPOSIT_MONEY("deposit_money_on_users_card"),
    SEE_ALL_CARDS("see_all_cards"),
    SEE_ALL_USERS("see_all_users"),
    SEE_POSSESS_CARDS("see_cards_which_possess"),
    REQUEST_BLOCK_CARD("request_block_card_which_possess"),
    TRANSFER_MONEY("transfer_money_between_carts_which_possess"),
    ;
    @Getter
    private final String permission;
}
