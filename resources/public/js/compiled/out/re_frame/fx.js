// Compiled by ClojureScript 1.9.89 {}
goog.provide('re_frame.fx');
goog.require('cljs.core');
goog.require('re_frame.router');
goog.require('re_frame.db');
goog.require('re_frame.interceptor');
goog.require('re_frame.interop');
goog.require('re_frame.events');
goog.require('re_frame.registrar');
goog.require('re_frame.loggers');
re_frame.fx.kind = new cljs.core.Keyword(null,"fx","fx",-1237829572);
if(cljs.core.truth_(re_frame.registrar.kinds.call(null,re_frame.fx.kind))){
} else {
throw (new Error("Assert failed: (re-frame.registrar/kinds kind)"));
}
re_frame.fx.register = cljs.core.partial.call(null,re_frame.registrar.register_handler,re_frame.fx.kind);
/**
 * An interceptor which actions a `context's` (side) `:effects`.
 * 
 *   For each key in the `:effects` map, call the `effects handler` previously
 *   registered using `reg-fx`.
 * 
 *   So, if `:effects` was:
 *    {:dispatch  [:hello 42]
 *     :db        {...}
 *     :undo      "set flag"}
 *   call the registered effects handlers for each of the map's keys:
 *   `:dispatch`, `:undo` and `:db`.
 */
re_frame.fx.do_fx = re_frame.interceptor.__GT_interceptor.call(null,new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"do-fx","do-fx",1194163050),new cljs.core.Keyword(null,"after","after",594996914),(function re_frame$fx$do_fx_after(context){
return cljs.core.doall.call(null,cljs.core.map.call(null,(function (p__9325){
var vec__9326 = p__9325;
var k = cljs.core.nth.call(null,vec__9326,(0),null);
var value = cljs.core.nth.call(null,vec__9326,(1),null);
var temp__4655__auto__ = re_frame.registrar.get_handler.call(null,re_frame.fx.kind,k,true);
if(cljs.core.truth_(temp__4655__auto__)){
var effect_fn = temp__4655__auto__;
return effect_fn.call(null,value);
} else {
return null;
}
}),new cljs.core.Keyword(null,"effects","effects",-282369292).cljs$core$IFn$_invoke$arity$1(context)));
}));
re_frame.fx.register.call(null,new cljs.core.Keyword(null,"dispatch-later","dispatch-later",291951390),(function (value){
var seq__9329 = cljs.core.seq.call(null,value);
var chunk__9330 = null;
var count__9331 = (0);
var i__9332 = (0);
while(true){
if((i__9332 < count__9331)){
var map__9333 = cljs.core._nth.call(null,chunk__9330,i__9332);
var map__9333__$1 = ((((!((map__9333 == null)))?((((map__9333.cljs$lang$protocol_mask$partition0$ & (64))) || (map__9333.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__9333):map__9333);
var effect = map__9333__$1;
var ms = cljs.core.get.call(null,map__9333__$1,new cljs.core.Keyword(null,"ms","ms",-1152709733));
var dispatch = cljs.core.get.call(null,map__9333__$1,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009));
if((cljs.core.empty_QMARK_.call(null,dispatch)) || (!(typeof ms === 'number'))){
re_frame.loggers.console.call(null,new cljs.core.Keyword(null,"error","error",-978969032),"re-frame: ignoring bad :dispatch-later value: ",effect);
} else {
re_frame.interop.set_timeout_BANG_.call(null,((function (seq__9329,chunk__9330,count__9331,i__9332,map__9333,map__9333__$1,effect,ms,dispatch){
return (function (){
return re_frame.router.dispatch.call(null,dispatch);
});})(seq__9329,chunk__9330,count__9331,i__9332,map__9333,map__9333__$1,effect,ms,dispatch))
,ms);
}

var G__9337 = seq__9329;
var G__9338 = chunk__9330;
var G__9339 = count__9331;
var G__9340 = (i__9332 + (1));
seq__9329 = G__9337;
chunk__9330 = G__9338;
count__9331 = G__9339;
i__9332 = G__9340;
continue;
} else {
var temp__4657__auto__ = cljs.core.seq.call(null,seq__9329);
if(temp__4657__auto__){
var seq__9329__$1 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__9329__$1)){
var c__6413__auto__ = cljs.core.chunk_first.call(null,seq__9329__$1);
var G__9341 = cljs.core.chunk_rest.call(null,seq__9329__$1);
var G__9342 = c__6413__auto__;
var G__9343 = cljs.core.count.call(null,c__6413__auto__);
var G__9344 = (0);
seq__9329 = G__9341;
chunk__9330 = G__9342;
count__9331 = G__9343;
i__9332 = G__9344;
continue;
} else {
var map__9335 = cljs.core.first.call(null,seq__9329__$1);
var map__9335__$1 = ((((!((map__9335 == null)))?((((map__9335.cljs$lang$protocol_mask$partition0$ & (64))) || (map__9335.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__9335):map__9335);
var effect = map__9335__$1;
var ms = cljs.core.get.call(null,map__9335__$1,new cljs.core.Keyword(null,"ms","ms",-1152709733));
var dispatch = cljs.core.get.call(null,map__9335__$1,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009));
if((cljs.core.empty_QMARK_.call(null,dispatch)) || (!(typeof ms === 'number'))){
re_frame.loggers.console.call(null,new cljs.core.Keyword(null,"error","error",-978969032),"re-frame: ignoring bad :dispatch-later value: ",effect);
} else {
re_frame.interop.set_timeout_BANG_.call(null,((function (seq__9329,chunk__9330,count__9331,i__9332,map__9335,map__9335__$1,effect,ms,dispatch,seq__9329__$1,temp__4657__auto__){
return (function (){
return re_frame.router.dispatch.call(null,dispatch);
});})(seq__9329,chunk__9330,count__9331,i__9332,map__9335,map__9335__$1,effect,ms,dispatch,seq__9329__$1,temp__4657__auto__))
,ms);
}

var G__9345 = cljs.core.next.call(null,seq__9329__$1);
var G__9346 = null;
var G__9347 = (0);
var G__9348 = (0);
seq__9329 = G__9345;
chunk__9330 = G__9346;
count__9331 = G__9347;
i__9332 = G__9348;
continue;
}
} else {
return null;
}
}
break;
}
}));
re_frame.fx.register.call(null,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),(function (value){
if(!(cljs.core.vector_QMARK_.call(null,value))){
return re_frame.loggers.console.call(null,new cljs.core.Keyword(null,"error","error",-978969032),"re-frame: ignoring bad :dispatch value. Expected a vector, but got: ",value);
} else {
return re_frame.router.dispatch.call(null,value);
}
}));
re_frame.fx.register.call(null,new cljs.core.Keyword(null,"dispatch-n","dispatch-n",-504469236),(function (value){
if(!(cljs.core.sequential_QMARK_.call(null,value))){
re_frame.loggers.console.call(null,new cljs.core.Keyword(null,"error","error",-978969032),"re-frame: ignoring bad :dispatch-n value. Expected a collection, got got: ",value);
} else {
}

var seq__9349 = cljs.core.seq.call(null,value);
var chunk__9350 = null;
var count__9351 = (0);
var i__9352 = (0);
while(true){
if((i__9352 < count__9351)){
var event = cljs.core._nth.call(null,chunk__9350,i__9352);
re_frame.router.dispatch.call(null,event);

var G__9353 = seq__9349;
var G__9354 = chunk__9350;
var G__9355 = count__9351;
var G__9356 = (i__9352 + (1));
seq__9349 = G__9353;
chunk__9350 = G__9354;
count__9351 = G__9355;
i__9352 = G__9356;
continue;
} else {
var temp__4657__auto__ = cljs.core.seq.call(null,seq__9349);
if(temp__4657__auto__){
var seq__9349__$1 = temp__4657__auto__;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__9349__$1)){
var c__6413__auto__ = cljs.core.chunk_first.call(null,seq__9349__$1);
var G__9357 = cljs.core.chunk_rest.call(null,seq__9349__$1);
var G__9358 = c__6413__auto__;
var G__9359 = cljs.core.count.call(null,c__6413__auto__);
var G__9360 = (0);
seq__9349 = G__9357;
chunk__9350 = G__9358;
count__9351 = G__9359;
i__9352 = G__9360;
continue;
} else {
var event = cljs.core.first.call(null,seq__9349__$1);
re_frame.router.dispatch.call(null,event);

var G__9361 = cljs.core.next.call(null,seq__9349__$1);
var G__9362 = null;
var G__9363 = (0);
var G__9364 = (0);
seq__9349 = G__9361;
chunk__9350 = G__9362;
count__9351 = G__9363;
i__9352 = G__9364;
continue;
}
} else {
return null;
}
}
break;
}
}));
re_frame.fx.register.call(null,new cljs.core.Keyword(null,"deregister-event-handler","deregister-event-handler",-1096518994),(function (value){
var clear_event = cljs.core.partial.call(null,re_frame.registrar.clear_handlers,re_frame.events.kind);
if(cljs.core.sequential_QMARK_.call(null,value)){
return cljs.core.doall.call(null,cljs.core.map.call(null,clear_event,value));
} else {
return clear_event.call(null,value);
}
}));
re_frame.fx.register.call(null,new cljs.core.Keyword(null,"db","db",993250759),(function (value){
return cljs.core.reset_BANG_.call(null,re_frame.db.app_db,value);
}));

//# sourceMappingURL=fx.js.map?rel=1483597754120