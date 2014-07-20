$(function() {

    $('.event-click').on('click', function(e) {
        e.preventDefault();

        var eventId = $(this).data('event-id');
        var attend = $(this).data('event-value');
        console.log(eventId);
        console.log(attend);

        var dataObject = {};
        dataObject['event'] = eventId;
        dataObject['attend'] = attend;

        var jqhxr = $.ajax({
            url: "/events/attend",
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(dataObject),
            dataType: 'json'
        }).done(function () {
        }).always(function () {
            console.log("Done");
            var target = $('a#event-'+eventId);
            var className = 'alert';
            if(attend) {
                target.removeClass(className);
                target.text("Skal");
            } else if(!attend) {
                target.addClass(className)
                target.text("Skal ikke")
            }
        });
    });

});