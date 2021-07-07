<%@ page import="zad1.Models.Book" %>
<%@ page import="zad1.Services.DbService" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%
    DbService dbService = new DbService();

    String id = request.getParameter("id");
    String filter = request.getParameter("filter");

    List<Book> books;
    if (id == null || filter == null || filter.isEmpty() || id.isEmpty())
        books = dbService.getBooksFromDb(DbService.QUERY_STANDARD);
    else books = dbService.getBooksFromDb(DbService.CREATE_QUERY(id, filter));
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="stylesheet" href="styles.css">

        <title>S20363</title>
    </head>

    <body>
        <form method="GET">
            <label>
                <select class="button" name="id">
                    <option value="Publisher">Publisher</option>
                    <option value="Author">Author</option>
                    <option value="Title">Title</option>
                    <option value="Price">Price</option>
                </select>
            </label>
            <label>
                <input type="text" name="filter" class="button"/>
            </label>
            <input type="submit" value="Filter" class="button"/>
        </form>

        <% if (!books.isEmpty()) { %>
            <table>
                <thead>
                    <tr>
                        <th>Publisher</th>
                        <th>Author</th>
                        <th>Title</th>
                        <th>Price</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Book book : books) { %>
                        <tr>
                            <td><%=book.getPublisher()%></td>
                            <td><%=book.getAuthor()%></td>
                            <td><%=book.getTitle()%></td>
                            <td><%=book.getPrice()%></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } else %> <h3>It seems like there are no books</h3>
    </body>
</html>