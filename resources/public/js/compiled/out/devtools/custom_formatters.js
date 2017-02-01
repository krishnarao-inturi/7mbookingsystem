// Compiled by ClojureScript 1.9.89 {}
goog.provide('devtools.custom_formatters');
goog.require('cljs.core');
goog.require('devtools.prefs');
goog.require('devtools.format');
goog.require('goog.labs.userAgent.browser');
devtools.custom_formatters._STAR_installed_STAR_ = false;
devtools.custom_formatters._STAR_sanitizer_enabled_STAR_ = true;
devtools.custom_formatters._STAR_monitor_enabled_STAR_ = false;
devtools.custom_formatters.formatter_key = "devtoolsFormatters";
devtools.custom_formatters.obsolete_formatter_key = "devtoolsFormatter";
devtools.custom_formatters.available_QMARK_ = (function devtools$custom_formatters$available_QMARK_(){
var and__5590__auto__ = goog.labs.userAgent.browser.isChrome();
if(cljs.core.truth_(and__5590__auto__)){
return goog.labs.userAgent.browser.isVersionOrHigher((47));
} else {
return and__5590__auto__;
}
});

/**
* @constructor
*/
devtools.custom_formatters.CLJSDevtoolsFormatter = (function (){
})

devtools.custom_formatters.CLJSDevtoolsFormatter.getBasis = (function (){
return cljs.core.PersistentVector.EMPTY;
});

devtools.custom_formatters.CLJSDevtoolsFormatter.cljs$lang$type = true;

devtools.custom_formatters.CLJSDevtoolsFormatter.cljs$lang$ctorStr = "devtools.custom-formatters/CLJSDevtoolsFormatter";

devtools.custom_formatters.CLJSDevtoolsFormatter.cljs$lang$ctorPrWriter = (function (this__6208__auto__,writer__6209__auto__,opt__6210__auto__){
return cljs.core._write.call(null,writer__6209__auto__,"devtools.custom-formatters/CLJSDevtoolsFormatter");
});

devtools.custom_formatters.__GT_CLJSDevtoolsFormatter = (function devtools$custom_formatters$__GT_CLJSDevtoolsFormatter(){
return (new devtools.custom_formatters.CLJSDevtoolsFormatter());
});

devtools.custom_formatters.find_fn_in_debug_ns = (function devtools$custom_formatters$find_fn_in_debug_ns(fn_name){
try{return (window["devtools"]["debug"][fn_name]);
}catch (e10695){var _ = e10695;
return null;
}});
devtools.custom_formatters.monitor_api_call_if_avail = (function devtools$custom_formatters$monitor_api_call_if_avail(name,api_call,args){
var temp__4655__auto__ = devtools.custom_formatters.find_fn_in_debug_ns.call(null,"monitor_api_call");
if(cljs.core.truth_(temp__4655__auto__)){
var monitor_api_call = temp__4655__auto__;
return monitor_api_call.call(null,name,api_call,args);
} else {
return cljs.core.apply.call(null,api_call,args);
}
});
devtools.custom_formatters.log_exception_if_avail = (function devtools$custom_formatters$log_exception_if_avail(var_args){
var args__6684__auto__ = [];
var len__6677__auto___10697 = arguments.length;
var i__6678__auto___10698 = (0);
while(true){
if((i__6678__auto___10698 < len__6677__auto___10697)){
args__6684__auto__.push((arguments[i__6678__auto___10698]));

var G__10699 = (i__6678__auto___10698 + (1));
i__6678__auto___10698 = G__10699;
continue;
} else {
}
break;
}

var argseq__6685__auto__ = ((((0) < args__6684__auto__.length))?(new cljs.core.IndexedSeq(args__6684__auto__.slice((0)),(0),null)):null);
return devtools.custom_formatters.log_exception_if_avail.cljs$core$IFn$_invoke$arity$variadic(argseq__6685__auto__);
});

devtools.custom_formatters.log_exception_if_avail.cljs$core$IFn$_invoke$arity$variadic = (function (args){
var temp__4655__auto__ = devtools.custom_formatters.find_fn_in_debug_ns.call(null,"log_exception");
if(cljs.core.truth_(temp__4655__auto__)){
var log_exception = temp__4655__auto__;
return cljs.core.apply.call(null,log_exception,args);
} else {
return null;
}
});

devtools.custom_formatters.log_exception_if_avail.cljs$lang$maxFixedArity = (0);

devtools.custom_formatters.log_exception_if_avail.cljs$lang$applyTo = (function (seq10696){
return devtools.custom_formatters.log_exception_if_avail.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq.call(null,seq10696));
});

devtools.custom_formatters.monitor_api_calls = (function devtools$custom_formatters$monitor_api_calls(name,api_call){
return (function() { 
var G__10700__delegate = function (args){
if(cljs.core.not.call(null,devtools.custom_formatters._STAR_monitor_enabled_STAR_)){
return cljs.core.apply.call(null,api_call,args);
} else {
return devtools.custom_formatters.monitor_api_call_if_avail.call(null,name,api_call,args);
}
};
var G__10700 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__10701__i = 0, G__10701__a = new Array(arguments.length -  0);
while (G__10701__i < G__10701__a.length) {G__10701__a[G__10701__i] = arguments[G__10701__i + 0]; ++G__10701__i;}
  args = new cljs.core.IndexedSeq(G__10701__a,0);
} 
return G__10700__delegate.call(this,args);};
G__10700.cljs$lang$maxFixedArity = 0;
G__10700.cljs$lang$applyTo = (function (arglist__10702){
var args = cljs.core.seq(arglist__10702);
return G__10700__delegate(args);
});
G__10700.cljs$core$IFn$_invoke$arity$variadic = G__10700__delegate;
return G__10700;
})()
;
});
devtools.custom_formatters.sanitize = (function devtools$custom_formatters$sanitize(name,api_call){
return (function() { 
var G__10705__delegate = function (args){
if(cljs.core.not.call(null,devtools.custom_formatters._STAR_sanitizer_enabled_STAR_)){
return cljs.core.apply.call(null,api_call,args);
} else {
try{return cljs.core.apply.call(null,api_call,args);
}catch (e10704){var e = e10704;
devtools.custom_formatters.log_exception_if_avail.call(null,[cljs.core.str(name),cljs.core.str(": "),cljs.core.str(e)].join(''));

return null;
}}
};
var G__10705 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__10706__i = 0, G__10706__a = new Array(arguments.length -  0);
while (G__10706__i < G__10706__a.length) {G__10706__a[G__10706__i] = arguments[G__10706__i + 0]; ++G__10706__i;}
  args = new cljs.core.IndexedSeq(G__10706__a,0);
} 
return G__10705__delegate.call(this,args);};
G__10705.cljs$lang$maxFixedArity = 0;
G__10705.cljs$lang$applyTo = (function (arglist__10707){
var args = cljs.core.seq(arglist__10707);
return G__10705__delegate(args);
});
G__10705.cljs$core$IFn$_invoke$arity$variadic = G__10705__delegate;
return G__10705;
})()
;
});
devtools.custom_formatters.build_cljs_formatter = (function devtools$custom_formatters$build_cljs_formatter(){
var wrap = (function (name,api_call){
var monitor = cljs.core.partial.call(null,devtools.custom_formatters.monitor_api_calls,name);
var sanitizer = cljs.core.partial.call(null,devtools.custom_formatters.sanitize,name);
cljs.core.comp.call(null,monitor,sanitizer).call(null,api_call);

return api_call;
});
var formatter = (new devtools.custom_formatters.CLJSDevtoolsFormatter());
var define_BANG_ = ((function (wrap,formatter){
return (function (name,fn){
return (formatter[name] = wrap.call(null,name,fn));
});})(wrap,formatter))
;
define_BANG_.call(null,"header",devtools.format.header_api_call);

define_BANG_.call(null,"hasBody",devtools.format.has_body_api_call);

define_BANG_.call(null,"body",devtools.format.body_api_call);

return formatter;
});
devtools.custom_formatters.is_ours_QMARK_ = (function devtools$custom_formatters$is_ours_QMARK_(o){
return (o instanceof devtools.custom_formatters.CLJSDevtoolsFormatter);
});
devtools.custom_formatters.get_formatters_safe = (function devtools$custom_formatters$get_formatters_safe(){
var formatters = (window[devtools.custom_formatters.formatter_key]);
if(cljs.core.array_QMARK_.call(null,formatters)){
return formatters;
} else {
return [];
}
});
devtools.custom_formatters.present_QMARK_ = (function devtools$custom_formatters$present_QMARK_(){
var formatters = devtools.custom_formatters.get_formatters_safe.call(null);
return cljs.core.boolean$.call(null,cljs.core.some.call(null,devtools.custom_formatters.is_ours_QMARK_,formatters));
});
devtools.custom_formatters.install_our_formatter_BANG_ = (function devtools$custom_formatters$install_our_formatter_BANG_(formatter){
var formatters = devtools.custom_formatters.get_formatters_safe.call(null).slice();
formatters.push(formatter);

(window[devtools.custom_formatters.formatter_key] = formatters);

if(cljs.core.truth_(devtools.prefs.pref.call(null,new cljs.core.Keyword(null,"legacy-formatter","legacy-formatter",-1954119499)))){
return (window[devtools.custom_formatters.obsolete_formatter_key] = formatter);
} else {
return null;
}
});
devtools.custom_formatters.uninstall_our_formatters_BANG_ = (function devtools$custom_formatters$uninstall_our_formatters_BANG_(){
var new_formatters = cljs.core.remove.call(null,devtools.custom_formatters.is_ours_QMARK_,cljs.core.vec.call(null,devtools.custom_formatters.get_formatters_safe.call(null)));
var new_formatters_js = ((cljs.core.empty_QMARK_.call(null,new_formatters))?null:cljs.core.into_array.call(null,new_formatters));
return (window[devtools.custom_formatters.formatter_key] = new_formatters_js);
});
devtools.custom_formatters.install_BANG_ = (function devtools$custom_formatters$install_BANG_(){
if(cljs.core.truth_((function (){var and__5590__auto__ = cljs.core.not.call(null,devtools.custom_formatters._STAR_installed_STAR_);
if(and__5590__auto__){
return devtools.custom_formatters.available_QMARK_.call(null);
} else {
return and__5590__auto__;
}
})())){
devtools.custom_formatters._STAR_installed_STAR_ = true;

devtools.custom_formatters.install_our_formatter_BANG_.call(null,devtools.custom_formatters.build_cljs_formatter.call(null));

return true;
} else {
return null;
}
});
devtools.custom_formatters.uninstall_BANG_ = (function devtools$custom_formatters$uninstall_BANG_(){
if(cljs.core.truth_(devtools.custom_formatters._STAR_installed_STAR_)){
devtools.custom_formatters._STAR_installed_STAR_ = false;

return devtools.custom_formatters.uninstall_our_formatters_BANG_.call(null);
} else {
return null;
}
});

//# sourceMappingURL=custom_formatters.js.map?rel=1483597758086