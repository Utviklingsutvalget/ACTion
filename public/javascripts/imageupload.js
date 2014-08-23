$(document).ready(function(){

    var UPLOAD_TASK = "upload";
    var DELETE_TASK = "delete";
    var arr = {};

    // Need to establish who uploaded image and intent
    // (personal/club/feed.. etc)

    /*
    * <div class="row imageUpload">
     <div class="medium-12 columns">
     <div id="xhrResponse"></div>
     <form action="@routes.Application.uploadImage()" class="dropzone" id="imageUploadForm" method="post"
     enctype="multipart/form-data">
     <label style="opacity: 0.0">
     <input type="file" id="uploadedimage" name="file">
     </label>
     <input type="hidden" name="clubId" value="this is clubId">
     <input type="hidden" name="userId" value="this is userId">
     <input type="hidden" name="intent" value="this is intent">
     <input type="hidden" name="feedId" value="this is feedId">
     </form>
     </div>
     </div>
    * */
    var fileCounter = 0;

    // configures our dropzone
    Dropzone.options.imageUploadForm = {
        paramName: "file",
        url: "/upload",
        maxFilesize: 5, // in MB
        maxFiles: 5,
        autoProcessQueue: true,
        addRemoveLinks: true,
        acceptedFiles: ".png, .jpg, .jpeg",
        accept: function(file){
            //alert("yay");
            arr[file.name] = file;
            fileCounter++;

            if(fileCounter > 5){
                //alert("only 5");
            }else{
                console.log(arr);
                console.log(file);
                sendToServer(file.name, file, UPLOAD_TASK);
            }
        },

        init: function(){

            $("input[id='submitImage']").click(function(){

                alert("processing");
            });

            this.on('removedfile', function(file){

                fileCounter--;
                sendToServer(file.name, file, DELETE_TASK);
            });
        }
    };
});

function appendXHRResponse(message){

    var htmlElement = $(document).find('#xhrResponse');
    htmlElement.html('');
    htmlElement.append(message);
}

function sendToServer(fileName, file, task){

    var form = document.getElementById('imageUploadForm');
    var formData = new FormData(form);
    var message;
    var FILE_FIELD = "file";
    var TASK_FIELD = "task";

    formData.append(FILE_FIELD, file);
    formData.append(TASK_FIELD, task);

    console.log("filename: " + fileName + "\nfile: " + file);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/upload', true);
    xhr.send(formData);

    xhr.onreadystatechange = function(){

        if(xhr.status == 200 && xhr.readyState == 4){

            message = "<div data-alert class=\"alert-box success text-center radius\">"
                + xhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";

        }else if(xhr.status == 400 && xhr.readyState == 4){

            message = "<div data-alert class=\"alert-box alert text-center radius\">"
                + xhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
        }

        console.log(message);
        appendXHRResponse(message);
    };
}
