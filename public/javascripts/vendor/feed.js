$('.feed-click').on('click', function (e){

    e.preventDefault();

    var dataObject = {};

    var container = $(document).find('.feed-container');
    var clubId = container.find('.clubId').data('clubId');
    var message = container.find('.message').data('message');
    var messageTitle = container.find('.messageTitle').data('messageTitle');
    var timeAndDate = container.find('.timeAndDate').data('timeAndDate');
    var userId = container.find('.userId').data('userId');

    if(container === undefined){
        console.log("container is undefined");
    }

    var xhttpReq = $.ajax({
        url: "/feed/create",
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(dataObject),
        dataType: 'json'

    }).done(function(){

        console.log("successfully sent json with feedinfo");
    })
});

function checkInput(field){

    var checked = false;

    if(field !== undefined){
        checked = true;
    }

    return checked;
}