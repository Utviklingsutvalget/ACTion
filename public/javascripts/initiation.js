function initiationSearch() {

    $(document).on('click', '.initiation-search', function(e) {
        e.preventDefault();
        var parent = $(this).closest('.location-container');
        var locationId = parent.data('location-id');
        var input = parent.find('.initiation-input').val();
        if(input == "") {
            return false;
        }
        var target = parent.find('.guardian-container');
        var dataObject = {};
        dataObject['location'] = locationId;
        dataObject['query'] = input;

        $.ajax({
            url: "/initiation",
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(dataObject),
            dataType: 'json',

            statusCode: {
                200: function (data) {
                    console.log(data);
                    target.replaceWith(data.responseText);
                },
                404: function(data) {
                    target.replaceWith(data.responseText);
                }
            }
        });
    });
}
