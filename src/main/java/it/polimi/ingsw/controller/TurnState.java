package it.polimi.ingsw.controller;

/**
 * Class State is an enumeration
 */
public enum TurnState {
    LEADER_MANAGE_BEFORE,


    //market
    MARKET_ACTION,
    WHITE_MARBLE_CONVERSION,
    MARKET_RESOURCE_POSITIONING,

    //buy dev
    BUY_DEVELOPMENT_ACTION,
    ANY_BUY_DEV_CONVERSION,
    BUY_DEV_RESOURCE_REMOVING,

    //production
    PRODUCTION_ACTION,
    ANY_PRODUCE_COST_CONVERSION,
    ANY_PRODUCE_PROFIT_CONVERSION,
    PRODUCTION_RESOURCE_REMOVING,

    //END
    LEADER_MANAGE_AFTER

}
