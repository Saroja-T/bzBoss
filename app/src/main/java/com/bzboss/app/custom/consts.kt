package com.iqamahtimes.app.custom;



/*shared preference*/
val  CHECK_DIALOG_OPEN_CLOSE="check_dialog_open_close"
val HOME_CHECK_SEARCH_OR_CHANGE="search_or_change"

/*CODE*/

val SUCCESS_CODE=200
val LOGIN=1
val ERROR_CODE = 400
val TOKEN_CODE = 405
val UNKNOW = 404
val REGISTER=2
val HOME_DATA_GET=3
val HOME_PREMISE_DATA=4
val HOME_GRAPH_DATA=5
val STORE_CONTACTS_DATA=6
val USER_STAFF_DATA=10
val USER_KNOWN_DETAILS_DATA=12
val USER_CONFIG_DATA=15
val USER_ACCESSS=15
/*api interface*/
val HOME_DATA = 201

var minsOpenedAt=0.0
var hoursOpenedAt=0.0
var secsOpenedAt=0.0

var minsFirstCustomer=0.0
var hoursFirstCustomer=0.0
var secsFirstCustomer=0.0

var minsClosedAt=0.0
var hoursClosedAt=0.0
var secsClosedAt=0.0

var toolTipTime : String = ""
var toolTipDate : String = ""
var isUserHavingAccess : Boolean = false

/*https://theadeptz.com/bzBoss/public/about/us
https://theadeptz.com/bzBoss/public/term/condition
https://theadeptz.com/bzBoss/public/privecy/policy*/
val TERM_AND_CONDITION="https://theadeptz.com/bzBoss/public/term/condition"
val PRIVACY_POLIVY="https://theadeptz.com/bzBoss/public/privecy/policy"
val ABOUTS_US="https://theadeptz.com/bzBoss/public/about/us"