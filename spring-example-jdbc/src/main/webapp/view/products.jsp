<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ include file="templates/paths.jsp" %>
<%@ include file="templates/roles.jsp" %>

<html>
    <head>
        <title>Products</title>
        <jsp:include page="templates/head-footer-dependencies.jsp"/>
        <style>
            .card{
                margin: 15px;
            }

            .special-card {
                margin-right: 0;
            }

            .custom-table-div-spacing {
                margin-top: 15px;
                margin-bottom: 30px;
            }

            .filter-sidebar {
                margin-top: 40px;
                width: 105%;

            }

            .filter-group-title {
                margin-bottom: 10px;
            }

            .filter-group-checkbox {
                margin-bottom: 10px;
            }
        </style>
    </head>

    <body class="d-flex flex-column min-vh-100">
        <jsp:include page="templates/navbar.jsp"/>

        <div class="container-fluid w-100">
            <div class="row">

                <div class="col-2">
                    <!-- START filter sidebar -->
                    <div class="card filter-sidebar">

                        <button class="btn btn-secondary w-100" type="button" data-toggle="collapse" data-target="#collapseFilter" aria-expanded="false" aria-controls="collapseFilter">
                            Filter
                        </button>

                        <div class="collapse show" id="collapseFilter">

                            <ul class="list-group list-group-flush">

                                <!-- START by price filter -->
                                <li class="list-group-item list-group-item-action">

                                    <p class="font-weight-bold filter-group-title">By price</p>

                                    <div class="form-check filter-group-checkbox ">
                                        <input class="form-check-input" type="radio" id="idPriceAscending" name="mainSorting" value="0" checked>
                                        <label class="form-check-label" for="idPriceAscending">
                                            Ascending
                                        </label>
                                    </div>

                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" id="idPriceDescending" name="mainSorting" value="1">
                                        <label class="form-check-label" for="idPriceDescending">
                                            Descending
                                        </label>
                                    </div>
                                </li>
                                <!-- END by price filter -->

                                <!-- START by date filter -->
                                <li class="list-group-item list-group-item-action">
                                    <p class="font-weight-bold filter-group-title">By date</p>

                                    <div class="form-check filter-group-checkbox">
                                        <input class="form-check-input" type="radio" id="idDateAscending" name="mainSorting" value="2">
                                        <label class="form-check-label" for="idDateAscending">
                                            Ascending
                                        </label>
                                    </div>

                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" id="idDateDescending" name="mainSorting" value="3">
                                        <label class="form-check-label" for="idDateDescending">
                                            Descending
                                        </label>
                                    </div>
                                </li>
                                <!-- END by price filter -->

                                <!-- START by price range filter -->
                                <li class="list-group-item list-group-item-action">
                                    <label for="idPriceRange" class="font-weight-bold">By price range</label>
                                    <input type="range" class="form-range" min="0" max="1000" step="100" id="idPriceRange">
                                    <div class="d-flex justify-content-between">
                                        <span>0</span>
                                        <span id="idCurrentMaxValue"></span>
                                        <span>1000</span>
                                    </div>
                                </li>
                                <!-- END by price filter -->

                            </ul>
                        </div>
                    </div>
                    <!-- END filter sidebar -->
                </div>

                <div class="col-10" id="idSubViewProductsTable">
                    <jsp:include page="templates/products-table.jsp"/>
                </div>

            </div>
        </div>

        <jsp:include page="templates/footer.jsp"/>

        <script type="text/javascript">

            let subViewProductsTable = document.getElementById("idSubViewProductsTable");
            let sortingRadios = document.querySelectorAll("input[type=radio][name=mainSorting]");
            let priceRangeElem = document.getElementById("idPriceRange");
            let currentMaxValue = document.getElementById("idCurrentMaxValue");
            priceRangeElem.value = priceRangeElem.max; // set a default value (for showing purpose only)
            currentMaxValue.textContent = '(' + priceRangeElem.value + ')';

            $(document).ready(new function (){
                // event listener for price range
                priceRangeElem.addEventListener('change', function (){
                    currentMaxValue.textContent = '(' + this.value + ')';
                    filterAndSortProducts(this.value, getCurrentSortingOption());

                });

                // event listener for sorting radios
                for(const radio in sortingRadios) {
                    sortingRadios[radio].onclick = function() {
                        filterAndSortProducts(getCurrentMaxPrice(), this.value)
                    }
                }
            });

            function filterAndSortProducts(maxPrice, sortOption){
                let final = 'maxPrice=' + maxPrice + '&sort=' + sortOption + "&subView=true";
                let queryUrl = '${pageContext.request.contextPath}${dynamic_productControllerFilterProductsLink}?' + final;

                if('${param.categoryId != null}' && '${param.search != null}'){
                    queryUrl = '${pageContext.request.contextPath}${dynamic_productControllerFilterProductsLink}?categoryId=${param.categoryId}&search=${param.search}&' + final;
                }
                else {
                    if('${param.categoryId != null}'){
                        queryUrl = '${pageContext.request.contextPath}${dynamic_productControllerFilterProductsLink}?categoryId=${param.categoryId}&' + final;
                    }

                    if('${param.search != null}'){
                        queryUrl = '${pageContext.request.contextPath}${dynamic_productControllerFilterProductsLink}?search=${param.search}&' + final;
                    }
                }

                $.ajax({
                        url: queryUrl,
                        method: "GET",
                        success: function (response) {
                            // https://datatables.net/reference/api/destroy()
                            // https://stackoverflow.com/questions/28454203/jquery-datatables-cannot-read-property-adatasort-of-undefined/50937586#50937586
                            $('#productsTable').DataTable().destroy();
                            $('#productsTable t_body').empty();

                            subViewProductsTable.innerHTML = response;

                            $('#productsTable').DataTable({
                                "pageLength": 2,
                                "paging": true,
                                "lengthChange": false,
                                "searching": false,
                                "ordering": false,
                                "info": false,
                                "autoWidth": false,
                                "columnDefs": [
                                    { "width": "25%", "targets": 0 },
                                    { "width": "25%", "targets": 1 },
                                    { "width": "25%", "targets": 2 },
                                    { "width": "25%", "targets": 3 }
                                ]
                            });
                        },
                        error: function (error) {
                            console.error(error);
                        }
                });
            }

            function getCurrentSortingOption(){
                for(const radio in sortingRadios) {
                    if(sortingRadios[radio].checked){
                        return sortingRadios[radio].value;
                    }
                }
            }

            function getCurrentMaxPrice(){
                return priceRangeElem.value;
            }

            // first table init
            $(document).ready(function() {
                $('#productsTable').DataTable({
                    "pageLength": 2,
                    "paging": true,
                    "lengthChange": false,
                    "searching": false,
                    "ordering": false,
                    "info": false,
                    "autoWidth": false,
                    "columnDefs": [
                        { "width": "25%", "targets": 0 },
                        { "width": "25%", "targets": 1 },
                        { "width": "25%", "targets": 2 },
                        { "width": "25%", "targets": 3 }
                    ]
                });
            });

        </script>
    </body>
</html>
