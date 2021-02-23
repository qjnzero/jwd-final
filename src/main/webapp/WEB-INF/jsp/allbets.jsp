<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css" integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" crossorigin="anonymous">

    <title>Tiger bet - Online Football Betting</title>
</head>
<body>

    <jsp:include page="header.jsp"/>
    <c:if test="${not empty requestScope.bets}">
        <h2>Columns</h2>
        <ol>
            <c:forEach var="bet" items="${requestScope.bets}">
                <form action="${pageContext.request.contextPath}/controller?command=cancel_bet" method="post">
                    <li>Bet money: ${bet.betMoney}, expected win: ${bet.expectedWin} ${requestScope.coefficient}</li>
                    <input type="hidden" name="betId" value="${bet.id}" />
                    <input type="hidden" name="matchId" value="${requestScope.matchId}" />
                    <input type="submit" value="Cancel bet" />
                </form>
                <form action="${pageContext.request.contextPath}/controller?command=update_bet" method="post">
                    <input type="hidden" name="betId" value="${bet.id}" />
                    <input type="submit" value="Update bet" />
                </form>
            </c:forEach>
        </ol>
    </c:if>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-Piv4xVNRyMGpqkS2by6br4gNJ7DXjqk09RmUpJ8jgGtD7zP9yug3goQfGII0yAns" crossorigin="anonymous"></script>

</body>
</html>
