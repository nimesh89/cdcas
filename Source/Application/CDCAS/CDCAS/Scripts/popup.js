/// <reference path="jquery-1.5.2-vsdoc.js" />
function popupInit() {
    $("#register-div-popup-content").position({
        my: "center",
        at: "center",
        of: "#register-div-popup"
    });
    hidePopup();
}

function hidePopup() {
    $("#register-div-popup").hide();
    $("#register-div-popup-content").hide();
}

function showPopup() {
    $("#register-div-popup").show();
    $("#register-div-popup-content").show();
}

$(window).resize(
                function () {
                    $("#register-div-popup-content").position({
                        my: "center",
                        at: "center",
                        of: "#register-div-popup"
                    });
                }
        );