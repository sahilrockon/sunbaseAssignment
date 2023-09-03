 <%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.*"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>SunBase Database</title>
</head>


<body>



<%
    // Assuming you have the access token stored in a variable named "accessToken"
    String accessToken = (String) request.getAttribute("accessToken");
    System.out.println(accessToken);

    // Check if the access token is available
    if (accessToken != null) {
        try {
            // Define the API URL for fetching the customer list
            String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";

            // Create a URL object
            URL url = new URL(apiUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Set the "Authorization" header with the access token
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();

            // Read the API response as plain text
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder respons = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        respons.append(responseLine);
                    }

                    // Split the response text into lines
                    String[] lines = respons.toString().split("\n");

                    // Start generating the HTML table
%>
                    <h2>Customer Details</h2>
                    <table border="1">
                        <tr>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Street</th>
                            <th>Address</th>
                            <th>City</th>
                            <th>State</th>
                            <th>Email</th>
                            <th>Phone</th>
                        </tr>
<%
                    // Process each line of customer data
                  
                    for (String line : lines) {
                        try {
                        	
                            String[] customerData = line.split(",");
                     
                           
                            for(int i=1;i<customerData.length;++i)
                            {
                            	customerData[i]=customerData[i].replaceAll("\"","");
                            
                            }
                            System.out.println(Arrays.toString(customerData));

                            
                            // Clean up data here if needed
                            
                            int k=0;
                            
                            
                            while (customerData.length >k) {
                            	++k;
                            	
                            	if(customerData[k].length()<10){
                            k+=8;
                            continue;
                            	}
                                String firstName = customerData[k++].substring(11);
                                if(firstName.compareTo("null")==0)
                                {
                                	k+=8;
                                	continue; }
                                String lastName="",street="",address="",city="",state="",email="",phone="";
                                
                                if(customerData[k].length()>=10)
                                 lastName = customerData[k++].substring(10);
                                if(customerData[k].length()>=7)
                                street = customerData[k++].substring(7);
                                if(customerData[k].length()>=8)
                                address = customerData[k++].substring(8);
                                if(customerData[k].length()>=5)
                                city = customerData[k++].substring(5);
                                if(customerData[k].length()>=6)
                                 state = customerData[k++].substring(6);                                
                                if(customerData[k].length()>=6)
                                 email = customerData[k++].substring(6);
                                if(customerData[k].length()>=6)
                                 phone = customerData[k].substring(6, customerData[k++].length()-1);
%>
                                <tr>
                                    <td><%= firstName %></td>
                                    <td><%= lastName %></td>
                                    <td><%= street %></td>
                                    <td><%= address %></td>
                                    <td><%= city %></td>
                                    <td><%= state %></td>
                                    <td><%= email %></td>
                                    <td><%= phone %></td>
                                </tr>
<%
                            }
                        } catch (Exception e) {
                            // Handle the exception or syntax error here for individual rows
                            out.println("Error processing customer data: " + e.getMessage());
                        }
                    }
%>
                    </table>
<%
                } catch (Exception e) {
                    // Handle error if needed
                    out.println("Error: " + e.getMessage());
                }
            } else {
                out.println("API Request Failed with Response Code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            // Handle API request error
            out.println("API Request Error: " + e.getMessage());
        }
    } else {
%>
    <p>No access token available. Please log in.</p>
<%
    }
%>

  

    
    
    
    <br><br>
    
    <button id='btn'>Add Customer</button>
    <form id="form" method="post" action="addCustomer" style="display:none">
        <h1>Customer Details</h1>
    
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" required>
        
        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" required>
        <br><br>
        <label for="street">Street:</label>
        <input type="text" id="street" name="street">
        
        <label for="address">Address:</label>
        <input id="address" name="address" >
        <br>
          <br>
        <label for="city">State:</label>
        <input type="text" id="city" name="city">
        
        <label for="state">State:</label>
        <input type="text" id="state" name="state">
        <br><br>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email">
        
        <label for="phoneNumber">Phone Number:</label>
        <input type="tel" id="phoneNumber" name="phoneNumber" required pattern="[0-9]{10}">
        <br><br>
        <input type="submit" value="submit"  >
    </form>

    <script type="text/javascript">

    const btn = document.getElementById('btn');

    btn.addEventListener('click', () => {
      const form = document.getElementById('form');

      if (form.style.display === 'none') {
        // 👇️ this SHOWS the form
        form.style.display = 'block';
      } else {
        // 👇️ this HIDES the form
        form.style.display = 'none';
      }
    });
</script>
       
    
</body>
</html>
