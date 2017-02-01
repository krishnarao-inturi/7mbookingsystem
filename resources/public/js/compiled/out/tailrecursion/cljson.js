// Compiled by ClojureScript 1.9.89 {}
goog.provide('tailrecursion.cljson');
goog.require('cljs.core');
goog.require('cljs.reader');
goog.require('goog.date.DateTime');
goog.require('clojure.string');


/**
 * Encode a cljs thing o as a JS tagged literal of the form {tag: value}, where
 *   value is composed of JS objects that can be encoded as JSON.
 * @interface
 */
tailrecursion.cljson.EncodeTagged = function(){};

tailrecursion.cljson._encode = (function tailrecursion$cljson$_encode(o){
if((!((o == null))) && (!((o.tailrecursion$cljson$EncodeTagged$_encode$arity$1 == null)))){
return o.tailrecursion$cljson$EncodeTagged$_encode$arity$1(o);
} else {
var x__6265__auto__ = (((o == null))?null:o);
var m__6266__auto__ = (tailrecursion.cljson._encode[goog.typeOf(x__6265__auto__)]);
if(!((m__6266__auto__ == null))){
return m__6266__auto__.call(null,o);
} else {
var m__6266__auto____$1 = (tailrecursion.cljson._encode["_"]);
if(!((m__6266__auto____$1 == null))){
return m__6266__auto____$1.call(null,o);
} else {
throw cljs.core.missing_protocol.call(null,"EncodeTagged.-encode",o);
}
}
}
});

/**
 * Convert clj data to JSON string.
 */
tailrecursion.cljson.clj__GT_cljson = (function tailrecursion$cljson$clj__GT_cljson(v){
return JSON.stringify(tailrecursion.cljson.encode.call(null,v));
});
/**
 * Convert JSON string to clj data.
 */
tailrecursion.cljson.cljson__GT_clj = (function tailrecursion$cljson$cljson__GT_clj(s){
return tailrecursion.cljson.decode.call(null,JSON.parse(s));
});
tailrecursion.cljson.enc_coll = (function tailrecursion$cljson$enc_coll(tag,val){
var len = cljs.core.count.call(null,val);
var out = [tag];
var i = (0);
var c = val;
while(true){
if((i < len)){
var i__$1 = (i + (1));
(out[i__$1] = cljs.core.first.call(null,c));

var G__9178 = i__$1;
var G__9179 = cljs.core.rest.call(null,c);
i = G__9178;
c = G__9179;
continue;
} else {
return out;
}
break;
}
});
Date.prototype.tailrecursion$cljson$EncodeTagged$ = true;

Date.prototype.tailrecursion$cljson$EncodeTagged$_encode$arity$1 = (function (o){
var o__$1 = this;
return ["inst",cljs.core.subs.call(null,cljs.core.pr_str.call(null,o__$1),(7),(36))];
});

cljs.core.UUID.prototype.tailrecursion$cljson$EncodeTagged$ = true;

cljs.core.UUID.prototype.tailrecursion$cljson$EncodeTagged$_encode$arity$1 = (function (o){
var o__$1 = this;
return ["uuid",o__$1.uuid];
});
/**
 * Attempts to encode an object that does not satisfy EncodeTagged,
 *   but for which the printed representation contains a tag.
 */
tailrecursion.cljson.interpret = (function tailrecursion$cljson$interpret(x){
var temp__4657__auto__ = cljs.core.second.call(null,cljs.core.re_matches.call(null,/#([^<].*)/,cljs.core.pr_str.call(null,x)));
if(cljs.core.truth_(temp__4657__auto__)){
var match = temp__4657__auto__;
var tag = cljs.reader.read_string.call(null,match);
var val = cljs.reader.read_string.call(null,cljs.core.subs.call(null,match,[cljs.core.str(tag)].join('').length));
return [[cljs.core.str(tag)].join(''),tailrecursion.cljson.encode.call(null,val)];
} else {
return null;
}
});
tailrecursion.cljson.encode = (function tailrecursion$cljson$encode(x){
var temp__4655__auto__ = (function (){var and__5590__auto__ = cljs.core._STAR_print_meta_STAR_;
if(cljs.core.truth_(and__5590__auto__)){
return cljs.core.meta.call(null,x);
} else {
return and__5590__auto__;
}
})();
if(cljs.core.truth_(temp__4655__auto__)){
var m = temp__4655__auto__;
return ["z",tailrecursion$cljson$encode.call(null,m),tailrecursion$cljson$encode.call(null,cljs.core.with_meta.call(null,x,null))];
} else {
if(((!((x == null)))?(((false) || (x.tailrecursion$cljson$EncodeTagged$))?true:(((!x.cljs$lang$protocol_mask$partition$))?cljs.core.native_satisfies_QMARK_.call(null,tailrecursion.cljson.EncodeTagged,x):false)):cljs.core.native_satisfies_QMARK_.call(null,tailrecursion.cljson.EncodeTagged,x))){
return tailrecursion.cljson._encode.call(null,x);
} else {
if((x instanceof cljs.core.Keyword)){
return ["k",cljs.core.subs.call(null,[cljs.core.str(x)].join(''),(1))];
} else {
if((x instanceof cljs.core.Symbol)){
return ["y",[cljs.core.str(x)].join('')];
} else {
if(cljs.core.vector_QMARK_.call(null,x)){
return tailrecursion.cljson.enc_coll.call(null,"v",cljs.core.map.call(null,tailrecursion$cljson$encode,x));
} else {
if(cljs.core.seq_QMARK_.call(null,x)){
return tailrecursion.cljson.enc_coll.call(null,"l",cljs.core.map.call(null,tailrecursion$cljson$encode,x));
} else {
if((cljs.core.map_QMARK_.call(null,x)) && (!(((!((x == null)))?((((x.cljs$lang$protocol_mask$partition0$ & (67108864))) || (x.cljs$core$IRecord$))?true:(((!x.cljs$lang$protocol_mask$partition0$))?cljs.core.native_satisfies_QMARK_.call(null,cljs.core.IRecord,x):false)):cljs.core.native_satisfies_QMARK_.call(null,cljs.core.IRecord,x))))){
return tailrecursion.cljson.enc_coll.call(null,"m",cljs.core.map.call(null,tailrecursion$cljson$encode,cljs.core.apply.call(null,cljs.core.concat,x)));
} else {
if(cljs.core.set_QMARK_.call(null,x)){
return tailrecursion.cljson.enc_coll.call(null,"s",cljs.core.map.call(null,tailrecursion$cljson$encode,x));
} else {
if((typeof x === 'string') || (typeof x === 'number') || ((x == null)) || (cljs.core._EQ_.call(null,true,x)) || (cljs.core._EQ_.call(null,false,x))){
return x;
} else {
var or__5602__auto__ = tailrecursion.cljson.interpret.call(null,x);
if(cljs.core.truth_(or__5602__auto__)){
return or__5602__auto__;
} else {
throw (new Error([cljs.core.str("No cljson encoding for type '"),cljs.core.str(cljs.core.type.call(null,x)),cljs.core.str("'.")].join('')));
}

}
}
}
}
}
}
}
}
}
});
tailrecursion.cljson.decode_tagged = (function tailrecursion$cljson$decode_tagged(o){
var tag = (o[(0)]);
var G__9187 = tag;
switch (G__9187) {
case "v":
var i = (1);
var len = o.length;
var out = cljs.core.transient$.call(null,cljs.core.PersistentVector.EMPTY);
while(true){
if((i < len)){
var G__9189 = (i + (1));
var G__9190 = len;
var G__9191 = cljs.core.conj_BANG_.call(null,out,tailrecursion.cljson.decode.call(null,(o[i])));
i = G__9189;
len = G__9190;
out = G__9191;
continue;
} else {
return cljs.core.persistent_BANG_.call(null,out);
}
break;
}

break;
case "m":
var i = (1);
var len = o.length;
var out = cljs.core.transient$.call(null,cljs.core.PersistentArrayMap.EMPTY);
while(true){
if((i < len)){
var G__9192 = (i + (2));
var G__9193 = len;
var G__9194 = cljs.core.assoc_BANG_.call(null,out,tailrecursion.cljson.decode.call(null,(o[i])),tailrecursion.cljson.decode.call(null,(o[(i + (1))])));
i = G__9192;
len = G__9193;
out = G__9194;
continue;
} else {
return cljs.core.persistent_BANG_.call(null,out);
}
break;
}

break;
case "l":
var i = (o.length - (1));
var out = cljs.core.List.EMPTY;
while(true){
if((i > (0))){
var G__9195 = (i - (1));
var G__9196 = cljs.core.conj.call(null,out,tailrecursion.cljson.decode.call(null,(o[i])));
i = G__9195;
out = G__9196;
continue;
} else {
return out;
}
break;
}

break;
case "s":
var i = (1);
var len = o.length;
var out = cljs.core.transient$.call(null,cljs.core.PersistentHashSet.EMPTY);
while(true){
if((i < len)){
var G__9197 = (i + (1));
var G__9198 = len;
var G__9199 = cljs.core.conj_BANG_.call(null,out,tailrecursion.cljson.decode.call(null,(o[i])));
i = G__9197;
len = G__9198;
out = G__9199;
continue;
} else {
return cljs.core.persistent_BANG_.call(null,out);
}
break;
}

break;
case "k":
return cljs.core.keyword.call(null,(o[(1)]));

break;
case "y":
var val = (o[(1)]);
var idx = val.indexOf("/");
if((idx < (0))){
return cljs.core.symbol.call(null,val);
} else {
return cljs.core.symbol.call(null,val.slice((0),idx),val.slice((idx + (1))));
}

break;
case "z":
var m = tailrecursion.cljson.decode.call(null,(o[(1)]));
var v = tailrecursion.cljson.decode.call(null,(o[(2)]));
return cljs.core.with_meta.call(null,v,m);

break;
default:
var temp__4655__auto__ = (function (){var or__5602__auto__ = cljs.core.get.call(null,cljs.core.deref.call(null,cljs.reader._STAR_tag_table_STAR_),tag);
if(cljs.core.truth_(or__5602__auto__)){
return or__5602__auto__;
} else {
return cljs.core.deref.call(null,cljs.reader._STAR_default_data_reader_fn_STAR_);
}
})();
if(cljs.core.truth_(temp__4655__auto__)){
var reader = temp__4655__auto__;
return reader.call(null,tailrecursion.cljson.decode.call(null,(o[(1)])));
} else {
throw (new Error([cljs.core.str("No reader function for tag '"),cljs.core.str(tag),cljs.core.str("'.")].join('')));
}

}
});
tailrecursion.cljson.decode = (function tailrecursion$cljson$decode(v){
if(cljs.core.array_QMARK_.call(null,v)){
return tailrecursion.cljson.decode_tagged.call(null,v);
} else {
return v;
}
});

//# sourceMappingURL=cljson.js.map?rel=1483597753790