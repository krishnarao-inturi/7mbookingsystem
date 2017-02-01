// Compiled by ClojureScript 1.9.89 {}
goog.provide('pushy.core');
goog.require('cljs.core');
goog.require('goog.events');
goog.require('goog.History');
goog.require('goog.history.Html5History');
goog.require('goog.history.Html5History.TokenTransformer');
goog.require('goog.history.EventType');
goog.require('goog.Uri');
pushy.core.on_click = (function pushy$core$on_click(funk){
return goog.events.listen(document,"click",funk);
});
/**
 * Traverses up the DOM tree and returns the first node that contains a href attr
 */
pushy.core.recur_href = (function pushy$core$recur_href(target){
if(cljs.core.truth_(target.href)){
return target;
} else {
if(cljs.core.truth_(target.parentNode)){
return pushy$core$recur_href.call(null,target.parentNode);
} else {
return null;
}
}
});
pushy.core.update_history_BANG_ = (function pushy$core$update_history_BANG_(h){
var G__9218 = h;
G__9218.setUseFragment(false);

G__9218.setPathPrefix("");

G__9218.setEnabled(true);

return G__9218;
});
pushy.core.set_retrieve_token_BANG_ = (function pushy$core$set_retrieve_token_BANG_(t){
t.retrieveToken = (function (path_prefix,location){
return [cljs.core.str(location.pathname),cljs.core.str(location.search)].join('');
});

return t;
});
pushy.core.set_create_url_BANG_ = (function pushy$core$set_create_url_BANG_(t){
t.createUrl = (function (token,path_prefix,location){
return [cljs.core.str(path_prefix),cljs.core.str(token)].join('');
});

return t;
});
pushy.core.new_history = (function pushy$core$new_history(var_args){
var args9219 = [];
var len__6677__auto___9222 = arguments.length;
var i__6678__auto___9223 = (0);
while(true){
if((i__6678__auto___9223 < len__6677__auto___9222)){
args9219.push((arguments[i__6678__auto___9223]));

var G__9224 = (i__6678__auto___9223 + (1));
i__6678__auto___9223 = G__9224;
continue;
} else {
}
break;
}

var G__9221 = args9219.length;
switch (G__9221) {
case 0:
return pushy.core.new_history.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return pushy.core.new_history.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args9219.length)].join('')));

}
});

pushy.core.new_history.cljs$core$IFn$_invoke$arity$0 = (function (){
return pushy.core.new_history.call(null,pushy.core.set_create_url_BANG_.call(null,pushy.core.set_retrieve_token_BANG_.call(null,(new goog.history.Html5History.TokenTransformer()))));
});

pushy.core.new_history.cljs$core$IFn$_invoke$arity$1 = (function (transformer){
return pushy.core.update_history_BANG_.call(null,(new goog.history.Html5History(window,transformer)));
});

pushy.core.new_history.cljs$lang$maxFixedArity = 1;


/**
 * @interface
 */
pushy.core.IHistory = function(){};

pushy.core.set_token_BANG_ = (function pushy$core$set_token_BANG_(var_args){
var args9226 = [];
var len__6677__auto___9232 = arguments.length;
var i__6678__auto___9233 = (0);
while(true){
if((i__6678__auto___9233 < len__6677__auto___9232)){
args9226.push((arguments[i__6678__auto___9233]));

var G__9234 = (i__6678__auto___9233 + (1));
i__6678__auto___9233 = G__9234;
continue;
} else {
}
break;
}

var G__9228 = args9226.length;
switch (G__9228) {
case 2:
return pushy.core.set_token_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return pushy.core.set_token_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args9226.length)].join('')));

}
});

pushy.core.set_token_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (this$,token){
if((!((this$ == null))) && (!((this$.pushy$core$IHistory$set_token_BANG_$arity$2 == null)))){
return this$.pushy$core$IHistory$set_token_BANG_$arity$2(this$,token);
} else {
var x__6265__auto__ = (((this$ == null))?null:this$);
var m__6266__auto__ = (pushy.core.set_token_BANG_[goog.typeOf(x__6265__auto__)]);
if(!((m__6266__auto__ == null))){
return m__6266__auto__.call(null,this$,token);
} else {
var m__6266__auto____$1 = (pushy.core.set_token_BANG_["_"]);
if(!((m__6266__auto____$1 == null))){
return m__6266__auto____$1.call(null,this$,token);
} else {
throw cljs.core.missing_protocol.call(null,"IHistory.set-token!",this$);
}
}
}
});

pushy.core.set_token_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (this$,token,title){
if((!((this$ == null))) && (!((this$.pushy$core$IHistory$set_token_BANG_$arity$3 == null)))){
return this$.pushy$core$IHistory$set_token_BANG_$arity$3(this$,token,title);
} else {
var x__6265__auto__ = (((this$ == null))?null:this$);
var m__6266__auto__ = (pushy.core.set_token_BANG_[goog.typeOf(x__6265__auto__)]);
if(!((m__6266__auto__ == null))){
return m__6266__auto__.call(null,this$,token,title);
} else {
var m__6266__auto____$1 = (pushy.core.set_token_BANG_["_"]);
if(!((m__6266__auto____$1 == null))){
return m__6266__auto____$1.call(null,this$,token,title);
} else {
throw cljs.core.missing_protocol.call(null,"IHistory.set-token!",this$);
}
}
}
});

pushy.core.set_token_BANG_.cljs$lang$maxFixedArity = 3;


pushy.core.replace_token_BANG_ = (function pushy$core$replace_token_BANG_(var_args){
var args9229 = [];
var len__6677__auto___9236 = arguments.length;
var i__6678__auto___9237 = (0);
while(true){
if((i__6678__auto___9237 < len__6677__auto___9236)){
args9229.push((arguments[i__6678__auto___9237]));

var G__9238 = (i__6678__auto___9237 + (1));
i__6678__auto___9237 = G__9238;
continue;
} else {
}
break;
}

var G__9231 = args9229.length;
switch (G__9231) {
case 2:
return pushy.core.replace_token_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return pushy.core.replace_token_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args9229.length)].join('')));

}
});

pushy.core.replace_token_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (this$,token){
if((!((this$ == null))) && (!((this$.pushy$core$IHistory$replace_token_BANG_$arity$2 == null)))){
return this$.pushy$core$IHistory$replace_token_BANG_$arity$2(this$,token);
} else {
var x__6265__auto__ = (((this$ == null))?null:this$);
var m__6266__auto__ = (pushy.core.replace_token_BANG_[goog.typeOf(x__6265__auto__)]);
if(!((m__6266__auto__ == null))){
return m__6266__auto__.call(null,this$,token);
} else {
var m__6266__auto____$1 = (pushy.core.replace_token_BANG_["_"]);
if(!((m__6266__auto____$1 == null))){
return m__6266__auto____$1.call(null,this$,token);
} else {
throw cljs.core.missing_protocol.call(null,"IHistory.replace-token!",this$);
}
}
}
});

pushy.core.replace_token_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (this$,token,title){
if((!((this$ == null))) && (!((this$.pushy$core$IHistory$replace_token_BANG_$arity$3 == null)))){
return this$.pushy$core$IHistory$replace_token_BANG_$arity$3(this$,token,title);
} else {
var x__6265__auto__ = (((this$ == null))?null:this$);
var m__6266__auto__ = (pushy.core.replace_token_BANG_[goog.typeOf(x__6265__auto__)]);
if(!((m__6266__auto__ == null))){
return m__6266__auto__.call(null,this$,token,title);
} else {
var m__6266__auto____$1 = (pushy.core.replace_token_BANG_["_"]);
if(!((m__6266__auto____$1 == null))){
return m__6266__auto____$1.call(null,this$,token,title);
} else {
throw cljs.core.missing_protocol.call(null,"IHistory.replace-token!",this$);
}
}
}
});

pushy.core.replace_token_BANG_.cljs$lang$maxFixedArity = 3;


pushy.core.get_token = (function pushy$core$get_token(this$){
if((!((this$ == null))) && (!((this$.pushy$core$IHistory$get_token$arity$1 == null)))){
return this$.pushy$core$IHistory$get_token$arity$1(this$);
} else {
var x__6265__auto__ = (((this$ == null))?null:this$);
var m__6266__auto__ = (pushy.core.get_token[goog.typeOf(x__6265__auto__)]);
if(!((m__6266__auto__ == null))){
return m__6266__auto__.call(null,this$);
} else {
var m__6266__auto____$1 = (pushy.core.get_token["_"]);
if(!((m__6266__auto____$1 == null))){
return m__6266__auto____$1.call(null,this$);
} else {
throw cljs.core.missing_protocol.call(null,"IHistory.get-token",this$);
}
}
}
});

pushy.core.start_BANG_ = (function pushy$core$start_BANG_(this$){
if((!((this$ == null))) && (!((this$.pushy$core$IHistory$start_BANG_$arity$1 == null)))){
return this$.pushy$core$IHistory$start_BANG_$arity$1(this$);
} else {
var x__6265__auto__ = (((this$ == null))?null:this$);
var m__6266__auto__ = (pushy.core.start_BANG_[goog.typeOf(x__6265__auto__)]);
if(!((m__6266__auto__ == null))){
return m__6266__auto__.call(null,this$);
} else {
var m__6266__auto____$1 = (pushy.core.start_BANG_["_"]);
if(!((m__6266__auto____$1 == null))){
return m__6266__auto____$1.call(null,this$);
} else {
throw cljs.core.missing_protocol.call(null,"IHistory.start!",this$);
}
}
}
});

pushy.core.stop_BANG_ = (function pushy$core$stop_BANG_(this$){
if((!((this$ == null))) && (!((this$.pushy$core$IHistory$stop_BANG_$arity$1 == null)))){
return this$.pushy$core$IHistory$stop_BANG_$arity$1(this$);
} else {
var x__6265__auto__ = (((this$ == null))?null:this$);
var m__6266__auto__ = (pushy.core.stop_BANG_[goog.typeOf(x__6265__auto__)]);
if(!((m__6266__auto__ == null))){
return m__6266__auto__.call(null,this$);
} else {
var m__6266__auto____$1 = (pushy.core.stop_BANG_["_"]);
if(!((m__6266__auto____$1 == null))){
return m__6266__auto____$1.call(null,this$);
} else {
throw cljs.core.missing_protocol.call(null,"IHistory.stop!",this$);
}
}
}
});

pushy.core.processable_url_QMARK_ = (function pushy$core$processable_url_QMARK_(uri){
return (!(clojure.string.blank_QMARK_.call(null,uri))) && (((cljs.core.not.call(null,uri.hasScheme())) && (cljs.core.not.call(null,uri.hasDomain()))) || (cljs.core.some_QMARK_.call(null,cljs.core.re_matches.call(null,cljs.core.re_pattern.call(null,[cljs.core.str("^"),cljs.core.str(location.origin),cljs.core.str(".*$")].join('')),[cljs.core.str(uri)].join('')))));
});
pushy.core.get_token_from_uri = (function pushy$core$get_token_from_uri(uri){
var path = uri.getPath();
var query = uri.getQuery();
if(cljs.core.empty_QMARK_.call(null,query)){
return path;
} else {
return [cljs.core.str(path),cljs.core.str("?"),cljs.core.str(query)].join('');
}
});
/**
 * Takes in three functions:
 *  * dispatch-fn: the function that dispatches when a match is found
 *  * match-fn: the function used to check if a particular route exists
 *  * identity-fn: (optional) extract the route from value returned by match-fn
 */
pushy.core.pushy = (function pushy$core$pushy(var_args){
var args__6684__auto__ = [];
var len__6677__auto___9253 = arguments.length;
var i__6678__auto___9254 = (0);
while(true){
if((i__6678__auto___9254 < len__6677__auto___9253)){
args__6684__auto__.push((arguments[i__6678__auto___9254]));

var G__9255 = (i__6678__auto___9254 + (1));
i__6678__auto___9254 = G__9255;
continue;
} else {
}
break;
}

var argseq__6685__auto__ = ((((2) < args__6684__auto__.length))?(new cljs.core.IndexedSeq(args__6684__auto__.slice((2)),(0),null)):null);
return pushy.core.pushy.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__6685__auto__);
});

pushy.core.pushy.cljs$core$IFn$_invoke$arity$variadic = (function (dispatch_fn,match_fn,p__9243){
var map__9244 = p__9243;
var map__9244__$1 = ((((!((map__9244 == null)))?((((map__9244.cljs$lang$protocol_mask$partition0$ & (64))) || (map__9244.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__9244):map__9244);
var processable_url_QMARK_ = cljs.core.get.call(null,map__9244__$1,new cljs.core.Keyword(null,"processable-url?","processable-url?",1865408336),pushy.core.processable_url_QMARK_);
var identity_fn = cljs.core.get.call(null,map__9244__$1,new cljs.core.Keyword(null,"identity-fn","identity-fn",-884182627),cljs.core.identity);
var history = pushy.core.new_history.call(null);
var event_keys = cljs.core.atom.call(null,null);
if(typeof pushy.core.t_pushy$core9246 !== 'undefined'){
} else {

/**
* @constructor
 * @implements {pushy.core.IHistory}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
pushy.core.t_pushy$core9246 = (function (dispatch_fn,match_fn,p__9243,map__9244,processable_url_QMARK_,identity_fn,history,event_keys,meta9247){
this.dispatch_fn = dispatch_fn;
this.match_fn = match_fn;
this.p__9243 = p__9243;
this.map__9244 = map__9244;
this.processable_url_QMARK_ = processable_url_QMARK_;
this.identity_fn = identity_fn;
this.history = history;
this.event_keys = event_keys;
this.meta9247 = meta9247;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
})
pushy.core.t_pushy$core9246.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (_9248,meta9247__$1){
var self__ = this;
var _9248__$1 = this;
return (new pushy.core.t_pushy$core9246(self__.dispatch_fn,self__.match_fn,self__.p__9243,self__.map__9244,self__.processable_url_QMARK_,self__.identity_fn,self__.history,self__.event_keys,meta9247__$1));
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

pushy.core.t_pushy$core9246.prototype.cljs$core$IMeta$_meta$arity$1 = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (_9248){
var self__ = this;
var _9248__$1 = this;
return self__.meta9247;
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

pushy.core.t_pushy$core9246.prototype.pushy$core$IHistory$ = true;

pushy.core.t_pushy$core9246.prototype.pushy$core$IHistory$set_token_BANG_$arity$2 = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (_,token){
var self__ = this;
var ___$1 = this;
return self__.history.setToken(token);
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

pushy.core.t_pushy$core9246.prototype.pushy$core$IHistory$set_token_BANG_$arity$3 = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (_,token,title){
var self__ = this;
var ___$1 = this;
return self__.history.setToken(token,title);
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

pushy.core.t_pushy$core9246.prototype.pushy$core$IHistory$replace_token_BANG_$arity$2 = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (_,token){
var self__ = this;
var ___$1 = this;
return self__.history.replaceToken(token);
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

pushy.core.t_pushy$core9246.prototype.pushy$core$IHistory$replace_token_BANG_$arity$3 = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (_,token,title){
var self__ = this;
var ___$1 = this;
return self__.history.replaceToken(token,title);
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

pushy.core.t_pushy$core9246.prototype.pushy$core$IHistory$get_token$arity$1 = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (_){
var self__ = this;
var ___$1 = this;
return self__.history.getToken();
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

pushy.core.t_pushy$core9246.prototype.pushy$core$IHistory$start_BANG_$arity$1 = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (this$){
var self__ = this;
var this$__$1 = this;
pushy.core.stop_BANG_.call(null,this$__$1);

cljs.core.swap_BANG_.call(null,self__.event_keys,cljs.core.conj,goog.events.listen(self__.history,goog.history.EventType.NAVIGATE,((function (this$__$1,history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (e){
var temp__4657__auto__ = self__.identity_fn.call(null,self__.match_fn.call(null,e.token));
if(cljs.core.truth_(temp__4657__auto__)){
var match = temp__4657__auto__;
return self__.dispatch_fn.call(null,match);
} else {
return null;
}
});})(this$__$1,history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
));

var temp__4657__auto___9256 = self__.identity_fn.call(null,self__.match_fn.call(null,pushy.core.get_token.call(null,this$__$1)));
if(cljs.core.truth_(temp__4657__auto___9256)){
var match_9257 = temp__4657__auto___9256;
self__.dispatch_fn.call(null,match_9257);
} else {
}

cljs.core.swap_BANG_.call(null,self__.event_keys,cljs.core.conj,pushy.core.on_click.call(null,((function (this$__$1,history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (e){
var temp__4657__auto__ = pushy.core.recur_href.call(null,e.target);
if(cljs.core.truth_(temp__4657__auto__)){
var el = temp__4657__auto__;
var uri = goog.Uri.parse(el.href);
if(cljs.core.truth_((function (){var and__5590__auto__ = self__.processable_url_QMARK_.call(null,uri);
if(cljs.core.truth_(and__5590__auto__)){
return (cljs.core.not.call(null,e.altKey)) && (cljs.core.not.call(null,e.ctrlKey)) && (cljs.core.not.call(null,e.metaKey)) && (cljs.core.not.call(null,e.shiftKey)) && (cljs.core.not.call(null,cljs.core.get.call(null,new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["_self",null,"_blank",null], null), null),el.getAttribute("target")))) && (cljs.core.not_EQ_.call(null,(1),e.button));
} else {
return and__5590__auto__;
}
})())){
var next_token = pushy.core.get_token_from_uri.call(null,uri);
if(cljs.core.truth_(self__.identity_fn.call(null,self__.match_fn.call(null,next_token)))){
var temp__4655__auto___9258 = el.title;
if(cljs.core.truth_(temp__4655__auto___9258)){
var title_9259 = temp__4655__auto___9258;
pushy.core.set_token_BANG_.call(null,this$__$1,next_token,title_9259);
} else {
pushy.core.set_token_BANG_.call(null,this$__$1,next_token);
}

return e.preventDefault();
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
});})(this$__$1,history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
));

return null;
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

pushy.core.t_pushy$core9246.prototype.pushy$core$IHistory$stop_BANG_$arity$1 = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (this$){
var self__ = this;
var this$__$1 = this;
var seq__9249_9260 = cljs.core.seq.call(null,cljs.core.deref.call(null,self__.event_keys));
var chunk__9250_9261 = null;
var count__9251_9262 = (0);
var i__9252_9263 = (0);
while(true){
if((i__9252_9263 < count__9251_9262)){
var key_9264 = cljs.core._nth.call(null,chunk__9250_9261,i__9252_9263);
goog.events.unlistenByKey(key_9264);

var G__9265 = seq__9249_9260;
var G__9266 = chunk__9250_9261;
var G__9267 = count__9251_9262;
var G__9268 = (i__9252_9263 + (1));
seq__9249_9260 = G__9265;
chunk__9250_9261 = G__9266;
count__9251_9262 = G__9267;
i__9252_9263 = G__9268;
continue;
} else {
var temp__4657__auto___9269 = cljs.core.seq.call(null,seq__9249_9260);
if(temp__4657__auto___9269){
var seq__9249_9270__$1 = temp__4657__auto___9269;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__9249_9270__$1)){
var c__6413__auto___9271 = cljs.core.chunk_first.call(null,seq__9249_9270__$1);
var G__9272 = cljs.core.chunk_rest.call(null,seq__9249_9270__$1);
var G__9273 = c__6413__auto___9271;
var G__9274 = cljs.core.count.call(null,c__6413__auto___9271);
var G__9275 = (0);
seq__9249_9260 = G__9272;
chunk__9250_9261 = G__9273;
count__9251_9262 = G__9274;
i__9252_9263 = G__9275;
continue;
} else {
var key_9276 = cljs.core.first.call(null,seq__9249_9270__$1);
goog.events.unlistenByKey(key_9276);

var G__9277 = cljs.core.next.call(null,seq__9249_9270__$1);
var G__9278 = null;
var G__9279 = (0);
var G__9280 = (0);
seq__9249_9260 = G__9277;
chunk__9250_9261 = G__9278;
count__9251_9262 = G__9279;
i__9252_9263 = G__9280;
continue;
}
} else {
}
}
break;
}

return cljs.core.reset_BANG_.call(null,self__.event_keys,null);
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

pushy.core.t_pushy$core9246.getBasis = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (){
return new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"dispatch-fn","dispatch-fn",-1401088155,null),new cljs.core.Symbol(null,"match-fn","match-fn",-795226853,null),new cljs.core.Symbol(null,"p__9243","p__9243",-856210316,null),new cljs.core.Symbol(null,"map__9244","map__9244",2048423732,null),new cljs.core.Symbol(null,"processable-url?","processable-url?",-789027433,null),new cljs.core.Symbol(null,"identity-fn","identity-fn",756348900,null),new cljs.core.Symbol(null,"history","history",1393136307,null),new cljs.core.Symbol(null,"event-keys","event-keys",804564896,null),new cljs.core.Symbol(null,"meta9247","meta9247",-2121322964,null)], null);
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

pushy.core.t_pushy$core9246.cljs$lang$type = true;

pushy.core.t_pushy$core9246.cljs$lang$ctorStr = "pushy.core/t_pushy$core9246";

pushy.core.t_pushy$core9246.cljs$lang$ctorPrWriter = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function (this__6208__auto__,writer__6209__auto__,opt__6210__auto__){
return cljs.core._write.call(null,writer__6209__auto__,"pushy.core/t_pushy$core9246");
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

pushy.core.__GT_t_pushy$core9246 = ((function (history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn){
return (function pushy$core$__GT_t_pushy$core9246(dispatch_fn__$1,match_fn__$1,p__9243__$1,map__9244__$2,processable_url_QMARK___$1,identity_fn__$1,history__$1,event_keys__$1,meta9247){
return (new pushy.core.t_pushy$core9246(dispatch_fn__$1,match_fn__$1,p__9243__$1,map__9244__$2,processable_url_QMARK___$1,identity_fn__$1,history__$1,event_keys__$1,meta9247));
});})(history,event_keys,map__9244,map__9244__$1,processable_url_QMARK_,identity_fn))
;

}

return (new pushy.core.t_pushy$core9246(dispatch_fn,match_fn,p__9243,map__9244__$1,processable_url_QMARK_,identity_fn,history,event_keys,cljs.core.PersistentArrayMap.EMPTY));
});

pushy.core.pushy.cljs$lang$maxFixedArity = (2);

pushy.core.pushy.cljs$lang$applyTo = (function (seq9240){
var G__9241 = cljs.core.first.call(null,seq9240);
var seq9240__$1 = cljs.core.next.call(null,seq9240);
var G__9242 = cljs.core.first.call(null,seq9240__$1);
var seq9240__$2 = cljs.core.next.call(null,seq9240__$1);
return pushy.core.pushy.cljs$core$IFn$_invoke$arity$variadic(G__9241,G__9242,seq9240__$2);
});

/**
 * Returns whether Html5History is supported
 */
pushy.core.supported_QMARK_ = (function pushy$core$supported_QMARK_(var_args){
var args9281 = [];
var len__6677__auto___9284 = arguments.length;
var i__6678__auto___9285 = (0);
while(true){
if((i__6678__auto___9285 < len__6677__auto___9284)){
args9281.push((arguments[i__6678__auto___9285]));

var G__9286 = (i__6678__auto___9285 + (1));
i__6678__auto___9285 = G__9286;
continue;
} else {
}
break;
}

var G__9283 = args9281.length;
switch (G__9283) {
case 0:
return pushy.core.supported_QMARK_.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return pushy.core.supported_QMARK_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args9281.length)].join('')));

}
});

pushy.core.supported_QMARK_.cljs$core$IFn$_invoke$arity$0 = (function (){
return pushy.core.supported_QMARK_.call(null,window);
});

pushy.core.supported_QMARK_.cljs$core$IFn$_invoke$arity$1 = (function (window){
return goog.history.Html5History.isSupported(window);
});

pushy.core.supported_QMARK_.cljs$lang$maxFixedArity = 1;

pushy.core.push_state_BANG_ = (function pushy$core$push_state_BANG_(var_args){
var args9288 = [];
var len__6677__auto___9291 = arguments.length;
var i__6678__auto___9292 = (0);
while(true){
if((i__6678__auto___9292 < len__6677__auto___9291)){
args9288.push((arguments[i__6678__auto___9292]));

var G__9293 = (i__6678__auto___9292 + (1));
i__6678__auto___9292 = G__9293;
continue;
} else {
}
break;
}

var G__9290 = args9288.length;
switch (G__9290) {
case 2:
return pushy.core.push_state_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return pushy.core.push_state_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args9288.length)].join('')));

}
});

pushy.core.push_state_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (dispatch_fn,match_fn){
return pushy.core.push_state_BANG_.call(null,dispatch_fn,match_fn,cljs.core.identity);
});

pushy.core.push_state_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (dispatch_fn,match_fn,identity_fn){
var h = pushy.core.pushy.call(null,dispatch_fn,match_fn,new cljs.core.Keyword(null,"identity-fn","identity-fn",-884182627),identity_fn);
pushy.core.start_BANG_.call(null,h);

return h;
});

pushy.core.push_state_BANG_.cljs$lang$maxFixedArity = 3;


//# sourceMappingURL=core.js.map?rel=1483597753966