// Compiled by ClojureScript 1.9.89 {}
goog.provide('sevenm_booking_system.core');
goog.require('cljs.core');
goog.require('reagent.core');
goog.require('sevenm_booking_system.views');
goog.require('sevenm_booking_system.subs');
goog.require('sevenm_booking_system.routes');
goog.require('sevenm_booking_system.config');
goog.require('devtools.core');
goog.require('sevenm_booking_system.handlers');
goog.require('re_frame.core');
sevenm_booking_system.core.dev_setup = (function sevenm_booking_system$core$dev_setup(){
if(cljs.core.truth_(sevenm_booking_system.config.debug_QMARK_)){
cljs.core.enable_console_print_BANG_.call(null);

cljs.core.println.call(null,"dev mode");

return devtools.core.install_BANG_.call(null);
} else {
return null;
}
});
sevenm_booking_system.core.mount_root = (function sevenm_booking_system$core$mount_root(){
return reagent.core.render.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.main_panel], null),document.getElementById("app"));
});
sevenm_booking_system.core.init = (function sevenm_booking_system$core$init(){
sevenm_booking_system.routes.app_routes.call(null);

re_frame.core.dispatch_sync.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"initialize-db","initialize-db",230998432)], null));

sevenm_booking_system.core.dev_setup.call(null);

return sevenm_booking_system.core.mount_root.call(null);
});
goog.exportSymbol('sevenm_booking_system.core.init', sevenm_booking_system.core.init);
sevenm_booking_system.core.init.call(null);

//# sourceMappingURL=core.js.map?rel=1484127040662