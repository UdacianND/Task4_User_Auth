$(document).ready(function(){


    $('[data-toggle="tooltip"]').tooltip();

    // Select/Deselect checkboxes
    let checkbox = $('table tbody input[type="checkbox"]');
    $("#selectAll").click(function(){
        if(this.checked){
            checkbox.each(function(){
                this.checked = true;
            });
        } else{
            checkbox.each(function(){
                this.checked = false;
            });
        }
    });
    checkbox.click(function(){
        if(!this.checked){
            $("#selectAll").prop("checked", false);
        }
    });


    let baseUrl = '/api/user/management/'
    function request(command){
        let selected = [];
        $('.table-checkbox:input:checked').each(function() {
            console.log($(this).attr('data-userid'));
            selected.push(parseInt($(this).attr('data-userid')));
        });

        $.ajax({
            url: baseUrl+command,
            type: 'POST',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(selected),
            async:true
        })
        setTimeout(function(){
            window.location.href = '/'
        }, 100)

    }

    $("#delete").click(function(){
        request('delete')
    })
    $("#block").click(function(){
        request('block')
    })

    $("#unblock").click(function(){
        request('unblock')
    })

});