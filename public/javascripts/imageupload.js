$(document).ready(function(){

    var UPLOAD_TASK = "upload";
    var DELETE_TASK = "delete";
    var DROPZONEELEMENT;
    var arr = {};
    var location = window.location.pathname;
    console.log(location);

    /**
     * thus far identifying intent and usecases is decided by calling window location
     * function which displays current path (such as /admin/site).
     */

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

            console.log(file.name);

            arr[file.name] = file;
            fileCounter++;

            if(fileCounter > 5){

                this.removeFile(arr[0]);

            }else{
                console.log(arr);
                console.log(file);
                sendToServer(file.name, file, UPLOAD_TASK);
            }
        },

        init: function(){
            var myDropzone = this;

            $.get("/upload", {url: location}).done(function(data){

                console.log("making the call");

                $.each(data, function(key, value){

                    var mockFile = {name: value.name, size: value.size};

                    console.log("val.size: " + value.size);
                    console.log("val.name: " + value.name);
                    console.log("val.url: " + value.url);

                    myDropzone.options.addedfile.call(myDropzone, mockFile);
                    myDropzone.options.thumbnail.call(myDropzone, mockFile, value.url);
                });
            });

            this.on('removedfile', function(file){

                console.log("removed file: " + file.name);
                fileCounter--;
                sendToServer(file.name, file, DELETE_TASK);
            });
        }
    };
});

function deleteExistingFile(){

}

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
