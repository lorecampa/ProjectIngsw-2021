package it.polimi.ingsw.controller;

/**
 * Class State is an enumeration
 */
public enum TurnState {
    ACTION_CHOOSING,

    LEADER_MANAGING,

    //market
    MARKET_ACTION,
    WHITE_MARBLE_CONVERSION,
    DEPOTS_POSITIONING,

    //buy dev
    BUY_DEVELOPMENT_ACTION,
    ANY_BUY_DEV_CONVERSION,
    WAREHOUSE_RESOURCE_REMOVING,

    //production
    PRODUCTION_ACTION,
    ANY_PRODUCE_COST_CONVERSION,
    ANY_PRODUCE_PROFIT_CONVERSION;


}
