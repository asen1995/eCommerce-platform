package com.ecommerence.platform.constants;

public class RsqlConstants {
    //TODO add order.id to query
    public static final String ORDER_GLOBAL_SEARCH_RSQL_QUERY
            = "comment==*searchString*,customer.username==*searchString*,customer.firstName==*searchString*,customer.lastName==*searchString*";
    public static final String ORDER_GLOBAL_SEARCH_FOR_LOGGED_USER_RSQL_QUERY =
            "customer.username==loggedUsername;comment==*searchString*,customer.firstName==*searchString*,customer.lastName==*searchString*";

}
