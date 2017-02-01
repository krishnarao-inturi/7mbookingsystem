// Compiled by ClojureScript 1.9.89 {}
goog.provide('sevenm_booking_system.config');
goog.require('cljs.core');
goog.require('alandipert.storage_atom');
sevenm_booking_system.config.debug_QMARK_ = goog.DEBUG;
sevenm_booking_system.config.session = alandipert.storage_atom.local_storage.call(null,cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY),new cljs.core.Keyword(null,"session","session",1008279103));
sevenm_booking_system.config.server = "http://localhost:8001/";

//# sourceMappingURL=config.js.map?rel=1484127036759