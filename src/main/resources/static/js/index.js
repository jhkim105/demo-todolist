$(function (){
  var authToken = $.session.get("authToken");
  console.log(authToken);
  if (authToken) {
    location.href = '/todo-list';
  } else {
    location.href = '/login';
  }
});