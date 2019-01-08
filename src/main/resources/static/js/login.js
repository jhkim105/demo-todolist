$(function (){
  // login
  $('#btn-login').on('click', function (){
    var username = $('#username').val();
    var password = $('#password').val();
    console.log(username,  password);

    $.ajax({
      type: 'get',
      async: false,
      url: '/auth/token',
      data: {username: username, password: password},
      dataType: 'json'
    }).done(function (data){
      if (!data.authToken) {
        alert('ERROR:empty token');
        return;
      }
      processLogin(data.authToken);

    }).fail(function (xhr){
      console.log(xhr);
      var message = '';
      if (xhr.responseJSON && xhr.responseJSON.message) {
        message = xhr.responseJSON.message;
      }
      alert("ERROR::" + message);
    });
  });

});

function processLogin(authToken){
  $.session.set("authToken", authToken);
  location.href= "/todo-list";
}