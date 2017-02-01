// Compiled by ClojureScript 1.9.89 {}
goog.provide('sevenm_booking_system.handlers');
goog.require('cljs.core');
goog.require('re_frame.core');
goog.require('sevenm_booking_system.db');
re_frame.core.reg_event_db.call(null,new cljs.core.Keyword(null,"initialize-db","initialize-db",230998432),(function (_,___$1){
return sevenm_booking_system.db.default_db;
}));
re_frame.core.reg_event_db.call(null,new cljs.core.Keyword(null,"set-active-panel","set-active-panel",-965871124),(function (db,p__10740){
var vec__10741 = p__10740;
var _ = cljs.core.nth.call(null,vec__10741,(0),null);
var active_panel = cljs.core.nth.call(null,vec__10741,(1),null);
return cljs.core.assoc.call(null,db,new cljs.core.Keyword(null,"active-panel","active-panel",-1802545994),active_panel);
}));

//# sourceMappingURL=handlers.js.map?rel=1483597758149