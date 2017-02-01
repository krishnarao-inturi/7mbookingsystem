// Compiled by ClojureScript 1.9.89 {}
goog.provide('ajax.xml_http_request');
goog.require('cljs.core');
goog.require('ajax.protocols');
ajax.xml_http_request.ready_state = (function ajax$xml_http_request$ready_state(e){
return new cljs.core.PersistentArrayMap(null, 5, [(0),new cljs.core.Keyword(null,"not-initialized","not-initialized",-1937378906),(1),new cljs.core.Keyword(null,"connection-established","connection-established",-1403749733),(2),new cljs.core.Keyword(null,"request-received","request-received",2110590540),(3),new cljs.core.Keyword(null,"processing-request","processing-request",-264947221),(4),new cljs.core.Keyword(null,"response-ready","response-ready",245208276)], null).call(null,e.target.readyState);
});
XMLHttpRequest.prototype.ajax$protocols$AjaxImpl$ = true;

XMLHttpRequest.prototype.ajax$protocols$AjaxImpl$_js_ajax_request$arity$3 = (function (this$,p__7453,handler){
var map__7454 = p__7453;
var map__7454__$1 = ((((!((map__7454 == null)))?((((map__7454.cljs$lang$protocol_mask$partition0$ & (64))) || (map__7454.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.call(null,cljs.core.hash_map,map__7454):map__7454);
var uri = cljs.core.get.call(null,map__7454__$1,new cljs.core.Keyword(null,"uri","uri",-774711847));
var method = cljs.core.get.call(null,map__7454__$1,new cljs.core.Keyword(null,"method","method",55703592));
var body = cljs.core.get.call(null,map__7454__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var headers = cljs.core.get.call(null,map__7454__$1,new cljs.core.Keyword(null,"headers","headers",-835030129));
var timeout = cljs.core.get.call(null,map__7454__$1,new cljs.core.Keyword(null,"timeout","timeout",-318625318),(0));
var with_credentials = cljs.core.get.call(null,map__7454__$1,new cljs.core.Keyword(null,"with-credentials","with-credentials",-1163127235),false);
var response_format = cljs.core.get.call(null,map__7454__$1,new cljs.core.Keyword(null,"response-format","response-format",1664465322));
var this$__$1 = this;
this$__$1.withCredentials = with_credentials;

this$__$1.onreadystatechange = ((function (this$__$1,map__7454,map__7454__$1,uri,method,body,headers,timeout,with_credentials,response_format){
return (function (p1__7452_SHARP_){
if(cljs.core._EQ_.call(null,new cljs.core.Keyword(null,"response-ready","response-ready",245208276),ajax.xml_http_request.ready_state.call(null,p1__7452_SHARP_))){
return handler.call(null,this$__$1);
} else {
return null;
}
});})(this$__$1,map__7454,map__7454__$1,uri,method,body,headers,timeout,with_credentials,response_format))
;

this$__$1.open(method,uri,true);

this$__$1.timeout = timeout;

var temp__4657__auto___7466 = new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(response_format);
if(cljs.core.truth_(temp__4657__auto___7466)){
var response_type_7467 = temp__4657__auto___7466;
this$__$1.responseType = cljs.core.name.call(null,response_type_7467);
} else {
}

var seq__7456_7468 = cljs.core.seq.call(null,headers);
var chunk__7457_7469 = null;
var count__7458_7470 = (0);
var i__7459_7471 = (0);
while(true){
if((i__7459_7471 < count__7458_7470)){
var vec__7460_7472 = cljs.core._nth.call(null,chunk__7457_7469,i__7459_7471);
var k_7473 = cljs.core.nth.call(null,vec__7460_7472,(0),null);
var v_7474 = cljs.core.nth.call(null,vec__7460_7472,(1),null);
this$__$1.setRequestHeader(k_7473,v_7474);

var G__7475 = seq__7456_7468;
var G__7476 = chunk__7457_7469;
var G__7477 = count__7458_7470;
var G__7478 = (i__7459_7471 + (1));
seq__7456_7468 = G__7475;
chunk__7457_7469 = G__7476;
count__7458_7470 = G__7477;
i__7459_7471 = G__7478;
continue;
} else {
var temp__4657__auto___7479 = cljs.core.seq.call(null,seq__7456_7468);
if(temp__4657__auto___7479){
var seq__7456_7480__$1 = temp__4657__auto___7479;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__7456_7480__$1)){
var c__6413__auto___7481 = cljs.core.chunk_first.call(null,seq__7456_7480__$1);
var G__7482 = cljs.core.chunk_rest.call(null,seq__7456_7480__$1);
var G__7483 = c__6413__auto___7481;
var G__7484 = cljs.core.count.call(null,c__6413__auto___7481);
var G__7485 = (0);
seq__7456_7468 = G__7482;
chunk__7457_7469 = G__7483;
count__7458_7470 = G__7484;
i__7459_7471 = G__7485;
continue;
} else {
var vec__7463_7486 = cljs.core.first.call(null,seq__7456_7480__$1);
var k_7487 = cljs.core.nth.call(null,vec__7463_7486,(0),null);
var v_7488 = cljs.core.nth.call(null,vec__7463_7486,(1),null);
this$__$1.setRequestHeader(k_7487,v_7488);

var G__7489 = cljs.core.next.call(null,seq__7456_7480__$1);
var G__7490 = null;
var G__7491 = (0);
var G__7492 = (0);
seq__7456_7468 = G__7489;
chunk__7457_7469 = G__7490;
count__7458_7470 = G__7491;
i__7459_7471 = G__7492;
continue;
}
} else {
}
}
break;
}

this$__$1.send((function (){var or__5602__auto__ = body;
if(cljs.core.truth_(or__5602__auto__)){
return or__5602__auto__;
} else {
return "";
}
})());

return this$__$1;
});

XMLHttpRequest.prototype.ajax$protocols$AjaxRequest$ = true;

XMLHttpRequest.prototype.ajax$protocols$AjaxRequest$_abort$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1.abort();
});

XMLHttpRequest.prototype.ajax$protocols$AjaxResponse$ = true;

XMLHttpRequest.prototype.ajax$protocols$AjaxResponse$_body$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1.response;
});

XMLHttpRequest.prototype.ajax$protocols$AjaxResponse$_status$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1.status;
});

XMLHttpRequest.prototype.ajax$protocols$AjaxResponse$_status_text$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1.statusText;
});

XMLHttpRequest.prototype.ajax$protocols$AjaxResponse$_get_response_header$arity$2 = (function (this$,header){
var this$__$1 = this;
return this$__$1.getResponseHeader(header);
});

XMLHttpRequest.prototype.ajax$protocols$AjaxResponse$_was_aborted$arity$1 = (function (this$){
var this$__$1 = this;
return cljs.core._EQ_.call(null,(0),this$__$1.readyState);
});

//# sourceMappingURL=xml_http_request.js.map?rel=1483597751010