import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Server extends HttpServlet {
	


	
public void service(HttpServletRequest req,HttpServletResponse res)
{

String loginId=req.getParameter("name");
String password=req.getParameter("pass");


try {
    // Define the API URL
    String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

    

    // Create a URL object
    URL url = new URL(apiUrl);

    
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    
    connection.setRequestMethod("POST");

   
    connection.setDoInput(true);
    connection.setDoOutput(true);

    
    connection.setRequestProperty("Content-Type", "application/json");

    
    String jsonInputString = "{\"login_id\": \"" + loginId + "\", \"password\": \"" + password + "\"}";

    // Send the POST request
    try (OutputStream os = connection.getOutputStream()) {
        byte[] input = jsonInputString.getBytes("utf-8");
        os.write(input, 0, input.length);
    }

    // Get the HTTP response code
    int responseCode = connection.getResponseCode();

    // Read the response from the API
    
   if(responseCode==200)
   {	   
       StringBuilder response = new StringBuilder();

    try (BufferedReader br = new BufferedReader
    		(new InputStreamReader(connection.getInputStream()))) {
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine);
        }

        
    }
    String temp=response.toString();
    System.out.println(temp);
    Pattern pattern = Pattern.compile("\"access_token\"\\s*:\\s*\"([^\"]+)\"");
    Matcher matcher = pattern.matcher(temp);
 String accessToken="";
    if (matcher.find()) {
        accessToken = matcher.group(1);
    }
    req.setAttribute("accessToken", accessToken);
    RequestDispatcher dispatcher = req.getRequestDispatcher("welcome.jsp");
    dispatcher.forward(req, res);
    
   }
   if(responseCode==500)
   {
	   
	   
	
       res.sendRedirect("error.html");
       


   }
   
    // Close the connection
    connection.disconnect();
    
    
} catch (Exception e) {
 
}




}

}
