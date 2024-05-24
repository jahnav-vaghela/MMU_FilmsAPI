<%--
  Created by IntelliJ IDEA.
  User: jahnav
  Date: 5/6/2024
  Time: 12:21 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="films ">
    <c:forEach items="${filmlist}" var="f">

        <div class="film film-<c:out value="${f.getId()}" /> l-box pure-u-1 pure-u-md-1-2 pure-u-lg-1-4">
            <h3 class="content-subhead title"> <c:out value="${f.getTitle()}"  /> </h3>
            <p>Year: <c:out value="${f.getYear()}" />  </p>
            <p>Director: <c:out value="${f.getDirector()}" /> </p>
            <p>Stars: <c:out value="${f.getStars()}" /> </p>
            <p>Review: <c:out value="${f.getReview()}" /> </p>
            <p>ID: <c:out value="${f.getId()}" /></p>
            <p>



                <a data-film-id="<c:out value="${f.getId()}" />"
                   data-title="<c:out value="${f.getTitle()}"  />"
                   data-year="<c:out value="${f.getYear()}" />"
                   data-director="<c:out value="${f.getDirector()}" />"
                   data-stars="<c:out value="${f.getStars()}" />"
                   data-review="<c:out value="${f.getReview()}" />"
                   class="edt-film button-success pure-button">Edit Film</a>
                <a data-film-id="<c:out value="${f.getId()}" />" class="del-film button-error pure-button">Delete Film</a>
            </p>
        </div>

    </c:forEach>
</div>


