// Compiled by ClojureScript 1.9.89 {}
goog.provide('sevenm_booking_system.routes');
goog.require('cljs.core');
goog.require('bidi.bidi');
goog.require('pushy.core');
goog.require('re_frame.core');
sevenm_booking_system.routes.routes = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",cljs.core.PersistentHashMap.fromArrays(["","fitquotations-list","fitbookings","groupbookings","vendors","cities-list","complete-payment-log","bookingfits-list","logos-list","items-list","groupquotations-list","register","complete-actitvity-log","unauthorized"],[new cljs.core.Keyword(null,"home","home",-74557309),new cljs.core.Keyword(null,"fit-quotations","fit-quotations",-1520677836),cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"fitbookings","fitbookings",227762754),"/add-fitbooking",new cljs.core.Keyword(null,"add-fitbooking","add-fitbooking",1804203067),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-fitbooking"], null),new cljs.core.Keyword(null,"edit-fitbooking","edit-fitbooking",-1607666750),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/fit-activitylog"], null),new cljs.core.Keyword(null,"fit-activitylog","fit-activitylog",-725463727),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/fit-paymentlog"], null),new cljs.core.Keyword(null,"fit-paymentlog","fit-paymentlog",1182127213)], true, false),cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"groupbookings","groupbookings",-787083220),"/add-groupbooking",new cljs.core.Keyword(null,"add-groupbooking","add-groupbooking",-564709166),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-groupbooking"], null),new cljs.core.Keyword(null,"edit-groupbooking","edit-groupbooking",2069309826),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/group-activitylog"], null),new cljs.core.Keyword(null,"group-activitylog","group-activitylog",-1486409568),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/group-paymentlog"], null),new cljs.core.Keyword(null,"group-paymentlog","group-paymentlog",289785927)], true, false),new cljs.core.PersistentArrayMap(null, 6, ["/vendors-list",cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"vendors","vendors",-153040496),"/add-vendor",new cljs.core.Keyword(null,"add-vendor","add-vendor",-527382632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-vendor"], null),new cljs.core.Keyword(null,"edit-vendor","edit-vendor",-1413725249)], true, false),"/events-list",cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"events","events",1792552201),"/add-event",new cljs.core.Keyword(null,"add-event","add-event",938429088),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-event"], null),new cljs.core.Keyword(null,"edit-event","edit-event",-969537481),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/view-event"], null),new cljs.core.Keyword(null,"view-event","view-event",1670309774)], true, false),"/hotels-list",cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"hotels","hotels",1552357142),"/add-hotel",new cljs.core.Keyword(null,"add-hotel","add-hotel",710530814),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-hotel"], null),new cljs.core.Keyword(null,"edit-hotel","edit-hotel",376112162),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/view-hotel"], null),new cljs.core.Keyword(null,"view-hotel","view-hotel",-686388496)], true, false),"/restaurants-list",cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"restaurants","restaurants",1693363505),"/add-restaurant",new cljs.core.Keyword(null,"add-restaurant","add-restaurant",862180421),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-restaurant"], null),new cljs.core.Keyword(null,"edit-restaurant","edit-restaurant",-709382400),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/view-restaurant"], null),new cljs.core.Keyword(null,"view-restaurant","view-restaurant",1556565268)], true, false),"/tourguides-list",cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"tourguides","tourguides",604110481),"/add-tourguide",new cljs.core.Keyword(null,"add-tourguide","add-tourguide",-595112847),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-tourguide"], null),new cljs.core.Keyword(null,"edit-tourguide","edit-tourguide",1622319808),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/view-tourguide"], null),new cljs.core.Keyword(null,"view-tourguide","view-tourguide",-823421758)], true, false),"/transportations-list",cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"transportations","transportations",949329735),"/add-transportation",new cljs.core.Keyword(null,"add-transportation","add-transportation",-769051174),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-transportation"], null),new cljs.core.Keyword(null,"edit-transportation","edit-transportation",-580535752),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/view-transportation"], null),new cljs.core.Keyword(null,"view-transportation","view-transportation",-2143737615)], true, false)], null),cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"cities","cities",-1295356824),"/add-city",new cljs.core.Keyword(null,"add-city","add-city",-1190166),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-city"], null),new cljs.core.Keyword(null,"edit-city","edit-city",-1721270384)], true, false),new cljs.core.Keyword(null,"complete-payment-log","complete-payment-log",-1841365671),cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"bookingfits","bookingfits",-1934442442),"/add-bookingfit",new cljs.core.Keyword(null,"add-bookingfit","add-bookingfit",1614968085),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-bookingfit"], null),new cljs.core.Keyword(null,"edit-bookingfit","edit-bookingfit",144640306)], true, false),cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"logos","logos",1354242661),"/add-logo",new cljs.core.Keyword(null,"add-logo","add-logo",-1927288932),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-logo"], null),new cljs.core.Keyword(null,"edit-logo","edit-logo",-682467110)], true, false),cljs.core.PersistentArrayMap.fromArray(["",new cljs.core.Keyword(null,"items","items",1031954938),"/add-item",new cljs.core.Keyword(null,"add-item","add-item",715813891),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/edit-item"], null),new cljs.core.Keyword(null,"edit-item","edit-item",1026303883),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["/",new cljs.core.Keyword(null,"id","id",-1388402092),"/view-item"], null),new cljs.core.Keyword(null,"view-item","view-item",241055094)], true, false),new cljs.core.Keyword(null,"group-quotations","group-quotations",1243399025),new cljs.core.Keyword(null,"register","register",1968522516),new cljs.core.Keyword(null,"complete-activity-log","complete-activity-log",-24244602),new cljs.core.Keyword(null,"unauthorized","unauthorized",-2089027806)])], null);
sevenm_booking_system.routes.dispatch_route = (function sevenm_booking_system$routes$dispatch_route(matched_route){
return re_frame.core.dispatch.call(null,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"set-active-panel","set-active-panel",-965871124),matched_route], null));
});
sevenm_booking_system.routes.history = pushy.core.pushy.call(null,sevenm_booking_system.routes.dispatch_route,cljs.core.partial.call(null,bidi.bidi.match_route,sevenm_booking_system.routes.routes));
sevenm_booking_system.routes.app_routes = (function sevenm_booking_system$routes$app_routes(){
return pushy.core.start_BANG_.call(null,sevenm_booking_system.routes.history);
});
sevenm_booking_system.routes.url_for = cljs.core.partial.call(null,bidi.bidi.path_for,sevenm_booking_system.routes.routes);

//# sourceMappingURL=routes.js.map?rel=1483597754439