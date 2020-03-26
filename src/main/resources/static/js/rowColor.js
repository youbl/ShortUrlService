function onRowOver(o) {
    var r = convertColor(o.style["background-color"]);
    r != __colorClick && r != __colorOver && r && (o.attributes["color"] = r),
    r != __colorClick && (arguments[1] ? (o.style["background-color"] = arguments[1]) : (o.style["background-color"] = __colorOver))
}
function onRowOut(o) {
    var r = o.attributes["color"];
    convertColor(o.style["background-color"]) != __colorClick && (arguments[1] ? (o.style["background-color"] = arguments[1]) : r ? (o.style["background-color"] = r) : (o.style["background-color"] = __colorOut))
}
function onRowClick(o) {
    var r;
    if (r = window.event ? window.event.srcElement: onRowClick.caller.arguments[0].target, !r || !r.tagName || "TD" == r.tagName) {
        var c = convertColor(o.style["background-color"]);
        c != __colorClick && (!arguments[1] || arguments[1] && c != arguments[1]) ? arguments[1] ? (o.style["background-color"] = arguments[1]) : (o.style["background-color"] = __colorClick) : arguments[2] ? (o).css("background-color", arguments[2]) : (o.style["background-color"] = __colorOut)
    }
}
function convertColor(o) {
    var r = /^rgb\((\d+),\s+(\d+),\s+(\d+)\)$/gi;
    return r.test(o) ? "#" + parseInt(RegExp.$1).toString("16") + parseInt(RegExp.$2).toString("16") + parseInt(RegExp.$3).toString("16") : o
}
var __colorOver = "#e1eda9",
    __colorOut = "#ffffff",
    __colorClick = "#ffcc99";