define(function (require, exports, module) {

    require("jquery");
    require("mutiSelect");
    $(document).ready(function() {
        $('#example-dropUp').multiselect({
            includeSelectAllOption: true,
            maxHeight: 400
        });
    });

})