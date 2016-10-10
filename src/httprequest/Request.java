package httprequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Request {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		String s = sendGET();
		System.out.println(s);
		System.out.print("Grade: " );
		parseIt(s);
		

	}

	private static String sendGET() throws IOException {
		Scanner scan = new Scanner(System.in);
		System.out.print("Please enter a tuid: ");
		String tuID = scan.nextLine();
		/* putting the user input in the api */
		String GET_URL = "http://129.32.92.49:8080/xml_lab/getgrade?xml=<tuid>" + tuID + "</tuid>";

		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		/* send in GET request */
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();

		/* HTTP_OK = 200 */
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			/* store the response in the variable name response */
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			/* return json response */
			return response.toString();
		} else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
			System.out.println("Product unavailable");
			return "Product unavailable";
		} else {
			System.out.println("GET Response Code :: " + responseCode);
			return "GET request not worked";
		}

	}

	public static void parseIt(String xmlString) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xmlString.getBytes("utf-8"))));
		doc.getDocumentElement().normalize();
		String timeStamp = doc.getElementsByTagName("content").item(0).getTextContent();
		System.out.println(timeStamp);
	}

}
