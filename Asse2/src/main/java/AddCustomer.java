import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddCustomer extends HttpServlet
{
	
	public void service(HttpServletRequest request,HttpServletResponse response)  throws ServletException, IOException

	{
		

   
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String street = request.getParameter("street");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        // Construct the JSON request body
        String requestBody = "{"
            + "\"first_name\":\"" + firstName + "\","
            + "\"last_name\":\"" + lastName + "\","
            + "\"street\":\"" + street + "\","
            + "\"address\":\"" + address + "\","
            + "\"city\":\"" + city + "\","
            + "\"state\":\"" + state + "\","
            + "\"email\":\"" + email + "\","
            + "\"phone\":\"" + phoneNumber + "\""
            + "}";
          
        System.out.println(requestBody);
        
        // API URL for adding a customer
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create";

        // Authorization token received from the authentication API call
        String authToken = "dGVzdEBzdW5iYXNlZGF0YS5jb206VGVzdEAxMjM=";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method to POST
            connection.setRequestMethod("POST");

            // Set authorization header
            connection.setRequestProperty("Authorization","Bearer "+authToken);

            // Set the content type to JSON
            connection.setRequestProperty("Content-Type", "application/json");
            

            // Enable input/output streams
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                try (DataOutputStream writer = new DataOutputStream(os)) {
                    writer.writeBytes(requestBody);
                    writer.flush();
                }
            }
              

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();
            PrintWriter out = response.getWriter();

            if (responseCode == 201) {
                // Successfully created the customer
                out.println("Customer successfully created!");
                request.setAttribute("accessToken", authToken);
                RequestDispatcher dispatcher = request.getRequestDispatcher("welcome.jsp");
                dispatcher.forward(request, response);

                
            } else {
            	
                // Handle error response here
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                try (BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream()))) {
                    String errorLine;
                    StringBuilder errorResponse = new StringBuilder();
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    out.println("Error creating customer. Response code: " + responseCode);
            
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            // Handle exceptions here
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.println("Error: " + e.getMessage());
        }
    
		
	}
}
