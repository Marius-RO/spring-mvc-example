<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- boostrap.min -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

<!-- Boostrap table  https://www.geekinsta.com/creating-a-table-with-search-sorting-and-paging/     -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" rel="stylesheet">

<link href="https://cdn.datatables.net/1.10.18/css/dataTables.bootstrap4.min.css" rel="stylesheet">

<!-- Bootstrap core JavaScript-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

<!-- Page level plugin JavaScript--><script src="https://cdn.datatables.net/1.10.18/js/jquery.dataTables.min.js"></script>

<script src="https://cdn.datatables.net/1.10.18/js/dataTables.bootstrap4.min.js"></script>
<!-- Finish Boostrap table -->

<!-- Cart icon   https://www.youtube.com/watch?v=nzxtEotE0YM&ab_channel=dcode   -->
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

<style>
    .icon-button {
        position: relative;
        display:flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
        border: none;
        outline: none;
        border-radius: 50%;
        color: white;
        background-color: #353a40;
        margin-right: 10px;
    }

    .icon-button:hover {
        cursor: pointer;
        color:#afafaf;
    }

    .icon-button__badge:hover {
        cursor: pointer;
        background: #afafaf;
    }

    .icon-button__badge {
        position: absolute;
        top: 2px;
        right: -20px;
        width: 25px;
        background: whitesmoke;
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 15px;
        color: #212529;
    }

    .bg-custom-header{
        background-color: #eeeff1;
    }

    .form-control {
        display: inline;
    }

    .special-nav-item-padding-up {
        padding-top: 15px;
    }

    .special-nav-item-padding-down{
        padding-top: 10px;
    }

    .custom-header {
        margin-bottom: 30px;
    }

    .custom-logout-form {
        margin-bottom: 0px;
    }

    .custom-search-div {
        margin-top: auto;
        margin-bottom: auto;
    }

    .custom-up-spacer {
        margin-top: 30px;
    }

    .custom-footer-link {
        text-decoration: none;
        color: whitesmoke;
    }

    .custom-footer-link:hover {
        color: #afafaf;
    }

</style>

<!-- Finish Cart icon -->
