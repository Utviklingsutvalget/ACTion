var app = angular.module('action', [
    'ngFileUpload'
]);

$(document).ready(function() {
    $(document).foundation();

    /**
     * Turns all input elements with the class eventdatetime into this predefined datetimepicker:
     */
    $('.eventdatetime').each(function (index, element) {
        element.datetimepicker();
    });
});

var dateinput = jQuery('#datetimepicker').datetimepicker();
if(dateinput) {
    dateinput.datetimepicker();
}
