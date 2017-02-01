// Compiled by ClojureScript 1.9.89 {}
goog.provide('reagent.debug');
goog.require('cljs.core');
reagent.debug.has_console = typeof console !== 'undefined';
reagent.debug.tracking = false;
if(typeof reagent.debug.warnings !== 'undefined'){
} else {
reagent.debug.warnings = cljs.core.atom.call(null,null);
}
if(typeof reagent.debug.track_console !== 'undefined'){
} else {
reagent.debug.track_console = (function (){var o = {};
o.warn = ((function (o){
return (function() { 
var G__6849__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"warn","warn",-436710552)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__6849 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__6850__i = 0, G__6850__a = new Array(arguments.length -  0);
while (G__6850__i < G__6850__a.length) {G__6850__a[G__6850__i] = arguments[G__6850__i + 0]; ++G__6850__i;}
  args = new cljs.core.IndexedSeq(G__6850__a,0);
} 
return G__6849__delegate.call(this,args);};
G__6849.cljs$lang$maxFixedArity = 0;
G__6849.cljs$lang$applyTo = (function (arglist__6851){
var args = cljs.core.seq(arglist__6851);
return G__6849__delegate(args);
});
G__6849.cljs$core$IFn$_invoke$arity$variadic = G__6849__delegate;
return G__6849;
})()
;})(o))
;

o.error = ((function (o){
return (function() { 
var G__6852__delegate = function (args){
return cljs.core.swap_BANG_.call(null,reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"error","error",-978969032)], null),cljs.core.conj,cljs.core.apply.call(null,cljs.core.str,args));
};
var G__6852 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__6853__i = 0, G__6853__a = new Array(arguments.length -  0);
while (G__6853__i < G__6853__a.length) {G__6853__a[G__6853__i] = arguments[G__6853__i + 0]; ++G__6853__i;}
  args = new cljs.core.IndexedSeq(G__6853__a,0);
} 
return G__6852__delegate.call(this,args);};
G__6852.cljs$lang$maxFixedArity = 0;
G__6852.cljs$lang$applyTo = (function (arglist__6854){
var args = cljs.core.seq(arglist__6854);
return G__6852__delegate(args);
});
G__6852.cljs$core$IFn$_invoke$arity$variadic = G__6852__delegate;
return G__6852;
})()
;})(o))
;

return o;
})();
}
reagent.debug.track_warnings = (function reagent$debug$track_warnings(f){
reagent.debug.tracking = true;

cljs.core.reset_BANG_.call(null,reagent.debug.warnings,null);

f.call(null);

var warns = cljs.core.deref.call(null,reagent.debug.warnings);
cljs.core.reset_BANG_.call(null,reagent.debug.warnings,null);

reagent.debug.tracking = false;

return warns;
});

//# sourceMappingURL=debug.js.map?rel=1483597749743