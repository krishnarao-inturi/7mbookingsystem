// Compiled by ClojureScript 1.9.89 {}
goog.provide('sevenm_booking_system.views');
goog.require('cljs.core');
goog.require('ajax.core');
goog.require('sevenm_booking_system.components');
goog.require('reagent.core');
goog.require('sevenm_booking_system.routes');
goog.require('sevenm_booking_system.activitylog');
goog.require('sevenm_booking_system.config');
goog.require('pushy.core');
goog.require('cljs.reader');
goog.require('re_frame.core');
sevenm_booking_system.views.set_div_to_disabled = (function sevenm_booking_system$views$set_div_to_disabled(star_rating_id){
$(".box-primary :input").attr("disabled",true);

return $([cljs.core.str("#"),cljs.core.str(star_rating_id)].join('')).rating("refresh",cljs.core.clj__GT_js.call(null,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"disabled","disabled",-1529784218),true,new cljs.core.Keyword(null,"showClear","showClear",-629942944),false,new cljs.core.Keyword(null,"showCaption","showCaption",1392635014),true], null)));
});
sevenm_booking_system.views.tourgide_set_div_disabled = (function sevenm_booking_system$views$tourgide_set_div_disabled(star_rating_id){
console.log("Hi");

$("#PerHours").prop("disabled",true).trigger("chosen:updated");

$(".box-primary :input").attr("disabled",true);

return $([cljs.core.str("#"),cljs.core.str(star_rating_id)].join('')).rating("refresh",cljs.core.clj__GT_js.call(null,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"disabled","disabled",-1529784218),true,new cljs.core.Keyword(null,"showClear","showClear",-629942944),false,new cljs.core.Keyword(null,"showCaption","showCaption",1392635014),true], null)));
});
sevenm_booking_system.views.unauthorized_panel = (function sevenm_booking_system$views$unauthorized_panel(){
return (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.content-wrapper","div.content-wrapper",-1101894014),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section.content-header","section.content-header",1755602328),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h1","h1",-1896887462),"\n        404 Error Page\n      "], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section.content","section.content",870025353),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.error-page","div.error-page",-1950439607),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h2.headline.text-yellow","h2.headline.text-yellow",-1328894361)," 404"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.error-content","div.error-content",-1090164587),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h3","h3",2067611163),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"i.fa.fa-warning.text-yellow","i.fa.fa-warning.text-yellow",1627019939)], null)," Oops! Page not found."], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p","p",151049309),"\n We could not find the page you were looking for.\n\n          Meanwhile, you may ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"href","href",-793805698),sevenm_booking_system.routes.url_for.call(null,new cljs.core.Keyword(null,"home","home",-74557309))], null),"return to login"], null)], null)], null)], null)], null)], null);
});
});
sevenm_booking_system.views.login_page = (function sevenm_booking_system$views$login_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.login_comp], null)], null);
});
sevenm_booking_system.views.register_page = (function sevenm_booking_system$views$register_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.register_comp], null)], null);
});
sevenm_booking_system.views.fit_quotations_page = (function sevenm_booking_system$views$fit_quotations_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.fit_quotations_comp], null)], null)], null);
});
sevenm_booking_system.views.group_quotations_page = (function sevenm_booking_system$views$group_quotations_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.group_quotations_comp], null)], null)], null);
});
sevenm_booking_system.views.fitbookings_page = (function sevenm_booking_system$views$fitbookings_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.fitbookings_table_comp], null)], null)], null);
});
sevenm_booking_system.views.add_fitbooking_page = (function sevenm_booking_system$views$add_fitbooking_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_fitbooking,sevenm_booking_system.components.fitbookings_atom], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_fitbooking = (function sevenm_booking_system$views$get_edit_data_fitbooking(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("bookings/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7819_SHARP_){
cljs.core.reset_BANG_.call(null,data,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"date","date",-1463434462),"get"], null));

return cljs.core.reset_BANG_.call(null,sevenm_booking_system.components.fitbookings_atom,sevenm_booking_system.components.bookings_get_data_destruct_for_edit.call(null,p1__7819_SHARP_));
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7820_SHARP_){
return console.log(p1__7820_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_fitbooking_page = (function sevenm_booking_system$views$edit_fitbooking_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_fitbooking.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_fitbooking,sevenm_booking_system.components.fitbookings_atom], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.groupbookings_page = (function sevenm_booking_system$views$groupbookings_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.groupbookings_table_comp], null)], null)], null);
});
sevenm_booking_system.views.add_groupbooking_page = (function sevenm_booking_system$views$add_groupbooking_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_groupbooking,sevenm_booking_system.components.groupbookings_atom], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_groupbooking = (function sevenm_booking_system$views$get_edit_data_groupbooking(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("bookings/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7821_SHARP_){
cljs.core.reset_BANG_.call(null,data,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"data","data",-232669377),"get"], null));

return cljs.core.reset_BANG_.call(null,sevenm_booking_system.components.groupbookings_atom,sevenm_booking_system.components.bookings_get_data_destruct_for_edit.call(null,p1__7821_SHARP_));
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7822_SHARP_){
return console.log(p1__7822_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_groupbooking_page = (function sevenm_booking_system$views$edit_groupbooking_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_groupbooking.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_groupbooking,sevenm_booking_system.components.groupbookings_atom], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.items_page = (function sevenm_booking_system$views$items_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.items_comp], null)], null)], null);
});
sevenm_booking_system.views.add_item_page = (function sevenm_booking_system$views$add_item_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_item_comp,new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"adultcost","adultcost",161039586),(0),new cljs.core.Keyword(null,"childcost","childcost",1864240071),(0),new cljs.core.Keyword(null,"description","description",-1428560544),"",new cljs.core.Keyword(null,"type","type",1174270348),"Both",new cljs.core.Keyword(null,"activitytitle","activitytitle",-1847406484),"",new cljs.core.Keyword(null,"subcategory","subcategory",-1171802998),""], null)], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_items = (function sevenm_booking_system$views$get_edit_data_items(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("items/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7823_SHARP_){
return cljs.core.reset_BANG_.call(null,data,p1__7823_SHARP_);
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7824_SHARP_){
return console.log(p1__7824_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_item_page = (function sevenm_booking_system$views$edit_item_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_items.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_item_comp,cljs.core.deref.call(null,data)], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.view_item = (function sevenm_booking_system$views$view_item(id,data){
return reagent.core.create_class.call(null,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component-did-mount","component-did-mount",-1126910518),(function (){
return sevenm_booking_system.views.set_div_to_disabled.call(null,"HotelRating");
}),new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_item_comp,cljs.core.deref.call(null,data)], null)], null);
})], null));
});
sevenm_booking_system.views.view_item_page = (function sevenm_booking_system$views$view_item_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_items.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_item,id,data], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.vendors_page = (function sevenm_booking_system$views$vendors_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.vendors_comp], null)], null)], null);
});
sevenm_booking_system.views.add_vendor_page = (function sevenm_booking_system$views$add_vendor_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_vendor_comp,cljs.core.PersistentArrayMap.EMPTY], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_vendors = (function sevenm_booking_system$views$get_edit_data_vendors(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("vendors/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7825_SHARP_){
return cljs.core.reset_BANG_.call(null,data,p1__7825_SHARP_);
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7826_SHARP_){
return console.log(p1__7826_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_vendor_page = (function sevenm_booking_system$views$edit_vendor_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_vendors.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_vendor_comp,cljs.core.deref.call(null,data)], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.cities_page = (function sevenm_booking_system$views$cities_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.cities_comp], null)], null)], null);
});
sevenm_booking_system.views.add_city_page = (function sevenm_booking_system$views$add_city_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_city_comp,cljs.core.PersistentArrayMap.EMPTY], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_cities = (function sevenm_booking_system$views$get_edit_data_cities(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("cities/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7827_SHARP_){
return cljs.core.reset_BANG_.call(null,data,p1__7827_SHARP_);
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7828_SHARP_){
return console.log(p1__7828_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_city_page = (function sevenm_booking_system$views$edit_city_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_cities.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_city_comp,cljs.core.deref.call(null,data)], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.hotels_page = (function sevenm_booking_system$views$hotels_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.hotels_comp], null)], null)], null);
});
sevenm_booking_system.views.add_hotel_page = (function sevenm_booking_system$views$add_hotel_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_hotel_comp,cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"remarks","remarks",1566599360),new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.Keyword(null,"category","category",-593092832),new cljs.core.Keyword(null,"address","address",559499426),new cljs.core.Keyword(null,"faxnumber","faxnumber",2142510311),new cljs.core.Keyword(null,"cancellationpolicy","cancellationpolicy",1403968968),new cljs.core.Keyword(null,"contactpersonname","contactpersonname",-1748859861),new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"filename","filename",-1428840783),new cljs.core.Keyword(null,"phonenumber","phonenumber",591435283),new cljs.core.Keyword(null,"contactpersonemail","contactpersonemail",-1420968074),new cljs.core.Keyword(null,"contactpersonphonenumber","contactpersonphonenumber",241524887),new cljs.core.Keyword(null,"cost","cost",-1094861735),new cljs.core.Keyword(null,"rating","rating",144173662)],["","","0 Star","","","","","Not Contract","","","","",(0),(0)])], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_hotels = (function sevenm_booking_system$views$get_edit_data_hotels(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("hotels/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7829_SHARP_){
return cljs.core.reset_BANG_.call(null,data,p1__7829_SHARP_);
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7830_SHARP_){
return console.log(p1__7830_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_hotel_page = (function sevenm_booking_system$views$edit_hotel_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_hotels.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_hotel_comp,cljs.core.deref.call(null,data)], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.view_hotels = (function sevenm_booking_system$views$view_hotels(id,data){
return reagent.core.create_class.call(null,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component-did-mount","component-did-mount",-1126910518),(function (){
return sevenm_booking_system.views.set_div_to_disabled.call(null,"HotelRating");
}),new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_hotel_comp,cljs.core.deref.call(null,data)], null)], null);
})], null));
});
sevenm_booking_system.views.view_hotel_page = (function sevenm_booking_system$views$view_hotel_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_hotels.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_hotels,id,data], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.restaurants_page = (function sevenm_booking_system$views$restaurants_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.restaurants_comp], null)], null)], null);
});
sevenm_booking_system.views.add_restaurant_page = (function sevenm_booking_system$views$add_restaurant_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_restaurant_comp,cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"remarks","remarks",1566599360),new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.Keyword(null,"lunch","lunch",-875508703),new cljs.core.Keyword(null,"waytoconfirm","waytoconfirm",-2049126719),new cljs.core.Keyword(null,"address","address",559499426),new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"faxnumber","faxnumber",2142510311),new cljs.core.Keyword(null,"cellnumber","cellnumber",-1608391155),new cljs.core.Keyword(null,"contactperson","contactperson",-190367504),new cljs.core.Keyword(null,"dinner","dinner",-446811662),new cljs.core.Keyword(null,"contactnumber","contactnumber",730706546),new cljs.core.Keyword(null,"kids","kids",1156670771),new cljs.core.Keyword(null,"area","area",472007256),new cljs.core.Keyword(null,"cuisine","cuisine",1109584060),new cljs.core.Keyword(null,"rating","rating",144173662)],["","",(0),"","","","","","",(0),"",(0),"","",(0)])], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_restaurants = (function sevenm_booking_system$views$get_edit_data_restaurants(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("restaurants/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7831_SHARP_){
return cljs.core.reset_BANG_.call(null,data,p1__7831_SHARP_);
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7832_SHARP_){
return console.log(p1__7832_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_restaurant_page = (function sevenm_booking_system$views$edit_restaurant_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_restaurants.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_restaurant_comp,cljs.core.deref.call(null,data)], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.view_restaurant = (function sevenm_booking_system$views$view_restaurant(id,data){
return reagent.core.create_class.call(null,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component-did-mount","component-did-mount",-1126910518),(function (){
return sevenm_booking_system.views.set_div_to_disabled.call(null,"RestaurantRating");
}),new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_restaurant_comp,cljs.core.deref.call(null,data)], null)], null);
})], null));
});
sevenm_booking_system.views.view_restaurant_page = (function sevenm_booking_system$views$view_restaurant_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_restaurants.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_restaurant,id,data], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.tourguides_page = (function sevenm_booking_system$views$tourguides_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.tourguides_comp], null)], null)], null);
});
sevenm_booking_system.views.add_tourguide_page = (function sevenm_booking_system$views$add_tourguide_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_tourguide_comp,cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.Keyword(null,"amount","amount",364489504),new cljs.core.Keyword(null,"address","address",559499426),new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"vendorid","vendorid",-675947897),new cljs.core.Keyword(null,"cellnumber","cellnumber",-1608391155),new cljs.core.Keyword(null,"contactnumber","contactnumber",730706546),new cljs.core.Keyword(null,"hours","hours",58380855),new cljs.core.Keyword(null,"rating","rating",144173662)],["",(0),"","",(0),"","",(0),(0)])], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_tourguides = (function sevenm_booking_system$views$get_edit_data_tourguides(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("tourguides/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7833_SHARP_){
return cljs.core.reset_BANG_.call(null,data,p1__7833_SHARP_);
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7834_SHARP_){
return console.log(p1__7834_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_tourguide_page = (function sevenm_booking_system$views$edit_tourguide_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_tourguides.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_tourguide_comp,cljs.core.deref.call(null,data)], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.view_tourguide = (function sevenm_booking_system$views$view_tourguide(id,data){
return reagent.core.create_class.call(null,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component-did-mount","component-did-mount",-1126910518),(function (){
return sevenm_booking_system.views.tourgide_set_div_disabled.call(null,"TourGuideRating");
}),new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_tourguide_comp,cljs.core.deref.call(null,data)], null)], null);
})], null));
});
sevenm_booking_system.views.view_tourguide_page = (function sevenm_booking_system$views$view_tourguide_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_tourguides.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_tourguide,id,data], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.transportations_page = (function sevenm_booking_system$views$transportations_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.transportations_comp], null)], null)], null);
});
sevenm_booking_system.views.add_transportation_page = (function sevenm_booking_system$views$add_transportation_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_transportation_comp,cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.Keyword(null,"address","address",559499426),new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"faxnumber","faxnumber",2142510311),new cljs.core.Keyword(null,"vendorid","vendorid",-675947897),new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"contactperson","contactperson",-190367504),new cljs.core.Keyword(null,"contactnumber","contactnumber",730706546),new cljs.core.Keyword(null,"bankinformation","bankinformation",-618140582),new cljs.core.Keyword(null,"rating","rating",144173662)],["","","","",(0),"None","","","",(0)])], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_transportations = (function sevenm_booking_system$views$get_edit_data_transportations(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("transportations/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7835_SHARP_){
return cljs.core.reset_BANG_.call(null,data,p1__7835_SHARP_);
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7836_SHARP_){
return console.log(p1__7836_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_transportation_page = (function sevenm_booking_system$views$edit_transportation_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_transportations.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_transportation_comp,cljs.core.deref.call(null,data)], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.view_transportation = (function sevenm_booking_system$views$view_transportation(id,data){
return reagent.core.create_class.call(null,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component-did-mount","component-did-mount",-1126910518),(function (){
return sevenm_booking_system.views.set_div_to_disabled.call(null,"TransportRating");
}),new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_transportation_comp,cljs.core.deref.call(null,data)], null)], null);
})], null));
});
sevenm_booking_system.views.view_transportation_page = (function sevenm_booking_system$views$view_transportation_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_transportations.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_transportation,id,data], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.bookingfits_page = (function sevenm_booking_system$views$bookingfits_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.bookingfits_comp], null)], null)], null);
});
sevenm_booking_system.views.add_bookingfit_page = (function sevenm_booking_system$views$add_bookingfit_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_bookingfit_comp,cljs.core.PersistentArrayMap.EMPTY], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_bookingfits = (function sevenm_booking_system$views$get_edit_data_bookingfits(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("bookingfits/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7837_SHARP_){
return cljs.core.reset_BANG_.call(null,data,p1__7837_SHARP_);
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7838_SHARP_){
return console.log(p1__7838_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_bookingfit_page = (function sevenm_booking_system$views$edit_bookingfit_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_bookingfits.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_bookingfit_comp,cljs.core.deref.call(null,data)], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.events_page = (function sevenm_booking_system$views$events_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.events_comp], null)], null)], null);
});
sevenm_booking_system.views.add_event_page = (function sevenm_booking_system$views$add_event_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_event_comp,cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"remarks","remarks",1566599360),new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.Keyword(null,"adultcost","adultcost",161039586),new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"meal","meal",-121971613),new cljs.core.Keyword(null,"faxnumber","faxnumber",2142510311),new cljs.core.Keyword(null,"vendorid","vendorid",-675947897),new cljs.core.Keyword(null,"childcost","childcost",1864240071),new cljs.core.Keyword(null,"contract","contract",798152745),new cljs.core.Keyword(null,"bookingguide","bookingguide",-654735699),new cljs.core.Keyword(null,"cp","cp",-678439312),new cljs.core.Keyword(null,"contactnumber","contactnumber",730706546),new cljs.core.Keyword(null,"phonenumber","phonenumber",591435283),new cljs.core.Keyword(null,"st","st",1455255828),new cljs.core.Keyword(null,"website","website",649297111),new cljs.core.Keyword(null,"actual","actual",107306363),new cljs.core.Keyword(null,"rating","rating",144173662)],["","",(0),"","","",(0),(0),"","",(0),"","",(0),"","",(0)])], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_events = (function sevenm_booking_system$views$get_edit_data_events(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("events/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7839_SHARP_){
return cljs.core.reset_BANG_.call(null,data,p1__7839_SHARP_);
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7840_SHARP_){
return console.log(p1__7840_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_event_page = (function sevenm_booking_system$views$edit_event_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_events.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_event_comp,cljs.core.deref.call(null,data)], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.view_event = (function sevenm_booking_system$views$view_event(id,data){
return reagent.core.create_class.call(null,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"component-did-mount","component-did-mount",-1126910518),(function (){
return sevenm_booking_system.views.set_div_to_disabled.call(null,"EventRating");
}),new cljs.core.Keyword(null,"reagent-render","reagent-render",-985383853),(function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_event_comp,cljs.core.deref.call(null,data)], null)], null);
})], null));
});
sevenm_booking_system.views.view_event_page = (function sevenm_booking_system$views$view_event_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_events.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_event,id,data], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.logos_page = (function sevenm_booking_system$views$logos_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.logos_comp], null)], null)], null);
});
sevenm_booking_system.views.add_logo_page = (function sevenm_booking_system$views$add_logo_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_logo_comp,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"logoname","logoname",1648014478),"",new cljs.core.Keyword(null,"filename","filename",-1428840783),""], null)], null)], null)], null);
});
sevenm_booking_system.views.get_edit_data_logos = (function sevenm_booking_system$views$get_edit_data_logos(id,data){
return ajax.core.GET.call(null,[cljs.core.str(sevenm_booking_system.config.server),cljs.core.str("logos/"),cljs.core.str(id)].join(''),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"handler","handler",-195596612),(function (p1__7841_SHARP_){
return cljs.core.reset_BANG_.call(null,data,p1__7841_SHARP_);
}),new cljs.core.Keyword(null,"error-handler","error-handler",-484945776),(function (p1__7842_SHARP_){
return console.log(p1__7842_SHARP_);
}),new cljs.core.Keyword(null,"format","format",-1306924766),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"response-format","response-format",1664465322),new cljs.core.Keyword(null,"json","json",1279968570),new cljs.core.Keyword(null,"keywords?","keywords?",764949733),true,new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"Authorization","Authorization",-1017527462),[cljs.core.str("Token "),cljs.core.str(cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"token","token",-1211463215)], null)))].join('')], null)], null));
});
sevenm_booking_system.views.edit_logo_page = (function sevenm_booking_system$views$edit_logo_page(id){
var data = reagent.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
sevenm_booking_system.views.get_edit_data_logos.call(null,id,data);

return ((function (data){
return (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,data)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.add_or_edit_logo_comp,cljs.core.deref.call(null,data)], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),"Loading ......"], null)], null))], null);
});
;})(data))
});
sevenm_booking_system.views.complete_activity_log_page = (function sevenm_booking_system$views$complete_activity_log_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.activitylog.activity_log_comp,"Complete Activity Log","activitylog?"], null)], null)], null);
});
sevenm_booking_system.views.fit_activitylog_page = (function sevenm_booking_system$views$fit_activitylog_page(id){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.activitylog.activity_log_comp,"Fit Booking Activity Log",[cljs.core.str("activitylog?bookingid="),cljs.core.str(id),cljs.core.str("&")].join('')], null)], null)], null);
});
sevenm_booking_system.views.group_activitylog_page = (function sevenm_booking_system$views$group_activitylog_page(id){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.activitylog.activity_log_comp,"Group Booking Activity Log",[cljs.core.str("activitylog?bookingid="),cljs.core.str(id),cljs.core.str("&")].join('')], null)], null)], null);
});
sevenm_booking_system.views.complete_payment_log_page = (function sevenm_booking_system$views$complete_payment_log_page(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,(0),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.activitylog.paymentlog_comp,"Complete Payment Log","paymentlog?"], null)], null)], null);
});
sevenm_booking_system.views.fit_paymentlog_page = (function sevenm_booking_system$views$fit_paymentlog_page(id){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.activitylog.paymentlog_comp,"Fit Booking Payment Log",[cljs.core.str("paymentlog?bookingid="),cljs.core.str(id),cljs.core.str("&")].join('')], null)], null)], null);
});
sevenm_booking_system.views.group_paymentlog_page = (function sevenm_booking_system$views$group_paymentlog_page(id){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.components.itineraries_tabs_comp,id,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.activitylog.activity_log_comp,"Group Booking Payment Log",[cljs.core.str("paymentlog?bookingid="),cljs.core.str(id),cljs.core.str("&")].join('')], null)], null)], null);
});
sevenm_booking_system.views.home_panel = (function sevenm_booking_system$views$home_panel(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),(function (){var role = cljs.core.get_in.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"session","session",1008279103),new cljs.core.Keyword(null,"user","user",1532431356),new cljs.core.Keyword(null,"role","role",-736691072)], null));
var G__7844 = role;
switch (G__7844) {
case "bookadmin":
return pushy.core.set_token_BANG_.call(null,sevenm_booking_system.routes.history,sevenm_booking_system.routes.url_for.call(null,new cljs.core.Keyword(null,"fit-quotations","fit-quotations",-1520677836)));

break;
case "bookstandard":
return pushy.core.set_token_BANG_.call(null,sevenm_booking_system.routes.history,sevenm_booking_system.routes.url_for.call(null,new cljs.core.Keyword(null,"fit-quotations","fit-quotations",-1520677836)));

break;
case "bookgroup":
return pushy.core.set_token_BANG_.call(null,sevenm_booking_system.routes.history,sevenm_booking_system.routes.url_for.call(null,new cljs.core.Keyword(null,"fit-quotations","fit-quotations",-1520677836)));

break;
default:
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);

}
})()], null);
});
sevenm_booking_system.views.fit_quotations_panel = (function sevenm_booking_system$views$fit_quotations_panel(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103))))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.fit_quotations_page], null):new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null))], null);
});
sevenm_booking_system.views.group_quotations_panel = (function sevenm_booking_system$views$group_quotations_panel(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),((cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103))))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.group_quotations_page], null):new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null))], null);
});
sevenm_booking_system.views.register_panel = (function sevenm_booking_system$views$register_panel(){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.register_page], null);
});
sevenm_booking_system.views.fitbookings_panel = (function sevenm_booking_system$views$fitbookings_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.fitbookings_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_fitbooking_panel = (function sevenm_booking_system$views$add_fitbooking_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_fitbooking_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_fitbooking_panel = (function sevenm_booking_system$views$edit_fitbooking_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_fitbooking_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.groupbookings_panel = (function sevenm_booking_system$views$groupbookings_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.groupbookings_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_groupbooking_panel = (function sevenm_booking_system$views$add_groupbooking_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_groupbooking_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_groupbooking_panel = (function sevenm_booking_system$views$edit_groupbooking_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_groupbooking_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.items_panel = (function sevenm_booking_system$views$items_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.items_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_item_panel = (function sevenm_booking_system$views$add_item_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_item_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_item_panel = (function sevenm_booking_system$views$edit_item_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_item_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.view_item_panel = (function sevenm_booking_system$views$view_item_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_item_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.vendors_panel = (function sevenm_booking_system$views$vendors_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.vendors_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_vendor_panel = (function sevenm_booking_system$views$add_vendor_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_vendor_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_vendor_panel = (function sevenm_booking_system$views$edit_vendor_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_vendor_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.cities_panel = (function sevenm_booking_system$views$cities_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.cities_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_city_panel = (function sevenm_booking_system$views$add_city_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_city_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_city_panel = (function sevenm_booking_system$views$edit_city_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_city_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.hotels_panel = (function sevenm_booking_system$views$hotels_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.hotels_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_hotel_panel = (function sevenm_booking_system$views$add_hotel_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_hotel_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_hotel_panel = (function sevenm_booking_system$views$edit_hotel_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_hotel_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.view_hotel_panel = (function sevenm_booking_system$views$view_hotel_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_hotel_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.restaurants_panel = (function sevenm_booking_system$views$restaurants_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.restaurants_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_restaurant_panel = (function sevenm_booking_system$views$add_restaurant_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_restaurant_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_restaurant_panel = (function sevenm_booking_system$views$edit_restaurant_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_restaurant_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.view_restaurant_panel = (function sevenm_booking_system$views$view_restaurant_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_restaurant_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.tourguides_panel = (function sevenm_booking_system$views$tourguides_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.tourguides_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_tourguide_panel = (function sevenm_booking_system$views$add_tourguide_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_tourguide_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_tourguide_panel = (function sevenm_booking_system$views$edit_tourguide_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_tourguide_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.view_tourguide_panel = (function sevenm_booking_system$views$view_tourguide_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_tourguide_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.transportations_panel = (function sevenm_booking_system$views$transportations_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.transportations_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_transportation_panel = (function sevenm_booking_system$views$add_transportation_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_transportation_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_transportation_panel = (function sevenm_booking_system$views$edit_transportation_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_transportation_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.view_transportation_panel = (function sevenm_booking_system$views$view_transportation_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_transportation_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.bookingfits_panel = (function sevenm_booking_system$views$bookingfits_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.bookingfits_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_bookingfit_panel = (function sevenm_booking_system$views$add_bookingfit_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_bookingfit_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_bookingfit_panel = (function sevenm_booking_system$views$edit_bookingfit_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_bookingfit_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.events_panel = (function sevenm_booking_system$views$events_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.events_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_event_panel = (function sevenm_booking_system$views$add_event_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_event_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_event_panel = (function sevenm_booking_system$views$edit_event_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_event_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.view_event_panel = (function sevenm_booking_system$views$view_event_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_event_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.logos_panel = (function sevenm_booking_system$views$logos_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.logos_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.add_logo_panel = (function sevenm_booking_system$views$add_logo_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_logo_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.edit_logo_panel = (function sevenm_booking_system$views$edit_logo_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_logo_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.complete_activity_log_panel = (function sevenm_booking_system$views$complete_activity_log_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.complete_activity_log_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.fit_activitylog_panel = (function sevenm_booking_system$views$fit_activitylog_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.fit_activitylog_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.group_activitylog_panel = (function sevenm_booking_system$views$group_activitylog_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.group_activitylog_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.complete_payment_log_panel = (function sevenm_booking_system$views$complete_payment_log_panel(){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.complete_payment_log_page], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.fit_paymentlog_panel = (function sevenm_booking_system$views$fit_paymentlog_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.fit_paymentlog_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
sevenm_booking_system.views.group_paymentlog_panel = (function sevenm_booking_system$views$group_paymentlog_panel(route_details){
if(cljs.core.seq.call(null,cljs.core.deref.call(null,sevenm_booking_system.config.session).call(null,new cljs.core.Keyword(null,"session","session",1008279103)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.group_paymentlog_page,cljs.core.get_in.call(null,route_details,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"route-params","route-params",2111411055),new cljs.core.Keyword(null,"id","id",-1388402092)], null))], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.login_page], null);
}
});
if(typeof sevenm_booking_system.views.panels !== 'undefined'){
} else {
sevenm_booking_system.views.panels = (function (){var method_table__6527__auto__ = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__6528__auto__ = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var method_cache__6529__auto__ = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__6530__auto__ = cljs.core.atom.call(null,cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__6531__auto__ = cljs.core.get.call(null,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),cljs.core.get_global_hierarchy.call(null));
return (new cljs.core.MultiFn(cljs.core.symbol.call(null,"sevenm-booking-system.views","panels"),((function (method_table__6527__auto__,prefer_table__6528__auto__,method_cache__6529__auto__,cached_hierarchy__6530__auto__,hierarchy__6531__auto__){
return (function (route_details){
return new cljs.core.Keyword(null,"handler","handler",-195596612).cljs$core$IFn$_invoke$arity$1(route_details);
});})(method_table__6527__auto__,prefer_table__6528__auto__,method_cache__6529__auto__,cached_hierarchy__6530__auto__,hierarchy__6531__auto__))
,new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__6531__auto__,method_table__6527__auto__,prefer_table__6528__auto__,method_cache__6529__auto__,cached_hierarchy__6530__auto__));
})();
}
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"home","home",-74557309),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.home_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"register","register",1968522516),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.register_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"fit-quotations","fit-quotations",-1520677836),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.fit_quotations_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"group-quotations","group-quotations",1243399025),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.group_quotations_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"fitbookings","fitbookings",227762754),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.fitbookings_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-fitbooking","add-fitbooking",1804203067),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_fitbooking_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-fitbooking","edit-fitbooking",-1607666750),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_fitbooking_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"fit-activitylog","fit-activitylog",-725463727),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.fit_activitylog_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"fit-paymentlog","fit-paymentlog",1182127213),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.fit_paymentlog_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"groupbookings","groupbookings",-787083220),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.groupbookings_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-groupbooking","add-groupbooking",-564709166),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_groupbooking_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-groupbooking","edit-groupbooking",2069309826),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_groupbooking_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"group-activitylog","group-activitylog",-1486409568),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.group_activitylog_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"group-paymentlog","group-paymentlog",289785927),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.group_paymentlog_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"items","items",1031954938),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.items_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-item","add-item",715813891),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_item_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-item","edit-item",1026303883),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_item_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"view-item","view-item",241055094),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_item_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"vendors","vendors",-153040496),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.vendors_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-vendor","add-vendor",-527382632),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_vendor_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-vendor","edit-vendor",-1413725249),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_vendor_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"cities","cities",-1295356824),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.cities_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-city","add-city",-1190166),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_city_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-city","edit-city",-1721270384),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_city_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"hotels","hotels",1552357142),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.hotels_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-hotel","add-hotel",710530814),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_hotel_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-hotel","edit-hotel",376112162),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_hotel_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"view-hotel","view-hotel",-686388496),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_hotel_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"restaurants","restaurants",1693363505),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.restaurants_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-restaurant","add-restaurant",862180421),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_restaurant_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-restaurant","edit-restaurant",-709382400),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_restaurant_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"view-restaurant","view-restaurant",1556565268),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_restaurant_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"tourguides","tourguides",604110481),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.tourguides_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-tourguide","add-tourguide",-595112847),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_tourguide_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-tourguide","edit-tourguide",1622319808),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_tourguide_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"view-tourguide","view-tourguide",-823421758),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_tourguide_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"transportations","transportations",949329735),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.transportations_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-transportation","add-transportation",-769051174),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_transportation_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-transportation","edit-transportation",-580535752),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_transportation_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"view-transportation","view-transportation",-2143737615),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_transportation_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"events","events",1792552201),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.events_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-event","add-event",938429088),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_event_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-event","edit-event",-969537481),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_event_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"view-event","view-event",1670309774),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.view_event_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"bookingfits","bookingfits",-1934442442),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.bookingfits_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"logos","logos",1354242661),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.logos_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-logo","add-logo",-1927288932),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_logo_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-logo","edit-logo",-682467110),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_logo_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"add-bookingfit","add-bookingfit",1614968085),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.add_bookingfit_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"edit-bookingfit","edit-bookingfit",144640306),(function (r){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.edit_bookingfit_panel,r], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"complete-activity-log","complete-activity-log",-24244602),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.complete_activity_log_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"complete-payment-log","complete-payment-log",-1841365671),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.complete_payment_log_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"unauthorized","unauthorized",-2089027806),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sevenm_booking_system.views.unauthorized_panel], null);
}));
cljs.core._add_method.call(null,sevenm_booking_system.views.panels,new cljs.core.Keyword(null,"default","default",-1987822328),(function (r){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632)], null);
}));
sevenm_booking_system.views.main_panel = (function sevenm_booking_system$views$main_panel(){
var active_panel = re_frame.core.subscribe.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"active-panel","active-panel",-1802545994)], null));
return ((function (active_panel){
return (function (){
return sevenm_booking_system.views.panels.call(null,cljs.core.deref.call(null,active_panel));
});
;})(active_panel))
});

//# sourceMappingURL=views.js.map?rel=1484127040550